package masterigis.com.jukebox2_0.ModeNonConnecte.Participant;
// le modèle de données pour chaque ligne.
public class NewItem {
    private String id;
    private String titre;
    private String artist;
    private String vote;
    public String getVote() {
        return vote;
    }

    public void setVote(String date) {
        this.vote = date;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



}