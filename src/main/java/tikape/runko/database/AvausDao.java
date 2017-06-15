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
import tikape.runko.domain.Keskustelun_avaus;
import tikape.runko.domain.Alue;

/**
 *
 * @author alekk
 */
public class AvausDao implements Dao<Keskustelun_avaus, Integer> {

    private Database database;

    public AvausDao(Database database) {
        this.database = database;
    }

    @Override
    public Keskustelun_avaus findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Keskustelun_avaus WHERE keskust_avaus_id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        int id = rs.getInt("keskust_avaus_id");
        String alue = rs.getString("alue");
        int kayttaja = rs.getInt("kayttaja");
        String keskust_avaus = rs.getString("keskust_avaus");
        String timestamp = rs.getString("timestamp");

        Keskustelun_avaus ka = new Keskustelun_avaus(id, alue, kayttaja, keskust_avaus, timestamp);

        rs.close();
        stmt.close();
        connection.close();

        return ka;
    }

    @Override
    public List<Keskustelun_avaus> findAll() throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Keskustelun_avaus");

        ResultSet rs = stmt.executeQuery();
        List<Keskustelun_avaus> avaukset = new ArrayList<>();
        while (rs.next()) {
            int id = rs.getInt("keskust_avaus_id");
            String alue = rs.getString("alue");
            int kayttaja = rs.getInt("kayttaja");
            String keskust_avaus = rs.getString("avaus");
            String timestamp = rs.getString("timestamp");

            avaukset.add(new Keskustelun_avaus(id, alue, kayttaja, keskust_avaus, timestamp));
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
    
    public List<Keskustelun_avaus> findLatest10(String key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT avaus AS avaus, ka.alue AS alue,"
                + "COUNT(v.vastaus_id) AS viesteja, ka.kayttaja AS kayttaja, MAX(v.timestamp) AS timestamp, keskust_avaus_id "
                + "FROM Keskustelun_avaus ka LEFT JOIN Vastaus v "
                + "ON  ka.keskust_avaus_id = v.keskust_avaus "
                + "WHERE ka.alue = ? GROUP BY keskust_avaus_id ORDER BY ka.timestamp DESC LIMIT 10");
        
        stmt.setString(1,key);

        ResultSet rs = stmt.executeQuery();
        List<Keskustelun_avaus> avaukset = new ArrayList<>();
        while (rs.next()) {
            int id = rs.getInt("keskust_avaus_id");
            String alue = rs.getString("alue");
            int kayttaja = rs.getInt("kayttaja");
            String keskust_avaus = rs.getString("avaus");
            String timestamp = rs.getString("timestamp");

            avaukset.add(new Keskustelun_avaus(id, alue, kayttaja, keskust_avaus, timestamp));
        }

        rs.close();
        stmt.close();
        connection.close();

        return avaukset;
    }

}
