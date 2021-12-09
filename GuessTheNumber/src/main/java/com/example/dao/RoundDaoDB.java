package com.example.dao;

import com.example.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.*;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.transaction.annotation.*;
import java.sql.*;
import java.util.*;

@Repository
public class RoundDaoDB implements RoundDao{

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public RoundDaoDB(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public static final class RoundMapper implements RowMapper<Round> {

        @Override
        public Round mapRow(ResultSet rs, int index) throws SQLException {

            Round round = new Round();
            round.setRoundId(rs.getInt("RoundId"));
            round.setGuess(rs.getInt("Guess"));
            round.setTime(rs.getTimestamp("Time").toLocalDateTime());
            round.setResult(rs.getString("Result"));
            return round;
        }
    }

    private Game getGameForRound(Round round) {//helper method

        final String sql = "SELECT g.* FROM Game g JOIN Round r ON g.GameId = r.GameId WHERE r.RoundId = ?";
        return jdbcTemplate.queryForObject(sql, new GameDaoDB.GameMapper(), round.getRoundId());
    }

    @Override
    public Round getRoundById(int id) {

        try {
            final String sql = "SELECT * FROM Round WHERE RoundId = ?;";
            Round round = jdbcTemplate.queryForObject(sql, new RoundDaoDB.RoundMapper(), id);
            round.setGame(getGameForRound(round));
            return round;
        }
        catch(DataAccessException ex){
            return null;
        }
    }

    @Override
    public List<Round> getAllRounds() {

        final String sql = "SELECT * FROM Round;";
        List<Round> rounds = jdbcTemplate.query(sql, new RoundDaoDB.RoundMapper());

        for(Round r : rounds) {
            r.setGame(getGameForRound(r));
        }

        return rounds;
    }

    @Override
    public List<Round> getRoundsForGame(int gameId) {

        final String sql = "SELECT * FROM Round WHERE GameId = ? ORDER BY Time"; //sorted by time
        List<Round> rounds = jdbcTemplate.query(sql, new RoundDaoDB.RoundMapper(), gameId);

        for(Round r : rounds) {
            r.setGame(getGameForRound(r));
        }

        return rounds;
    }

    @Override
    @Transactional
    public Round addRound(Round round) {

        final String sql = "INSERT INTO Round( GameId, Guess, Time, Result) VALUES(?,?,?,?);";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update((Connection conn) -> {
            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, round.getGame().getGameId());
            statement.setInt(2, round.getGuess());
            statement.setTimestamp(3, Timestamp.valueOf(round.getTime()));
            statement.setString(4, round.getResult());
            return statement;
        }, keyHolder);

        round.setRoundId(keyHolder.getKey().intValue());

        return round;
    }

    @Override
    public void updateRound(Round round) {

        final String sql = "UPDATE Round SET GameId = ?, Guess = ?, Time = ?, Result = ? WHERE RoundId = ?;";
        jdbcTemplate.update(sql,
                round.getGame().getGameId(),
                round.getGuess(),
                Timestamp.valueOf(round.getTime()),
                round.getResult(),
                round.getRoundId());
    }

    @Override
    public void deleteRoundById(int id) {

        final String sql = "DELETE FROM Round WHERE RoundId = ?;";
        jdbcTemplate.update(sql, id);
    }

}
