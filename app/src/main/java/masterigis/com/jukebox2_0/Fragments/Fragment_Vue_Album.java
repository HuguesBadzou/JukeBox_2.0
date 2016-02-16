package masterigis.com.jukebox2_0.Fragments;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import m1geii.com.jukebox2_0.R;
import masterigis.com.jukebox2_0.Model.AdapterChansons;
import masterigis.com.jukebox2_0.Model.Chanson;
import masterigis.com.jukebox2_0.MusicService;

public class Fragment_Vue_Album extends Fragment {

    String nomAlbumRecup;
    private ArrayList<Chanson> arrayChansons;
    private ListView liste_chansons_album;
    private MusicService musicSrv;
    private Intent playIntent;
    private boolean musicBound=false;
    Bundle donneesChanson=new Bundle();

     public Fragment_Vue_Album() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nomAlbumRecup=getArguments().getString("Album");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View viewAlbum= inflater.inflate(R.layout.fragment_vue_album, container, false);
        liste_chansons_album=(ListView)viewAlbum.findViewById(R.id.listView_vue_album);

        arrayChansons=new ArrayList<>();
        recupererChansonsAlbum(nomAlbumRecup.replace("'", "''").replace("\"", "\"\"")); // Pour eviter les crash avec les nom
        AdapterChansons adaptArtiste = new AdapterChansons(getActivity(), arrayChansons);
        liste_chansons_album.setAdapter(adaptArtiste);

        // Gestion du clique
        liste_chansons_album.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                musicSrv.setList(arrayChansons);
                musicSrv.setChanson(position);
                musicSrv.playChanson();
            }
        });

        // Gestion du clique long
        registerForContextMenu(liste_chansons_album);

        return viewAlbum;
    }

    // Méthode de récupération des musiques de l'album selectionné
    public void recupererChansonsAlbum(String album) {
        ContentResolver musicResolver = getActivity().getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri,
                null,
                MediaStore.Audio.Media.ALBUM+"='"+album+"'", // On cherche seulement les row_chansons ou le nom de l'abum est le même que celui selectionné
                null,
                MediaStore.Audio.Media.TRACK);

        if (musicCursor != null && musicCursor.moveToFirst()) {

            int idAlbumColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int titreChansonAlbumColumn = musicCursor.getColumnIndex(MediaStore.MediaColumns.TITLE);
            int artisteAlbumColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int nomAlbumColumn = musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media.ALBUM );

            // Boucle d'ajout des musiques
            do {
                long idChansonAlbum = musicCursor.getLong(idAlbumColumn);
                String titreChansonAlbum = musicCursor.getString(titreChansonAlbumColumn);
                String artisteAlbum = musicCursor.getString(artisteAlbumColumn);
                String titreAlbum = musicCursor.getString(nomAlbumColumn);

                arrayChansons.add(new Chanson(idChansonAlbum,titreChansonAlbum,artisteAlbum));
            }
            while (musicCursor.moveToNext());
            musicCursor.close();
        }
    }

    //Connexion au service
    private ServiceConnection musicConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder)service;
            //get service
            musicSrv = binder.getService();
            //pass list
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        if(playIntent==null){
            playIntent = new Intent(getActivity(), MusicService.class);
            getActivity().bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            getActivity().startService(playIntent);
        }
    }

    @Override
    public void onDestroy() {
        if(musicSrv!=null){
            musicSrv.stopSelf();
        }
        super.onDestroy();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    // Partie gérant le menu contextuel
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater Inflater = getActivity().getMenuInflater();
        Inflater.inflate(R.menu.contextmenu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        // Déclaration du fragment de l'interface
        Fragment fragment;
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

        switch (item.getItemId()) {
            case R.id.AjoutListeLecture:
                String titreMusiqueAjout = arrayChansons.get(info.position).getTitle();
                String artisteMusiqueAjout = arrayChansons.get(info.position).getArtist();
                Long idMusiqueAjout = arrayChansons.get(info.position).getID();

                donneesChanson.putString("titreMusiqueAjout", titreMusiqueAjout);
                donneesChanson.putString("artisteMusiqueAjout", artisteMusiqueAjout);
                donneesChanson.putLong("idMusiqueAjout", idMusiqueAjout);

                fragment = new Fragment_Ajout_Musiques();
                fragment.setArguments(donneesChanson); // Envoie des infos de la musique au fragment
                ft.replace(R.id.mainFrame, fragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .addToBackStack(null)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                        .commit();
                break;

            /*case R.id.AllerAPageAlbum:

                break;

            case R.id.AllerAPageArtiste:
                String titreMusique = arrayChansons.get(info.position).getTitle();
                String artisteMusique = arrayChansons.get(info.position).getArtist();

                break;*/

            case R.id.SuppressionChanson:
                //deleteNote(info.id);
                break;
            default:
                return super.onContextItemSelected(item);
        }
        return true;
    }


}
