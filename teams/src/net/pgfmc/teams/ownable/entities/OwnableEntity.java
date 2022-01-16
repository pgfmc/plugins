package net.pgfmc.teams.ownable.entities;

import java.util.LinkedHashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import net.pgfmc.core.playerdataAPI.PlayerData;
import net.pgfmc.teams.ownable.Ownable;

/**

Stores data for container entities.


Possible entities:

Minecart with Chest
Minecart with Hopper
Item Frame
Armor Stand
Horse
Donkey
Mule
Any Nametagged mob (maybe?)

-----------------------------------

@author CrimsonDart

 */

public class OwnableEntity extends Ownable {
	
	public static LinkedHashMap<UUID, OwnableEntity> entities = new LinkedHashMap<>();
	
	UUID entity;
	
	public OwnableEntity(PlayerData player, Lock lock, UUID entity) {
		super(player, lock);
		
		this.entity = entity;
		entities.put(entity, this);
	}
	
	public static void remove(Entity entitiy) {
		entities.remove(entitiy.getUniqueId());
	}
	
	public static void remove(UUID entitiy) {
		entities.remove(entitiy);
	}
	
	public Location getLocation() {
		return Bukkit.getEntity(entity).getLocation();
	}
	
	public static OwnableEntity getContainer(Entity entity) {
		if (entity != null) {
			return getContainer(entity.getUniqueId());
		}
		return null;
	}
	
	public static OwnableEntity getContainer(UUID entity) {
		if (entity != null) {
			
			for (UUID uuid : entities.keySet()) {
				if (uuid.equals(entity)) {
					return entities.get(uuid);
				}
			}
			
			return null;
		}
		return null;
	}
	
	public Entity getEntity() {
		return Bukkit.getEntity(entity);
	}
	
	public static LinkedHashMap<UUID, OwnableEntity> getContainers() {
		return entities;
	}
}
