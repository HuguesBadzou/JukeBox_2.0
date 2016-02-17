package masterigis.com.jukebox2_0.ModeNonConnecte.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
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

import m1geii.com.jukebox2_0.R;
import masterigis.com.jukebox2_0.ModeNonConnecte.Diffuseur.Serveur;
import masterigis.com.jukebox2_0.Model.BDAdapter;


public class Fragment_Ajout_Musiques extends Fragment {

    EditText Nomentre;
    String nomListeDeLecture;
    FloatingActionButton btnFloatingAjout;
    Bundle donneesPlaylist=new Bundle();
    long idl;
    ListView liste_playlist;
    Context context;
    Fragment fragment;
    FragmentTransaction ft;

    ArrayList<String> arrayPlaylists = new ArrayList<>();
    ArrayAdapter<String> adapter;
    BDAdapter base;
    Boolean ajout=false;

    // Données musique ajoutée
    String titreMusiqueAjoutee,artisteMusiqueAjoutee;
    long idMusiqueAjoutee;

    public Fragment_Ajout_Musiques() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        titreMusiqueAjoutee=getArguments().getString("titreMusiqueAjout");
        artisteMusiqueAjoutee=getArguments().getString("artisteMusiqueAjout");
        idMusiqueAjoutee=getArguments().getLong("idMusiqueAjout");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View vueListeMusiques=inflater.inflate(R.layout.fragment_listes_de_lecture_bis, container, false);

        ///Création de la base de données permettant d'enregistrer toutes les listes créées
        base = new BDAdapter(getActivity());

        // Déclaration d'une fenêtre de dialog pour saisir le nom de la liste de lecture
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Nom de la Nouvelle Playlist");

        ///Variable permettant de récupérer la saisie de l'utilisateur
        Nomentre = new EditText(getActivity());
        btnFloatingAjout = (FloatingActionButton)vueListeMusiques.findViewById(R.id.bouton_flottant_ajouter_bis);
        liste_playlist = (ListView)vueListeMusiques.findViewById(R.id.listview_listes_de_lecture_bis);
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

        base.fermer();

        liste_playlist.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        registerForContextMenu(liste_playlist);

        // Ecouteur lors du clic sur un élément d'un ListView
        liste_playlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(!ajout) {
                    nomListeDeLecture = parent.getItemAtPosition(position).toString();
                    if (titreMusiqueAjoutee != null && artisteMusiqueAjoutee!= null) {
                        base.ouvrirBase();
                        long result1 = base.ajoutLdm(titreMusiqueAjoutee,artisteMusiqueAjoutee,nomListeDeLecture,String.valueOf(idMusiqueAjoutee));
                        if (result1 > 0) {
                            Nomentre.setText("");
                        } else {
                            Toast.makeText(getContext(), "Echec", Toast.LENGTH_SHORT).show();
                        }
                    }

                    Toast.makeText(getContext(), titreMusiqueAjoutee + " - " + artisteMusiqueAjoutee + " ajouté à la liste " + nomListeDeLecture, Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();
                    base.fermer();
                    ajout = true;
                }
                else
                {
                    donneesPlaylist.putString("identifiantPlaylist",nomListeDeLecture);
                    donneesPlaylist.putString("Flag","ajouter");
                    ft = getActivity().getSupportFragmentManager().beginTransaction();

                    fragment = new Fragment_contenu_liste_de_lecture();
                    fragment.setArguments(donneesPlaylist); // Envoie des infos de la musique au fragment
                    ft.replace(R.id.mainFrame, fragment)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .addToBackStack(null)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                            .commit();
                }
            }
        });

        // Lors de l'appuie sur le bouton OK de la boite de dialogue
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String txt = Nomentre.getText().toString();

                base.ouvrirBase();
                long result = base.ajoutLdl(txt);

                if (result > 0) {
                    Nomentre.setText("");
                } else {
                    Toast.makeText(getContext(), "Echec", Toast.LENGTH_SHORT).show();
                }

                arrayPlaylists.add(txt);
                adapter.notifyDataSetChanged();
                base.fermer();

                Toast.makeText(getContext(), txt + " ajoutée", Toast.LENGTH_SHORT).show();
            }
        });

        // Gestion du clique sur annuler de la fenêtre de dialog
        builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                base.fermer();
            }
        });

        final AlertDialog ad  = builder.create(); // Création de la fenêtre de dialogue

        btnFloatingAjout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.show(); // Affichage de celle ci
            }
        });


        return vueListeMusiques;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    // Menu contextuel lors d'un clique long sur une liste de lecture
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.contextmenulistes, menu);
    }


    // Gestion des clics sur les éléments du menu contextuel
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId())
        {
            case R.id.SupprimerListeLecture:
                String l = arrayPlaylists.get(info.position);
                base.SupprimerLdl(l);
                adapter.remove(l);

                ft = getActivity().getSupportFragmentManager().beginTransaction();

                fragment = new Fragment_Listes_de_Lecture();
                getActivity().getSupportFragmentManager().popBackStack();
                ft.replace(R.id.mainFrame, fragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .addToBackStack(null)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                        .commit();
                return true;

            case R.id.PartageListeLecture:
                String nomList =  arrayPlaylists.get(info.position);
                Intent i=new Intent (getActivity(),Serveur.class);
                i.putExtra("nomListe", nomList);
                startActivity(i);
        }

        return super.onContextItemSelected(item);
    }
}
