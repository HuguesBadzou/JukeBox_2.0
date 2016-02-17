package masterigis.com.jukebox2_0;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import m1geii.com.jukebox2_0.R;
import masterigis.com.jukebox2_0.ModeNonConnecte.Fragments.Fragment_Ajout_Musiques;
import masterigis.com.jukebox2_0.Model.AdapterAjoutMusique;
import masterigis.com.jukebox2_0.Model.BDAdapter;
import masterigis.com.jukebox2_0.Model.Chanson;

public class Ajouter_Musique extends AppCompatActivity {

    private ArrayList<Chanson> arrayChansons;
    private ListView listes_chansons;
    FloatingActionButton btnFlottantAjoutMusique;
    boolean selected = false;
    ArrayList <String> checkedValue;
    int i;
    private String id;
    private String artiste = null;
    private String titre = null;
    private String chansonId;
    private String contenu = null;
    private String[] tab = null;
    AdapterAjoutMusique adaptChanson = null;
    final BDAdapter base = new BDAdapter(this);
    Bundle donneesAjoutMusiques=new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_musique);

        donneesAjoutMusiques = getIntent().getExtras();
        id = donneesAjoutMusiques.getString("IdentifiantListe");

        btnFlottantAjoutMusique=(FloatingActionButton)findViewById(R.id.bouton_flottant_valider_musiques);
        ListView listeMusiqueAAjouter=(ListView)findViewById(R.id.listview_chansons_a_ajouter);

        arrayChansons = new ArrayList<>();

        // Appel de la méthode de récupération des musiques
        recupererListeChansons();

        adaptChanson = new AdapterAjoutMusique(this, arrayChansons);
        listeMusiqueAAjouter.setAdapter(adaptChanson);
    }

    // Fonction de cochage de éléments
    public void selectionElement(int position) {

        btnFlottantAjoutMusique.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (i = 0; i < adaptChanson.getHm().size(); i++) {

                    contenu = adaptChanson.getHm().get(i);

                    tab = contenu.split("/");
                    artiste = tab[0];
                    titre = tab[1];
                    chansonId = tab[2];

                    if (artiste != null && contenu != null) {

                        base.ouvrirBase();
                        long result1 = base.ajoutLdm(titre, artiste, id,chansonId);

                    }
                }

                Toast.makeText(getApplicationContext(), "Musiques ajoutées à la liste", Toast.LENGTH_SHORT).show();

                base.fermer();
                adaptChanson.getHm().clear();

                Fragment_Ajout_Musiques fragment_ajout_musiques=new Fragment_Ajout_Musiques();
                Bundle envoiDonneesAjoutMusiques=new Bundle();
                envoiDonneesAjoutMusiques.putString("IdentifiantListe",id);
                fragment_ajout_musiques.setArguments(donneesAjoutMusiques);

                finish(); // Une fois les musique ajouté on ferme l'activité courante
            }
        });
    }

    // Fonction des récupération des musiques
    public void recupererListeChansons(){
        //retrieve song info
        ContentResolver musicResolver = this.getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI; // Recherche les fichier MP3 sur la mémoire interne est externe
        Cursor musicCursor = musicResolver.query(musicUri,
                null,
                null,
                null,
                MediaStore.Audio.Media.TITLE + " ASC"); // Le troisième paramètre défini l'ordre de recherche des musiques ici par titre dans l'ordre alphabétique

        if(musicCursor!=null && musicCursor.moveToFirst()){
            //get columns
            int idColumn = musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media._ID);
            int titleColumn = musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media.TITLE);
            int artistColumn = musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media.ARTIST);

            //add songs to list
            do {
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                arrayChansons.add(new Chanson(thisId, thisTitle, thisArtist));
            }
            while (musicCursor.moveToNext());
            musicCursor.close();
        }
    }
}
