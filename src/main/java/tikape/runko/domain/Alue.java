
package tikape.runko.domain;


public class Alue {
    
    private String nimi;
    private String timestamp;
    private Integer viesteja;
    
    public Alue(String nimi){
        this.nimi = nimi;
        this.timestamp = "Ei viestejä";
        this.viesteja = 0;
        
        
    }
    
    public void setTimestamp(String timestamp){
        this.timestamp = timestamp;
    }
    
    public void setViesteja(Integer viesteja){
        this.viesteja = viesteja;
    }
    
    public String getNimi(){
        return this.nimi;
    }
    
    public Integer getViesteja(){
        return this.viesteja;
    }
    
    public String getTimestamp(){
        return this.timestamp;
    }
    
    public String toString(){
       return this.nimi + " | " + this.viesteja + " | " + this.timestamp;
    }
    
}
