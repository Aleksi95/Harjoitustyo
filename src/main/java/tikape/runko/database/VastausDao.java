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
import tikape.runko.domain.Vastaus;

/**
 *
 * @author alekk
 */
public class VastausDao implements Dao<Vastaus, Integer> {

    private Database database;

    public VastausDao(Database database) {
        this.database = database;
    }

    @Override
    public Vastaus findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT v.vastaus, "
                + "v.vastaus_id, k.nimi as kayttaja, v.alue, v.timestamp, "
                + "v.keskust_avaus, ka.avaus as avaus "
                + "FROM Vastaus v LEFT JOIN Keskustelun_avaus ka "
                + "ON v.keskust_avaus = ka.keskust_avaus_id"
                + "LEFT JOIN Kayttaja k ON v.kayttaja = k.id "
                + "WHERE v.keskust_avaus = ? ORDER BY v.timestamp");

        stmt.setObject(1, key);
        ResultSet rs = stmt.executeQuery();
        Integer id = rs.getInt("vastaus_id");
        String kayttaja = rs.getString("kayttaja");
        String alue = rs.getString("alue");
        String vastaus = rs.getString("vastaus");
        String timestamp = rs.getString("timestamp");
        String avaus = rs.getString("avaus");
        Vastaus v = new Vastaus(id, kayttaja, alue, vastaus, timestamp, avaus);

        rs.close();
        stmt.close();
        connection.close();

        return v;
    }

    @Override
    public List<Vastaus> findAll() throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT v.vastaus, "
                + "v.vastaus_id, k.nimi as kayttaja, v.alue, v.timestamp, "
                + "v.keskust_avaus, ka.avaus as avaus "
                + "FROM Vastaus v LEFT JOIN Keskustelun_avaus ka "
                + "ON v.keskust_avaus = ka.keskust_avaus_id"
                + "LEFT JOIN Kayttaja k ON v.kayttaja = k.id "
                + "ORDER BY v.timestamp");

        ResultSet rs = stmt.executeQuery();
        List<Vastaus> vastaukset = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("vastaus_id");
            String kayttaja = rs.getString("kayttaja");
            String alue = rs.getString("alue");
            String vastaus = rs.getString("vastaus");
            String timestamp = rs.getString("timestamp");
            String avaus = rs.getString("avaus");
            vastaukset.add(new Vastaus(id, kayttaja, alue, vastaus, timestamp, avaus));
        }

        rs.close();
        stmt.close();
        connection.close();

        return vastaukset;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<Vastaus> findAllInThread(String key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT v.vastaus, "
                + "v.vastaus_id, k.nimi as kayttaja, v.alue, v.timestamp, "
                + "v.keskust_avaus, ka.avaus as avaus "
                + "FROM Vastaus v LEFT JOIN Keskustelun_avaus ka "
                + "ON v.keskust_avaus = ka.keskust_avaus_id "
                + "LEFT JOIN Kayttaja k ON v.kayttaja = k.id "
                + "WHERE v.keskust_avaus = ? ORDER BY v.timestamp");

        stmt.setObject(1, key);
        ResultSet rs = stmt.executeQuery();
        List<Vastaus> vastaukset = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("vastaus_id");
            String kayttaja = rs.getString("kayttaja");
            String alue = rs.getString("alue");
            String vastaus = rs.getString("vastaus");
            String timestamp = rs.getString("timestamp");
            String avaus = rs.getString("avaus");
            vastaukset.add(new Vastaus(id, kayttaja, alue, vastaus, timestamp, avaus));
        }

        rs.close();
        stmt.close();
        connection.close();

        return vastaukset;
    }

    public void lisaaVastaus(String kayttaja, String alue, String key, String avaus) throws SQLException {
        Connection c = database.getConnection();
        PreparedStatement stmt = c.prepareStatement("INSERT INTO Vastaus (vastaus, kayttaja, keskust_avaus, alue) "
                + "VALUES (?, ?, ?, ?)");
        stmt.setString(1, key);
        stmt.setString(2, kayttaja);
        stmt.setString(3, avaus);
        stmt.setString(4, alue);
        stmt.execute();
        
        c.close();
    }

}
