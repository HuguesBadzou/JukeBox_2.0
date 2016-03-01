package masterigis.com.jukebox2_0.ModeConnecte;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import m1geii.com.jukebox2_0.R;
import masterigis.com.jukebox2_0.Model.Chanson;

/**
 * Created by Mahafous on 26/02/2016.
 */
public class AdapterAjouter_Musique_Dossier_Diffuseur extends BaseAdapter {
    private ArrayList<Chanson> chansons;
    private LayoutInflater songInf;
    private Context context;
    boolean[] itemChecked;
    public static String contenuArtiste = null;
    public static String contenuTitre = null;
    public static String contenuPathMusique = null;
    public static long contenuId;
    public static String contenu = null;
    private int pst;
    private long idChanson;
    public static ArrayList<String> hm = new ArrayList<>();

    public AdapterAjouter_Musique_Dossier_Diffuseur(Context c, ArrayList<Chanson> lesChansons){
        chansons=lesChansons;
        songInf=LayoutInflater.from(c);
        this.context = c;
        itemChecked = new boolean[chansons.size()];
    }

    public int getCount() {
        return chansons.size();
    }


    public Object getItem(int position)
    {
        return chansons.get(position);
    }



    public long getItemId(int position) {
        return position;
    }

   /* public void setSelected(boolean selected){
        this.selected = selected;
    }*/

    int selected_position = -1;

    public View getView(final int position, View convertView, ViewGroup parent) {

        //map to song layout
        LinearLayout songLay = (LinearLayout)songInf.inflate(R.layout.listeitem_playlist, parent, false);
        //get title and artist views
        final TextView viewChanson = (TextView)songLay.findViewById(R.id.listItemPlaylist_titre);
        final TextView artistView = (TextView)songLay.findViewById(R.id.listItemPlaylist_artiste);
        final CheckBox rd = (CheckBox)songLay.findViewById(R.id.CheckBoxAjout);
        rd.setChecked(false);


        if (selected_position==position){
            rd.setChecked(true);}
        else{
            rd.setChecked(false);}

        //get song using position
        final Chanson chansonCourante = chansons.get(position);

        rd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (((CheckBox)v).isChecked()) {
                    selected_position=position;

                    contenuTitre = viewChanson.getText().toString();
                    contenuArtiste = artistView.getText().toString();
                    contenuId = chansonCourante.getID();
                    contenuPathMusique = chansonCourante.getPath();

                    //hm.clear();

                    contenu = contenuArtiste + "/" + contenuTitre + "/" + contenuId + "/" + contenuPathMusique;

                    hm.add(contenu);
                    pst = hm.indexOf(contenu);

                    ((Uploader_dossier_diffuseur)context).selectionElements(pst);

                } else {
                    selected_position=-1;
                    pst = hm.indexOf(contenu);
                    hm.remove(pst);
                }
                notifyDataSetChanged();
            }

        });

        //get song using position
        Chanson chansonCourrante = chansons.get(position);
        //get title and artist strings
        viewChanson.setText(chansonCourrante.getTitle());
        artistView.setText(chansonCourrante.getArtist());
        //set position as tag
        songLay.setTag(position);

        return songLay;
    }

    public static String getContenuArtiste() {
        return contenuArtiste;
    }

    public static String getContenuTitre() {
        return contenuTitre;
    }

    public int getPst() {
        return pst;
    }

    // Fonction de récupération de l'array de string contenant les noms de musiques
    public static ArrayList<String> getHm() {
        return hm;
    }
}
