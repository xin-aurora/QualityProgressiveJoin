package dataStructure;

import java.io.Serializable;
import java.util.Arrays;

public class StringTuple2 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4555071459185032762L;

	private int mHashKey = -1;
//	private static final int SIZE = 2;
	private String[] mValue = new String[2];

	public StringTuple2(String val0, String val1) {
		this.mValue[0] = val0;
		this.mValue[1] = val1;
	}
	
	public StringTuple2(String val0, String val1, int mHashKey) {
		this.mValue[0] = val0;
		this.mValue[1] = val1;
		this.mHashKey = mHashKey;
	}
	

	public String[] toArray() {
		return this.mValue;
	}
	
	public int computeHashkey(int keyIdx) {
		this.mHashKey = mValue[keyIdx].hashCode();
		return this.mHashKey;
	}

	public int getHashkey(int keyIdx) {
		return this.mHashKey;
	}
	
	// return size of the tuple
	public int getSize() {
		return 2;
	}

	@Override
	public final String toString() {
		return Arrays.toString(this.mValue);
	}

}
