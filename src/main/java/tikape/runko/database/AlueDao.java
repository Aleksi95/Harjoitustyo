
package tikape.runko.database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import tikape.runko.domain.Alue;

public class AlueDao implements Dao<Alue, String>{
    
    private Database database;
    
    public AlueDao(Database database){
        this.database = database;
    }

    @Override
    public Alue findOne(String key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Alue Where nimi = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

   
        String nimi = rs.getString("nimi");

        Alue a = new Alue(nimi);

        rs.close();
        stmt.close();
        connection.close();

        return a;
    }

    @Override
    public List<Alue> findAll() throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT alue.nimi as alue, "
                + "Count(viesti.viesti_id) AS viesteja,"
                + "date_trunc('second', (MAX(viesti.timestamp ) + INTERVAL '3 hours'))  AS viimeisin "
                + "FROM Alue LEFT JOIN Avaus ON alue.nimi = avaus.alue LEFT JOIN viesti ON  "
                + "avaus.avaus_id = viesti.avaus GROUP BY alue.nimi");

        ResultSet rs = stmt.executeQuery();
        List<Alue> alueet = new ArrayList<>();
        while (rs.next()) {
           
            String nimi = rs.getString("alue");
            Alue alue = new Alue(nimi);
            
            if(rs.getInt("viesteja") == 0){
                
            }else{
                alue.setViesteja(rs.getInt("viesteja"));
                alue.setTimestamp(rs.getString("viimeisin"));
            }

            alueet.add(alue);
        }

        rs.close();
        stmt.close();
        connection.close();

        return alueet;
    }

    @Override
    public void delete(String key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void lisaaAlue(String key) throws SQLException{
        Connection connection = database.getConnection();

        PreparedStatement stmt = connection.prepareStatement("INSERT INTO Alue (nimi) VALUES(?)");
       
        stmt.setString(1, key);
        stmt.execute();
        
        connection.close();
        
    }
    
}
