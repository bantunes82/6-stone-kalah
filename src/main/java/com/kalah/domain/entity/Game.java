/**
 * 
 */
package com.kalah.domain.entity;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.kalah.exception.LastStoneOnEmptyPitException;
import com.kalah.exception.LastStoneOnOwnKalahException;

/**
 * @author bruno.romao.antunes
 *
 */
@Document(collection="games")
public class Game implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2314584259838425342L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Game.class);

	@Id
	private String name;
	
	private ActualPlayer actualPlayer;
	
	private Winner winner;
	
	private Status status;
	
	private List<Player> players;

	private Game() {
	}

	public Game(String name) {
		this.name = name;
		this.actualPlayer = ActualPlayer.PLAYER_ONE;
		Player playerOne = new Player(ActualPlayer.PLAYER_ONE.getPlayerNumer());
		Player playerTwo = new Player(ActualPlayer.PLAYER_TWO.getPlayerNumer());
		players = Arrays.asList(playerOne,playerTwo);
		this.status = Status.NOT_STARTED;
		this.winner = Winner.NO_ONE_YET;
	}
	
	/**
	 * 
	 * @param name
	 * @param players
	 */
	public Game(String name,List<Player> players) {
		this.name = name;
		this.players = players;
	}

	/**
	 * 
	 * @param pitNumber
	 * @return
	 */
	public Game play(int pitNumber) {
		this.status = Status.STARTED;
		
		try{
			play(getPlayerPlaying(),getPlayerWaiting(),pitNumber);
			changeActualPlayerToNextPlay();
		}catch(LastStoneOnOwnKalahException e){
			LOGGER.info("Last Stone On Own Kalah",e);
		}finally{
			tryFinalizeGame();
		}

		return this;
	}

	/**
	 * 
	 * @param playerPlaying
	 * @param playerWaiting
	 * @param pitNumber
	 */
	private void play(Player playerPlaying, Player playerWaiting, int pitNumber) {
		
		int stonesNumber = playerPlaying.removeStonesFromPit(pitNumber);

		while (stonesNumber > 0) {
			try {
				stonesNumber = playerPlaying.putStonesOnPitsAndKalah(pitNumber + 1, stonesNumber);
			} catch (LastStoneOnEmptyPitException e) {
				LOGGER.info("Last Stone On Empty Pit",e);
				stonesNumber=0;
				removeFromPlayerWaitingPitAndPutOnPlayerPlayingKalah(playerPlaying,playerWaiting,e.getIndexPit());
			} 
			
			if (stonesNumber > 0) {
				stonesNumber = playerWaiting.putStonesOnPits(stonesNumber);
			}
		}
	}
	
	/**
	 * 
	 * @param playerPlaying
	 * @param playerWaiting
	 * @param pitNumber
	 */
	private void removeFromPlayerWaitingPitAndPutOnPlayerPlayingKalah(Player playerPlaying, Player playerWaiting, int pitNumber){
		int stonesNumber = playerWaiting.removeStonesFromPit(pitNumber);
		playerPlaying.incrementKalah(stonesNumber+1);		
	}
	
	/**
	 * 
	 */
	private void changeActualPlayerToNextPlay(){
		if (actualPlayer.equals(ActualPlayer.PLAYER_ONE)){
			actualPlayer = ActualPlayer.PLAYER_TWO;
		}else{
			actualPlayer = ActualPlayer.PLAYER_ONE;
		}
	}
	
	/**
	 * 
	 */
	private void tryFinalizeGame(){
		if(getPlayerOne().verifyIfallPitsAreEmpty()){
			finalizeGame(getPlayerTwo());
		}else if(getPlayerTwo().verifyIfallPitsAreEmpty()){
			finalizeGame(getPlayerOne());
		}
	}
	
	/**
	 * 
	 * @param player
	 */
	private void finalizeGame(Player player){
		player.moveAllStonesFromPitsToKalah();
		this.status = Status.FINALIZED;
		
		if(getPlayerOne().getKalah()> getPlayerTwo().getKalah()){
			this.winner = Winner.PLAYER_ONE;
		}else if( getPlayerTwo().getKalah()>getPlayerOne().getKalah()){
			this.winner = Winner.PLAYER_TWO;
		}else{
			this.winner = Winner.DRAW;
		}
		
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * 
	 * @return
	 */
	public Player getPlayerPlaying(){
		if (actualPlayer.equals(ActualPlayer.PLAYER_ONE)){
			return getPlayerOne();
		}else{
			return getPlayerTwo();
		}
	}
	
	/**
	 * 
	 * @return
	 */
	private Player getPlayerWaiting(){
		if (actualPlayer.equals(ActualPlayer.PLAYER_ONE)){
			return getPlayerTwo();
		}else{
			return getPlayerOne();
		}
	}

	/**
	 * @return the winner
	 */
	public Winner getWinner() {
		return winner;
	}

	/**
	 * @return the status
	 */
	public Status getStatus() {
		return status;
	}

	/**
	 * @return the playerOne
	 */
	public Player getPlayerOne() {
		return players.get(0);
	}

	/**
	 * @return the playerTwo
	 */
	public Player getPlayerTwo() {
		return players.get(1);
	}

	/**
	 * @return
	 */
	public ActualPlayer getActualPlayer() {
		return actualPlayer;
	}
	
	
	

}
