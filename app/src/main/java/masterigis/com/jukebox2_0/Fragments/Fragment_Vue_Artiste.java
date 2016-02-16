package masterigis.com.jukebox2_0.Fragments;

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
import masterigis.com.jukebox2_0.Model.AdapterAlbums;
import masterigis.com.jukebox2_0.Model.Album;

public class Fragment_Vue_Artiste extends Fragment {

    String nomArtisteRecup;
    private ArrayList<Album> arrayAlbums,arrayAlbumsTriee;
    private ListView liste_albums;
    Bundle donneesAlbum=new Bundle();
    Fragment_Vue_Album fragment_vue_album=new Fragment_Vue_Album();

    public Fragment_Vue_Artiste() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Récupération des données passées par le fragment précedent
        nomArtisteRecup=getArguments().getString("Artiste");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_vue_artiste, container, false);
        liste_albums=(ListView)view.findViewById(R.id.listView_vue_artiste);

        arrayAlbums=new ArrayList<>();
        arrayAlbumsTriee=new ArrayList<>();
        recupererAlbum(nomArtisteRecup.replace("'","''").replace("\"","\"\"")); // Pour eviter les crash avec les non
        trierDoublons(arrayAlbums);
        AdapterAlbums adaptArtiste = new AdapterAlbums(getActivity(), arrayAlbumsTriee);
        liste_albums.setAdapter(adaptArtiste);

        // Gestion du clique
        liste_albums.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Album ligne_albumSelect = (Album) liste_albums.getItemAtPosition(position);
                String titreAlbumSelect=ligne_albumSelect.getTitle();

                donneesAlbum.putString("Album",titreAlbumSelect);
                fragment_vue_album.setArguments(donneesAlbum);

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame, fragment_vue_album)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .addToBackStack(null)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                        .commit();
            }
        });

        return view;
    }

    // Méthode de récupération des album
    public void recupererAlbum(String artist) {

        ContentResolver musicResolver = getActivity().getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection= {"DISTINCT "+MediaStore.Audio.Media.ALBUM+", "+MediaStore.Audio.Media.ARTIST,MediaStore.Audio.Media._ID,MediaStore.Audio.Media.YEAR}; // Je choisi les éléments que je vais récupérer des mes fichiers musicaux
        Cursor musicCursor = musicResolver.query(musicUri,
                projection,
                MediaStore.Audio.Media.ARTIST+"='"+artist+"'",
                null,
                MediaStore.Audio.Media.ALBUM +" ASC");

        if (musicCursor != null && musicCursor.moveToFirst()) {
            long idAlbumColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int albumColumn = musicCursor.getColumnIndex (MediaStore.Audio.Media.ALBUM);
            int anneeAlbumColumn = musicCursor.getColumnIndex (MediaStore.Audio.Media.YEAR);


            //add songs to list
            do {
                long idAlbum=musicCursor.getLong((int) idAlbumColumn);
                String titreAlbum=musicCursor.getString(albumColumn);
                String artisteAlbum=musicCursor.getString(artistColumn);
                String anneeAlbum=musicCursor.getString(anneeAlbumColumn);

                arrayAlbums.add(new Album(idAlbum,titreAlbum,anneeAlbum,anneeAlbum));
            }
            while (musicCursor.moveToNext());
            musicCursor.close();
        }
    }

    // Méthode de supression des doublons
    void trierDoublons(ArrayList<Album> arrayAlbums){
        for(int i=0;i<arrayAlbums.size()-1;i++){
            if(!((arrayAlbums.get(i)).getTitle()).equals((arrayAlbums.get(i+1)).getTitle())){
                arrayAlbumsTriee.add(arrayAlbums.get(i));
            }
        }
        // Pour ajouter la dernière ligne du tableau
        arrayAlbumsTriee.add(arrayAlbums.get(arrayAlbums.size()-1));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}
