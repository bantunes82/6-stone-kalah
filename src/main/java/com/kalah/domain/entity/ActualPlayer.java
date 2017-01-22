/**
 * 
 */
package com.kalah.domain.entity;

/**
 * @author bruno.romao.antunes
 *
 */
public enum ActualPlayer {
	
	PLAYER_ONE(1), PLAYER_TWO(2);
	
	private int playerNumer;
	
	private ActualPlayer(int playerNumer){
		this.playerNumer = playerNumer;
	}
	/**
	 * @return the playerNumer
	 */
	public int getPlayerNumer() {
		return playerNumer;
	}
	
	

}
