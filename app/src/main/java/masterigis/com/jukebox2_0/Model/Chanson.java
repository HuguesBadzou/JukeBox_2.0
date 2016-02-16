package masterigis.com.jukebox2_0.Model;

/**
 * Created by Hugues on 10/12/2015.
 */
public class Chanson {
    private long id;
    private String titre;
    private String artiste;

    public Chanson(long idChanson, String titreChanson, String artisteDeLaChanson) {
        id=idChanson;
        titre=titreChanson;
        artiste=artisteDeLaChanson;
    }

    public long getID(){return id;}
    public String getTitle(){return titre;}
    public String getArtist(){return artiste;}

}
