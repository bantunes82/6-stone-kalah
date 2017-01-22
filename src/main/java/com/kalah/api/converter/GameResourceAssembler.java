/**
 * 
 */
package com.kalah.api.converter;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Service;

import com.kalah.api.controller.GameController;
import com.kalah.api.resource.GameResource;
import com.kalah.api.resource.PlayerResource;
import com.kalah.domain.entity.Game;

/**
 * @author bruno.romao.antunes
 *
 */
@Service
public class GameResourceAssembler extends ResourceAssemblerSupport<Game, GameResource> {

	private EntityLinks entityLinks;

	@Autowired
	public GameResourceAssembler(final EntityLinks entityLinks) {
		super(GameController.class, GameResource.class);
		this.entityLinks = entityLinks;
	}

	@Override
	protected GameResource instantiateResource(Game game) {

		PlayerResource playerOneResource = new PlayerResource(game.getPlayerOne().getId(),game.getPlayerOne().getKalah(),
				game.getPlayerOne().getPits());
		PlayerResource playerTwoResource = new PlayerResource(game.getPlayerTwo().getId(),game.getPlayerTwo().getKalah(),
				game.getPlayerTwo().getPits());
		
		List<PlayerResource> players = Arrays.asList(playerOneResource,playerTwoResource);

		return new GameResource(game.getName(), game.getWinner().toString(), game.getStatus().toString(), players);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.hateoas.ResourceAssembler#toResource(java.lang.
	 * Object)
	 */
	@Override
	public GameResource toResource(Game game) {
		final GameResource resource = createResourceWithId(game.getName(), game);

		buildPitsLink(game, resource);

		return resource;
	}

	/**
	 * 
	 * @param player
	 * @param actualPlayer
	 * @param resource
	 */
	private void buildPitsLink(Game game, final GameResource resource) {
		Link linkPit;
		int indexPit = 1;

		for (int pitValue : game.getPlayerPlaying().getPits()) {
			if(pitValue!=0){
				linkPit = linkTo(methodOn(GameController.class).playGame(resource.getName(), game.getActualPlayer().getPlayerNumer(), indexPit))
						.withRel(game.getActualPlayer()+".pits" + indexPit);
				resource.add(linkPit);
			}
			indexPit++;
		}
	}

	/**
	 * 
	 * @param game
	 * @return
	 */
	public Link linkToSingleResource(Game game) {
		return entityLinks.linkToSingleResource(GameResource.class, game.getName());
	}

}
