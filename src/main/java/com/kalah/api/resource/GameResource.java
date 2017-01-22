/**
 * 
 */
package com.kalah.api.resource;

import java.util.List;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author bruno.romao.antunes
 *
 */
@Relation(value = "game", collectionRelation = "games")
public class GameResource extends ResourceSupport {

	private String name;

	private String winner;

	private String status;

	private List<PlayerResource> players;

	/**
	 * @param name
	 * @param winner
	 * @param status
	 */
	@JsonCreator
	public GameResource(@JsonProperty("name") String name, @JsonProperty("winner") String winner,
			@JsonProperty("status") String status,@JsonProperty("players") List<PlayerResource> players) {
		super();
		this.name = name;
		this.winner = winner;
		this.status = status;
		this.players = players;
	}

	/**
	 * @return the players
	 */
	public List<PlayerResource> getPlayers() {
		return players;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the winner
	 */
	public String getWinner() {
		return winner;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((players == null) ? 0 : players.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((winner == null) ? 0 : winner.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		GameResource other = (GameResource) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (players == null) {
			if (other.players != null)
				return false;
		} else if (!players.equals(other.players))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (winner == null) {
			if (other.winner != null)
				return false;
		} else if (!winner.equals(other.winner))
			return false;
		return true;
	}
	
	

}
