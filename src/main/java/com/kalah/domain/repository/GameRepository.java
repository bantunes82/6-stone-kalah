/**
 * 
 */
package com.kalah.domain.repository;

import org.springframework.data.repository.CrudRepository;

import com.kalah.domain.entity.Game;


/**
 * @author bruno.romao.antunes
 *
 */
public interface GameRepository extends CrudRepository<Game, String>{
	
	/**
	 * 
	 * @param name
	 * @param id
	 * @return
	 */
	public Game findByNameAndPlayersId(String name, int id);
	

}
