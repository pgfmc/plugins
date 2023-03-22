package net.pgfmc.parkour.events;

import java.io.Serializable;

import org.bukkit.World;

import net.pgfmc.core.CoreMain;
import net.pgfmc.core.util.DimManager;
import net.pgfmc.core.util.Vector4;

public class Region implements Cloneable, Serializable {


    // LEGEND for directions (in minecraft)
    //
    // north = -Z
    // south = +Z
    // east  = +X
    // west  = -X
    // up    = +Y
    // down  = -Y




    private int north; // -Z
    private int south; // +Z
    private int east;  // +X
    private int west;  // -X
    private int up;    // +Y
    private int down;  // -Y
    private int world; // world int

    // CONSTRUCTORS --------------------------------------------------
    public Region(int north, int south, int east, int west, int up, int down, int world) {
        this.north = north;
        this.south = south;
        this.east = east;
        this.west = west;
        this.up = up;
        this.down = down;
        this.world = world;
    }

    /*
     * Constructor for the Region object.
     *
     * the world this object uses is taken from @code{bound1}
     *
     * @param bound1
     * @param bound2
     */
    public Region(Vector4 bound1, Vector4 bound2) {

        // The code's structure prevents branch misses, increasing performance.
        // having no 'else' statements does this.
        north = bound2.z();
        south = bound1.z();
        if (bound1.z() < bound2.z()) {
            north = bound1.z();
            south = bound2.z();
        }

        east = bound2.x();
        west = bound1.x();
        if (bound1.x() > bound2.x()) {
            east = bound1.x();
            west = bound2.x();
        }

        up = bound2.y();
        down = bound1.y();
        if (bound1.y() > bound2.y()) {
            up = bound1.y();
            down = bound2.y();
        }

        world = bound1.w();
    }

    // getters

    public int north() {
        return north;
    }

    public int south() {
        return south;
    }

    public int east() {
        return east;
    }

    public int west() {
        return west;
    }

    public int up() {
        return up;
    }

    public int down() {
        return down;
    }

    public int w() {
        return world;
    }

    public World world() {
        return DimManager.intToWorld(world, "survival");
    }

    // Operations / Comparisons

    public boolean contains(Vector4 pos) {

        CoreMain.plugin.getLogger().info("Checking in Region for Location " + pos.toString());
        CoreMain.plugin.getLogger().info(west + " < " + pos.x() + " < " + east);
        CoreMain.plugin.getLogger().info(north + " < " + pos.z() + " < " + south);
        CoreMain.plugin.getLogger().info(down + " < " + pos.y() + " < " + up);

        return (pos.x() > west && pos.x() < east
                && pos.z() > north && pos.z() < south
                && pos.y() > down && pos.y() < up);
    }


}
