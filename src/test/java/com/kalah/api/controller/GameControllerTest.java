/**
 * 
 */
package com.kalah.api.controller;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.kalah.api.resource.GameResource;
import com.kalah.domain.entity.ActualPlayer;
import com.kalah.domain.entity.Game;
import com.kalah.domain.entity.Status;
import com.kalah.domain.repository.GameRepository;

/**
 * @author bruno.romao.antunes
 *
 */

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class GameControllerTest {

	@Autowired
	private GameRepository gameRepository;

	@Autowired
	private TestRestTemplate restTemplate;

	private Game gameTeste;

	@Before
	public void setupTestDataset() {
		gameRepository.deleteAll();

		gameTeste = gameRepository.save(new Game("teste"));
	}

	@Test
	public void startNewGame() {
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.ACCEPT, "application/xml;q=0.9,application/json;q=0.8");
		HttpEntity<?> request = new HttpEntity<>(headers);

		ResponseEntity<GameResource> response = restTemplate.exchange("/6-stone-kalah/games/teste2", HttpMethod.POST,
				request, GameResource.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(response.getHeaders().getLocation()).hasPath("/6-stone-kalah/games/teste2");

	}
	
	@Test
	public void startNewExistingGame() {
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.ACCEPT, "application/xml;q=0.9,application/json;q=0.8");
		HttpEntity<?> request = new HttpEntity<>(headers);

		ResponseEntity<GameResource> response = restTemplate.exchange("/6-stone-kalah/games/teste", HttpMethod.POST,
				request, GameResource.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);

	}

	@Test
	public void showGame() {
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.ACCEPT, "application/xml;q=0.9,application/json;q=0.8");
		HttpEntity<?> request = new HttpEntity<>(headers);

		ResponseEntity<GameResource> response = restTemplate.exchange("/6-stone-kalah/games/teste", HttpMethod.GET,
				request, GameResource.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getHeaders().getContentType())
		.isEqualTo(MediaType.parseMediaType("application/json;charset=UTF-8"));

		assertThat(response.getBody().getName()).isEqualTo(gameTeste.getName());
		assertThat(response.getBody().getWinner()).isEqualTo(gameTeste.getWinner().toString());
		assertThat(response.getBody().getStatus()).isEqualTo(gameTeste.getStatus().toString());

		assertThat(response.getBody().getPlayers().get(0).getPlayerId()).isEqualTo(gameTeste.getPlayerOne().getId());
		assertThat(response.getBody().getPlayers().get(0).getKalah()).isEqualTo(gameTeste.getPlayerOne().getKalah());
		assertThat(response.getBody().getPlayers().get(0).getPits().size())
				.isEqualTo(gameTeste.getPlayerOne().getPits().size());

		for (int i = 0; i < response.getBody().getPlayers().get(0).getPits().size(); i++) {
			assertThat(response.getBody().getPlayers().get(0).getPits().get(i))
					.isEqualTo(gameTeste.getPlayerOne().getPits().get(i));
		}

		assertThat(response.getBody().getPlayers().get(1).getPlayerId()).isEqualTo(gameTeste.getPlayerTwo().getId());
		assertThat(response.getBody().getPlayers().get(1).getKalah()).isEqualTo(gameTeste.getPlayerTwo().getKalah());
		assertThat(response.getBody().getPlayers().get(1).getPits().size())
				.isEqualTo(gameTeste.getPlayerTwo().getPits().size());

		for (int i = 0; i < response.getBody().getPlayers().get(1).getPits().size(); i++) {
			assertThat(response.getBody().getPlayers().get(1).getPits().get(i))
					.isEqualTo(gameTeste.getPlayerTwo().getPits().get(i));
		}

	}

	@Test
	public void showGameNotFound() {
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.ACCEPT, "application/xml;q=0.9,application/json;q=0.8");
		HttpEntity<?> request = new HttpEntity<>(headers);

		ResponseEntity<GameResource> response = restTemplate.exchange("/6-stone-kalah/games/teste444", HttpMethod.GET,
				request, GameResource.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

	}
	
	@Test
	public void playGame(){
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.ACCEPT, "application/xml;q=0.9,application/json;q=0.8");
		HttpEntity<?> request = new HttpEntity<>(headers);

		ResponseEntity<GameResource> response = restTemplate.exchange("/6-stone-kalah/games/teste/players/1/pits/1", HttpMethod.PUT,
				request, GameResource.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

		Game gameResult = gameRepository.findOne("teste");
		
		assertThat(gameResult.getName()).isEqualTo(gameTeste.getName());
		assertThat(gameResult.getStatus()).isEqualTo(Status.STARTED);
		assertThat(gameResult.getWinner()).isEqualTo(gameTeste.getWinner());
		assertThat(gameResult.getPlayerPlaying().getId()).isEqualTo(ActualPlayer.PLAYER_ONE.getPlayerNumer());
		assertThat(gameResult.getPlayerPlaying().getKalah()).isEqualTo(1);
		assertThat(gameResult.getPlayerPlaying().getPits().get(0)).isEqualTo(0);
		for(int i=1; i < gameResult.getPlayerPlaying().getPits().size(); i++){
			assertThat(gameResult.getPlayerPlaying().getPits().get(i)).isEqualTo(7);
		}
		
	}
	
	@Test
	public void playGameNotFound(){
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.ACCEPT, "application/xml;q=0.9,application/json;q=0.8");
		HttpEntity<?> request = new HttpEntity<>(headers);

		ResponseEntity<GameResource> response = restTemplate.exchange("/6-stone-kalah/games/teste/players/1/pits/7", HttpMethod.PUT,
				request, GameResource.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
		
	}

}
