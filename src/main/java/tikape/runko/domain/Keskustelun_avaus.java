
package tikape.runko.domain;


public class Keskustelun_avaus {
    private Integer id;
    private Alue alue;
    private Kayttaja kayttaja;
    private String keskust_avaus;
    
    public Keskustelun_avaus(Integer id, Alue alue, Kayttaja kayttaja, String keskust_avaus){
        this.id = id;
        this.alue = alue;
        this.kayttaja = kayttaja;
        this.keskust_avaus = keskust_avaus;
        //timestamp 
    }
    
    public String getKeskustelun_avaus(){
        return this.keskust_avaus;
    }
}
