package utils;

public class UtilsFunction {
	
	public static int computeSegLength(int i, int seg1) {

		double length = (Math.sqrt(i + 1) - Math.sqrt(i)) * seg1;
//		System.out.println("Original double length = " + length);

		return (int) length;
	}
	
	public static double[] GetDisArrayFromNum(int[] numArray) {
		double[] disArray = new double[numArray.length];

		int total = 0;
		for (int i = 0; i < numArray.length; i++) {
			total += numArray[i];
		}

		for (int i = 0; i < numArray.length; i++) {
			disArray[i] = numArray[i] / (double) total;
		}

		return disArray;
	}
	
	public static boolean isOverlapInterval(double tMin, double tMax, double sMin, double sMax) {

		if (tMax <= sMin || sMax <= tMin) {
			return false;
		} else {
			return true;
		}

	}
	
	public static boolean isOverlapPointCell(double tMinLon, double tMinLat, double tMaxLon, double tMaxLat, double lon,
			double lat) {

		if (tMaxLon < lon || lon < tMinLon || tMaxLat < lat || lat < tMinLat) {
			return false;
		}

		return true;

	}


}
