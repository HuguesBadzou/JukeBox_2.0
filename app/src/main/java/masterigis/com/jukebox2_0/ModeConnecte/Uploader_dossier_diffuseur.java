package masterigis.com.jukebox2_0.ModeConnecte;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import m1geii.com.jukebox2_0.R;
import masterigis.com.jukebox2_0.Model.BDAdapter;
import masterigis.com.jukebox2_0.Model.Chanson;

public class Uploader_dossier_diffuseur extends AppCompatActivity {

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
    private String pathChanson;
    private String contenu = null;
    private String[] tab = null;
    AdapterAjouter_Musique_Dossier_Diffuseur adaptChanson = null;
    final BDAdapter base = new BDAdapter(this);
    Bundle donneesAjoutMusiques=new Bundle();

    private long totalSize = 0;

    private Handler handler;
    String titreRecup="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploader_dossier_diffuseur);

        handler = new Handler (getMainLooper());

        //donneesAjoutMusiques = getIntent().getExtras();
        //id = donneesAjoutMusiques.getString("IdentifiantListe");

        btnFlottantAjoutMusique=(FloatingActionButton)findViewById(R.id.bouton_flottant_valider_musiques_deposer);
        final ListView listeMusiqueAAjouter=(ListView)findViewById(R.id.listview_chansons_a_ajouter_dossier_diffuseur);

        arrayChansons = new ArrayList<>();

        // Appel de la méthode de récupération des musiques
        recupererListeChansons();

        //Test
        btnFlottantAjoutMusique.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Test", Toast.LENGTH_SHORT).show();
            }
        });

        adaptChanson = new AdapterAjouter_Musique_Dossier_Diffuseur(this, arrayChansons);
        listeMusiqueAAjouter.setAdapter(adaptChanson);

        listeMusiqueAAjouter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Chanson chansonSelect=(Chanson)listeMusiqueAAjouter.getItemAtPosition(position);
                titreRecup =chansonSelect.getTitle();
                Toast.makeText(getApplicationContext(),titreRecup, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Fonction de selection de  cochage des éléments
    public void selectionElements(final int position) {

        btnFlottantAjoutMusique.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contenu = adaptChanson.getHm().get(position);

                tab = contenu.split("/");
                artiste = tab[0];
                titre = tab[1];
                chansonId = tab[2];
                pathChanson = tab[3];

                /*if (artiste != null && contenu != null) {

                    base.ouvrirBase();
                    long result1 = base.ajoutLdm(titre, artiste, id,chansonId);

                }*/

                Toast.makeText(getApplicationContext(),pathChanson, Toast.LENGTH_SHORT).show();

            }
        });
    }

    // Fonction des récupération des musiques
    public void recupererListeChansons(){
        //retrieve song info
        ContentResolver musicResolver = this.getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI; // Recherche les fichier MP3 sur la mémoire interne et externe
        Cursor musicCursor = musicResolver.query(musicUri,
                null,
                null,
                null,
                MediaStore.Audio.Media.TITLE + " ASC"); // Le troisième paramètre défini l'ordre de recherche des musiques ici par titre dans l'ordre alphabétique


        if(musicCursor!=null && musicCursor.moveToFirst()){
            //get columns
            int idColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int titleColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int artistColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int pathColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME);

            //add songs to list
            do {
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                String thisPath = musicCursor.getString(pathColumn);
                arrayChansons.add(new Chanson(thisId, thisTitle, thisArtist, thisPath));
            }
            while (musicCursor.moveToNext());
            musicCursor.close();
        }
    }
}
