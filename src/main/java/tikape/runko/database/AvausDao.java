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
            Integer viesteja = rs.getInt("viesteja");

            Avaus ka = new Avaus(id, alue, avaus, timestamp, viesteja);

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
            Integer viesteja = rs.getInt("viesteja");

            avaukset.add(new Avaus(id, alue, avaus, timestamp, viesteja));
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
                + "strftime('%d - %m  - %Y ', MAX(v.timestamp) AS timestamp, avaus_id "
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
            Integer viesteja = rs.getInt("viesteja");

            avaukset.add(new Avaus(id, alue, avaus, timestamp, viesteja));
        }

        rs.close();
        stmt.close();
        connection.close();

        return avaukset;
    }

    public void lisaaAvaus(String avaus, String alue, String nimi, String viesti) throws SQLException {
        Connection c = null;
        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;
        PreparedStatement stmt3 = null;
        try {
            c = database.getConnection();
            c.setAutoCommit(false);

            stmt = c.prepareStatement("INSErT INTO Avaus(avaus, alue) "
                    + "VALUES (?, ?);");
            stmt.setString(1, avaus);
            stmt.setString(2, alue);
            stmt.executeUpdate();
            stmt2 = c.prepareStatement("INSERT INTO Kayttaja(nimi) Values(?);");
            stmt2.setString(1, nimi);
            stmt2.executeUpdate();
            stmt3 = c.prepareStatement("INSERT INTO Viesti(kayttaja, viesti, alue, avaus)"
                    + "Values((SELECT MAX(id) FROM kayttaja), ?, ?, (SELECT MAX(avaus_id) FROM avaus));");
            stmt3.setString(1, viesti);
            stmt3.setString(2, alue);
            stmt3.executeUpdate();

            c.commit();
            System.out.println("Done!");

            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            c.rollback();
        } finally {
            if (stmt != null) {
                stmt.close();
            }

            if (stmt2 != null) {
                stmt2.close();
            }
            
            if(stmt3 != null){
                stmt3.close();
            }

            if (c != null) {
                c.close();
            }
        }

    }

}
