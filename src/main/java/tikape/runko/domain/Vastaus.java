
package tikape.runko.domain;


public class Vastaus {
    private Integer id;
    private Kayttaja kayttaja;
    private Alue alue;
    private String vastaus;
    // timestamp
    
    public Vastaus ( Integer id, Kayttaja kayttaja, Alue alue, String Vastaus){
        this.id = id;
        this.kayttaja = kayttaja;
        this.alue = alue;
        this.vastaus = vastaus;
        
    }
    
    public String getVastaus(){
        return this.vastaus;
    }
    
    public String getKayttaja(){
        return this.kayttaja.getNimi();
    }
    
    
}
