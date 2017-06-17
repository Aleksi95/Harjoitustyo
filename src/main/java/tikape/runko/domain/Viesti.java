
package tikape.runko.domain;


public class Viesti {
    private int id;
    private String viesti;
    private String kayttaja;
    private String alue;
    private int avaus;
    private String timestamp;

    
    public Viesti (Integer id, String viesti, String kayttaja, String alue, String timestamp, int avaus){
        this.id = id;
        this.viesti = viesti;
        this.kayttaja = kayttaja;
        this.alue = alue;
        this.timestamp = timestamp;
        this.avaus = avaus;
    }
    
    public String getViesti(){
        return this.viesti;
    }
    
    public String getKayttaja(){
        return this.kayttaja;
    }
    
    public int getAvaus(){
        return this.avaus;
    }

    public int getId() {
        return id;
    }
    
    
}
