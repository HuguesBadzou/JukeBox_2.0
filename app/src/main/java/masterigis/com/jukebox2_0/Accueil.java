package masterigis.com.jukebox2_0;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import m1geii.com.jukebox2_0.R;

public class Accueil extends AppCompatActivity {

    // Test Mahafous coucou
    Bundle mode=new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);

        Button boutonModeConnecte=(Button)findViewById(R.id.bouton_mode_connecte);
        Button boutonModeNonConnecte=(Button)findViewById(R.id.bouton_mode_non_connecte);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/HelveticaNeue.ttf");
        boutonModeConnecte.setTypeface(typeface);
        boutonModeNonConnecte.setTypeface(typeface);

        boutonModeNonConnecte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mode.putString("mode","modeNonConnecte");
                Intent a=new Intent(Accueil.this,Choix_modes.class);
                a.putExtras(mode);
                startActivity(a);
            }
        });

        boutonModeConnecte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mode.putString("mode","modeConnecte");
                Intent b=new Intent(Accueil.this,Choix_modes.class);
                b.putExtras(mode);
                startActivity(b);
            }
        });
    }
}
