package net.pgfmc.claims.ownable.block.table;

import net.pgfmc.claims.ownable.block.Claim;
import net.pgfmc.core.util.vector4.Vector4;

public class ClaimsLogic {
	
	public static int CSS  = 512;
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
        FOREIGNRENDER,
        PROTECTCULL,
        MERGECULL,
        FOREIGNCULL;
        

		
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
            case PROTECTCULL:
                return protectedRange - 1;
            case MERGECULL:
                return mergeRange - 1;
            case FOREIGNCULL:
                return foreignRange - 1;
            default:
                return 0;
			}
		}

        public Range render() {
			switch(this) {
			case FOREIGN:
				return FOREIGNRENDER; 
			case MERGE:
				return MERGERENDER;
			case PROTECTED:
				return PROTECTRENDER;
            case PISTONPROTECT:
                return PROTECTRENDER;
            case PROTECTRENDER:
                return PROTECTRENDER;
            case MERGERENDER:
                return MERGERENDER;
            case FOREIGNRENDER:
                return FOREIGNRENDER;
            case PROTECTCULL:
                return PROTECTRENDER;
            case MERGECULL:
                return MERGERENDER;
            case FOREIGNCULL:
                return FOREIGNRENDER;
            default: 
                return PROTECTRENDER;
			}
        }

        public Range cull() {
			switch(this) {
			case FOREIGN:
				return FOREIGNCULL; 
			case MERGE:
				return MERGECULL;
			case PROTECTED:
				return PROTECTCULL;
            case PISTONPROTECT:
                return PROTECTCULL;
            case PROTECTRENDER:
                return PROTECTCULL;
            case MERGERENDER:
                return MERGECULL;
            case FOREIGNRENDER:
                return FOREIGNCULL;
            case PROTECTCULL:
                return PROTECTCULL;
            case MERGECULL:
                return MERGECULL;
            case FOREIGNCULL:
                return FOREIGNCULL;
            default: 
                return PROTECTCULL;
			}
        }

        public Range normal() {
            switch(this) {
			case FOREIGN:
				return FOREIGN; 
			case MERGE:
				return MERGE;
			case PROTECTED:
				return PROTECTED;
            case PISTONPROTECT:
                return PROTECTED;
            case PROTECTRENDER:
                return PROTECTED;
            case MERGERENDER:
                return MERGE;
            case FOREIGNRENDER:
                return FOREIGN;
            case PROTECTCULL:
                return PROTECTED;
            case MERGECULL:
                return MERGE;
            case FOREIGNCULL:
                return FOREIGN;
            default: 
                return PROTECTED;
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
				claimPosition.z() + x >= vector.z() && 
                claimPosition.w() == vector.w();
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
