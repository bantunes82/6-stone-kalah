/**
 * 
 */
package com.kalah.api.controller;

import static com.kalah.util.ResourceHandlingUtils.entityOrNotFoundException;
import static com.kalah.util.ResourceHandlingUtils.noEntityOrConflictException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.kalah.api.converter.GameResourceAssembler;
import com.kalah.api.resource.GameResource;
import com.kalah.domain.entity.Game;
import com.kalah.domain.repository.GameRepository;
import com.kalah.exception.ResourceNotFoundException;

/**
 * @author bruno.romao.antunes
 *
 */
@RestController
@ExposesResourceFor(GameResource.class)
@RequestMapping("/games")
public class GameController {

	@Autowired
	private GameRepository gameRepository;
	
	@Autowired
	private GameResourceAssembler gameResourceAssembler;

	@RequestMapping(value = "/{name}", method = RequestMethod.POST)
	public ResponseEntity<Void> startNewGame(@PathVariable("name") final String name) {
		noEntityOrConflictException(gameRepository.findOne(name));

		Game game = gameRepository.save(new Game(name));

		final HttpHeaders headers = new HttpHeaders();
		final Link linkToNewGame = gameResourceAssembler.linkToSingleResource(game);
		headers.add("Location", linkToNewGame.getHref());

		return new ResponseEntity<>(headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GameResource> showGame(@PathVariable("name") final String name) {
		Game game = entityOrNotFoundException(gameRepository.findOne(name));

		GameResource gameResource = gameResourceAssembler.toResource(game);

		return ResponseEntity.ok(gameResource);
	}

	@RequestMapping(value = "/{name}/players/{playerId}/pits/{pitId}", method = RequestMethod.PUT)
	public ResponseEntity<Void> playGame(@PathVariable("name") final String name,
			@PathVariable("playerId") final int playerId, @PathVariable("pitId") final int pitId) {

		Game game = entityOrNotFoundException(gameRepository.findByNameAndPlayersId(name, playerId));
		
		if(validateEntity(playerId, pitId, game)){
			game.play(pitId - 1);
			gameRepository.save(game);
		}else{
			 throw new ResourceNotFoundException();
		}

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	/**
	 * @param playerId
	 * @param pitId
	 * @param game
	 * @return
	 */
	private boolean validateEntity(final int playerId, final int pitId, Game game) {
		return game.getPlayerPlaying().getId()==playerId && pitId <= game.getPlayerPlaying().getPits().size()
				&& pitId >0 && game.getPlayerPlaying().getPits().get(pitId-1) > 0;
	}

}
