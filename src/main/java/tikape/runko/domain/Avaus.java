
package tikape.runko.domain;


public class Avaus {
    private Integer id;
    private String alue;
    private String avaus;
    private String timestamp;
    private int viesteja;
    
    public Avaus(Integer id, String alue, String avaus, String timestamp, int viesteja){
        this.id = id;
        this.alue = alue;
        this.avaus = avaus;
        this.timestamp = timestamp; 
        this.viesteja = viesteja;
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
    
    public int getViesteja(){
        return this.viesteja;
    }
}
