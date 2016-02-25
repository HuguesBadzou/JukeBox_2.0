package masterigis.com.jukebox2_0.ModeConnecte.ParticipantCo;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.ChannelListener;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import m1geii.com.jukebox2_0.R;

/**
 * Created by hamila on 16/01/2016.
 * initialisation frame work Wi-Fi direct
 */
public class Rejoindre_Playlist extends AppCompatActivity {
    private static final String TAG = "WiFiDirectActivity";
    ListView listView;
    private FloatingActionButton buttonDiscover;
    private DeviceAdapter adapteurDevice;
    IntentFilter peerfilter;
    IntentFilter connectionfilter;
    IntentFilter p2pEnabled;
    //on utilise la variable test pour connectionChangedReceiver
    int test=0;
    Boolean isconnectionWifiDirect=false;
    //inisalisation client recepteur

    /**
     * Listing 16-18: Initializing Wi-Fi Direct
     */
    private WifiP2pManager wifiP2pManager;
    private Channel wifiDirectChannel;
    private List<Device> deviceList = new ArrayList<>(); // Liste des appareils trouvés

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p_rejoindre_playlist);
       // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);
        setTitle("Client");
        listView = (ListView)findViewById(R.id.listview_devices);
        adapteurDevice = new DeviceAdapter(this,deviceList);
        listView.setAdapter(adapteurDevice);

        initializeWiFiDirect(); // Initialise le framework wi-fi direct sur chaque appareil

        peerfilter = new IntentFilter(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        // Indique si l'état de la liste des paires disponible a changé
        connectionfilter = new IntentFilter(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        // Indique si  l'état de la connexion Wi-Fi peer to peer a changé
        p2pEnabled = new IntentFilter(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        // Indique si le Wi-Fi P2P est établi (activé)

        final WifiManager wifiActive=(WifiManager)this.getSystemService(Context.WIFI_SERVICE);

        Button buttonRechercherDiffuseur = (Button)findViewById(R.id.bouton_rechercher_diffuseur);
        buttonRechercherDiffuseur.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if(wifiActive!=null){
                    if(wifiActive.isWifiEnabled()){
                        discoverPeers();
                        ProgressDialogRecherche task = new ProgressDialogRecherche(Rejoindre_Playlist.this);
                        task.execute();
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Activez votre Wi-Fi ou Wi-Fi Direct",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        Button voirList = (Button)findViewById(R.id.voir_liste);
        voirList.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if(isconnectionWifiDirect){
                    Intent i = new Intent(Rejoindre_Playlist.this, InterfaceList.class);
                    startActivity(i);
                }
                else{
                    new AlertDialog.Builder(Rejoindre_Playlist.this)
                            .setMessage("connectez vous d'abord au diffuseur")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface arg0, int arg1) {
                                }
                            }).create().show();
                }

            }


        });

        // Gestion du clique sur un diffuseur
        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int index, long arg3) {
                test = 1;
                connectTo(deviceList.get(index));
                new connexionSocketClient().execute();
            }
        });

    }

    /**
     * Listing 16-21: Receiving a Wi-Fi Direct status change
     */
    // Broadcast Receiver détectant l'état du Wi-Fi si activé on peut appuyer sur le bouton sinon on ne peut pas
    BroadcastReceiver p2pStatusReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            switch (state) {
                case (WifiP2pManager.WIFI_P2P_STATE_ENABLED):
                    Toast.makeText(getApplicationContext(), "Wi-Fi direct activé", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Log.i("Message: ","Wi-Fi non activé");
            }
        }
    };

    /**
     * Listing 16-22: Discovering Wi-Fi Direct peers
     */

    // Méthode de découverte des pairs en wi-fi direct
    private void discoverPeers() {

        wifiP2pManager.discoverPeers(wifiDirectChannel, actionListener);
    }

    BroadcastReceiver peerDiscoveryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            wifiP2pManager.requestPeers(wifiDirectChannel,
                    new PeerListListener() {
                        public void onPeersAvailable(WifiP2pDeviceList peers) {
                            deviceList.clear();
                            for (WifiP2pDevice device : peers.getDeviceList())
                            {
                                Device appareilRecup=new Device();
                                appareilRecup.setdeviceName(device.deviceName);
                                appareilRecup.setDevice(device);
                                deviceList.add(appareilRecup);
                            }
                            adapteurDevice.notifyDataSetChanged();
                        }
                    });
        }
    };


    /**
     * Listing 16-23: Requesting a connection to a Wi-Fi Direct peer
     */

    // Fonction de connexion en wi-fi direct au smartphone
    private void connectTo(Device device) {
        WifiP2pDevice c=device.getDevice();
        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = c.deviceAddress;
        config.groupOwnerIntent=0;     // Pour forcer le smartphone à être le client
        wifiP2pManager.connect(wifiDirectChannel, config, actionListener);
        config.groupOwnerIntent=14;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(peerDiscoveryReceiver, peerfilter);
        registerReceiver(p2pStatusReceiver, p2pEnabled);
        test=0;
        isconnectionWifiDirect=false;

    }
    /*@Override
    public void onBackPressed() {
        wifiP2pManager.removeGroup(wifiDirectChannel, actionListener);
        new AlertDialog.Builder(this)
                .setTitle("Attention")
                .setMessage("Si vous quittez la page, vous allez perdre la connexion")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {


                    public void onClick(DialogInterface arg0, int arg1) {
                        finish();
                    }
                }).create().show();
    }*/

    protected void onDestroy() {
        super.onDestroy();
        wifiP2pManager.removeGroup(wifiDirectChannel, actionListener);
        deletePersistentGroups();
    }



    private void initializeWiFiDirect() {
        wifiP2pManager =
                (WifiP2pManager)getSystemService(Context.WIFI_P2P_SERVICE);

        wifiDirectChannel = wifiP2pManager.initialize(this, getMainLooper(),
                new ChannelListener() {
                    public void onChannelDisconnected() {
                        initializeWiFiDirect();
                    }
                }
        );
    }

    /**
     * Listing 16-19: Creating a WiFi P2P Manager Action Listener
     */
    private ActionListener actionListener = new ActionListener() {
        public void onFailure(int reason) {
            String errorMessage ="WiFi Direct Failed: ";
            switch (reason) {
                case WifiP2pManager.BUSY :
                    errorMessage += "Framework busy."; break;
                case WifiP2pManager.ERROR :
                    errorMessage += "Internal error."; break;
                case WifiP2pManager.P2P_UNSUPPORTED :
                    errorMessage += "Unsupported."; break;
                default:
                    errorMessage += "Unknown error."; break;

            }
            Log.d(TAG, errorMessage);
        }

        public void onSuccess() { }
    };
    private void deletePersistentGroups(){
        try {
            Method[] methods = WifiP2pManager.class.getMethods();
            for (int i = 0; i < methods.length; i++) {
                if (methods[i].getName().equals("deletePersistentGroup")) {
                    // Delete any persistent group
                    for (int netid = 0; netid < 32; netid++) {
                        methods[i].invoke(wifiP2pManager, wifiDirectChannel, netid, null);
                    }
                }
            }
        } catch(Exception e) {
            Log.e("msg", Log.getStackTraceString(e));
        }
    }
    BroadcastReceiver connectionChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(test ==1 ) {

                // Extract the NetworkInfo
                String extraKey = WifiP2pManager.EXTRA_NETWORK_INFO;
                NetworkInfo networkInfo = intent.getParcelableExtra(extraKey);

                // Check if we're connected
                if (networkInfo.isConnected()) {
                    isconnectionWifiDirect = true;
                    Toast.makeText(getApplicationContext(), "vous etes connecté  ", Toast.LENGTH_LONG).show();
                }
                else {
                    isconnectionWifiDirect=false;
                    wifiP2pManager.removeGroup(wifiDirectChannel, actionListener);
                    Toast.makeText(getApplicationContext(), "vous etes non connecté  ", Toast.LENGTH_LONG).show();

                }
                test=0;


            }
        }
    };

    private class connexionSocketClient extends AsyncTask<String, String,String> {
        ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Rejoindre_Playlist.this);
            dialog.setMessage("Connexion en cours..");
            dialog.show();
        }

        // Traitement
        @Override
        protected String doInBackground(String... params) {
            try{
                Thread.sleep(10000);
            }catch(Exception e){}

            return null ;

        }

        protected void onPostExecute(String file_url) {
            dialog.dismiss();
            registerReceiver(connectionChangedReceiver, connectionfilter);
        }
    }
}
