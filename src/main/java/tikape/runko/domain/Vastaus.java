
package tikape.runko.domain;


public class Vastaus {
    private int id;
    private String kayttaja;
    private String alue;
    private String vastaus;
    private String avaus;
    private String timestamp;
    // timestamp
    
    public Vastaus (Integer id, String kayttaja, String alue, String vastaus, String timestamp, String avaus){
        this.id = id;
        this.kayttaja = kayttaja;
        this.alue = alue;
        this.vastaus = vastaus;
        this.timestamp = timestamp;
        this.avaus = avaus;
    }
    
    public String getVastaus(){
        return this.vastaus;
    }
    
    public String getKayttaja(){
        return this.kayttaja;
    }
    
    public String getAvaus(){
        return this.avaus;
    }

    public int getId() {
        return id;
    }
    
    
}
