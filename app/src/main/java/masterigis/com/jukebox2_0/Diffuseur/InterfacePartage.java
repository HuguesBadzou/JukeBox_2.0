package masterigis.com.jukebox2_0.Diffuseur;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

import m1geii.com.jukebox2_0.R;
import masterigis.com.jukebox2_0.Fragments.Fragment_Music_Playback;
import masterigis.com.jukebox2_0.Model.Chanson;
import masterigis.com.jukebox2_0.MusicService;

public class InterfacePartage extends AppCompatActivity {
    ArrayList image_details = null;         // Liste statique
    ArrayList<Chanson> array_chansons;
    Button partager;
    IntentFilter connectionfilter;
    private WifiP2pManager wifiP2pManager;
    private WifiP2pManager.Channel wifiDirectChannel;
    ServerSocket socketserver = null;

    // Variable pour l'envoie de Socket
    DataOutputStream [] oos = new DataOutputStream[70];
    DataInputStream  [] in  = new DataInputStream[70];
    Socket [] socket = new Socket[70];
    int numbreClientConnecte = 0;
     ListView lv1;
    Timer myTimer ;

    // Partie déclaration du service
    private MusicService musicSrv;
    private Intent playIntent;
    private boolean musicBound=false;

    Fragment fragment;
    FrameLayout vueCommande_Lecture;
    /////////////////////////////////

    ///////// Broadcast Receiver DEBUT //////////////////

    ///////// Broadcast Receiver FIN //////////////////

