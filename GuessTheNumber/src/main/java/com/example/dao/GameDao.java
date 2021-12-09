package com.example.dao;

import com.example.entity.Game;
import java.util.List;

public interface GameDao {

    List<Game> getAllGames();
    Game getGameById(int id);
    Game addGame(Game game);
    void updateGame(Game game);
    void deleteGameById(int id);

}
