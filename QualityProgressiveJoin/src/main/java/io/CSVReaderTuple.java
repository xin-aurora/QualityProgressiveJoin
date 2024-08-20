package io;


public abstract class CSVReaderTuple {
	
	protected String mRegex;
	
	/** Directs the record reader to skip the first line in the file */
	protected boolean mSkipHeaderLine;
	
	
	public CSVReaderTuple(String regex, boolean skipHeaderLine) {
		this.mRegex = regex;
		this.mSkipHeaderLine = skipHeaderLine;
	}
	
	// TODO: add initialize method to set mRegex and skip head
	
	public void updateSettings(String regex, boolean skipHeaderLine) {
		this.mRegex = regex;
		this.mSkipHeaderLine = skipHeaderLine;
	}
}