    public android.os.Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.d_activity_interface_partage);
        setTitle("Serveur");

        // récupération de la valeur
        Bundle objetbunble  = this.getIntent().getExtras();
        String InfoPasse= objetbunble.getString("nomListe");

        ///////////// Ajout code Hugues /////////////
        final FloatingActionButton boutonLancerMusique=(FloatingActionButton)findViewById(R.id.bouton_flottant_lancer_musique);
        array_chansons=new ArrayList<>();

        // ****** recuperation de la liste ******
        image_details = new CreerListBDD(InterfacePartage.this).getList(InfoPasse) ;
        connectionfilter = new IntentFilter(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);

        initializeWiFiDirect();
        lv1 = (ListView) findViewById(R.id.liste_musiques_votes);
        lv1.setAdapter(new CustomListAdapter(this, image_details));


        /////////////////// Code ajouté Hugues ///////////////////
        vueCommande_Lecture=(FrameLayout)findViewById(R.id.conteneur_commandes_lecture);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        fragment=new Fragment_Music_Playback();

        ft.replace(R.id.conteneur_commandes_lecture,fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();

        boutonLancerMusique.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boutonLancerMusique.setVisibility(View.INVISIBLE);
                vueCommande_Lecture.setVisibility(View.VISIBLE);
                creerListeChansons(image_details);
                musicSrv.setList(array_chansons);
                musicSrv.systemeDeVotes(); // Indique au service qu'on est dans le système de vote
                musicSrv.setChanson(0);
                musicSrv.playChanson();
                Collections.sort(image_details);
                ((NewItem)image_details.get(0)).setVote("Vote : 0");
                lv1.invalidateViews();

            }
        });
        // Intent filter pour musique finie
        IntentFilter intentFilter=new IntentFilter("finMusic");
        registerReceiver((musicFinie), intentFilter);
        // Intent filter pour musique finie

        registerReceiver(connectionChangedReceiver, connectionfilter);
        initializeWiFiDirect();
        MyTimerTask myTask = new MyTimerTask();
        myTimer = new Timer();
        myTimer.schedule(myTask, 20000, 20000);

    }

    // Cette classe permet de réactiver la Wi-Fi direct après 2 minutes pour garder
    class MyTimerTask extends TimerTask {
        public void run() {
            wifiP2pManager.discoverPeers(wifiDirectChannel, actionListener);
        }
    }
    private WifiP2pManager.ActionListener actionListener = new WifiP2pManager.ActionListener() {

        public void onFailure(int reason) {
            String errorMessage = "Echec Wi-Fi direct: ";
            switch (reason) {
                case WifiP2pManager.BUSY :
                    errorMessage += "Framework occuppé."; break;
                case WifiP2pManager.ERROR :
                    errorMessage += "Erreur interne."; break;
                case WifiP2pManager.P2P_UNSUPPORTED :
                    errorMessage += "Wifi direct non supporté.";
                    Toast.makeText(getApplicationContext(), "Wifi direct non supporté.", Toast.LENGTH_LONG).show();
                    break;
                default:
                    errorMessage += "Erreur inconnue."; break;

            }
            // Log.d(TAG, errorMessage);
        }

        public void onSuccess() {
        }
    };

    ///////////////////////// Broacast Receiver ///////////////////////

    // Ecouteur d'événement
    BroadcastReceiver musicFinie=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            creerListeChansons(image_details);
            musicSrv.setList(array_chansons);
            musicSrv.setChanson(0);
            musicSrv.playChanson();
            ((NewItem)image_details.get(0)).setVote("Vote : 0");
            Collections.sort(image_details);

            lv1.invalidateViews();
        }
    };

    ///////////////////////// Broacast Receiver ///////////////////////

    /////// Méthode de recréation d'une arrayList de row_chansons DEBUT ///////
    private void creerListeChansons(ArrayList liste_recup){
        array_chansons.clear();
        for(int i=0;i<liste_recup.size();i++){
            NewItem ligne_recup=(NewItem)lv1.getItemAtPosition(i);
            array_chansons.add(new Chanson(Long.valueOf(ligne_recup.getId()),ligne_recup.getTitre(),ligne_recup.getArtist()));
        }
    }

    /////// Méthode de recréation d'une arrayList de row_chansons FIN ///////

    /////// Connexion au service de musique DEBUT ///////

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

    /////// Connexion au service de musique FIN ///////

    /////// Début cycle pour le service  ///////

    @Override
    public void onStart() {
        super.onStart();
        if(playIntent==null){
            playIntent = new Intent(InterfacePartage.this, MusicService.class);
            this.bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            this.startService(playIntent);
        }
    }

    /////// Cycle de vie pour le service  ///////


    BroadcastReceiver connectionChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            // Extract the NetworkInfo
            String extraKey = WifiP2pManager.EXTRA_NETWORK_INFO;
            NetworkInfo networkInfo = intent.getParcelableExtra(extraKey);

            // Check if we're connected
            if (networkInfo.isConnected()) {
                wifiP2pManager.requestConnectionInfo(wifiDirectChannel,
                        new WifiP2pManager.ConnectionInfoListener() {
                            public void onConnectionInfoAvailable(WifiP2pInfo info) {
                                // If the connection is established
                                    if (info.groupFormed) {
                                        if (info.isGroupOwner) {
                                            Log.i("msg :",numbreClientConnecte+"");
                                            try {
                                                    socketserver = new ServerSocket(1030);
                                                    new CreerSocketServer(socketserver, InterfacePartage.this).initiateServerSocket();
                                            }catch (Exception e){
                                                Log.e("msg", Log.getStackTraceString(e));
                                            }
                                        }
                                    }

                                    }



                            //}
                        });
            }
            else
            {
                Log.d("msg", "Wi-Fi Direct Disconnected");
            }
        }
    };
    private void initializeWiFiDirect() {
        wifiP2pManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);

          wifiDirectChannel = wifiP2pManager.initialize(this, getMainLooper(),
                new WifiP2pManager.ChannelListener() {
                    public void onChannelDisconnected() {
                        initializeWiFiDirect();
                    }
                }
        );
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        try {
            // Si le timer continuer de compter, on le ferme
            if(myTimer !=null)
                myTimer.cancel();
            // On ferme toutes les connexions (les sockets) si le diffuseur quitte la page.
            if(socketserver!=null){
                socketserver.close();
                for(int i=0;i<numbreClientConnecte;i++){
                    if(socket[i]!=null)
                        socket[i].close();
                    if(oos[i]!=null)
                        oos[i].close();
                    if(in[i]!=null)
                        in[i].close();
                }
            }


        } catch (IOException e)
        {
            Log.e("msg", Log.getStackTraceString(e));

        }

        if(musicSrv!=null){
            musicSrv.stopSelf();
        }
    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Attention")
                .setMessage("Si vous quittez la page, vous allez perdre la connexion")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        musicSrv.finVotes();
                        finish();
                    }
                }).create().show();
    }

}

