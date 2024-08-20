package QPJ.QualityProgressiveJoin;

import java.util.Arrays;

import framework.Driver;
import utils.Configuration;

public class EquiJoinExp {
	public static void EqualJoin5Par(int totalRound, String errorBound, String splitMethod, String doEvaluation,
			int numFile) {
		String[] GT = { "1440277", "2347450", "1948329", "1530766", "1739705" };
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

		int numPar = 5;
		String[][] paths = new String[numFile][2];

		for (int i = 0; i < numFile; i++) {
			String[] path = new String[2];
//			path[0] = "/home/xzhan261/vldb/data/hash/" + numPar + "p/0p_" + i + ".csv";
//			path[1] = "/home/xzhan261/vldb/data/hash/" + numPar + "p/1p_" + i + ".csv";
			
			path[0] = "/home/xzhan261/vldb/data/sigmodHash/10p_f9/" + 10 + "p/0p_" + i + ".csv";
			path[1] = "/home/xzhan261/vldb/data/sigmodHash/10p_f9/" + 10 + "p/1p_" + i + ".csv";
			paths[i] = path;
		}

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
//		conf.setConf("split.method", "equal");
		conf.setConf("split.method", splitMethod);

		// set postpond threshold
//		conf.setConf("postponed.Threshold", "1.15");

		// set error bound
		conf.setConf("error.Bound", errorBound);

		// do evaluation
		conf.setConf("do.evaluation", doEvaluation);
		conf.setConf("GT", GT);
		conf.setConf("disGT", disGT);

		Driver driver = new Driver(totalRound);
		driver.run(conf);
	}
	
	public static void EqualJoin7Par(int totalRound, String errorBound, String splitMethod, String doEvaluation) {
		String[] GT = { "3019360", "4630283", "4221580", "3235272", "3444540",
				"13764853", "8153167"};
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

		int numPar = 7;
		String[][] paths = new String[numPar][2];

		for (int i = 0; i < numPar; i++) {
			String[] path = new String[2];
//			path[0] = "/home/xzhan261/vldb/data/hash/" + numPar + "p/0p_" + i + ".csv";
//			path[1] = "/home/xzhan261/vldb/data/hash/" + numPar + "p/1p_" + i + ".csv";
			path[0] = "/home/xzhan261/vldb/data/sigmodHash/10p_f9/" + 10 + "p/0p_" + i + ".csv";
			path[1] = "/home/xzhan261/vldb/data/sigmodHash/10p_f9/" + 10 + "p/1p_" + i + ".csv";
			paths[i] = path;
		}

		conf.setConf("data.paths", paths);
		conf.setConf("operator.name", "HashJoin");
		conf.setConf("partition.method", "HashPartition");
		conf.setConf("hashPartition.parNum", String.valueOf(numPar));
//		conf.setConf("hashPartition.bucketNum", String.valueOf(numPar * 2));
		conf.setConf("load.regex", ";");
		conf.setConf("load.skipHead", "false");
		String[] keyIdx = { "2", "2" };
		conf.setConf("hashPartition.keyIdx", keyIdx);
		String[] filterState = {};
		conf.setConf("hashPartition.filter", filterState);
//		conf.setConf("split.method", "equal");
		conf.setConf("split.method", splitMethod);

		// set postpond threshold
//		conf.setConf("postponed.Threshold", "1.15");

		// set error bound
		conf.setConf("error.Bound", errorBound);

		// do evaluation
		conf.setConf("do.evaluation", doEvaluation);
		conf.setConf("GT", GT);
		conf.setConf("disGT", disGT);

		Driver driver = new Driver(totalRound);
		driver.run(conf);
	}
	
