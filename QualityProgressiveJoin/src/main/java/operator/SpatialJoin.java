package operator;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.apache.hadoop.util.IndexedSortable;
import org.apache.hadoop.util.QuickSort;

import dataStructure.DoubleTuple4;

public class SpatialJoin {

	public interface SJResult {
		void accept(int i, int j, double[] refPoint);
	}
	

	public static Queue<String> SpatialJoinTuple(List<DoubleTuple4> recordList1, List<DoubleTuple4> recordList2) {
		Queue<String> results = new LinkedList<String>();
		
		double[][] coords1 = extractAttributesToArray(recordList1);
		double[][] coords2 = extractAttributesToArray(recordList2);
		double[][] minCoord1 = new double[][]{coords1[1], coords1[2]};
		double[][] maxCoord1 =  new double[][]{coords1[3], coords1[4]};

		double[][] minCoord2 = new double[][]{coords2[1], coords2[2]};
		double[][] maxCoord2 = new double[][]{coords2[3], coords2[4]};
		

		int joinSize = PlaneSweepRectangles(minCoord1, maxCoord1, minCoord2, maxCoord2, (i,j, refPoint) -> {
		      results.add((int) coords1[0][i]+","+(int) coords2[0][j]);
		    }, true, true);
		
//		System.out.println("Join size = " + joinSize + ", results size = " + results.size());
		
		return results;
	}

	public static int PlaneSweepRectangles(double[][] minCoord1, double[][] maxCoord1, double[][] minCoord2,
			double[][] maxCoord2, SJResult results, boolean sortC1, boolean sortC2) {

//		System.out.println("plane sweep join in updated algorithm");
//		long t1 = System.nanoTime();
		assert minCoord1.length == maxCoord1.length;
		assert minCoord1.length == minCoord2.length;
		assert minCoord1.length == maxCoord2.length;
		int count = 0;
		int numDimensions = minCoord1.length;
		int n1 = minCoord1[0].length;
		int n2 = minCoord2[0].length;
//		LOG.debug(String.format("Running the plane sweep join algorithm between lists sizes of %d and %d", n1, n2));
		// Generate indexes to report the answer
		int[] indexes1 = new int[n1];
		int[] indexes2 = new int[n2];
		for (int $i = 0; $i < n1; $i++)
			indexes1[$i] = $i;
		for (int $i = 0; $i < n2; $i++)
			indexes2[$i] = $i;
//		long localMBRTests = 0;
		// Sort by the first coordinate and keep track of the array index for each entry
		// for reporting
		QuickSort sorter = new QuickSort();

		if (sortC1) {
			IndexedSortable sortable1 = new IndexedSortable() {
				@Override
				public int compare(int i, int j) {
					return (int) Math.signum(minCoord1[0][i] - minCoord1[0][j]);
				}

				@Override
				public void swap(int i, int j) {
					if (i == j)
						return;
					// Swap indexes
					indexes1[i] ^= indexes1[j];
					indexes1[j] ^= indexes1[i];
					indexes1[i] ^= indexes1[j];
					// Swap coordinates
					for (int $d = 0; $d < numDimensions; $d++) {
						double t = minCoord1[$d][i];
						minCoord1[$d][i] = minCoord1[$d][j];
						minCoord1[$d][j] = t;
						t = maxCoord1[$d][i];
						maxCoord1[$d][i] = maxCoord1[$d][j];
						maxCoord1[$d][j] = t;
					}
				}
			};
			sorter.sort(sortable1, 0, n1);
		}
		if (sortC2) {
			IndexedSortable sortable2 = new IndexedSortable() {
				@Override
				public int compare(int i, int j) {
					return (int) Math.signum(minCoord2[0][i] - minCoord2[0][j]);
				}

				@Override
				public void swap(int i, int j) {
					if (i == j)
						return;
					// Swap indexes
					indexes2[i] ^= indexes2[j];
					indexes2[j] ^= indexes2[i];
					indexes2[i] ^= indexes2[j];
					// Swap coordinates
					for (int $d = 0; $d < numDimensions; $d++) {
						double t = minCoord2[$d][i];
						minCoord2[$d][i] = minCoord2[$d][j];
						minCoord2[$d][j] = t;
						t = maxCoord2[$d][i];
						maxCoord2[$d][i] = maxCoord2[$d][j];
						maxCoord2[$d][j] = t;
					}
				}
			};
			sorter.sort(sortable2, 0, n2);
		}

		// Now, run the planesweep algorithm
		int i = 0, j = 0;
		double[] refPoint = new double[numDimensions];
		while (i < n1 && j < n2) {
			if (minCoord1[0][i] < minCoord2[0][j]) {
				// R1[i] is the left-most rectangle. Activate it and compare to all rectangles
				// R2 until passing the right end
				// of R1[i]
				int jj = j;
				while (jj < n2 && minCoord2[0][jj] < maxCoord1[0][i]) {
//					localMBRTests++;
					// Compare the two rectangles R1[i] and R2[jj] and report if needed
					if (rectanglesOverlap(minCoord1, maxCoord1, i, minCoord2, maxCoord2, jj)) {
						// Found an overlap
						count++;
						if (results != null) {
							for (int d = 0; d < numDimensions; d++)
								refPoint[d] = Math.max(minCoord1[d][i], minCoord2[d][jj]);
							results.accept(indexes1[i], indexes2[jj], refPoint);
						}
					}
					jj++;
				}
				// Skip until the first record that might produce a result from i
				do {
					i++;
				} while (i < n1 && maxCoord1[0][i] < minCoord2[0][j]);
			} else {
				// R2[j] is the left-most rectangle. Activate it and compare to all rectangles
				// of R1 until passing the right
				// end of R2[j]
				int ii = i;
				while (ii < n1 && minCoord1[0][ii] < maxCoord2[0][j]) {
					// Compare the two rectangles R1[ii] and R2[j] and report if needed
//					localMBRTests++;
					if (rectanglesOverlap(minCoord1, maxCoord1, ii, minCoord2, maxCoord2, j)) {
						// Found an overlap
						count++;
						if (results != null) {
							for (int d = 0; d < numDimensions; d++)
								refPoint[d] = Math.max(minCoord1[d][ii], minCoord2[d][j]);
							results.accept(indexes1[ii], indexes2[j], refPoint);
						}
					}
					ii++;
				}
				// Skip until the first record that might produce a result from j
				do {
					j++;
				} while (j < n2 && maxCoord2[0][j] < minCoord1[0][i]);
			}
		}
//		if (mbrTests != null)
//			mbrTests.accumulate(localMBRTests);
//		System.out.println("localMBRTests = " + localMBRTests);
		return count;

	}

