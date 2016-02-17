package masterigis.com.jukebox2_0.ModeConnecte.FragmentsCo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/////attention aux imports
import m1geii.com.jukebox2_0.R;
import masterigis.com.jukebox2_0.ModeNonConnecte.Diffuseur.Serveur;
import masterigis.com.jukebox2_0.ModeConnecte.FragmentsCo.Fragment_contenu_liste_de_lecture;
import masterigis.com.jukebox2_0.Model.BDAdapter;


public class Fragment_Listes_de_Lecture extends Fragment {

    EditText Nomentre;
    FloatingActionButton btnFloatingAjout;
    Bundle donneesPlaylist=new Bundle();
    long idl;
    ListView liste_playlist;
    Context context;
    Fragment fragment,fragment2;
    FragmentTransaction ft;

    ArrayList<String> arrayPlaylists = new ArrayList<>();
    ArrayAdapter<String> adapter;
    BDAdapter base;


    public Fragment_Listes_de_Lecture() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View vueListesDeLecture=inflater.inflate(R.layout.fragment_listes_de_lecture, container, false);

        ///Création de la base de données permettant d'enregistrer toutes les listes créées
        base = new BDAdapter(getActivity());

        // Déclaration d'une fenêtre de dialog pour saisir le nom de la liste de lecture
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Nom de la Nouvelle Playlist");

        ///Variable permettant de récupérer la saisie de l'utilisateur
        Nomentre = new EditText(getActivity());
        btnFloatingAjout = (FloatingActionButton)vueListesDeLecture.findViewById(R.id.bouton_flottant_ajouter);
        liste_playlist = (ListView)vueListesDeLecture.findViewById(R.id.listview_listes_de_lecture);
        adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_selectable_list_item, arrayPlaylists);

        builder.setView(Nomentre);

        base.ouvrirBase();

        Cursor c = base.ObtenirToutesLesListes();
        arrayPlaylists.clear();
        while(c.moveToNext())
        {
            String name = c.getString(0);
            arrayPlaylists.add(name);
        }

        liste_playlist.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        // Gestion du clique long
        registerForContextMenu(liste_playlist);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String txt = Nomentre.getText().toString();
                Boolean wantToCloseDialog=(txt.trim()).isEmpty();

                if(!wantToCloseDialog) {

                    base.ouvrirBase();
                    long result = base.ajoutLdl(txt);

                    if (result > 0) {
                        Nomentre.setText("");
                        arrayPlaylists.add(txt);
                        adapter.notifyDataSetChanged();
                        base.fermer();
                        Toast.makeText(getContext(), txt + " ajoutée", Toast.LENGTH_SHORT).show();
                    }

                    else {
                        Toast.makeText(getContext(), "Ce nom existe déjà", Toast.LENGTH_SHORT).show();
                    }
                }

                else{
                    Toast.makeText(getContext(),"Veuillez entrer un nom", Toast.LENGTH_LONG).show();
                }
            }
        });



        builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                base.fermer();
            }
        });

        liste_playlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String noml = parent.getItemAtPosition(position).toString();
                donneesPlaylist.putString("identifiantPlaylist",noml);
                donneesPlaylist.putString("Flag","ajouter");    // Pour faire apparaître le bouton ajouter musique dans le prochain fragment
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

        final AlertDialog ad  = builder.create(); // Création de la fenêtre de dialogue

        btnFloatingAjout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.show(); // Affichage de celle ci
            }
        });

        return vueListesDeLecture;
    }


    // Menu contextuel lors d'un clique long sur une liste de lecture
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.contextmenuevenement, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId())
        {
            case R.id.SupprimerEvenement:
                String l = arrayPlaylists.get(info.position);
                base.SupprimerLdl(l);
                adapter.remove(l);
                adapter.notifyDataSetChanged(); // Rafraîchit les données de l'adapteur l'adapter
                return true;

            case R.id.PartageEvenement:
                WifiManager activerWifi=(WifiManager)getActivity().getSystemService(Context.WIFI_SERVICE);;
                String nomList =  arrayPlaylists.get(info.position);
                //donneesContenuPlaylist.putString("identifiantPlaylist", nomList);
                Intent i=new Intent (getActivity(),Serveur.class);
                i.putExtra("nomListe", nomList);
                activerWifi.setWifiEnabled(true);
                startActivity(i);
        }

        return super.onContextItemSelected(item);
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
