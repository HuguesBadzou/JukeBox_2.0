package masterigis.com.jukebox2_0.Model;

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
import masterigis.com.jukebox2_0.Ajouter_Musique;
import masterigis.com.jukebox2_0.ModeNonConnecte.Fragments.Fragment_Ajout_Musiques;

/**
 * Created by naveck on 21/01/2016.
 */
public class AdapterAjoutMusique extends BaseAdapter {
    private ArrayList<Chanson> chansons;
    private LayoutInflater songInf;
    private Context context;
    boolean[] itemChecked;
    public static String contenuArtiste = null;
    public static String contenuTitre = null;
    public static long contenuId;
    public static String contenu = null;
    private int pst;
    private long idChanson;
    public static ArrayList<String> hm = new ArrayList<String>();
    Fragment_Ajout_Musiques fragment;

    public AdapterAjoutMusique(Context c, ArrayList<Chanson> lesChansons){
        chansons=lesChansons;
        songInf=LayoutInflater.from(c);
        this.context = c;
        itemChecked = new boolean[chansons.size()];
    }

    @Override
    public int getCount() {
        return chansons.size();
    }

    @Override
    public Object getItem(int position)
    {
        return chansons.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

   /* public void setSelected(boolean selected){
        this.selected = selected;
    }*/

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        //map to song layout
        LinearLayout songLay = (LinearLayout)songInf.inflate(R.layout.listeitem_playlist, parent, false);
        //get title and artist views
        final TextView viewChanson = (TextView)songLay.findViewById(R.id.listItemPlaylist_titre);
        final TextView artistView = (TextView)songLay.findViewById(R.id.listItemPlaylist_artiste);
        final CheckBox rd = (CheckBox)songLay.findViewById(R.id.CheckBoxAjout);
        rd.setChecked(false);


        if (itemChecked[position])
            rd.setChecked(true);
        else
            rd.setChecked(false);

        //get song using position
        final Chanson chansonCourante = chansons.get(position);

        rd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (rd.isChecked()) {
                    itemChecked[position] = true;

                    contenuTitre = viewChanson.getText().toString();
                    contenuArtiste = artistView.getText().toString();
                    contenuId = chansonCourante.getID();
                    //hm.clear();

                    contenu = contenuArtiste + "/" + contenuTitre + "/" + contenuId;

                    hm.add(contenu);
                    pst = hm.indexOf(contenu);

                    ((Ajouter_Musique)context).selectionElement(pst);
                    //Toast.makeText(context.getApplicationContext(), hm.size()+"", Toast.LENGTH_SHORT).show();

                } else {
                    itemChecked[position] = false;
                    pst = hm.indexOf(contenu);
                    hm.remove(pst);
                }
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

    public static ArrayList<String> getHm() {
        return hm;
    }
}
