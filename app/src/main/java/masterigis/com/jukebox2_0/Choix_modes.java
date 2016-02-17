package masterigis.com.jukebox2_0;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import m1geii.com.jukebox2_0.R;
import masterigis.com.jukebox2_0.ModeNonConnecte.Participant.Rejoindre_Playlist;

public class Choix_modes extends AppCompatActivity {

    final private int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    private Boolean permission=true;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choix_modes);



        Button boutonDiffuseur=(Button)findViewById(R.id.boutonDiffuseur);
        Button boutonParticipant=(Button)findViewById(R.id.boutonParticipant);
        final WifiManager wifi=(WifiManager)this.getSystemService(Context.WIFI_SERVICE);
        final Bundle choixModeRecup=getIntent().getExtras();

        /////////// Vérification des permissions DEBUT ///////////

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }


        /////////// Vérification des permissions FIN ///////////

        boutonDiffuseur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (choixModeRecup.get("mode").equals("modeNonConnecte")) {
                    if (permission) {
                        Intent a = new Intent(Choix_modes.this, Mode_Diffuseur.class);
                        startActivity(a);
                    } else {
                        new AlertDialog.Builder(Choix_modes.this)
                                .setMessage("Vous devez autoriser la permission d'accès aux données du téléphone")
                                .setPositiveButton("OK", null)
                                .create()
                                .show();
                    }
                }

                else{
                    // Faire la redirection vers une autre activité
                }
            }
        });

        boutonParticipant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(choixModeRecup.get("mode").equals("modeNonConnecte")) {
                    Intent b = new Intent(Choix_modes.this, Rejoindre_Playlist.class);
                    wifi.setWifiEnabled(true);
                    startActivity(b);
                }

                else{
                    // Faire la direction vers une autre activité
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (!(grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Permission Denied
                    Toast.makeText(Choix_modes.this, "Permission refusée", Toast.LENGTH_SHORT)
                            .show();
                    permission=false;
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
