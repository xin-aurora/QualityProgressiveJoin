package QPJ.QualityProgressiveJoin;

import java.util.Arrays;

import framework.Driver;
import utils.Configuration;

public class SpatialJoinExp {

	public static void SpatialJoin4Par(int totalRound, String errorBound, String splitMethod, String doEvaluation) {

		String[] GT = { "6181616", "7660160", "259036", "355514" };
//		String[] GT = {"5418252", "5494919", "2784510", "758679"};
		int totalGT = 0;
		for (int i = 0; i < GT.length; i++) {
			totalGT += Integer.parseInt(GT[i]);
		}
		String[] disGT = new String[GT.length];
		for (int i = 0; i < GT.length; i++) {
			disGT[i] = String.valueOf(Integer.parseInt(GT[i]) / (double) totalGT);
		}
		System.out.println("GT = " + Arrays.toString(GT));
		System.out.println("dis GT = " + Arrays.toString(disGT));

		Configuration conf = new Configuration();

		int numPar = 4;
		String[][] paths = new String[numPar][2];
		
		for (int i=0; i<numPar; i++) {
			String[] path = new String[2];
			path[0] = "/home/xzhan261/vldb/data/grid/" + numPar + "p/0p_" + i+ ".csv";
			path[1] = "/home/xzhan261/vldb/data/grid/" + numPar + "p/1p_" + i + ".csv";
			paths[i] = path;
		}

		conf.setConf("data.paths", paths);
		conf.setConf("operator.name", "SpatialJoin");
		conf.setConf("partition.method", "GridPartition");
		conf.setConf("gridPartition.minLon", "-58.0757602");
		conf.setConf("gridPartition.minLat", "-74.4856425");
		conf.setConf("gridPartition.maxLon", "179.9837763");
		conf.setConf("gridPartition.maxLat", "83.6664731");
//		conf.setConf("gridPartition.estLevel", "7");
//		String[] buckets = { "4", "4" };
		String[] partitions = { "4", "1" };
//		conf.setConf("gridPartition.bucketNum", buckets);
		conf.setConf("gridPartition.parNum", partitions);
		conf.setConf("load.regex", ";");
		conf.setConf("load.skipHead", "false");

//		conf.setConf("split.method", "equal");
		conf.setConf("split.method", splitMethod);

		// set error bound
		conf.setConf("error.Bound", errorBound);

		// do evaluation
		conf.setConf("do.evaluation", doEvaluation);
		conf.setConf("GT", GT);
		conf.setConf("disGT", disGT);

		Driver driver = new Driver(totalRound);
		driver.run(conf);
	}
	
