package masterigis.com.jukebox2_0.Model;

public class Artiste {
    private long id;
    private String nom;
    private String totalChansons;

    public Artiste(long idArtiste, String nomArtiste, String nbChansons) {
        id=idArtiste;
        nom=nomArtiste;
        totalChansons=nbChansons;
    }

    public long getID(){return id;}
    public String getNomArtiste(){return nom;}
    public String getTotalChansons(){return totalChansons;}
}
