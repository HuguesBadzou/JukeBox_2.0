package masterigis.com.jukebox2_0;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import m1geii.com.jukebox2_0.R;
import masterigis.com.jukebox2_0.ModeConnecte.FragmentsCo.Fragment_Ma_Musique_Diffuseur_Co;
import masterigis.com.jukebox2_0.ModeNonConnecte.Fragments.Fragment_Music_Playback;
import masterigis.com.jukebox2_0.ModeNonConnecte.Fragments.Fragment_Partager_Liste_De_Lecture;

/**
 * Created by naveck on 17/02/2016.
 */
public class Mode_DiffuseurCo extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode_diffuseurco);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Fragment fragmentMaMusique,fragmentControlLecteur;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        fragmentMaMusique = new Fragment_Ma_Musique_Diffuseur_Co();
        fragmentControlLecteur=new Fragment_Music_Playback();
        ft.replace(R.id.mainFrame, fragmentMaMusique)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

        ft.replace(R.id.playbackContentControl,fragmentControlLecteur)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Identification de chaque clique sur les option du drawer
        int id = item.getItemId();

        // Déclaration du fragment de l'interface
        Fragment fragment,currentFragment;
        currentFragment=getSupportFragmentManager().findFragmentById(R.id.mainFrame);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        switch(id) {

            case R.id.accueil_mode_diffuseur:
                finish();
                break;

            case R.id.bibliotheque_mode_diffuseur:
                if(currentFragment instanceof Fragment_Ma_Musique_Diffuseur_Co){
                    // On ne le recharge pas
                }

                else{
                    fragment = new Fragment_Ma_Musique_Diffuseur_Co();
                    ft.replace(R.id.mainFrame, fragment)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .addToBackStack(null)
                            .commit();
                }
                break;

            case R.id.mes_evenements:
                if(currentFragment instanceof Fragment_Partager_Liste_De_Lecture){
                    // On ne le recharge pas le fragment
                }
                else {
                    fragment = new Fragment_Partager_Liste_De_Lecture();
                    ft.replace(R.id.mainFrame, fragment)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .addToBackStack(null)
                            .commit();
                }
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
