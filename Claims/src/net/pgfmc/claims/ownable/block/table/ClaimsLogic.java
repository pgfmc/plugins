package net.pgfmc.claims.ownable.block.table;

import net.pgfmc.claims.Main;
import net.pgfmc.claims.ownable.block.Claim;
import net.pgfmc.core.util.vector4.Vector4;

public class ClaimsLogic {
	
	public static int CSS  = 256;
	public static int protectedRange = Main.plugin.getConfig().getInt("claim-range");
	public static int mergeRange = Main.plugin.getConfig().getInt("claim-merge-range");
	public static int foreignRange = Main.plugin.getConfig().getInt("foreign-claim-range");
	
	
	public static enum Range {
		PROTECTED,
		MERGE,
		FOREIGN,
        PISTONPROTECT;
		
		public int getRange() {
			switch(this) {
			case FOREIGN:
				return foreignRange; 
			case MERGE:
				return mergeRange;
			case PROTECTED:
				return protectedRange;
            case PISTONPROTECT:
                return protectedRange + 1;
			default:
				return 0;
			}
		}
	}
	
	/**
	 * @param claim 
	 * @param vector
	 * @return true if {@code vector} is within the protected range of the {@code claim}.
	 */
	public static boolean isInRange(Claim claim, Vector4 vector, Range range) {
		
		Vector4 claimPosition = claim.getLocation();
		
		int x = range.getRange();
		
		return claimPosition.x() - x <= vector.x() &&
				claimPosition.x() + x >= vector.x() &&
				claimPosition.z() - x <= vector.z() &&
				claimPosition.z() + x >= vector.z();
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
