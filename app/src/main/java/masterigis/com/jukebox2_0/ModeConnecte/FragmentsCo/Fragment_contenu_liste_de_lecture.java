package masterigis.com.jukebox2_0.ModeConnecte.FragmentsCo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import m1geii.com.jukebox2_0.R;
import masterigis.com.jukebox2_0.Ajouter_Musique;
import masterigis.com.jukebox2_0.ModeNonConnecte.Diffuseur.Serveur;
import masterigis.com.jukebox2_0.Model.AdapterChansons;
import masterigis.com.jukebox2_0.Model.BDAdapter;
import masterigis.com.jukebox2_0.Model.Chanson;
import masterigis.com.jukebox2_0.MusicService;


public class Fragment_contenu_liste_de_lecture extends Fragment {
    String NomListe;
    ArrayList<Chanson> Chansons = new ArrayList<>();
    ArrayList<String> ltitres = new ArrayList<>();
    ArrayList<String> lartistes = new ArrayList<>();
    ArrayList<String> idMusiquePlaylist = new ArrayList<>();

    ArrayAdapter<String> adapter;
    ListView listeMusiquePlaylist;
    EditText NomDeLaNouvelleListe;
    FloatingActionButton btnFloatingAjoutTitre, btnFloatingPartagerPlaylist;
    LayoutInflater songInf;
    String id,flag;
    AdapterChansons adaptChanson;
    BDAdapter base;
    Cursor curseurBase;
    Bundle donneesContenuPlaylist=new Bundle();

    // Parti déclaration du service chanson
    private MusicService musicSrv;
    private Intent playIntent;
    private boolean musicBound=false;

    FragmentTransaction ft;
    Fragment fragment;

    public Fragment_contenu_liste_de_lecture() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Récupération des données
        id=getArguments().getString("identifiantPlaylist");
        flag=getArguments().getString("Flag");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View vueContenuPlaylist=inflater.inflate(R.layout.fragment_contenu_liste_de_lecture, container, false);

        btnFloatingAjoutTitre=(FloatingActionButton)vueContenuPlaylist.findViewById(R.id.bouton_flottant_ajouter_musiques);
        btnFloatingPartagerPlaylist =(FloatingActionButton)vueContenuPlaylist.findViewById(R.id.bouton_flottant_partager_liste_de_lecture);
        listeMusiquePlaylist=(ListView)vueContenuPlaylist.findViewById(R.id.liste_des_musiques);

        if(flag.equals("partager")){
            btnFloatingPartagerPlaylist.setVisibility(View.VISIBLE);
            btnFloatingAjoutTitre.setVisibility(View.INVISIBLE);
        }

        else{
            btnFloatingAjoutTitre.setVisibility(View.VISIBLE);
            btnFloatingPartagerPlaylist.setVisibility(View.INVISIBLE);
        }

        adaptChanson = new AdapterChansons(getActivity(),Chansons);
        listeMusiquePlaylist.setAdapter(adaptChanson);

        // Listener lors du clique sur une musique de la liste de lecture
        listeMusiquePlaylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                musicSrv.setList(Chansons);
                musicSrv.setChanson(position);
                musicSrv.playChanson();
            }
        });

        // Gestion du clique long
        registerForContextMenu(listeMusiquePlaylist);

        base = new BDAdapter(getActivity());
        base.ouvrirBase();
        curseurBase = base.ObtenirTousLesTitres(id);
        Chansons.clear();

        if(curseurBase!=null && curseurBase.moveToFirst()){
            do
            {
                String colum_idm=curseurBase.getString(0);

                String name = curseurBase.getString(1);
                ltitres.add(name);

                String art = curseurBase.getString(2);
                lartistes.add(art);

                String ids = curseurBase.getString(3);
                long idsBis=Long.parseLong(ids);
                idMusiquePlaylist.add(colum_idm);     // Id de la chanson  dans la liste de lecture (clé primaire)

                Chansons.add(new Chanson(idsBis, name, art));
            }
            while(curseurBase.moveToNext());

            curseurBase.close();
        }

        // Ecouteur bouton Ajouter
        btnFloatingAjoutTitre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                donneesContenuPlaylist.putString("identifiantPlaylist", id);
                Intent i = new Intent(getActivity(), Ajouter_Musique.class);
                i.putExtra("IdentifiantListe", id);
                startActivity(i);
                getActivity().getSupportFragmentManager().popBackStack();

                //Fragment fragment;
                //FragmentTransaction ft;
                /*ft = getActivity().getSupportFragmentManager().beginTransaction();

                fragment = new Fragment_Ajout_Musiques();
                fragment.setArguments(donneesContenuPlaylist); // Envoie des infos de la musique au fragment
                ft.replace(R.id.mainFrame, fragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .addToBackStack(null)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                        .commit();*/
            }
        });

        // Ecouteur bouton partager
        btnFloatingPartagerPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), Serveur.class);
                WifiManager activerWifi=(WifiManager)getActivity().getSystemService(Context.WIFI_SERVICE);;
                i.putExtra("nomListe",id);
                activerWifi.setWifiEnabled(true);
                //getActivity().getSupportFragmentManager().popBackStack();
                startActivity(i);
            }
        });

        return vueContenuPlaylist;
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


    /*@Override
    public void onPause(){
        curseurBase.close();
        base.fermer();
        super.onPause();
    }*/

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
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.contextmenumusiques, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId())
        {
            /*case R.id.AllerAPageAlbum:

                return true;
            case R.id.AllerAPageArtiste:
                return true;*/

            case R.id.SuppressionChanson:   // Supprime toutes le musiques de la base ayant ce titre

                // Je récupère la position de la musique cliqué puis je récupère sa clé primaire dans le tableau contenant les clé primaire
                String idClePrimaire=idMusiquePlaylist.get(info.position);
                base.Supprimerm(idClePrimaire);
                Chansons.remove(Chansons.get(info.position));   // On supprime toute la ligne
                adaptChanson.notifyDataSetChanged();

                Toast.makeText(getActivity().getApplicationContext(),"Supprimée", Toast.LENGTH_SHORT).show();
        }

        return super.onContextItemSelected(item);
    }
}
