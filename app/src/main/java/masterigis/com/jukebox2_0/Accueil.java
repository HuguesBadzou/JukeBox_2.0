package masterigis.com.jukebox2_0;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import m1geii.com.jukebox2_0.R;
import masterigis.com.jukebox2_0.ModeConnecte.GoogleSignIn;

public class Accueil extends AppCompatActivity {

    Bundle mode=new Bundle();
    SharedPreferences infos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);

        Button boutonModeConnecte=(Button)findViewById(R.id.bouton_mode_connecte);
        Button boutonModeNonConnecte=(Button)findViewById(R.id.bouton_mode_non_connecte);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/HelveticaNeue.ttf");
        boutonModeConnecte.setTypeface(typeface);
        boutonModeNonConnecte.setTypeface(typeface);

        // Partie shared preferences
        infos=this.getSharedPreferences(".donneesUtilisateur",MODE_PRIVATE);
        final String siConnecte = infos.getString("etat","");

        boutonModeNonConnecte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mode.putString("mode","modeNonConnecte");
                Intent a=new Intent(Accueil.this,Choix_roles.class);
                a.putExtras(mode);
                startActivity(a);
            }
        });

        boutonModeConnecte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mode.putString("mode", "modeConnecte");
                if(siConnecte.equals("dejaConnecte")) {
                    Intent b = new Intent(Accueil.this, Choix_roles.class);
                    b.putExtras(mode);
                    startActivity(b);
                }

                else {
                    Intent b = new Intent(Accueil.this, GoogleSignIn.class);
                    b.putExtras(mode);
                    startActivity(b);
                }
                //Toast.makeText(getApplicationContext(),"prochainement...",Toast.LENGTH_LONG).show();
            }
        });
    }
}
