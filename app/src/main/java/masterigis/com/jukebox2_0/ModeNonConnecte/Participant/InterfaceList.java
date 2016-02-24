package masterigis.com.jukebox2_0.ModeNonConnecte.Participant;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import m1geii.com.jukebox2_0.R;


public class InterfaceList extends AppCompatActivity {



    ArrayList image_details = null;
    NewItem newsData;
    FloatingActionButton btnFlottantPartager;
    ListView lv1;
    IntentFilter connectionfilter;
    private WifiP2pManager wifiP2pManager;
    private WifiP2pManager.Channel wifiDirectChannel;

    // Variable pour l'envoie de Socket
    DataOutputStream  oos = null;
    //inisalisation client recepteur
    Socket socketclient=null;
    DataInputStream in = null;
    Boolean connexionTest=true;
    int vote;
    View row;
    AlertDialog alertDialog;
    ListView listAlert ;
    public Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p_activity_interface_list);
        setTitle("Client");
        image_details = getListData();

        connectionfilter = new IntentFilter(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION); // Indique si  l'état de la connexion Wi-Fi peer to peer a changÃƒÂ©

        initializeWiFiDirect();
        Log.i("msg ", " ProgressDialogConnexion");
        lv1 = (ListView) findViewById(R.id.liste_musiques_votes);

        //modification syteme du vote
        final CharSequence[] items = {
                "1 er ", "2 ème ",
                "3 ème ", "4 ème ",
                "5 ème ","remettre à 0"
        };
        AlertDialog.Builder alerDialogBuilder = new AlertDialog.Builder(InterfaceList.this);
        boolean enable = true;
        alerDialogBuilder.setTitle("Classer par préférence")
                .setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        vote = item ;
                        listAlert = ((AlertDialog) dialog).getListView();


                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton("Valider", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        String voteList =  newsData.getVote();
                       if(!listAlert.getChildAt(vote).isEnabled()){
                           new AlertDialog.Builder(InterfaceList.this)
                                   .setTitle("Attention")
                                   .setMessage("vous avez déjà utilisé ce choix")
                                   .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                       public void onClick(DialogInterface arg0, int arg1) {
                                       }
                                   }).create().show();

                       }else {
                           if (voteList.compareTo("Vote : 0") == 0) {
                               if(vote !=5) {
                                   listAlert.getChildAt(vote).setEnabled(false);
                                   listAlert.getChildAt(vote).setSelected(false);
                                   newsData.setVote("Vote : " + items[vote]);
                                   row.setBackgroundColor(Color.GREEN);
                                   lv1.invalidateViews();
                               }
                           } else {
                               int index = 5;

                               if (voteList.compareTo("Vote : "+items[0]) == 0) {
                                   index =0 ;
                               } else if (voteList.compareTo("Vote : "+items[1]) == 0) {
                                   index = 1;
                               } else if (voteList.compareTo("Vote : "+items[2]) == 0) {
                                   index = 2;
                               } else if (voteList.compareTo("Vote : "+items[3]) == 0) {
                                   index = 3;
                               } else {
                                   index = 4;
                               }
                               if (vote == 5) {
                                   newsData.setVote("Vote : " + 0 + "");
                                   listAlert.getChildAt(vote).setEnabled(true);
                                   listAlert.getChildAt(vote).setSelected(true);
                               } else {
                                   listAlert.getChildAt(vote).setEnabled(false);
                                   listAlert.getChildAt(vote).setSelected(false);
                                   newsData.setVote("Vote : " + items[vote] + "");

                               }
                               listAlert.getChildAt(index).setEnabled(true);
                               listAlert.getChildAt(index).setSelected(true);
                               row.setBackgroundColor(Color.GREEN);
                               // row.setClickable(true);
                               lv1.invalidateViews();
                           }
                       }
                    }
                });
        alertDialog = alerDialogBuilder.create();

        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Object o = lv1.getItemAtPosition(position);
                newsData = (NewItem) o;
                row = v;
                if (newsData.getVote().endsWith("*") == false) {
                    alertDialog.show();

                   // alertDialog.getListView().getChildAt(1).setEnabled(false);
                   // alertDialog.getListView().getChildAt(1).setSelected(false);
                   /* new AlertDialog.Builder(InterfaceList.this)
                            .setTitle(newsData.getTitre())
                            .setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int item) {
                                    vote = item;
                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .setPositiveButton("Voter", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    if (vote != 0) {
                                        newsData.setVote("Vote : " + vote + "");
                                        row.setBackgroundColor(Color.GREEN);
                                        // row.setClickable(true);
                                        lv1.invalidateViews();
                                    }

                                }

                            }).create().show();*/
                }
            }
        });


                    btnFlottantPartager=(FloatingActionButton)

                    findViewById(R.id.bouton_flottant_voter);

                    btnFlottantPartager.setOnClickListener(new View.OnClickListener()

                    {
                        public void onClick (View v){
                        String msg1 = new ConvertirListToString().convertirToString(InterfaceList.this);
                        Log.i("msg aaaa", msg1);
                        if (image_details != null && msg1 != "") {
                            new EnvoyerMsg(oos, msg1).start();
                        }

                    }
                    }

                    );
                }

    private ArrayList getListData() {
        ArrayList<NewItem> results = new ArrayList<NewItem>();

        return results;
    }

    BroadcastReceiver connectionChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            // Extract the NetworkInfo
            if(connexionTest) {
                String extraKey = WifiP2pManager.EXTRA_NETWORK_INFO;
                NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra(extraKey);

                // Check if we're connected
                if (networkInfo.isConnected()) {
                    wifiP2pManager.requestConnectionInfo(wifiDirectChannel,
                            new WifiP2pManager.ConnectionInfoListener() {
                                public void onConnectionInfoAvailable(WifiP2pInfo info) {
                                    // If the connection is established
                                    // If we're the client
                                    if (info.groupFormed) {
                                        Log.i("msg", info.groupOwnerAddress.getHostAddress());
                                        // TODO Initiate client socket.
                                        ProgressDialogConnexion task = new ProgressDialogConnexion(InterfaceList.this);
                                        task.execute();
                                        new CreerSocketClient().initiateServerSocket(info.groupOwnerAddress.getHostAddress().toString(), InterfaceList.this);

                                    }
                                }

                            });
                } else {
                    Log.d("msg", "Wi-Fi Direct Disconnected");
                }
                connexionTest=false;
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
    protected void onResume() {
        super.onResume();
        registerReceiver(connectionChangedReceiver, connectionfilter);


    }
    protected  void onDestroy(){
        super.onDestroy();
        if(socketclient != null){
            try {
                socketclient.close();

            }catch (Exception e){}
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
                        //  Intent i=new Intent(InterfacePartage.this,Serveur.class);
                        //  startActivity(i);
                        finish();
                    }
                }).create().show();
    }

}

