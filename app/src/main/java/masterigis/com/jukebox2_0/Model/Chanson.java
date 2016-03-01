package masterigis.com.jukebox2_0.Model;

/**
 * Created by Hugues on 10/12/2015.
 */
public class Chanson {
    private long id;
    private String titre;
    private String artiste;
    private String path;

    public Chanson(long idChanson, String titreChanson, String artisteDeLaChanson) {
        id=idChanson;
        titre=titreChanson;
        artiste=artisteDeLaChanson;
    }

    // Constructeur dans le cas où nous voulons récupérer les chemin du fichier
    public Chanson(long idChanson, String titreChanson, String artisteDeLaChanson, String pathChanson) {
        id=idChanson;
        titre=titreChanson;
        artiste=artisteDeLaChanson;
        path=pathChanson;
    }

    public long getID(){return id;}
    public String getTitle(){return titre;}
    public String getArtist(){return artiste;}
    public String getPath(){return path;}

}