	/**
	 * Test if two rectangles overlap R1[i] and R2[j].
	 * 
	 * @param minCoord1 the lower corners of the rectangles in the first set
	 * @param maxCoord1 the upper corners of the rectangles in the first set
	 * @param i         the index of the first rectangle
	 * @param minCoord2 the lower corners of the rectangles in the second set
	 * @param maxCoord2 the upper corners of the rectangles in the second set
	 * @param j         the index of the second rectangle
	 * @return {@code true} if the rectangles overlap, i.e., not disjoint, and false
	 *         if they are disjoint
	 */
	static final boolean rectanglesOverlap(double[][] minCoord1, double[][] maxCoord1, int i, double[][] minCoord2,
			double[][] maxCoord2, int j) {
		assert minCoord1.length == 2;
		return !(minCoord1[0][i] >= maxCoord2[0][j] || minCoord2[0][j] >= maxCoord1[0][i]
				|| minCoord1[1][i] >= maxCoord2[1][j] || minCoord2[1][j] >= maxCoord1[1][i]);
	}
	
	public static double[][] extractAttributesToArray(List<DoubleTuple4> recordList) {
		int listSize = recordList.size();
		double[][] coords = new double[5][listSize];
		for (int i = 0; i < listSize; i++) {
			coords[0][i] = i;
			coords[1][i] = recordList.get(i).toArray()[0];
			coords[2][i] = recordList.get(i).toArray()[1];
			coords[3][i] = recordList.get(i).toArray()[2];
			coords[4][i] = recordList.get(i).toArray()[3];
		}

		return coords;
	}
}
