package tikape.runko.database;

import java.sql.*;
import java.util.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private String databaseAddress;

    public Database(String databaseAddress) throws ClassNotFoundException {
        this.databaseAddress = databaseAddress;

        init();
    }

    public Connection getConnection() throws SQLException {
        if (this.databaseAddress.contains("postgres")) {
            try {
                URI dbUri = new URI(databaseAddress);

                String username = dbUri.getUserInfo().split(":")[0];
                String password = dbUri.getUserInfo().split(":")[1];
                String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();

                return DriverManager.getConnection(dbUrl, username, password);
            } catch (Throwable t) {
                System.out.println("Error: " + t.getMessage());
                t.printStackTrace();
            }
        }
        return DriverManager.getConnection(databaseAddress);
    }

    public void init() {
        List<String> lauseet = null;
        if (this.databaseAddress.contains("postgres")) {
            lauseet = postgreLauseet();
        } else {
            lauseet = sqliteLauseet();
        }

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
    
    private List<String> postgreLauseet() {
         ArrayList<String> lista = new ArrayList<>();
         lista.add("DROP TABLE Alue");
         lista.add("DROP TABLE Kayttaja");
         lista.add("DROP TABLE Avaus");
         lista.add("DROP TABLE Viesti");
         
         lista.add("CREATE TABLE Alue (nimi VARCHAR(20) PRIMARY KEY);");

        lista.add("CREATE TABLE Kayttaja (id INTEGER SERIAL PRIMARY KEY, nimi varchar(15));");

        lista.add("CREATE TABLE Avaus (avaus_id INTEGER SERIAL PRIMARY KEY, avaus VARCHAR(100),"
                + "timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,"
                + "alue varchar(20) NOT NULL,"
                + "FOREIGN KEY (alue) REFERENCES Alue(nimi));");

        lista.add("CREATE TABLE Viesti(viesti_id INTEGER SERIAL PRIMARY KEY, "
                + "kayttaja INTEGER NOT NULL, viesti VARCHAR(300),"
                + "timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,"
                + "alue VARCHAR(20),avaus INTEGER NOT NULL,"
                + "FOREIGN KEY (avaus) REFERENCES Avaus(avaus_id),"
                + "FOREIGN KEY (kayttaja) REFERENCES kayttaja(id),"
                + "FOREIGN KEY (alue) REFERENCES alue(nimi));");

        //Lisätään käyttäjiä:
        lista.add("INSERT INTO Kayttaja(id,nimi) VALUES(1, 'Jonne')");
        lista.add("INSERT INTO Kayttaja(id,nimi) VALUES(2, 'Joni')");
        lista.add("INSERT INTO Kayttaja(id,nimi) VALUES(3, 'Jonna')");
        lista.add("INSERT INTO Kayttaja (id,nimi) VALUES(4, 'Anni')");

        //Lisätään Alueita
        lista.add("INSERT INTO Alue(nimi) VALUES('sekalainen')");
        lista.add("INSERT INTO Alue (nimi) VALUES('kalat')");
        lista.add("INSERT INTO Alue (nimi) VALUES('koirat')");

        // Lisätään keskustelunavauksia
        lista.add("INSERT INTO Avaus (avaus, alue)"
                + " VALUES('Miksi', 'sekalainen')");
        lista.add("INSERT INTO Avaus (avaus, alue) VALUES('Siksi', 'sekalainen')");
        lista.add("INSERT INTO Avaus (avaus,alue) VALUES('Hauki kala','kalat')");

        //Lisätään vastauksia
        lista.add("INSERT INTO Viesti (kayttaja, viesti,alue, avaus)"
                + " VALUES( 4, 'Kissa on kala','kalat', 3)");
        lista.add("INSERT INTO Viesti (kayttaja, viesti,alue, avaus)"
                + "VALUES(1, 'Koira on kala',  'kalat', 3)");
        lista.add("INSERT INTO Viesti (kayttaja, viesti ,alue, avaus)"
                + "VALUES( 2, 'Ankka on kala', 'kalat', 3)");
        lista.add("INSERT INTO Viesti (kayttaja, viesti ,alue, avaus)"
                + "VALUES( 3, 'Kissa on kala','kalat', 3)");
        lista.add("INSERT INTO Viesti (kayttaja, viesti,alue, avaus)"
                + " VALUES(4, 'Kissa on kala','sekalainen', 3)");

        return lista;
    }

    private List<String> sqliteLauseet() {
        ArrayList<String> lista = new ArrayList<>();

        // tietokantataulujen luomiseen tarvittavat komennot suoritusjärjestyksessä
        lista.add("CREATE TABLE Alue (nimi VARCHAR(20)  PRIMARY KEY);");

        lista.add("CREATE TABLE Kayttaja (id INTEGER PRIMARY KEY, nimi varchar(15));");

        lista.add("CREATE TABLE Avaus (avaus_id INTEGER PRIMARY KEY, avaus VARCHAR(100),"
                + "timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,"
                + "alue varchar(20) NOT NULL,"
                + "FOREIGN KEY (alue) REFERENCES Alue(nimi));");

        lista.add("CREATE TABLE Viesti(viesti_id INTEGER PRIMARY KEY, "
                + "kayttaja INTEGER NOT NULL, viesti VARCHAR(300),"
                + "timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,"
                + "alue VARCHAR(20),avaus INTEGER NOT NULL,"
                + "FOREIGN KEY (avaus) REFERENCES Avaus(avaus_id),"
                + "FOREIGN KEY (kayttaja) REFERENCES kayttaja(id),"
                + "FOREIGN KEY (alue) REFERENCES alue(nimi));");

        //Lisätään käyttäjiä:
        lista.add("INSERT INTO Kayttaja(id,nimi) VALUES(1, 'Jonne')");
        lista.add("INSERT INTO Kayttaja(id,nimi) VALUES(2, 'Joni')");
        lista.add("INSERT INTO Kayttaja(id,nimi) VALUES(3, 'Jonna')");
        lista.add("INSERT INTO Kayttaja (id,nimi) VALUES(4, 'Anni')");

        //Lisätään Alueita
        lista.add("INSERT INTO Alue(nimi) VALUES('sekalainen')");
        lista.add("INSERT INTO Alue (nimi) VALUES('kalat')");
        lista.add("INSERT INTO Alue (nimi) VALUES('koirat')");

        // Lisätään keskustelunavauksia
        lista.add("INSERT INTO Avaus (avaus, alue)"
                + " VALUES('Miksi', 'sekalainen')");
        lista.add("INSERT INTO Avaus (avaus, alue) VALUES('Siksi', 'sekalainen')");
        lista.add("INSERT INTO Avaus (avaus,alue) VALUES('Hauki kala','kalat')");

        //Lisätään vastauksia
        lista.add("INSERT INTO Viesti (kayttaja, viesti,alue, avaus)"
                + " VALUES( 4, 'Kissa on kala','kalat', 3)");
        lista.add("INSERT INTO Viesti (kayttaja, viesti,alue, avaus)"
                + "VALUES(1, 'Koira on kala',  'kalat', 3)");
        lista.add("INSERT INTO Viesti (kayttaja, viesti ,alue, avaus)"
                + "VALUES( 2, 'Ankka on kala', 'kalat', 3)");
        lista.add("INSERT INTO Viesti (kayttaja, viesti ,alue, avaus)"
                + "VALUES( 3, 'Kissa on kala','kalat', 3)");
        lista.add("INSERT INTO Viesti (kayttaja, viesti,alue, avaus)"
                + " VALUES(4, 'Kissa on kala','sekalainen', 3)");

        return lista;
    }
}
