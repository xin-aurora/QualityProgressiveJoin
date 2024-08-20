package utils;

public class UtilsFunctionConfidenceInterval {
	/**
	 * |X - E[X]| <= R * sqrt(log(2/delta)/2n)
	 * 
	 * @param sampleMean:      X
	 * @param dataRange:       R
	 * @param sampleSize:      n
	 * @param confidenceLevel: delta
	 * @return
	 */
	public static double[] HoeffidingBoundMean(double sampleMean, double dataRange, double sampleSize,
			double confidenceLevel) {
		double[] interval = new double[2];
		double tmp = Math.log(2 / confidenceLevel) / (2 * sampleSize);
		double marginOfError = dataRange * Math.sqrt(tmp);
//		System.out.println("marginOfError = " + marginOfError + ", ");
//		System.out.print(dataRange + ", " + marginOfError + ", ");
		if (marginOfError > 0) {
			interval[0] = sampleMean - marginOfError;
			interval[1] = sampleMean + marginOfError;
		} else {
			interval[0] = sampleMean + marginOfError;
			interval[1] = sampleMean - marginOfError;
		}
		return interval;
	}

	/**
	 * |Sn - E[Sn]| <= R * sqrt(nlog(2/delta)/2)
	 * 
	 * @param sampleSum:       Sn
	 * @param dataRange:       R
	 * @param sampleSize:      n
	 * @param confidenceLevel: delta
	 * @return
	 */
	public static double[] HoeffidingBoundSum(double sampleSum, double dataRange, double sampleSize,
			double confidenceLevel) {
		double[] interval = new double[2];
		double tmp = sampleSize * Math.log(2 / confidenceLevel) / 2;
		double marginOfError = dataRange * Math.sqrt(tmp);
//		System.out.println("sampleSum = " + sampleSum + ", marginOfError = " + marginOfError);
		if (marginOfError > 0) {
			interval[0] = sampleSum - marginOfError;
			interval[1] = sampleSum + marginOfError;
		} else {
			interval[0] = sampleSum + marginOfError;
			interval[1] = sampleSum - marginOfError;
		}
		return interval;
	}

	/**
	 * |X - E[X]| <= var(X) * sqrt(2*log(3/delta) / n) + 3*R*log(3/delta)/t
	 * 
	 * @param sampleMean:     X
	 * @param dataRange:      R
	 * @param sampleSize:     n
	 * @param sampleVariance: var(X)^2
	 * @param confidenceLevel
	 * @return
	 */
	public static double[] EmpiricalBernsteinBoundMean(double sampleMean, double dataRange, double sampleSize,
			double sampleVariance, double confidenceLevel) {
		double[] interval = new double[2];
		double confidenceTmp = Math.log(3 / confidenceLevel);
		double tmp1 = Math.sqrt(2 * confidenceTmp / sampleSize);
		double tmp2 = 3 * dataRange * confidenceTmp / sampleSize;
		double marginOfError = Math.sqrt(sampleVariance) * tmp1 + tmp2;
		System.out.println("marginOfError = " + marginOfError);
		if (marginOfError > 0) {
			interval[0] = sampleMean - marginOfError;
			interval[1] = sampleMean + marginOfError;
		} else {
			interval[0] = sampleMean + marginOfError;
			interval[1] = sampleMean - marginOfError;
		}
		return interval;
	}

	/**
	 * |Sn - E[Sn]| <= var(X)*sqrt(s*n*log(3/delta)) + 3*log(3/delta)
	 * 
	 * @param sampleMean:      Sn
	 * @param dataRange:       R
	 * @param sampleSize:      n
	 * @param sampleVariance:  var(X)^2
	 * @param confidenceLevel: delta
	 * @return
	 */
	public static double[] EmpiricalBernsteinBoundSum(double sampleSum, double dataRange, double sampleSize,
			double sampleVariance, double confidenceLevel) {
		double[] interval = new double[2];
		double confidenceTmp = Math.log(3 / confidenceLevel);
		double tmp1 = Math.sqrt(2 * sampleSize * confidenceTmp);
		double tmp2 = 3 * dataRange * confidenceTmp;
		double marginOfError = Math.sqrt(sampleVariance) * tmp1 + tmp2;
		if (marginOfError > 0) {
			interval[0] = sampleSum - marginOfError;
			interval[1] = sampleSum + marginOfError;
		} else {
			interval[0] = sampleSum + marginOfError;
			interval[1] = sampleSum - marginOfError;
		}
		return interval;
	}
}
