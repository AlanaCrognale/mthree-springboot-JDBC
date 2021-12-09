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
public class RoundDaoDBTest {

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
    public void testAddGetRound() {

        Game game = new Game();
        game.setAnswer(1234);
        game.setStatus("in progress");

        game = gameDao.addGame(game);

        game = gameDao.getGameById(game.getGameId());

        Round round = new Round();
        round.setGame(game);
        round.setGuess(2345);
        round.setTime(LocalDateTime.now().withNano(0));
        round.setResult("e:0:p:3");

        round = roundDao.addRound(round);

        Round fromDao = roundDao.getRoundById(round.getRoundId());

        assertEquals(round, fromDao);
    }

    @Test
    public void testGetAllRounds() {

        Game game = new Game();
        game.setAnswer(1234);
        game.setStatus("in progress");

        game = gameDao.addGame(game);

        Round round = new Round();
        round.setGame(game);
        round.setGuess(2345);
        round.setTime(LocalDateTime.now().withNano(0));
        round.setResult("e:0:p:3");

        round = roundDao.addRound(round);

        Round round2 = new Round();
        round2.setGame(game);
        round2.setGuess(6345);
        round2.setTime(LocalDateTime.now().withNano(0));
        round2.setResult("e:0:p:2");

        round2 = roundDao.addRound(round2);

        List<Round> rounds = roundDao.getAllRounds();

        assertEquals(2, rounds.size());
        assertTrue(rounds.contains(round));
        assertTrue(rounds.contains(round2));
    }

    @Test
    public void testUpdateRound() {

        Game game = new Game();
        game.setAnswer(1234);
        game.setStatus("in progress");

        game = gameDao.addGame(game);

        Round round = new Round();
        round.setGame(game);
        round.setGuess(2345);
        round.setTime(LocalDateTime.now().withNano(0));
        round.setResult("e:0:p:3");

        round = roundDao.addRound(round);

        Round fromDao = roundDao.getRoundById(round.getRoundId());

        assertEquals(round, fromDao);

        round.setGuess(2346);
        roundDao.updateRound(round);

        assertNotEquals(round, fromDao);

        fromDao = roundDao.getRoundById(round.getRoundId());

        assertEquals(round, fromDao);
    }
    @Test
    public void testDeleteRound() {

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

        roundDao.deleteRoundById(round.getRoundId());
        Round fromDao = roundDao.getRoundById(round.getRoundId());

        assertNull(fromDao);
    }

    @Test
    public void testGetRoundsForGame() {

        Game game = new Game();
        game.setAnswer(1234);
        game.setStatus("in progress");

        game = gameDao.addGame(game);

        Round round = new Round();
        round.setGame(game);
        round.setGuess(2345);
        round.setTime(LocalDateTime.now().withNano(0));
        round.setResult("e:0:p:3");

        round = roundDao.addRound(round);

        Round round2 = new Round();
        round2.setGame(game);
        round2.setGuess(6345);
        round2.setTime(LocalDateTime.now().withNano(0));
        round2.setResult("e:0:p:2");

        round2 = roundDao.addRound(round2);

        List<Round> fromDao = roundDao.getRoundsForGame(game.getGameId());

        assertEquals(2, fromDao.size());
        assertTrue(fromDao.contains(round));
        assertTrue(fromDao.contains(round2));
    }

}
