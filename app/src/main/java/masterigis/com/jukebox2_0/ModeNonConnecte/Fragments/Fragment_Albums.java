package masterigis.com.jukebox2_0.ModeNonConnecte.Fragments;

import android.content.ContentResolver;
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
import masterigis.com.jukebox2_0.Model.AdapterAlbums;
import masterigis.com.jukebox2_0.Model.Album;


public class Fragment_Albums extends Fragment{
    private ArrayList<Album> arrayAlbums;
    private ListView listes_albums;
    Bundle donneesAlbum=new Bundle();
    Fragment fragmentVueAlbum=new Fragment_Vue_Album();

    public Fragment_Albums() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_albums, container, false);

        listes_albums = (ListView) v.findViewById(R.id.listview_albums);
        arrayAlbums = new ArrayList<>();
        AdapterAlbums adaptAlbum = new AdapterAlbums(getActivity(), arrayAlbums);

        // Appel de la méthode de récupération des musiques
        recupererListeAlbums();

        listes_albums.setAdapter(adaptAlbum);
        listes_albums.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Album ligne_albumSelect = (Album) listes_albums.getItemAtPosition(position);
                String titreAlbumSelect=ligne_albumSelect.getTitle();

                donneesAlbum.putString("Album",titreAlbumSelect);
                fragmentVueAlbum.setArguments(donneesAlbum);

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame, fragmentVueAlbum)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .addToBackStack(null)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                        .commit();
            }
        });

        return v;
    }

    // Méthode de récupération des Albums
    public void recupererListeAlbums(){
        //retrieve song info
        ContentResolver musicResolver = getActivity().getContentResolver();
        Uri musicUri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;

        Cursor musicCursor = musicResolver.query(musicUri,
                null,
                null,
                null,
                MediaStore.Audio.Albums.ALBUM +" ASC");


        if((musicCursor!=null && musicCursor.moveToFirst())){

            //get columns
            int titleColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);

            int idColumn = musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media._ID);

            int artistColumn = musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media.ARTIST);

            int anneeAlbumColumn = musicCursor.getColumnIndex (MediaStore.Audio.Albums.FIRST_YEAR);

            do {
                long idAlbum = musicCursor.getLong(idColumn);
                String titreAlbum = musicCursor.getString(titleColumn);
                String artistAlbum = musicCursor.getString(artistColumn);
                String anneeAlbum=musicCursor.getString(anneeAlbumColumn);

                arrayAlbums.add(new Album(idAlbum, titreAlbum, artistAlbum, anneeAlbum));
            }
            while (musicCursor.moveToNext());
            musicCursor.close();
        }
    }
}
