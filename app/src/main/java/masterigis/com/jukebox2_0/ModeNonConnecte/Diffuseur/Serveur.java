package masterigis.com.jukebox2_0.ModeNonConnecte.Diffuseur;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import m1geii.com.jukebox2_0.R;

public class Serveur extends AppCompatActivity {
    private WifiP2pManager wifiP2pManager;
    private WifiP2pManager.Channel wifiDirectChannel;
    public static final String TAG ="WiFiDirectActivity";
    IntentFilter peerfilter;
    IntentFilter connectionfilter;
    IntentFilter p2pEnabled;
    Button bNom,bPartager;
    TextView nom;
    String nomAppareil;
    String InfoPasse ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.d_activity_serveur);

        setTitle("Serveur");
        Bundle objetbunble  = this.getIntent().getExtras();
        // récupération de la valeur
        InfoPasse= objetbunble.getString("nomListe");
        // on afffiche l'information dans l'edittext
        peerfilter = new IntentFilter(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION); // Indique si l'Ã©tat de la liste des paires disponible a changÃ©
        connectionfilter = new IntentFilter(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION); // Indique si  l'Ã©tat de la connexion Wi-Fi peer to peer a changÃ©
        p2pEnabled = new IntentFilter(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION); // Indique si le Wi-Fi P2P est Ã©tabli (activÃ©)

        initializeWiFiDirect();
        nomAppareil = Build.MANUFACTURER +"_"+ Build.MODEL;
        // Attribuer le nouveau nom

        bNom = (Button)findViewById(R.id.bouton_changer_nom);
        bNom.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String nomWifi;
         /* Alert Dialog Code Start*/
                AlertDialog.Builder alert = new AlertDialog.Builder(Serveur.this);
                alert.setTitle("Changer de nom"); //Set Alert dialog title here
                alert.setMessage("Entrer votre nom ici"); //Message here

                // Set an EditText view to get user input
                final EditText input = new EditText(Serveur.this);
                alert.setView(input);

                alert.setPositiveButton("Valider", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //You will get as string input data in this variable.
                        // here we convert the input to a string and show in a toast.
                        String nomWifi = input.getEditableText().toString();
                        if (nomWifi.compareTo("") != 0)
                            setDeviceName(nomWifi);
                        else {
                            new AlertDialog.Builder(Serveur.this)
                                    .setTitle("Attention")
                                    .setMessage("Vous ne pouvez pas entrer un nom vide, sinon cliquez sur partager")
                                    .setPositiveButton(android.R.string.yes, null)
                                    .create().show();
                        }

                    } // End of onClick(DialogInterface dialog, int whichButton)
                }); //End of alert.setPositiveButton
                alert.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                        dialog.cancel();
                    }
                }); //End of alert.setNegativeButton
                AlertDialog alertDialog = alert.create();
                alertDialog.show();
            }
        });

        discoverPeers();
        bPartager =  (Button)findViewById(R.id.bouton_partager_liste);
        bPartager.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i=new Intent(Serveur.this,InterfacePartage.class);
                i.putExtra("nomListe", InfoPasse);
                 startActivity(i);
            }
        });
    }

    private void initializeWiFiDirect() {
        wifiP2pManager = (WifiP2pManager)getSystemService(Context.WIFI_P2P_SERVICE);
        wifiDirectChannel = wifiP2pManager.initialize(this, getMainLooper(),
                new WifiP2pManager.ChannelListener() {
                    public void onChannelDisconnected() {
                        initializeWiFiDirect();
                    }
                }
        );
    }

    private void discoverPeers() {

        wifiP2pManager.discoverPeers(wifiDirectChannel, actionListener);
        //wifiP2pManager.WIFI_P2P_STATE_DISABLED
        Toast.makeText(getApplicationContext(), "Wifi direct activé", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(getApplicationContext(),"Wifi direct non supporté.",Toast.LENGTH_LONG).show();
                    break;
                default:
                    errorMessage += "Erreur inconnue."; break;

            }
            Log.d(TAG, errorMessage);
        }

        public void onSuccess() {
        }
    };

    // Pour supprimer les groupes enregistrÃ©s afin d'Ã©viter les mÃ©morisation des rÃ´les de chacun
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

    // Pour renommer l'appareil
    public String setDeviceName(String devName) {
        String oldDeviceName="";
        try {
            Class[] paramTypes = new Class[3];
            paramTypes[0] = WifiP2pManager.Channel.class;
            paramTypes[1] = String.class;
            paramTypes[2] = WifiP2pManager.ActionListener.class;
            Method setDeviceName = wifiP2pManager.getClass().getMethod("setDeviceName", paramTypes);
            setDeviceName.setAccessible(true);

            Object arglist[] = new Object[3];
            arglist[0] =  wifiDirectChannel;
            oldDeviceName=paramTypes[1].toString();
            arglist[1] = devName;
            arglist[2] = new WifiP2pManager.ActionListener() {

                @Override
                public void onSuccess() {
                    Log.i("infos", "Changement du nom");                        }

                @Override
                public void onFailure(int reason) {
                    Log.i("infos","Changement du nom");
                }
            };

            setDeviceName.invoke(wifiP2pManager, arglist);

        } catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return oldDeviceName;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        setDeviceName(nomAppareil);
        wifiP2pManager.removeGroup(wifiDirectChannel, actionListener);
        deletePersistentGroups();
    }
}

