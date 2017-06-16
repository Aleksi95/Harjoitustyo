
package tikape.runko.domain;


public class Avaus {
    private Integer id;
    private String alue;
    private String avaus;
    private String timestamp;
    
    public Avaus(Integer id, String alue, String avaus, String timestamp){
        this.id = id;
        this.alue = alue;
        this.avaus = avaus;
        this.timestamp = timestamp; 
    }
    
    public String getAvaus(){
        return " " + this.avaus;
    }
    
    public Integer getId(){
        return this.id;
    }
    
    public String getTimestamp(){
        return this.timestamp;
    }
    
    public String getAlue(){
        return this.alue;
    }
}
