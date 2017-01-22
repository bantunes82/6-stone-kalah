/**
 * 
 */
package com.kalah.exception;

/**
 * @author bruno.romao.antunes
 *
 */
public class LastStoneOnEmptyPitException extends RuntimeException{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4924344101461132336L;
	private final int indexPit;
	
	
	public LastStoneOnEmptyPitException(int indexPit){
		super();
		this.indexPit = indexPit;
	}


	/**
	 * @return the indexPit
	 */
	public int getIndexPit() {
		return indexPit;
	}
	
	

}
