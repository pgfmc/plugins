package net.pgfmc.claims.ownable.block.table;

import net.pgfmc.claims.ownable.block.Claim;
import net.pgfmc.core.util.vector4.Vector4;

public class ClaimsLogic {
	
	public static int CSS  = 256;
	public static int protectedRange = 30;
	public static int mergeRange = protectedRange * 2 + 1;
	public static int foreignRange = protectedRange * 6 + 2;
	
	
	public static enum Range {
		PROTECTED,
		MERGE,
		FOREIGN,
        PISTONPROTECT,
        PROTECTRENDER,
        MERGERENDER,
        FOREIGNRENDER;
        

		
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
            case PROTECTRENDER:
                return protectedRange + 7;
            case MERGERENDER:
                return mergeRange + 14;
            case FOREIGNRENDER:
                return foreignRange + 21;
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
