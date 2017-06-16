/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.runko.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import tikape.runko.domain.Avaus;
import tikape.runko.domain.Alue;

/**
 *
 * @author alekk
 */
public class AvausDao implements Dao<Avaus, Integer> {

    private Database database;

    public AvausDao(Database database) {
        this.database = database;
    }

    @Override
    public Avaus findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Avaus "
                + "WHERE Avaus_id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        int id = rs.getInt("avaus_id");
        String alue = rs.getString("alue");
        String avaus = rs.getString("avaus");
        String timestamp = rs.getString("timestamp");

        Avaus ka = new Avaus(id, alue, avaus, timestamp);

        rs.close();
        stmt.close();
        connection.close();

        return ka;
    }

    @Override
    public List<Avaus> findAll() throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Avaus");

        ResultSet rs = stmt.executeQuery();
        List<Avaus> avaukset = new ArrayList<>();
        while (rs.next()) {
            int id = rs.getInt("avaus_id");
            String alue = rs.getString("alue");
            String avaus = rs.getString("avaus");
            String timestamp = rs.getString("timestamp");

            avaukset.add(new Avaus(id, alue, avaus, timestamp));
        }

        rs.close();
        stmt.close();
        connection.close();

        return avaukset;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<Avaus> findLatest10(String key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT ka.avaus AS avaus, "
                + "ka.alue AS alue,"
                + "COUNT(v.viesti_id) AS viesteja,"
                + "MAX(v.timestamp) AS timestamp, avaus_id "
                + "FROM Avaus ka LEFT JOIN Viesti v "
                + "ON  ka.avaus_id = v.avaus "
                + "WHERE ka.alue = ? GROUP BY avaus_id ORDER BY ka.timestamp DESC LIMIT 10");

        stmt.setString(1, key);

        ResultSet rs = stmt.executeQuery();
        List<Avaus> avaukset = new ArrayList<>();
        while (rs.next()) {
            int id = rs.getInt("avaus_id");
            String alue = rs.getString("alue");
            String avaus = rs.getString("avaus");
            String timestamp = rs.getString("timestamp");

            avaukset.add(new Avaus(id, alue, avaus, timestamp));
        }

        rs.close();
        stmt.close();
        connection.close();

        return avaukset;
    }

    public void lisaaAvaus(String avaus, String alue) throws SQLException {
        Connection c = database.getConnection();
        PreparedStatement stmt = c.prepareStatement("INSERT INTO Avaus (avaus, alue) "
                + "VALUES (?, ?)");
        stmt.setString(1, avaus);
        stmt.setString(2, alue);
        stmt.execute();
        c.close();
    }

}
