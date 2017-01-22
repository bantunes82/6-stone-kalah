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
@Relation(value = "player", collectionRelation = "players")
public class PlayerResource extends ResourceSupport{
	
	private int playerId;
	
	/**
	 * 
	 */
	private int kalah;
	/**
	 * 
	 */
	private List<Integer> pits;
	
	/**
	 * @param kalah
	 * @param pits
	 */
	@JsonCreator
	public PlayerResource(@JsonProperty("id") int id,@JsonProperty("kalah") int kalah,@JsonProperty("pits") List<Integer> pits) {
		super();
		this.playerId = id;
		this.kalah = kalah;
		this.pits = pits;
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
	 * @return the id
	 */
	public int getPlayerId() {
		return playerId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + kalah;
		result = prime * result + ((pits == null) ? 0 : pits.hashCode());
		result = prime * result + playerId;
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
		PlayerResource other = (PlayerResource) obj;
		if (kalah != other.kalah)
			return false;
		if (pits == null) {
			if (other.pits != null)
				return false;
		} else if (!pits.equals(other.pits))
			return false;
		if (playerId != other.playerId)
			return false;
		return true;
	}
	
	
	
	
	

}
