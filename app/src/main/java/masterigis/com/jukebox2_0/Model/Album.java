package masterigis.com.jukebox2_0.Model;

/**
 * Created by Hugues on 10/12/2015.
 */
public class Album {
    private long id;
    private String nomAlbum;
    private String artisteAlbum;
    private String nombresChansons;

    public Album(long idAlbum, String titreAlbum, String artisteDeLalbum, String nombreChansonsAlbum) {
        id=idAlbum;
        nomAlbum=titreAlbum;
        artisteAlbum=artisteDeLalbum;
        nombresChansons=nombreChansonsAlbum;
    }

    public long getID(){return id;}
    public String getTitle(){return nomAlbum;}
    public String getArtist(){return artisteAlbum;}
    public String getNombresChansons(){return nombresChansons; }
}
