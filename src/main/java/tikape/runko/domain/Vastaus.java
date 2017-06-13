
package tikape.runko.domain;


public class Vastaus {
    private int id;
    private int kayttaja_id;
    private String alue;
    private String vastaus;
    private int avaus_id;
    private String timestamp;
    // timestamp
    
    public Vastaus (Integer id, Integer kayttaja, String alue, String vastaus, String timestamp, Integer avaus_id){
        this.id = id;
        this.kayttaja_id = kayttaja;
        this.alue = alue;
        this.vastaus = vastaus;
        this.timestamp = timestamp;
        this.avaus_id = avaus_id;
    }
    
    public String getVastaus(){
        return this.vastaus;
    }
    
    public int getKayttaja(){
        return this.kayttaja_id;
    }

    public int getId() {
        return id;
    }
    
    
}
