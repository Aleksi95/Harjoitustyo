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
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Vastaus WHERE vastaus_id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        int id = rs.getInt("vastaus_id");
        int kayttaja = rs.getInt("kayttaja");
        String alue = rs.getString("alue");
        String vastaus = rs.getString("vastaus");
        String timestamp = rs.getString("timestamp");
        int avaus = rs.getInt("keskust_avaus");

        Vastaus v = new Vastaus(id, kayttaja, alue, vastaus, timestamp, avaus);

        rs.close();
        stmt.close();
        connection.close();

        return v;
    }

    @Override
    public List<Vastaus> findAll() throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Keskustelun_avaus");

        ResultSet rs = stmt.executeQuery();
        List<Vastaus> vastaukset = new ArrayList<>();
        while (rs.next()) {
            int id = rs.getInt("vastaus_id");
            int kayttaja = rs.getInt("kayttaja");
            String alue = rs.getString("alue");
            String vastaus = rs.getString("vastaus");
            String timestamp = rs.getString("timestamp");
            int avaus = rs.getInt("keskust_avaus");

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
    
    public List<Vastaus> findAllInThread() throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT Vastaus.vastaus, kayttaja.nimi FROM Vastaus LEFT JOIN Keskustelun_avaus, kayttaja ON vastaus.keskust_avaus = keskustelun_avaus.keskust_avaus_id AND vastaus.kayttaja = kayttaja.kayttaja_id WHERE keskustelun_avaus.keskust_avaus_id = ? ORDER BY vastaus.timestamp");

        ResultSet rs = stmt.executeQuery();
        List<Vastaus> vastaukset = new ArrayList<>();
        while (rs.next()) {
            int id = rs.getInt("vastaus_id");
            int kayttaja = rs.getInt("kayttaja");
            String alue = rs.getString("alue");
            String vastaus = rs.getString("vastaus");
            String timestamp = rs.getString("timestamp");
            int avaus = rs.getInt("keskust_avaus");

            vastaukset.add(new Vastaus(id, kayttaja, alue, vastaus, timestamp, avaus));
        }

        rs.close();
        stmt.close();
        connection.close();

        return vastaukset;
    }

}
