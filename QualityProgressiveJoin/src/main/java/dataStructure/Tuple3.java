package dataStructure;

/**
 * The implementation is extended based on
 * https://github.com/javatuples/javatuples,
 * https://github.com/javatuples/javatuples/blob/master/src/main/java/org/javatuples/Triplet.java
 * 
 * @author xin_aurora
 *
 * @param <A>
 * @param <B>
 * @param <C>
 */
public class Tuple3<A, B, C> extends Tuple {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4659628598237758069L;

	private static final int SIZE = 3;

	private final A val0;
	private final B val1;
	private final C val2;
	private int mHashKey = -1;

	public Tuple3(final A value0, final B value1, final C value2) {
		super(value0, value1, value2);
		this.val0 = value0;
		this.val1 = value1;
		this.val2 = value2;
	}

	public A getValue0() {
		return this.val0;
	}

	public B getValue1() {
		return this.val1;
	}

	public C getValue2() {
		return this.val2;
	}

	public int computeHashkey(int keyIdx) {
		if (keyIdx == 0) {
			this.mHashKey = val0.hashCode();
		} else if (keyIdx == 1) {
			this.mHashKey = val1.hashCode();
		} else {
			this.mHashKey = val2.hashCode();
		}

		return this.mHashKey;
	}

	public int getHashkey(int keyIdx) {
		return this.mHashKey;
	}

	@Override
	public int getSize() {
		return SIZE;
	}

}
