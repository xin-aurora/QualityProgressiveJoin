package dataStructure;

/**
 * The implementation is extended based on https://github.com/javatuples/javatuples,
 * https://github.com/javatuples/javatuples/blob/master/src/main/java/org/javatuples/Triplet.java
 * @author xin_aurora
 *
 * @param <A>
 * @param <B>
 * @param <C>
 * @param <D>
 */
public class Tuple4<A, B, C, D> extends Tuple {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4659628598237758069L;

	private static final int SIZE = 4;

	private final A val0;
	private final B val1;
	private final C val2;
	private final D val3;
	
	public Tuple4(
			final A value0, 
			final B value1,
			final C value2,
			final D value3) {
		super(value0, value1, value2, value3);
        this.val0 = value0;
        this.val1 = value1;
        this.val2 = value2;
        this.val3 = value3;
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
    
    public D getValue3() {
        return this.val3;
    }

    @Override
    public int getSize() {
        return SIZE;
    }

}
