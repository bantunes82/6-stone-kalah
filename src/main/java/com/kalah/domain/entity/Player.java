/**
 * 
 */
package com.kalah.domain.entity;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;

import com.kalah.exception.LastStoneOnEmptyPitException;
import com.kalah.exception.LastStoneOnOwnKalahException;

/**
 * @author  bruno.romao.antunes
 */
public class Player implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7398689355841429087L;

	@Indexed
	private int id;

	/**
	 * 
	 */
	private int kalah;
	/**
	 * 
	 */
	private List<Integer> pits;
	
	@Transient
	private int pitIndex;
	
	@Transient
	private int stonesNumber;

	/**
	 * 
	 */
	private Player() {
	}
	
	/**
	 * 
	 * @param actualPlaying
	 */
	public Player(int id){
		this.id = id;
		this.kalah = 0;
		this.pits = Arrays.asList(6,6,6,6,6,6);
	}

	/**
	 * 
	 * @param pitNumber
	 * @return
	 */
	public int removeStonesFromPit(int pitNumber){
		int stones = pits.get(pitNumber);
		pits.set(pitNumber, 0);
		
		return stones;
	}
	
	/**
	 * 
	 * @param pitNumber
	 * @param stonesNumber
	 * @return
	 */
	public int putStonesOnPitsAndKalah(int pitIndex,int stonesNumber){
		this.pitIndex = pitIndex;
		this.stonesNumber = stonesNumber;
		
		for(;this.pitIndex < pits.size() && this.stonesNumber > 0;this.stonesNumber--,this.pitIndex++){
			
			if(this.stonesNumber==1 && pits.get(this.pitIndex)==0 ){
				throw new LastStoneOnEmptyPitException(this.pitIndex);
			}
			
			pits.set(this.pitIndex, pits.get(this.pitIndex)+1);
		}
		
		if(this.stonesNumber >0){
			kalah++;
			if(this.stonesNumber==1){
				throw new LastStoneOnOwnKalahException();
			}
			
			this.stonesNumber--;
		}
		
		return this.stonesNumber;
	}
	
	/**
	 * 
	 * @param stonesNumber
	 * @return
	 */
	public int putStonesOnPits(int stonesNumber) {
		this.stonesNumber = stonesNumber;
		
		for(pitIndex=0;pitIndex < pits.size() && this.stonesNumber > 0;this.stonesNumber--,pitIndex++){
						
			pits.set(pitIndex, pits.get(pitIndex)+1);
		}
			
		return this.stonesNumber;
	}
	
	/**
	 * 
	 * @param stonesNumber
	 */
	public void incrementKalah(int stonesNumber){
		this.kalah = this.kalah + stonesNumber;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean verifyIfallPitsAreEmpty(){
		return pits.stream().count()==0;
	}
	
	/**
	 * 
	 */
	public void moveAllStonesFromPitsToKalah(){
		this.kalah = (int) (this.kalah + pits.stream().count());
		
		pits.forEach(i-> i=0);
		
	}

	/**
	 * @return the kalah
	 */
	public int getKalah() {
		return kalah;
	}

	/**
	 * @return the pits
	 */
	public List<Integer> getPits() {
		return pits;
	}

	/**
	 * @return the playerNumber
	 */
	public int getId() {
		return id;
	}
	
	
	
}