	public static void EqualJoin8Par(int totalRound, String errorBound, String splitMethod, String doEvaluation) {
//		String[] GT = { "4267396", "8704338", "6831491", "5571091", "5812181",
//				"21677203", "13398124", "13868518", "18051077"};
		String[] GT = { "3580407", "5960815", "5370819", "4448583", "4588698",
				"17139591", "10464294", "10682656"};
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

		for (int i = 0; i < numPar; i++) {
			String[] path = new String[2];
//			path[0] = "/home/xzhan261/vldb/data/hash/" + numPar + "p/0p_" + i + ".csv";
//			path[1] = "/home/xzhan261/vldb/data/hash/" + numPar + "p/1p_" + i + ".csv";
			path[0] = "/home/xzhan261/vldb/data/sigmodHash/10p_f9/" + 10 + "p/0p_" + i + ".csv";
			path[1] = "/home/xzhan261/vldb/data/sigmodHash/10p_f9/" + 10 + "p/1p_" + i + ".csv";
			paths[i] = path;
		}

		conf.setConf("data.paths", paths);
		conf.setConf("operator.name", "HashJoin");
		conf.setConf("partition.method", "HashPartition");
		conf.setConf("hashPartition.parNum", String.valueOf(numPar));
//		conf.setConf("hashPartition.bucketNum", String.valueOf(numPar * 2));
		conf.setConf("load.regex", ";");
		conf.setConf("load.skipHead", "false");
		String[] keyIdx = { "2", "2" };
		conf.setConf("hashPartition.keyIdx", keyIdx);
		String[] filterState = {};
		conf.setConf("hashPartition.filter", filterState);
//		conf.setConf("split.method", "equal");
		conf.setConf("split.method", splitMethod);

		// set postpond threshold
//		conf.setConf("postponed.Threshold", "1.15");

		// set error bound
		conf.setConf("error.Bound", errorBound);

		// do evaluation
		conf.setConf("do.evaluation", doEvaluation);
		conf.setConf("GT", GT);
		conf.setConf("disGT", disGT);

		Driver driver = new Driver(totalRound);
		driver.run(conf);
	}

	public static void EqualJoin10Par(int totalRound, String errorBound, String splitMethod, String doEvaluation, int numPar, int numFile
			,String numLowPar) {
//		// F = 9 by fileParId
//		String[] GTTmp = {"5798546", "10451293", "8367816", "6532186", "6993528", 
//				"26882800", "17042147", "16461563", "24136233", "13621035"};
		// F = 9 by hashKey
		String[] GTTmp = {"15494631", "12687898", "12428411", "18294537", "20806912", 
				"11245934", "9674835", "10242169", "13883261", "11528559"};
		String[] GT = new String[numPar];
		for (int i=0; i<numPar; i++) {
			GT[i] = GTTmp[i];
		}
		// F = 5
		int totalGT = 0;
		for (int i = 0; i < numPar; i++) {
			totalGT += Integer.parseInt(GT[i]);
		}
		String[] disGT = new String[numPar];
		for (int i = 0; i < numPar; i++) {
			disGT[i] = String.valueOf(Integer.parseInt(GT[i]) / (double) totalGT);
		}
		System.out.println("GT = " + Arrays.toString(GT));
		System.out.println("dis GT = " + Arrays.toString(disGT));

		Configuration conf = new Configuration();

//		int numPar = 10;
		String[][] paths = new String[numFile][2];

		for (int i = 0; i < numFile; i++) {
			String[] path = new String[2];
//			path[0] = "/home/xzhan261/vldb/data/hash/" + numPar + "p/0p_" + i + ".csv";
//			path[1] = "/home/xzhan261/vldb/data/hash/" + numPar + "p/1p_" + i + ".csv";
			path[0] = "/home/xzhan261/synopsis/data/sigmodHash/10p_f9/" + 10 + "p/0p_" + i + ".csv";
			path[1] = "/home/xzhan261/synopsis/data/sigmodHash/10p_f9/" + 10 + "p/1p_" + i + ".csv";
//			path[0] = "/home/xzhan261/vldb/data/sigmodHash/30p_f9/0p_" + i + ".csv";
//			path[1] = "/home/xzhan261/vldb/data/sigmodHash/30p_f9/1p_" + i + ".csv";
			paths[i] = path;
		}

		conf.setConf("data.paths", paths);
		conf.setConf("operator.name", "HashJoin");
		conf.setConf("partition.method", "HashPartition");
		conf.setConf("hashPartition.parNum", "10");
//		conf.setConf("hashPartition.bucketNum", String.valueOf(numPar * 2));
		conf.setConf("load.regex", ";");
		conf.setConf("load.skipHead", "false");
		String[] keyIdx = { "0", "0" };
		conf.setConf("hashPartition.keyIdx", keyIdx);
		String[] filterState = {};
		conf.setConf("hashPartition.filter", filterState);
//		conf.setConf("split.method", "equal");
		conf.setConf("split.method", splitMethod);
		
		conf.setConf("multiPartition.lowLevelNumPar", numLowPar);
		String frequencyPath = "/home/xzhan261/synopsis/hashJoin/" + numLowPar + "/";
		conf.setConf("multiPartition.frequencyPath", frequencyPath);

		// set postpond threshold
//		conf.setConf("postponed.Threshold", "1.15");

		// set error bound
		conf.setConf("error.Bound", errorBound);

		// do evaluation
		conf.setConf("do.evaluation", doEvaluation);
		conf.setConf("GT", GT);
		conf.setConf("disGT", disGT);

		Driver driver = new Driver(totalRound);
		driver.run(conf);
	}

