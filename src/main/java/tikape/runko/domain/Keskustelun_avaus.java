
package tikape.runko.domain;


public class Keskustelun_avaus {
    private Integer id;
    private String alue;
    private int kayttaja;
    private String keskust_avaus;
    private String timestamp;
    
    public Keskustelun_avaus(Integer id, String alue, int kayttaja, String keskust_avaus, String timestamp){
        this.id = id;
        this.alue = alue;
        this.kayttaja = kayttaja;
        this.keskust_avaus = keskust_avaus;
        this.timestamp = timestamp; 
    }
    
    public String getAvaus(){
        return this.keskust_avaus;
    }
    
    public Integer getId(){
        return this.id;
    }
}
