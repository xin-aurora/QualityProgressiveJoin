package motivation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class DataGenerator {

	double minLon = Double.MAX_VALUE;
	double minlat = Double.MAX_VALUE;
	double maxLon = Double.MIN_VALUE;
	double maxLat = Double.MIN_VALUE;

	public DataGenerator(String folder, String bit03Path, String uniformPath) {

		generator1D(bit03Path, folder + "1d/bit02mo.csv", 1000, 2000);

		generator1D(uniformPath, folder + "1d/bit04mo.csv", 1050, 2050);

//		System.out.println("minLon = " + minLon + ", maxLon = " + maxLon);
	}

	public void generator1D(String filePath, String outputPath, int minB, int maxB) {
//		Random random = new Random();
//		int boundary = random.nextInt(maxB - minB + 1) + minB;
		double min = Double.MAX_VALUE;
		double max = Double.MIN_VALUE;
//		minLon = Math.min(min, minLon);
//		maxLon = Math.max(max, maxLon);
//		if (min + boundary > maxB) {
//			max = min;
//			min = boundary - min;
//		}
//		int numRecord = random.nextInt(5000000 - 3000000 + 1) + 3000000;
		int numRecord = 1000000;
		System.out.println(outputPath);
//		System.out.println("min = " + minB + ", max = " + maxB);
//		System.out.println("boundary = " + boundary + ", numRecord = " + numRecord);

		File file = new File(filePath);

		BufferedReader reader;
		try {
			FileWriter myWriter = new FileWriter(outputPath, false);
			reader = new BufferedReader(new FileReader(file));
			reader.readLine(); // reader header
			String line = reader.readLine();
			int cnt = 1;
			String header = minB + "," + maxB + "\n";
			myWriter.write(header);
			while (cnt < numRecord) {

				double pos = Double.parseDouble(line.split(",")[0]);
				double newPos = pos * 1000 + minB;
				min = Math.min(min, newPos);
				max = Math.max(max, newPos);
				String str = newPos + "\n";
				myWriter.write(str);
				cnt++;
				line = reader.readLine();
			}
			myWriter.close();
			reader.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("minLon = " + min + ", maxLon = " + max);

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String folder = "/Users/xin_aurora/Downloads/Work/2019/UCR/Research/Spatial/sketches/data/medium/point/";
		String uniformPath = folder + "bit04.csv";
		String bit03 = folder + "bit02.csv";
//		String bit07 = folder + "bit07.csv";
//		boolean is1d = true;

		DataGenerator generator = new DataGenerator(folder, bit03, uniformPath);
	}

}
