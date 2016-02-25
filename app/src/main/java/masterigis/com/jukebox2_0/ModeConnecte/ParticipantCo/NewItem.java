package masterigis.com.jukebox2_0.ModeConnecte.ParticipantCo;
// le modèle de données pour chaque ligne.
public class NewItem implements Comparable{
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


    // Classe fesant la comparaison de 2 objets dans le cas ci contre le nombre de vote
    @Override
    public int compareTo(Object another) {

        NewItem objet =(NewItem) another;
        if(vote.length()>objet.getVote().length())
            return -1;
        else if (vote.length()<objet.getVote().length())
            return 1;
        return objet.getVote().compareTo(vote);


    }

}