package operator;

public class EstimationResult {
	
	private int[] mEstimation;
	private double[] mRatio;
	private double mMinRatio = Integer.MAX_VALUE;
	
	public EstimationResult(int numPartiiton) {
		this.mEstimation = new int[numPartiiton];
		this.mRatio = new double[numPartiiton];
	}
	
	public int[] getEstimation() {
		return this.mEstimation;
	}

	public double[] getRatio() {
		return this.mRatio;
	}

	public double getMinRatio() {
		return this.mMinRatio;
	}
	
	public void addEstimationAndnRatio(int parId, int estimation, double ratio) {
		this.mEstimation[parId] = estimation;
		this.mRatio[parId] = ratio;
		this.mMinRatio = Math.min(ratio, mMinRatio);
	}
	
	public void addEstimation(int parId, int estimation, double ratio) {
		this.mEstimation[parId] = estimation;
		this.mRatio[parId] = ratio;
	}
	
	public void refresh(int[] estimation, double[] ratio) {
		this.mEstimation = estimation;
		this.mRatio = ratio;
	}
	
	public void setMinOutputRatio(double minOutputRatio) {
		this.mMinRatio = minOutputRatio;
	}

}
