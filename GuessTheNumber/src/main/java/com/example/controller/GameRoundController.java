package com.example.controller;

import com.example.dao.*;
import com.example.entity.*;
import java.time.LocalDateTime;
import java.util.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;

@RestController
@RequestMapping("/api/numbergame")
public class GameRoundController {

    private final GameDao gameDao;
    private final RoundDao roundDao;

    public GameRoundController(GameDao gameDao, RoundDao roundDao) {
        this.gameDao = gameDao;
        this.roundDao = roundDao;
    }

    @PostMapping("/begin")
    public ResponseEntity<Integer> begin(){
        //    "begin" - POST – Starts a game, generates an answer, and sets the correct status.
        //    Should return a 201 CREATED message as well as the created gameId.

        //generate an answer (4 non-repeating digits)
        List<Integer> digits = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
        Collections.shuffle(digits);
        while (digits.get(0)==0){ //in case first digit is 0, inadvertently changes 4 digit int to 3 digit int
            Collections.shuffle(digits);
        }
        int answer = 1000*digits.get(0) + 100*digits.get(1) + 10*digits.get(2) + digits.get(3);

        //create game object, add to db
        Game game = new Game();
        game.setAnswer(answer);
        game.setStatus("in progress");
        game = gameDao.addGame(game);

        return new ResponseEntity(game.getGameId(),HttpStatus.CREATED);
    }


    @PostMapping("/guess")
    public ResponseEntity<Round> guess(@RequestBody Guess roundGuess){
        //   "guess" – POST – Makes a guess by passing the guess and gameId in as JSON.
        //   The program must calculate the results of the guess and mark the game finished if the guess is correct.
        //   It returns the Round object with the results filled in.

        //calculate result, initialize
        int e = 0;
        int p = 0;

        //get each digit from guess
        int g = roundGuess.getGuess();
        int dig4 = g % 10;
        g/=10;
        int dig3 = g % 10;
        g/=10;
        int dig2 = g % 10;
        g/=10;
        int dig1 = g % 10;

        int[] guessArray = new int[]{dig1,dig2,dig3,dig4};

        //get each digit from game answer
        Game game = gameDao.getGameById(roundGuess.getGameId());
        int a = game.getAnswer();
        int digAnswer4 = a % 10;
        a/=10;
        int digAnswer3 = a % 10;
        a/=10;
        int digAnswer2 = a % 10;
        a/=10;
        int digAnswer1 = a % 10;

        int[] answerArray = new int[]{digAnswer1,digAnswer2,digAnswer3,digAnswer4};

        //compare digits to calculate result
        for (int i = 0; i < 4; i++){
            if (guessArray[i]==answerArray[i]){
                e++;
            }
            else{
                for (int digit : answerArray) {
                    if (digit == guessArray[i]) {
                        p++;
                    }
                }
            }
        }

        String result = "e:"+e+":p:"+p;

        //add new round
        Round round = new Round();
        round.setGame(game);
        round.setGuess(roundGuess.getGuess());
        round.setTime(LocalDateTime.now());
        round.setResult(result);
        round = roundDao.addRound(round);

        //update game if guessed correctly
        if (e==4) {
            game.setStatus("finished");
            gameDao.updateGame(game);
        }
        else{
            game.setAnswer(0); //to hide answer
        }

        //returns Round object
        return ResponseEntity.ok(round);
    }

    @GetMapping("/games")
    public List<Game> games(){
        //    "game" – GET – Returns a list of all games. Be sure in-progress games do not display their answer.

        List<Game> games = gameDao.getAllGames();
        for (Game g : games){
            if (g.getStatus().equals("in progress")){
                g.setAnswer(0);//"hide" in progress games' answers
            }
        }
        return games;
    }

    @GetMapping("/game/{gameId}")
    public Game game(@PathVariable int gameId){
        //    "game/{gameId}" - GET – Returns a specific game based on ID.
        //    Be sure in-progress games do not display their answer.

        Game game = gameDao.getGameById(gameId);
        if (game.getStatus().equals("in progress")){
            game.setAnswer(0);//"hide" in progress games' answers
        }
        return game;
    }

    @GetMapping("/rounds/{gameId}")
    public List<Round> rounds(@PathVariable int gameId){
        //    "rounds/{gameId} – GET – Returns a list of rounds for the specified game sorted by time.

        List<Round> rs = roundDao.getRoundsForGame(gameId);
        for (Round r:rs){
            if (r.getGame().getStatus().equals("in progress")){
                r.getGame().setAnswer(0); //hide answer for in progress games
            }
        }
        return rs;
    }

}
