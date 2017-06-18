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
import tikape.runko.domain.Viesti;

/**
 *
 * @author alekk
 */
public class ViestiDao implements Dao<Viesti, Integer> {

    private Database database;

    public ViestiDao(Database database) {
        this.database = database;
    }

    @Override
    public Viesti findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT v.viesti, "
                + "v.viesti_id, k.nimi as kayttaja, v.alue, "
                + "v.timestamp AT TIME ZONE 'EETDST', "
                + "v.avaus FROM Viesti v "
                + "LEFT JOIN Avaus ka ON v.avaus = ka.avaus_id "
                + "LEFT JOIN Kayttaja k ON v.kayttaja = k.id "
                + "WHERE v.avaus = ? ORDER BY v.timestamp");

        stmt.setObject(1, key);
        ResultSet rs = stmt.executeQuery();
        Integer id = rs.getInt("viesti_id");
        String kayttaja = rs.getString("kayttaja");
        String alue = rs.getString("alue");
        String viesti = rs.getString("viesti");
        String timestamp = rs.getString("timestamp");
        int avaus = rs.getInt("avaus");
        Viesti v = new Viesti(id, viesti, kayttaja, alue, timestamp, avaus);

        rs.close();
        stmt.close();
        connection.close();

        return v;
    }

    @Override
    public List<Viesti> findAll() throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT v.viesti, "
                + "v.viesti_id, k.nimi as kayttaja, v.alue, "
                + "v.timestamp AT TIME ZONE 'EETDST', "
                + "v.avaus"
                + "FROM Viesti v LEFT JOIN Avaus ka "
                + "ON v.avaus = ka.avaus_id"
                + "LEFT JOIN Kayttaja k ON v.kayttaja = k.id "
                + "ORDER BY v.timestamp");

        ResultSet rs = stmt.executeQuery();
        List<Viesti> viestit = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("vastaus_id");
            String kayttaja = rs.getString("kayttaja");
            String alue = rs.getString("alue");
            String vastaus = rs.getString("vastaus");
            String timestamp = rs.getString("timestamp");
            Integer avaus = rs.getInt("avaus");
            viestit.add(new Viesti(id, kayttaja, alue, vastaus, timestamp, avaus));
        }

        rs.close();
        stmt.close();
        connection.close();

        return viestit;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<Viesti> findAllInThread(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT v.viesti, "
                + "v.viesti_id, k.nimi as kayttaja, v.alue, "
                + "v.timestamp AT TIME ZONE 'EETDST', "
                + "v.avaus "
                + "FROM Viesti v LEFT JOIN Avaus ka "
                + "ON v.avaus = ka.avaus_id "
                + "LEFT JOIN Kayttaja k ON v.kayttaja = k.id "
                + "WHERE v.avaus = ? ORDER BY v.timestamp");

        stmt.setObject(1, key);
        ResultSet rs = stmt.executeQuery();
        List<Viesti> viestit = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("viesti_id");
            String kayttaja = rs.getString("kayttaja");
            String alue = rs.getString("alue");
            String viesti = rs.getString("viesti");
            String timestamp = rs.getString("timestamp");
            int avaus = rs.getInt("avaus");
            viestit.add(new Viesti(id, viesti, kayttaja, alue, timestamp, avaus));
        }

        rs.close();
        stmt.close();
        connection.close();

        return viestit;
    }

    public void lisaaViesti(String viesti, String nimi, String alue, int avaus) throws SQLException {
        Connection c = null;
        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;
        try {
            c = database.getConnection();
            c.setAutoCommit(false);

            stmt = c.prepareStatement("INSERT INTO Kayttaja(nimi) "
                    + "VALUES (?);");
            stmt.setString(1, nimi);
            stmt.executeUpdate();
            stmt2 = c.prepareStatement("INSERT INTO Viesti(kayttaja, viesti, alue, avaus)"
                    + " Values((SELECT MAX(id) FROM kayttaja), ?, ?, ?);");
            stmt2.setString(1, viesti);
            stmt2.setString(2, alue);
            stmt2.setInt(3, avaus);
            
            stmt2.executeUpdate();

            c.commit();
            System.out.println("Done!");

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

            if (c != null) {
                c.close();
            }
        }
    }

}
