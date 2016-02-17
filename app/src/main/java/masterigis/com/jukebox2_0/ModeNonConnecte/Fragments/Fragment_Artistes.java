package masterigis.com.jukebox2_0.ModeNonConnecte.Fragments;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import m1geii.com.jukebox2_0.R;
import masterigis.com.jukebox2_0.Model.AdapterArtistes;
import masterigis.com.jukebox2_0.Model.Artiste;

public class Fragment_Artistes extends Fragment {

    // Préparation du fragment à lancer
    Fragment fragmentVueArtiste=new Fragment_Vue_Artiste();
    Bundle donneesArtiste =new Bundle();
    private ArrayList<Artiste> arrayArtistes;
    private ListView listes_artistes;

    public Fragment_Artistes(){
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_artistes,container,false);

        listes_artistes=(ListView)v.findViewById(R.id.listview_artistes);
        arrayArtistes = new ArrayList<>();

        //Appel de la methode de récupération des row_artistes
        recupererListeArtistes();

        AdapterArtistes adapterArtiste = new AdapterArtistes(getActivity(),arrayArtistes);
        listes_artistes.setAdapter(adapterArtiste);

        listes_artistes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Artiste ligne_artistSelect = (Artiste) listes_artistes.getItemAtPosition(position);
                String nomArtisteSelect=ligne_artistSelect.getNomArtiste();

                donneesArtiste.putString("Artiste", nomArtisteSelect);
                fragmentVueArtiste.setArguments(donneesArtiste);

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame, fragmentVueArtiste)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .addToBackStack(null)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                        .commit();
            }
        });

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    //Methode de recuperation des row_artistes
    public void recupererListeArtistes(){
        ContentResolver musicResolver = getActivity().getContentResolver();
        Uri musicUri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri,
                null,
                null,
                null,
                MediaStore.Audio.Artists.ARTIST+" ASC");

        if((musicCursor!=null && musicCursor.moveToFirst())){
            //get columns
            int artistColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int idColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int nbSongsColumn = musicCursor.getColumnIndex(MediaStore.Audio.ArtistColumns.NUMBER_OF_TRACKS);

            do {
                long thisId = musicCursor.getLong(idColumn);
                String thisArtist = musicCursor.getString(artistColumn).replaceAll("'", "\\'");
                String nbChansons = musicCursor.getString(nbSongsColumn);
                arrayArtistes.add(new Artiste(thisId, thisArtist, nbChansons));
            }
            while (musicCursor.moveToNext());
            musicCursor.close();
        }
    }
}