	public static void SpatialJoin6Par(int totalRound, String errorBound, String splitMethod, String doEvaluation,
			String numLowPar) {

//		String[] GT= {"5418840", "7007995", "1414739", "189385", "244398", "180949"};
//		5418050, 13737, 5481388, 2418078, 496874, 628857
		String[] GT = {"5503035", "13803", "5692188", "2419016", "497521", "629969"};
//		String[] GT = {"5418050", "13737", "5481388", "2418078", "496874", "628857"};
		int totalGT = 0;
		for (int i = 0; i < GT.length; i++) {
			totalGT += Integer.parseInt(GT[i]);
		}
		String[] disGT = new String[GT.length];
		for (int i = 0; i < GT.length; i++) {
			disGT[i] = String.valueOf(Integer.parseInt(GT[i]) / (double) totalGT);
		}
		System.out.println("GT = " + Arrays.toString(GT));
		System.out.println("dis GT = " + Arrays.toString(disGT));

		Configuration conf = new Configuration();

		int numPar = 6;
		String[][] paths = new String[numPar][2];
		
		for (int i=0; i<numPar; i++) {
			String[] path = new String[2];
			path[0] = "/home/xzhan261/synopsis/data/spatial/" + numPar + "p/0p_" + i+ ".csv";
			path[1] = "/home/xzhan261/synopsis/data/spatial/" + numPar + "p/1p_" + i + ".csv";
//			path[0] = "/home/xzhan261/synopsis/data/grid/" + numPar + "p/0p_" + i+ ".csv";
//			path[1] = "/home/xzhan261/synopsis/data/grid/" + numPar + "p/1p_" + i + ".csv";
			// 5418050, 13737, 5481388, 2418078, 496874, 628857
			paths[i] = path;
		}

		conf.setConf("data.paths", paths);
		conf.setConf("operator.name", "SpatialJoin");
		conf.setConf("partition.method", "GridPartition");
		conf.setConf("gridPartition.minLon", "-58.0757602");
		conf.setConf("gridPartition.minLat", "-74.4856425");
		conf.setConf("gridPartition.maxLon", "179.9837763");
		conf.setConf("gridPartition.maxLat", "83.6664731");
//		conf.setConf("gridPartition.estLevel", "7");
//		String[] buckets = { "4", "4" };
//		String[] partitions = { "1", "6" };
		String[] partitions = { "6", "1" };
//		conf.setConf("gridPartition.bucketNum", buckets);
		conf.setConf("gridPartition.parNum", partitions);
		conf.setConf("load.regex", ";");
		conf.setConf("load.skipHead", "false");

//		conf.setConf("split.method", "equal");
		conf.setConf("split.method", splitMethod);
		
//		conf.setConf("multiPartition.lowLevelNumPar", numLowPar);
//		String frequencyPath = "/home/xzhan261/synopsis/spatialJoin/" + numLowPar + "/";
//		conf.setConf("multiPartition.frequencyPath", frequencyPath);

		// set error bound
		conf.setConf("error.Bound", errorBound);

		// do evaluation
		conf.setConf("do.evaluation", doEvaluation);
		conf.setConf("GT", GT);
		conf.setConf("disGT", disGT);

		Driver driver = new Driver(totalRound);
		driver.run(conf);
	}
	
	public static void SpatialJoin8Par(int totalRound, String errorBound, String splitMethod, String doEvaluation) {

		String[] GT= {"5418091", "763525", "6906439", "753990",
				"154367", "104671", "256511", "99016"};
		int totalGT = 0;
		for (int i = 0; i < GT.length; i++) {
			totalGT += Integer.parseInt(GT[i]);
		}
		String[] disGT = new String[GT.length];
		for (int i = 0; i < GT.length; i++) {
			disGT[i] = String.valueOf(Integer.parseInt(GT[i]) / (double) totalGT);
		}
		System.out.println("GT = " + Arrays.toString(GT));
		System.out.println("dis GT = " + Arrays.toString(disGT));

		Configuration conf = new Configuration();

		int numPar = 8;
		String[][] paths = new String[numPar][2];
		
		for (int i=0; i<numPar; i++) {
			String[] path = new String[2];
			path[0] = "/home/xzhan261/vldb/data/grid/" + numPar + "p/0p_" + i+ ".csv";
			path[1] = "/home/xzhan261/vldb/data/grid/" + numPar + "p/1p_" + i + ".csv";
			paths[i] = path;
		}

		conf.setConf("data.paths", paths);
		conf.setConf("operator.name", "SpatialJoin");
		conf.setConf("partition.method", "GridPartition");
		conf.setConf("gridPartition.minLon", "-58.0757602");
		conf.setConf("gridPartition.minLat", "-74.4856425");
		conf.setConf("gridPartition.maxLon", "179.9837763");
		conf.setConf("gridPartition.maxLat", "83.6664731");
//		conf.setConf("gridPartition.estLevel", "7");
//		String[] buckets = { "4", "4" };
		String[] partitions = { "8", "1" };
//		conf.setConf("gridPartition.bucketNum", buckets);
		conf.setConf("gridPartition.parNum", partitions);
		conf.setConf("load.regex", ";");
		conf.setConf("load.skipHead", "false");

//		conf.setConf("split.method", "equal");
		conf.setConf("split.method", splitMethod);

		// set error bound
		conf.setConf("error.Bound", errorBound);

		// do evaluation
		conf.setConf("do.evaluation", doEvaluation);
		conf.setConf("GT", GT);
		conf.setConf("disGT", disGT);

		Driver driver = new Driver(totalRound);
		driver.run(conf);
	}
}
