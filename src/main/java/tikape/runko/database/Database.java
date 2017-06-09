package tikape.runko.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private String databaseAddress;

    public Database(String databaseAddress) throws ClassNotFoundException {
        this.databaseAddress = databaseAddress;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(databaseAddress);
    }

    public void init() {
        List<String> lauseet = sqliteLauseet();

        // "try with resources" sulkee resurssin automaattisesti lopuksi
        try (Connection conn = getConnection()) {
            Statement st = conn.createStatement();

            // suoritetaan komennot
            for (String lause : lauseet) {
                System.out.println("Running command >> " + lause);
                st.executeUpdate(lause);
            }

        } catch (Throwable t) {
            // jos tietokantataulu on jo olemassa, ei komentoja suoriteta
            System.out.println("Error >> " + t.getMessage());
        }
    }

    private List<String> sqliteLauseet() {
        ArrayList<String> lista = new ArrayList<>();

        // tietokantataulujen luomiseen tarvittavat komennot suoritusjärjestyksessä
        lista.add("CREATE TABLE Alue (nimi VARCHAR(20)  PRIMARY KEY);");
        
        lista.add("CREATE TABLE Kayttaja (kayttaja_id INTEGER PRIMARY KEY, nimi varchar(15));");
        
        lista.add("CREATE TABLE Keskustelun_avaus (keskust_avaus_id INTEGER PRIMARY KEY, "
                + "kayttaja INTEGER ,avaus VARCHAR(200),timestamp TIMESTAMP,alue varchar(20) NOT NULL, "
                + "FOREIGN KEY (kayttaja) REFERENCES Kayttaja(kayttaja_id),"
                + "FOREIGN KEY (alue) REFERENCES Alue(nimi));");
        
        lista.add("CREATE TABLE Vastaus (vastaus_id INTEGER PRIMARY KEY, "
                + "kayttaja INTEGER, vastaus VARCHAR(200),"
                + "timestamp TIMESTAMP,alue VARCHAR(20),keskust_avaus INTEGER NOT NULL,"
                + "FOREIGN KEY (keskust_avaus) REFERENCES Keskustelun_avaus(keskust_avaus_id),"
                + "FOREIGN KEY (kayttaja) REFERENCES kayttaja(kayttaja_id),"
                + "FOREIGN KEY (alue) REFERENCES alue(nimi));");
        
        //Lisätään käyttäjiä:
        
        lista.add("INSERT INTO Kayttaja(kayttaja_id,nimi) VALUES(1, 'Jonne')");
        lista.add("INSERT INTO Kayttaja(kayttaja_id,nimi) VALUES(2, 'Joni')");
        lista.add("INSERT INTO Kayttaja(kayttaja_id,nimi) VALUES(3, 'Jonna')");
        lista.add("INSERT INTO Kayttaja (kayttaja_id,nimi) VALUES(4, 'Anni')");
        
        //Lisätään Alueita
        lista.add("INSERT INTO Alue(nimi) VALUES('sekalainen')");
        lista.add("INSERT INTO Alue (nimi) VALUES('kalat')");
        lista.add("INSERT INTO Alue (nimi) VALUES('koirat')");
        
        // Lisätään keskustelunavauksia
        lista.add("INSERT INTO Keskustelun_avaus (keskust_avaus_id, kayttaja, avaus, timestamp, alue)"
                + " VALUES(1, 1, 'Miksi', '2017-02-05 06:08:12', 'sekalainen')");
        lista.add("INSERT INTO Keskustelun_avaus (keskust_avaus_id, kayttaja, avaus, timestamp, alue) VALUES(2, 2, 'Siksi', '2015-02-05 07:08:12', 'sekalainen')");
        lista.add("INSERT INTO Keskustelun_avaus (keskust_avaus_id, kayttaja, avaus, timestamp, alue) VALUES(3, 3, 'Hauki kala', '2010-02-05 08:08:12', 'kalat')");
        
        //Lisätään vastauksia
        lista.add("INSERT INTO Vastaus (vastaus_id, kayttaja, vastaus, timestamp, alue, keskust_avaus)"
                + " VALUES(1, 4, 'Kissa on kala', '2011-02-05 09:08:12', 'kalat', 3)");
        lista.add("INSERT INTO Vastaus (vastaus_id, kayttaja, vastaus, timestamp, alue, keskust_avaus) VALUES(2, 1, 'Koira on kala', '2011-03-05 11:08:12', 'kalat', 3)");
        lista.add("INSERT INTO Vastaus (vastaus_id, kayttaja, vastaus, timestamp, alue, keskust_avaus) VALUES(3, 2, 'Ankka on kala', '2011-04-05 03:08:12', 'kalat', 3)");
        lista.add("INSERT INTO Vastaus (vastaus_id, kayttaja, vastaus, timestamp, alue, keskust_avaus) VALUES(4, 3, 'Kissa on kala', '2011-05-05 15:08:12', 'kalat', 3)");
        
        

        return lista;
    }
}
