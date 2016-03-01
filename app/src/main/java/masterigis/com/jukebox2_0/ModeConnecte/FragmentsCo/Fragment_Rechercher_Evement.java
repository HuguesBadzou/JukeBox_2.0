package masterigis.com.jukebox2_0.ModeConnecte.FragmentsCo;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import m1geii.com.jukebox2_0.R;


public class Fragment_Rechercher_Evement extends Fragment {

    FloatingActionButton btnFloatingRechercher;
    EditText mEdit;
    // Progress Dialog
    private ProgressDialog pDialog;
    private ListView evenement_trouver;
    Fragment fragment;
    FragmentTransaction ft;

    // Creating JSON Parser object
    //HttpClient jParser = new HttpClient();

    ArrayList<HashMap<String, String>> productsList;
    String nom_evenement;
    String verification_nom_evenement;

    // url to get all products list
    //private static String url_event = "http://192.168.43.186/judebox/recherche.php?nom=";  // le 'nom' permet de passer par la methode get dans le fichier php sur le serveur afin de faire une comparaison MYSQL
    private static String url_event = "http://jukeboxv20.olympe.in/diffuseurs/recherche.php?nom=";
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "products";
    private static final String TAG_PID = "id";
    private static final String TAG_NAME = "nom_evenement";
    // products JSONArray
    JSONArray products = null;

    public Fragment_Rechercher_Evement() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //jParser.execute();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rechercherEvenemt=inflater.inflate(R.layout.fragment_rechercher_evement, container, false);
        btnFloatingRechercher = (FloatingActionButton)rechercherEvenemt.findViewById(R.id.bouton_flottant_rechercher);


        evenement_trouver = (ListView)rechercherEvenemt.findViewById(R.id.listView_evenement);

// Hashmap for ListView
        productsList = new ArrayList<HashMap<String, String>>();
        btnFloatingRechercher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEdit = (EditText) rechercherEvenemt.findViewById(R.id.rechercher_evenement);
                 nom_evenement = mEdit.getText().toString();
               // Toast.makeText(getContext(), "coucou = " + nom_evenement, Toast.LENGTH_LONG).show(); // Affichage de celle ci
               // url_event = url_event + nom_evenement; // on concatene l'url avec le nom de la variable pour pouvoir faire une requete GET dessus avec le ficiher php

                // Loading products in Background Thread
                new LoadAllProducts().execute();
            }
        });

        evenement_trouver.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
           // Toast.makeText(getActivity(),"vous",Toast.LENGTH_LONG).show();

              /*  donneesPlaylist.putString("identifiantPlaylist",nomListeDeLecture);
                donneesPlaylist.putString("Flag","ajouter");
                ft = getActivity().getSupportFragmentManager().beginTransaction();*/

                fragment = new Fragment_Contenu_Evenement_Participant();
               // fragment.setArguments(donneesPlaylist); // Envoie des infos de la musique au fragment
                ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.mainFrame, fragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .addToBackStack(null)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                        .commit();
            }
        });


        return rechercherEvenemt;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }
/*
    * Background Async Task to Load all product by making HTTP Request
    * */
    class LoadAllProducts extends AsyncTask<String, String, String> {

    ArrayAdapter<String> adapter;
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Recherche événement...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting All products from url
         * */
        protected String doInBackground(String... args) {
            // Building Parameters
            verification_nom_evenement=null;
            try {
                OkHttpClient client = new OkHttpClient();
                RequestBody body = new FormEncodingBuilder()
                        .add("nom",nom_evenement)
                        .build();
                Request request = new Request.Builder()
                        .url("http://jukeboxv20.olympe.in/diffuseurs/recherche.php").post(body)
                        .build();
                Response responses = null;

                try {
                    responses = client.newCall(request).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String jsonData = responses.body().string();
                Log.i("msg nom_evenement  ", jsonData);


                JSONObject Jobject = new JSONObject(jsonData);
                JSONArray Jarray = Jobject.getJSONArray("evenement");//diffuseurs c est le nom définir dans le fichier php
                for (int i = 0; i < Jarray.length(); i++) {
                    JSONObject object = Jarray.getJSONObject(i);
                    Log.i("msg nom_evenement  ", object.get("nom_evenement").toString());
                  verification_nom_evenement = object.get("nom_evenement").toString();
                  //  Toast.makeText(getActivity(),"toctoc="+verification_nom_evenement, Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }



            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    //L'adapter pour la listview des evenements trouvé

                    String[] trouver = new String[]{verification_nom_evenement};
                    adapter=new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, trouver);


                    if (verification_nom_evenement==null) {
                        Toast.makeText(getActivity(),"Aucun evenement trouvé !", Toast.LENGTH_LONG).show();
                    } else {
                        evenement_trouver.setAdapter(adapter);
                       // adapter.clear();
                       // adapter.notifyDataSetChanged();
                        //adapter.setNotifyOnChange(true);
                       // Toast.makeText(getActivity(), "toctoc=" + verification_nom_evenement, Toast.LENGTH_LONG).show();
                        Toast.makeText(getActivity(), "Evenement trouvé", Toast.LENGTH_LONG).show();
                    }


                }
            });

        }


    }


}
