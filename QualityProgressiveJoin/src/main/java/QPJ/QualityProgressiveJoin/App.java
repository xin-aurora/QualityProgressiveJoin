package QPJ.QualityProgressiveJoin;

import java.io.IOException;
import java.util.Arrays;

import framework.Driver;
import utils.Configuration;

/**
 * Hello world!
 *
 */
public class App 
{
	public static void SpatialJoinTest() {

		int numPar = 4;
		int numFile = 4;
		int[] GTTmp = { 19542, 9710, 120172, 353601 };
		String[] GT = new String[numPar];
		for (int i = 0; i < numPar; i++) {
//			GT[i] = GTTmp[i]; 
			GT[i] = String.valueOf(GTTmp[i]);
		}
		double totalGT = 0.0;
		for (int i = 0; i < numPar; i++) {
//			totalGT += Integer.parseInt(GT[i]);
			totalGT += GTTmp[i];
		}
		String[] disGT = new String[numPar];
		for (int i = 0; i < numPar; i++) {
//			disGT[i] = String.valueOf(Integer.parseInt(GT[i]) / (double) totalGT);
			disGT[i] = String.valueOf(GTTmp[i] / totalGT);
		}

		String[][] paths = new String[numFile][2];
		String pathTmp0 = "/Users/xin_aurora/Downloads/Work/2019/UCR/" + "Research/Spatial/Progressive_Join/newExp/"
				+ "sparkStructure/spatialJoin/1p/4p/0p_";
		String pathTmp1 = "/Users/xin_aurora/Downloads/Work/2019/UCR/" + "Research/Spatial/Progressive_Join/newExp/"
				+ "sparkStructure/spatialJoin/1p/4p/1p_";

		for (int i = 0; i < numFile; i++) {
			String[] path = new String[2];
			path[0] = pathTmp0 + i + ".csv";
			path[1] = pathTmp1 + i + ".csv";
			paths[i] = path;
			System.out.println(Arrays.toString(path));
		}

		Configuration conf = new Configuration();

		conf.setConf("data.paths", paths);
		conf.setConf("operator.name", "SpatialJoin");
		conf.setConf("partition.method", "GridPartition");
		conf.setConf("gridPartition.minLon", "-58.0757602");
		conf.setConf("gridPartition.minLat", "-74.4856425");
		conf.setConf("gridPartition.maxLon", "179.9837763");
		conf.setConf("gridPartition.maxLat", "83.6664731");
		conf.setConf("gridPartition.estLevel", "7");
//		String[] buckets = { "4", "4" };
		String[] partitions = { "1", "4" };
//		String[] partitions = {"1", "4"};
//		conf.setConf("gridPartition.bucketNum", buckets);
		conf.setConf("gridPartition.parNum", partitions);
		conf.setConf("load.regex", ";");
		conf.setConf("load.skipHead", "false");

//		conf.setConf("split.method", "equal");
		conf.setConf("split.method", "balance");

		// multi-level partition
		conf.setConf("multiPartition.lowLevelNumPar", "4");
		String frequencyPath = "/Users/xin_aurora/Downloads/Work/2019/UCR/Research/Spatial/Progressive_Join"
				+ "/newExp/sparkStructure/equalJoin/1p/20partitions/multiSpatialPar/";
		conf.setConf("multiPartition.frequencyPath", frequencyPath);

		// set error bound
		conf.setConf("error.Bound", "0.0");

		// do evaluation
		conf.setConf("do.evaluation", "false");
		conf.setConf("GT", GT);
		conf.setConf("disGT", disGT);

		int totalRound = 3;
		Driver driver = new Driver(totalRound);
		driver.run(conf);
	}

