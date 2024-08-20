package io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import optHistogram.SimpleHistogramOpt;
import optHistogram.SimpleSpatialHistogramOpt;

public class LoadHistogram {

	public LoadHistogram() {

	}

	public SimpleHistogramOpt loadSimpleHistogramOpt(String path, int srcNum) {

		SimpleHistogramOpt histogram = null;
		File file = new File(path);

		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));

			String[] header = reader.readLine().split(",");
			double min = Double.parseDouble(header[0]);
			double max = Double.parseDouble(header[1]);
			histogram = new SimpleHistogramOpt(min, max, srcNum);
			String line = reader.readLine();
			while (line != null) {

//				histogram.addRecord(Double.parseDouble(line));
				histogram.addRecordNonUniform(Double.parseDouble(line));

				line = reader.readLine();
			}
			reader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return histogram;
	}
	
	public SimpleHistogramOpt loadSimpleHistogramOptBysHist(String path, SimpleHistogramOpt histogram) {

		File file = new File(path);

		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));

			String[] header = reader.readLine().split(",");
			String line = reader.readLine();
			while (line != null) {

				histogram.addRecordNonUniform(Double.parseDouble(line));

				line = reader.readLine();
			}
			reader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return histogram;
	}

	public SimpleSpatialHistogramOpt loadSimpleSpatialHistogramOpt(String path, int lon, int lat) {

		SimpleSpatialHistogramOpt histogram = null;
		File file = new File(path);

		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));

			String[] header = reader.readLine().split(",");
			double minLon = 0;
			double maxLon = 0;
			double minLat = 0;
			double maxLat = 0;
			if (header.length > 2) {
				minLon = Double.parseDouble(header[0].split(":")[1].trim());
				maxLon = Double.parseDouble(header[1].split(":")[1].trim());
				minLat = Double.parseDouble(header[2].split(":")[1].trim());
				maxLat = Double.parseDouble(header[3].split(":")[1].trim());
			} else {
				minLon = Double.parseDouble(header[0]);
				maxLon = Double.parseDouble(header[1]);
				minLat = Double.parseDouble(header[0]);
				maxLat = Double.parseDouble(header[1]);
			}
			histogram = new SimpleSpatialHistogramOpt(minLon, minLat, maxLon, maxLat, lon, lat);
			String line = reader.readLine();
			while (line != null) {
				String[] tem = line.split(",");

//				histogram.addRecordNonUnitform(Double.parseDouble(tem[0]), Double.parseDouble(tem[1]));
				histogram.addRecord(Double.parseDouble(tem[0]), Double.parseDouble(tem[1]));

				line = reader.readLine();
			}
			reader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return histogram;
	}
	
	
	public SimpleSpatialHistogramOpt loadSimpleSpatialHistogramOptByHist(String[] paths, SimpleSpatialHistogramOpt histogram) {

		for (int i=0; i<paths.length; i++) {
			String path = paths[i];
			File file = new File(path);

			try {
				BufferedReader reader = new BufferedReader(new FileReader(file));

				String header = reader.readLine();
				
				String line = reader.readLine();
				while (line != null) {
					String[] tem = line.split(",");

					histogram.addRecordNonUnitform(Double.parseDouble(tem[0]), Double.parseDouble(tem[1]));

					line = reader.readLine();
				}
				reader.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		

		return histogram;
	}

}
