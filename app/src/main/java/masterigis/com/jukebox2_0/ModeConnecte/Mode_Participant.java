package masterigis.com.jukebox2_0.ModeConnecte;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;


import m1geii.com.jukebox2_0.R;
import masterigis.com.jukebox2_0.ModeConnecte.FragmentsCo.Fragment_Ma_Musique_Participant;
import masterigis.com.jukebox2_0.ModeConnecte.FragmentsCo.Fragment_Rechercher_Evement;
import masterigis.com.jukebox2_0.ModeNonConnecte.Fragments.Fragment_Music_Playback;
import masterigis.com.jukebox2_0.ModeNonConnecte.Participant.Rejoindre_Playlist;

public class Mode_Participant extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode_participant);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_mode_participant);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Fragment fragmentBibliotheque,fragmentControlLecteur;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        fragmentBibliotheque = new Fragment_Ma_Musique_Participant();
        fragmentControlLecteur=new Fragment_Music_Playback();

        // Ajout du Fragment contenant  les
        ft.replace(R.id.mainFrame, fragmentBibliotheque)
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mode_participant_menu, menu);
        return true;
    }

   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.mon_compte_participant) {
            return true;
        }

        else if (id == R.id.reglages_participant) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
*/
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
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
                if(currentFragment instanceof Fragment_Ma_Musique_Participant){
                    // On ne le recharge pas
                }
                else {
                    fragment = new Fragment_Ma_Musique_Participant();
                    ft.replace(R.id.mainFrame, fragment)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .addToBackStack(null)
                            .commit();
                }
                break;

            case R.id.rejoindre_playlist:
                Intent i=new Intent (Mode_Participant.this,Rejoindre_Playlist.class);
                startActivity(i);
                break;

            case R.id.rechercher_evenement:
                fragment = new Fragment_Rechercher_Evement();
                ft.replace(R.id.mainFrame, fragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .addToBackStack(null)
                        .commit();
                break;

        }

        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