	public static void HashJoinTest() {
		System.out.println("HashJoinTest");
		String[] GT = { "1424440", "1393629", "1153499", "1137200" };
//		String[] GT = {"1635680", "852825", "718678", "615412"};
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
		int numFile = 2;
		String[][] paths = new String[numFile][2];
		String pathTmp0 = "/Users/xin_aurora/Downloads/Work/2019/UCR/Research/Spatial/Progressive_Join"
				+ "/newExp/sparkStructure/equalJoin/1p/20partitions/20p/0p_";
		String pathTmp1 = "/Users/xin_aurora/Downloads/Work/2019/UCR/Research/Spatial/Progressive_Join"
				+ "/newExp/sparkStructure/equalJoin/1p/20partitions/20p/1p_";

		for (int i = 0; i < numFile; i++) {
			String[] path = new String[2];
			path[0] = pathTmp0 + i + ".csv";
			path[1] = pathTmp1 + i + ".csv";
			paths[i] = path;
			System.out.println(Arrays.toString(path));
		}

//		conf.setConf("data.paths", paths);
//		conf.setConf("operator.name", "HashJoin");
//		conf.setConf("partition.method", "HashPartition");
//		conf.setConf("hashPartition.parNum", String.valueOf(numPar));
//		conf.setConf("load.regex", ";");
////		conf.setConf("load.skipHead", "false");
//		String[] keyIdx = { "0", "0" };
//		conf.setConf("hashPartition.keyIdx", keyIdx);
//		String[] filterState = {};
//		conf.setConf("hashPartition.filter", filterState);
//		conf.setConf("split.method", "equal");
////		conf.setConf("split.method", "balance");
		// multi-level partition
		conf.setConf("multiPartition.lowLevelNumPar", "5");
		String frequencyPath = "/Users/xin_aurora/Downloads/Work/2019/UCR/Research/Spatial/Progressive_Join"
				+ "/newExp/sparkStructure/equalJoin/1p/20partitions/multiHashPar/";
		conf.setConf("multiPartition.frequencyPath", frequencyPath);
//
//		// set postpond threshold
////		conf.setConf("postponed.Threshold", "1.15");
//
//		// set error bound
//		// k * varepsilon
//		conf.setConf("error.Bound", "0.0");
//
//		// do evaluation
//		conf.setConf("do.evaluation", "false");
//		conf.setConf("GT", GT);
//		conf.setConf("disGT", disGT);
		
		conf.setConf("data.paths", paths);
		conf.setConf("operator.name", "HashJoin");
		conf.setConf("partition.method", "HashPartition");
		conf.setConf("hashPartition.parNum", String.valueOf(numPar));
//		conf.setConf("hashPartition.bucketNum", String.valueOf(numPar * 2));
		conf.setConf("load.regex", ";");
		conf.setConf("load.skipHead", "false");
		String[] keyIdx = { "0", "0" };
		conf.setConf("hashPartition.keyIdx", keyIdx);
		String[] filterState = {};
		conf.setConf("hashPartition.filter", filterState);
		conf.setConf("split.method", "equal");

		// set postpond threshold
//		conf.setConf("postponed.Threshold", "1.15");

		// set error bound
		conf.setConf("error.Bound", "0.0");

		// do evaluation
		conf.setConf("do.evaluation", "true");
		conf.setConf("GT", GT);
		conf.setConf("disGT", disGT);

		int totalRound = 3;

		Driver driver = new Driver(totalRound);
		driver.run(conf);
	}

	public static void main(String[] args) throws IOException, ClassNotFoundException {

		// get ground truth
		// "HashJoin", "SpatialJoin"
		String joinType = "HashJoin";
//		String joinType = "SpatialJoin";
		int numPar = 2;
		String errorBound = "";
		String splitMethod = "";
		String doEvaluation = "";
		int totalRound = 0;
		int numFile = 2;
		String numLowPar = "";
		if (args.length > 0) {
			for (int i = 0; i < args.length; i++) {
				if (args[i].equals("-joinType")) {
					joinType = args[++i];
				} else if (args[i].equals("-numPar")) {
					numPar = Integer.parseInt(args[++i]);
				} else if (args[i].equals("-errorBound")) {
					errorBound = args[++i];
				} else if (args[i].equals("-splitMethod")) {
					splitMethod = args[++i];
				} else if (args[i].equals("-doEvaluation")) {
					doEvaluation = args[++i];
				} else if (args[i].equals("-totalRound")) {
					totalRound = Integer.parseInt(args[++i]);
				} else if (args[i].equals("-numFile")) {
					numFile = Integer.parseInt(args[++i]);
				} else if (args[i].equals("-numLowPar")) {
					numLowPar = args[++i];
				}
			}
		}
		if (joinType.toLowerCase().equals("hashjoin")) {
			if (numPar == 30) {
				EquiJoinExp.EqualJoin30Par(totalRound, errorBound, splitMethod, doEvaluation, numPar, numFile);
			} else if (numPar == 20) {
				EquiJoinExp.EqualJoin20Par(totalRound, errorBound, splitMethod, doEvaluation, numPar, numFile);
			} else if (numPar == 15) {
				EquiJoinExp.EqualJoin15Par(totalRound, errorBound, splitMethod, doEvaluation, numPar, numFile);
			} else if (numPar == 10) {
				EquiJoinExp.EqualJoin10Par(totalRound, errorBound, splitMethod, doEvaluation, numPar, numFile,
						numLowPar);
			} else if (numPar == 5) {
				EquiJoinExp.EqualJoin5Par(totalRound, errorBound, splitMethod, doEvaluation, numFile);
			} else {
				HashJoinTest();
			}
		} else {
			if (numPar == 4) {
				SpatialJoinExp.SpatialJoin4Par(totalRound, errorBound, splitMethod, doEvaluation);
			} else if (numPar == 6) {
				SpatialJoinExp.SpatialJoin6Par(totalRound, errorBound, splitMethod, doEvaluation, numLowPar);
			} else if (numPar == 8) {
				SpatialJoinExp.SpatialJoin8Par(totalRound, errorBound, splitMethod, doEvaluation);
			} else {
				SpatialJoinTest();
			}
		}
	}
}
