package com.example.dao;

import com.example.entity.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.dao.*;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.*;
import java.sql.*;
import java.util.*;

@Repository
public class GameDaoDB implements GameDao{

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GameDaoDB(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public static final class GameMapper implements RowMapper<Game> {

        @Override
        public Game mapRow(ResultSet rs, int index) throws SQLException {
            Game gm = new Game();
            gm.setGameId(rs.getInt("GameId"));
            gm.setAnswer(rs.getInt("Answer"));
            gm.setStatus(rs.getString("Status"));
            return gm;
        }
    }

    @Override
    public List<Game> getAllGames(){
        final String sql = "SELECT * FROM Game;";
        return jdbcTemplate.query(sql, new GameMapper());
    }

    @Override
    public Game getGameById(int id){
        try {
            final String sql = "SELECT * FROM Game WHERE GameId = ?;";
            return jdbcTemplate.queryForObject(sql, new GameMapper(), id);
        }
        catch(DataAccessException ex){
            return null;
        }
    }

    @Override
    @Transactional
    public Game addGame(Game game){

        final String sql = "INSERT INTO Game(`Answer`, `Status`) VALUES(?,?);";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update((Connection conn) -> {
            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, game.getAnswer());
            statement.setString(2, game.getStatus());
            return statement;
            }, keyHolder);

        game.setGameId(keyHolder.getKey().intValue());

        return game;
    }

    @Override
    public void updateGame(Game game){

        final String sql = "UPDATE Game SET Answer = ?, Status = ? WHERE GameId = ?;";
        jdbcTemplate.update(sql, game.getAnswer(), game.getStatus(), game.getGameId());
    }

    @Override
    @Transactional
    public void deleteGameById(int id){

        final String sql1 = "DELETE FROM Round WHERE GameId = ?;";
        final String sql2 = "DELETE FROM Game WHERE GameId = ?;";
        jdbcTemplate.update(sql1, id);
        jdbcTemplate.update(sql2, id);
    }
}
