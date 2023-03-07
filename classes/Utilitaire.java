package model;

public class Utilitaire {
    public String getProcessUrl(String url){
        String[] parts = url.split("//");
        String[] parts2 = parts[1].split("/");
        
        return parts2[2];
        
    }
}
