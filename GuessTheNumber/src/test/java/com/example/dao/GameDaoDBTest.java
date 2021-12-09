package com.example.dao;

import com.example.entity.*;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GameDaoDBTest {

    @Autowired
    GameDao gameDao;

    @Autowired
    RoundDao roundDao;

    @Before
    public void setUp() {

        List<Round> rounds = roundDao.getAllRounds();
        for(Round round : rounds) {
            roundDao.deleteRoundById(round.getRoundId());
        }

        List<Game> games = gameDao.getAllGames();
        for(Game game : games) {
            gameDao.deleteGameById(game.getGameId());
        }
    }

    @Test
    public void testAddGetGame(){

        Game game = new Game();
        game.setAnswer(1234);
        game.setStatus("in progress");
        game = gameDao.addGame(game);

        Game fromDao = gameDao.getGameById(game.getGameId());

        assertEquals(game, fromDao);
    }

    @Test
    public void testGetAllGames() {

        Game game = new Game();
        game.setAnswer(1234);
        game.setStatus("finished");
        game = gameDao.addGame(game);

        Game game2 = new Game();
        game2.setAnswer(5678);
        game2.setStatus("in progress");
        game2 = gameDao.addGame(game2);

        List<Game> games = gameDao.getAllGames();

        assertEquals(2, games.size());
        assertTrue(games.contains(game));
        assertTrue(games.contains(game2));
    }

    @Test
    public void testUpdateGame() {

        Game game = new Game();
        game.setAnswer(1234);
        game.setStatus("in progress");
        game = gameDao.addGame(game);

        Game fromDao = gameDao.getGameById(game.getGameId());

        assertEquals(game, fromDao);

        game.setStatus("finished");

        gameDao.updateGame(game);

        assertNotEquals(game, fromDao);

        fromDao = gameDao.getGameById(game.getGameId());

        assertEquals(game, fromDao);
    }

    @Test
    public void testDeleteGame() {

        Game game = new Game();
        game.setAnswer(1234);
        game.setStatus("in progress");
        game = gameDao.addGame(game);

        Round round = new Round();
        round.setGame(game);
        round.setGuess(2345);
        round.setTime(LocalDateTime.now());
        round.setResult("e:0:p:3");
        round = roundDao.addRound(round);

        gameDao.deleteGameById(game.getGameId());
        Game fromDao = gameDao.getGameById(game.getGameId());

        assertNull(fromDao);
    }

}