	public static void EqualJoin15Par(int totalRound, String errorBound, String splitMethod, String doEvaluation,
			int numPar, int numFile) {
		// 10p_f9
		int[] GTTmp = {9357419, 6630479, 5898631, 11666952, 12768878, 
				8753497, 8928507, 8181962, 10072238, 13043277, 
				8629649, 6803747, 8589987, 10438608, 6523316};
		String[] GT = new String[numPar];
		for (int i=0; i<numPar; i++) {
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
		System.out.println("GT = " + Arrays.toString(GT));
		System.out.println("dis GT = " + Arrays.toString(disGT));

		Configuration conf = new Configuration();

//		int numPar = 15;
		String[][] paths = new String[numFile][2];

		for (int i = 0; i < numFile; i++) {
			String[] path = new String[2];
			path[0] = "/home/xzhan261/vldb/data/sigmodHash/10p_f9/" + 10 + "p/0p_" + i + ".csv";
			path[1] = "/home/xzhan261/vldb/data/sigmodHash/10p_f9/" + 10 + "p/1p_" + i + ".csv";
//			path[0] = "/home/xzhan261/vldb/data/sigmodHash/30p_f9/0p_" + i + ".csv";
//			path[1] = "/home/xzhan261/vldb/data/sigmodHash/30p_f9/1p_" + i + ".csv";
			paths[i] = path;
		}

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
//		conf.setConf("split.method", "equal");
		conf.setConf("split.method", splitMethod);

		// set postpond threshold
//		conf.setConf("postponed.Threshold", "1.15");

		// set error bound
		conf.setConf("error.Bound", errorBound);

		// do evaluation
		conf.setConf("do.evaluation", doEvaluation);
		conf.setConf("GT", GT);
		conf.setConf("disGT", disGT);

		Driver driver = new Driver(totalRound);
		driver.run(conf);
	}

	public static void EqualJoin20Par(int totalRound, String errorBound, String splitMethod, String doEvaluation,
			int numPar, int numFile) {
		// 10p_f9
		int[] GTTmp = {8252703, 7347705, 5750335, 8937471, 13870852, 
				6462370, 4514426, 5583394, 7003641, 6135963, 
				7241928, 5340193, 6678076, 9357066, 6936060, 
				4783564, 5160409, 4658775, 6879620, 5392596};
		String[] GT = new String[numPar];
		for (int i=0; i<numPar; i++) {
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
		System.out.println("GT = " + Arrays.toString(GT));
		System.out.println("dis GT = " + Arrays.toString(disGT));

		Configuration conf = new Configuration();

//		int numPar = 10;
		String[][] paths = new String[numFile][2];

		for (int i = 0; i < numFile; i++) {
			String[] path = new String[2];
			path[0] = "/home/xzhan261/vldb/data/sigmodHash/10p_f9/" + 10 + "p/0p_" + i + ".csv";
			path[1] = "/home/xzhan261/vldb/data/sigmodHash/10p_f9/" + 10 + "p/1p_" + i + ".csv";
//			path[0] = "/home/xzhan261/vldb/data/sigmodHash/30p_f9/0p_" + i + ".csv";
//			path[1] = "/home/xzhan261/vldb/data/sigmodHash/30p_f9/1p_" + i + ".csv";
			paths[i] = path;
		}

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
//		conf.setConf("split.method", "equal");
		conf.setConf("split.method", splitMethod);

		// set postpond threshold
//		conf.setConf("postponed.Threshold", "1.15");

		// set error bound
		conf.setConf("error.Bound", errorBound);

		// do evaluation
		conf.setConf("do.evaluation", doEvaluation);
		conf.setConf("GT", GT);
		conf.setConf("disGT", disGT);

		Driver driver = new Driver(totalRound);
		driver.run(conf);
	}

	public static void EqualJoin30Par(int totalRound, String errorBound, String splitMethod, String doEvaluation, int numPar, int numFile) {
		// F = 9, by filePar id
//		String[] GTTmp = { "5546735", "2524387", "2062137", "1860479", "9353521", 
//				"4395709", "3269616", "9473694", "1938145", "276627", 
//				"8743271", "4127982", "8076492", "4112704", "2806689",
//				"1833695", "3214015", "4762702", "6703053", "10583812", 
//				"856145", "4891749", "5716373", "7559465", "1775258", 
//				"5223762", "6027954", "1662858", "2035343", "2249487"};
//		// F = 9
//		String[] GTTmp = {"4868897", "3895999", "3016415", "5412960", "9783949", 
//				"2868766", "3605609", "4364863", "4185013", "5106217", 
//				"4545677", "3349220", "5513036", "6808595", "3025126", 
//				"4282736", "2570547", "2764301", "6041873", "2815658", 
//				"5766675", "5141413", "3683938", "5679613", "7672469", 
//				"3875003", "3324950", "2895736", "3417284", "3381321"};
		// F = 9 by 10p_f9/
		int[] GTTmp = {4984343, 4020459, 3075479, 5516472, 9914301, 
				2902151, 3663113, 4450928, 4263911, 5222247, 
				4658942, 3402045, 5621898, 6969738, 3071581, 
				4373076, 2610020, 2823152, 6150480, 2854577, 
				5851346, 5265394, 3731034, 5808327, 7821030, 
				3970707, 3401702, 2968089, 3468870, 3451735};
		String[] GT = new String[numPar];
		for (int i=0; i<numPar; i++) {
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
		System.out.println("GT = " + Arrays.toString(GT));
		System.out.println("dis GT = " + Arrays.toString(disGT));

		Configuration conf = new Configuration();

//		int numPar = 10;
		String[][] paths = new String[numFile][2];

		for (int i = 0; i < numFile; i++) {
			String[] path = new String[2];
			path[0] = "/home/xzhan261/vldb/data/sigmodHash/10p_f9/" + 10 + "p/0p_" + i + ".csv";
			path[1] = "/home/xzhan261/vldb/data/sigmodHash/10p_f9/" + 10 + "p/1p_" + i + ".csv";
//			path[0] = "/home/xzhan261/vldb/data/sigmodHash/30p_f9/0p_" + i + ".csv";
//			path[1] = "/home/xzhan261/vldb/data/sigmodHash/30p_f9/1p_" + i + ".csv";
			paths[i] = path;
		}

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
//		conf.setConf("split.method", "equal");
		conf.setConf("split.method", splitMethod);

		// set postpond threshold
//		conf.setConf("postponed.Threshold", "1.15");

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
