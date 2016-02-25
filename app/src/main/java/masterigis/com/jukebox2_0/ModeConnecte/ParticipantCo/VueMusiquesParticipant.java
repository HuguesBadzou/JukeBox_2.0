package masterigis.com.jukebox2_0.ModeConnecte.ParticipantCo;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import m1geii.com.jukebox2_0.R;
import masterigis.com.jukebox2_0.Model.AdapterChansons;

public class VueMusiquesParticipant extends Fragment {

    int success;
    private static ProgressDialog pDialog;                 //Fenêtre de dialogue de progression de la tâche
    //static JSONParser jsonParser = new JSONParser();       //Parseur JSON
    JSONArray tableau_lieux_flashlike = null; //Tableau ou sera stocké la liste des lieux récupérés
    private static final String url_details_lieu = "";    //URL fichier php de connexion DetailsMusiquesEvenement.php
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    // Variables où seront stockées les champs renvoyés par le php
    private static final String TAG_Titre = "Titre";
    private static final String TAG_Artiste = "Artiste";
    private static final String TAG_Id_Evenement = "Id_Evenement";
    private static final String TAG_Nom_Fichier = "Nom_Fichier";
    private static final String TAG_Votes = "votes";

    String Id_musique;
    String Titre;
    String Artiste;
    String Id_Evenement;
    String Nom_Fichier;
    String votes;

    ListView listeMusiqueBD;
    FloatingActionButton btnFlottantVote;
    NewItem newsData;
    int vote;
    View row;
    String id,flag;
    ArrayList Chansons = null;
    AdapterChansons adaptChanson;
    CustomListAdapter morceaux;
    ArrayList<NewItem> Listeitems;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Récupération des données
        id=getArguments().getString("identifiantEvenement");
        flag=getArguments().getString("Flag");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View content = inflater.inflate(R.layout.fragment_vue_musiques_participant, container, false);

        listeMusiqueBD = (ListView)content.findViewById(R.id.listview_listes_de_musiques_bd);
        btnFlottantVote = (FloatingActionButton)content.findViewById(R.id.bouton_flottant_voter);


        /*adaptChanson = new AdapterChansons(getActivity(),Chansons);
        listeMusiqueBD.setAdapter(adaptChanson);*/

        morceaux = new CustomListAdapter(getActivity(),Listeitems);
        listeMusiqueBD.setAdapter(morceaux);

        listeMusiqueBD.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            final CharSequence[] items = {"0", "1", "2", "3", "4", "5"};

            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Object o = listeMusiqueBD.getItemAtPosition(position);
                newsData = (NewItem) o;
                row = v;
                if (newsData.getVote().endsWith("*") == false) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle(newsData.getTitre())
                            .setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int item) {
                                    vote = item;
                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .setPositiveButton("Voter", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    if (vote != 0) {
                                        newsData.setVote("Vote : " + vote + "");
                                        row.setBackgroundColor(Color.GREEN);
                                        // row.setClickable(true);
                                        listeMusiqueBD.invalidateViews();
                                    }

                                }

                            }).create().show();
                }
            }
        });

        new HTTPClient().execute();
        return content;
    }





    class HTTPClient extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //  Toast.makeText(getApplicationContext(), "DÃ©but du traitement asynchrone", Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onProgressUpdate(Void...arg0){
            super.onProgressUpdate();
            // Mise Ã  jour de la ProgressBar
            // mProgressBar.setProgress(values[0]);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http://jukeboxv20.olympe.in/Naveck/SelectionMusiqueBase.php")
                        .build();
                Response responses = null;

                try {
                    responses = client.newCall(request).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String jsonData = responses.body().string();
                JSONObject Jobject = new JSONObject(jsonData);
                JSONArray Jarray = Jobject.getJSONArray("musique");//diffuseurs c est le nom dÃ©finir dans le fichier php
                for (int i = 0; i < Jarray.length(); i++) {
                    NewItem newsData = new NewItem();
                    JSONObject object = Jarray.getJSONObject(i);
                    //Log.i("msg Titre  ", object.get("titre").toString());
                    //Log.i("msg Artiste ", object.get("artiste").toString());
                    //Log.i("msg Id evenement  ", object.get("id_even").toString());
                    //Log.i("msg Nom fichier  ", object.get("nom_fichier").toString());
                    //Log.i("msg Nombre de Votes  ", object.get("Votes").toString());
                    Id_musique = object.get("id").toString();
                    Titre = object.get("titre").toString();
                    Artiste = object.get("titre").toString();
                    Id_Evenement = object.get("id_even").toString();
                    Nom_Fichier = object.get("nom_fichier").toString();
                    votes = object.get("Votes").toString();

                    newsData.setVote(votes);
                    newsData.setTitre(Titre);
                    newsData.setId(id);
                    newsData.setArtist(Artiste);

                    Listeitems.add(newsData);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null ;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Toast.makeText(getApplicationContext(), "Le traitement asynchrone est terminÃ©", Toast.LENGTH_LONG).show();
        }
    }


}
