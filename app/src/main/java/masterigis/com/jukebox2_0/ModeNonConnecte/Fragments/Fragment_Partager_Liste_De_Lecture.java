package masterigis.com.jukebox2_0.ModeNonConnecte.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import m1geii.com.jukebox2_0.R;
import masterigis.com.jukebox2_0.Model.BDAdapter;

public class Fragment_Partager_Liste_De_Lecture extends Fragment {

    FloatingActionButton btnFloatingAjout;
    Bundle donneesPlaylist=new Bundle();
    long idl;
    ListView liste_playlist_a_ajouter;
    Context context;
    Fragment fragment;
    FragmentTransaction ft;

    ArrayList<String> arrayPlaylists = new ArrayList<>();
    ArrayAdapter<String> adapter;
    BDAdapter base;

    public Fragment_Partager_Liste_De_Lecture() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vuePartagerListesDeLecture=inflater.inflate(R.layout.fragment_partager_liste_de_lecture, container, false);

        // Changement du titre de l'action Bar lors du changement de Fragment
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("Partager Liste de Lecture");

        ///Création de la base de données permettant d'enregistrer toutes les listes créées
        base = new BDAdapter(getActivity());

        // Déclaration d'une fenêtre de dialog pour saisir le nom de la liste de lecture
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Nom de la Nouvelle Playlist");

        ///Variable permettant de récupérer la saisie de l'utilisateur
        btnFloatingAjout = (FloatingActionButton)vuePartagerListesDeLecture.findViewById(R.id.bouton_flottant_partager_liste_de_lecture);
        liste_playlist_a_ajouter = (ListView)vuePartagerListesDeLecture.findViewById(R.id.listview_listes_de_lecture_a_partager);
        adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_selectable_list_item, arrayPlaylists);

        base.ouvrirBase();

        Cursor c = base.ObtenirToutesLesListes();
        arrayPlaylists.clear();
        while(c.moveToNext())
        {
            String name = c.getString(0);
            arrayPlaylists.add(name);
        }

        liste_playlist_a_ajouter.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        liste_playlist_a_ajouter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String noml = parent.getItemAtPosition(position).toString();
                donneesPlaylist.putString("identifiantPlaylist", noml);
                donneesPlaylist.putString("Flag","partager"); // Pour faire afficher le bouton partager à la prochaine page
                ft = getActivity().getSupportFragmentManager().beginTransaction();

                fragment = new Fragment_contenu_liste_de_lecture();
                fragment.setArguments(donneesPlaylist); // Envoie des infos de la musique au fragment
                ft.replace(R.id.mainFrame, fragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .addToBackStack(null)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                        .commit();
            }
        });

        return vuePartagerListesDeLecture;
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }
}
