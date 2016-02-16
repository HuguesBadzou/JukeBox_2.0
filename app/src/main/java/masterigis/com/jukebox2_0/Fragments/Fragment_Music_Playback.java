package masterigis.com.jukebox2_0.Fragments;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import m1geii.com.jukebox2_0.R;
import masterigis.com.jukebox2_0.MusicService;


public class Fragment_Music_Playback extends Fragment {

    private MusicService musicSrv;
    private Intent playIntent;
    private boolean musicBound=false;
    ImageButton boutonPrecedent,boutonAvancer,boutonPlayPause;
    TextView titreMusique,artisteMusique;
    String idMusique;
    Drawable picPaused;
    BroadcastReceiver playReceiver;  // Ecouteur d'événement servant à détecter le lancement de la musique

    public Fragment_Music_Playback() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment_music_playback, container, false);
        // Assignation des bouton
        titreMusique=(TextView)v.findViewById(R.id.titre_chanson);
        artisteMusique=(TextView)v.findViewById(R.id.artist);
        boutonPrecedent= (ImageButton) v.findViewById(R.id.previous_button);
        boutonAvancer= (ImageButton) v.findViewById(R.id.next_button);
        boutonPlayPause= (ImageButton) v.findViewById(R.id.play_pause);
        picPaused=ContextCompat.getDrawable(getContext(),R.drawable.paused_white_64);
        final Drawable picPlay=ContextCompat.getDrawable(getContext(),R.drawable.play_white_64);

        // Ecouteur d'événement
        playReceiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //String receiveMessage=intent.getStringExtra("replyMessage");
                boutonPlayPause.setBackground(picPaused);
                titreMusique.setText(intent.getStringExtra("titre"));
                artisteMusique.setText(intent.getStringExtra("artiste"));
                idMusique=intent.getStringExtra("id");
                //Toast.makeText(getContext(),intent.getStringExtra("titre")+"-"+idMusique,Toast.LENGTH_SHORT).show();
            }
        };

        IntentFilter intentFilter=new IntentFilter("m1geii.com.jukebox20beta");
        getActivity().registerReceiver((playReceiver), intentFilter);

        // Lorsque l'on clique sur la zone du bas (commande lecteur) lance une full Screen activity
        /*v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(getActivity(), ActivityAlbum.class);
                startActivity(a);
            }
        });*/

        // Ecouteurs commande lecteur
        boutonPrecedent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicSrv.playPrev();
                titreMusique.setText(musicSrv.getCurrentSong().getTitle());   // Je mets à jour les informations de la musique actuellement joué
                artisteMusique.setText(musicSrv.getCurrentSong().getArtist());
            }
        });

        boutonPlayPause.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                if(musicSrv.flag()) {
                    if (!musicSrv.ifPlaying()) {
                        boutonPlayPause.setBackground(picPaused);
                        musicSrv.go();
                    } else {
                        boutonPlayPause.setBackground(picPlay);
                        musicSrv.pausePlayer();
                    }
                }
            }
        });

        boutonAvancer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicSrv.playNext();
                titreMusique.setText(musicSrv.getCurrentSong().getTitle());   // Je mets à jour les informations de la musique actuellement joué
                artisteMusique.setText(musicSrv.getCurrentSong().getArtist());
            }
        });
        return v;
    }

    ///////////////////////////////////////////////////////////////////////////////

    @Override
    public void onResume(){
        super.onResume();
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
    public void onStop(){
        super.onStop();
    }

    //Connexion au service
    public ServiceConnection musicConnection = new ServiceConnection(){
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder)service;
            //get service
            musicSrv = binder.getService();
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicSrv=null;
            musicBound = false;
        }
    };

    ///////////////////////////////////////////////////////////////////////////////
    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void onDestroy(){
        super.onDestroy();
    }
}
