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

public class Fragment_Chansons extends Fragment{

    public Fragment_Chansons() {
        // Required empty public constructor
    }

    ListView listes_chansons;
    private ArrayList<Chanson> arrayChansons;
    private MusicService musicSrv;
    private Intent playIntent;
    private boolean musicBound=false;
    Bundle donneesChanson=new Bundle();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment_chansons, container, false);
        listes_chansons = (ListView) v.findViewById(R.id.listview_chansons);
        arrayChansons = new ArrayList<>();

        // Appel de la méthode de récupération des musiques
        recupererListeChansons();

        AdapterChansons adaptChanson = new AdapterChansons(getActivity(), arrayChansons);
        listes_chansons.setAdapter(adaptChanson);

        listes_chansons.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                musicSrv.setList(arrayChansons);
                musicSrv.setChanson(position);
                musicSrv.playChanson();
            }
        });

        // Gestion du clique long
        registerForContextMenu(listes_chansons);

        return v;
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
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void recupererListeChansons(){
        //retrieve song info
        ContentResolver musicResolver = getActivity().getContentResolver();
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

                donneesChanson.putString("titreMusiqueAjout",titreMusiqueAjout);
                donneesChanson.putString("artisteMusiqueAjout",artisteMusiqueAjout);
                donneesChanson.putLong("idMusiqueAjout", idMusiqueAjout);

                fragment = new Fragment_Ajout_Musiques();
                fragment.setArguments(donneesChanson); // Envoie des infos de la musique au fragment
                ft.replace(R.id.mainFrame, fragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .addToBackStack(null)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                        .commit();
                break;
/*
            case R.id.AllerAPageAlbum:

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
}