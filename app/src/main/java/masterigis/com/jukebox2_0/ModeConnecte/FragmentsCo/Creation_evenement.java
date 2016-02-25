package masterigis.com.jukebox2_0.ModeConnecte.FragmentsCo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import m1geii.com.jukebox2_0.R;

public class Creation_evenement extends Fragment {
    private TextView Createur, Nom_Evenement, date, pwd;
    private ProgressDialog pDialog;

    //Déclaration de l'URL de notre fonction php
    private static final String url_Ajout_Evenement="!!!!!!!!!!!!!!!!!!!";

    //Déclaration des identifiants de statut des requêtes
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    String nomSaisi,titreSaisi,motdepasseSaisi,mail, DateEv;
    Button ajouter;

    ArrayList<String> arrayEvenements = new ArrayList<>();
    AlertDialogManager alert = new AlertDialogManager();
    ListView liste_evenements;
    ArrayAdapter<String> adapter;
    FloatingActionButton btnFloatingAjoutEvt;

  //  EditText createur, nomeven,dateeven;
    LayoutInflater inf;


    public Creation_evenement() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view= inflater.inflate(R.layout.fragment_liste_evenements, container, false);

        btnFloatingAjoutEvt = (FloatingActionButton)view.findViewById(R.id.bouton_flottant_ajouter_evenement);
        liste_evenements = (ListView)view.findViewById(R.id.listview_liste_evenements);
        adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_selectable_list_item, arrayEvenements);

        btnFloatingAjoutEvt.setOnClickListener(new View.OnClickListener(){

         @Override
         public void onClick(View v) {
             //View vue = v;
             //View vue;
             //LayoutInflater inf = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

             //final View layout = inf.inflate(R.layout.password_dialog, (ViewGroup) findViewById(R.id.root));
             /*vue = inf.inflate(R.layout.fragment_creation_evenement,null);
             AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
             builder.setTitle("Création d'un Nouvel Evenement");
             createur = (EditText)vue.findViewById(R.id.createur);
             nomeven = (EditText)vue.findViewById(R.id.titre);
             dateeven = (EditText)vue.findViewById(R.id.date);
             pwd=(TextView)vue.findViewById(R.id.pwd);
             ajouter = (Button)vue.findViewById(R.id.ajouter);
             builder.setView(vue);
             builder.create().show();*/
             afficherAlertDialog();
         }
     });

       /* Createur =(TextView)view.findViewById(R.id.createur);
       // View viewTitre= inflater.inflate(R.layout.fragment_creation_evenement, container, false);
        Nom_Evenement =(TextView)view.findViewById(R.id.titre);
       // View viewDate= inflater.inflate(R.layout.fragment_creation_evenement, container, false);
        date=(TextView)view.findViewById(R.id.date);
       // View viewPwd= inflater.inflate(R.layout.fragment_creation_evenement, container, false);
        pwd=(TextView)view.findViewById(R.id.pwd);
        ajouter = (Button)view.findViewById(R.id.ajouter);*/


        return view;
    }


    public void afficherAlertDialog(){
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View vue = inflater.inflate(R.layout.fragment_creation_evenement, null);
        final EditText nomeven = (EditText)vue.findViewById(R.id.titre);
        final EditText dateeven = (EditText)vue.findViewById(R.id.date);
        final EditText pwd = (EditText)vue.findViewById(R.id.mdp);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Création d'un Nouvel Evenement");
        builder.setCancelable(false);


        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Pattern pDate = Pattern.compile("(0[1-9]|[1-2][0-9]|30|31)-(0[1-9]|1[0-2])-([0-9]{4})");      //Format de la date jj-mm-aaaa
                Matcher mDate = pDate.matcher(dateeven.getText().toString());

                // Pour remplacer les espaces par des champs vides pour éviter les mauvaises insertions en base de données
                titreSaisi = nomeven.getText().toString().replaceAll(" ","");
                motdepasseSaisi = pwd.getText().toString().replaceAll(" ","");
                DateEv = dateeven.getText().toString();
                DetectionConnexionInternet siInternet=new DetectionConnexionInternet(getActivity());    //Verification Connexion Internet

                if(siInternet.isConnectingToInternet()) {
                    if ( titreSaisi.equals("") || motdepasseSaisi.equals("") || dateeven.getText().toString().equals("") ) {
                        Toast.makeText(getActivity().getApplicationContext(), "Veuillez remplir tous les champs ", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        /***** Test pour le format de la date *****/
                        if (!mDate.matches()) {
                    Toast.makeText(getActivity().getApplicationContext(), "Format date de naissance  invalide", Toast.LENGTH_SHORT).show();
                }
                if (motdepasseSaisi.length() < 6) {
                    Toast.makeText(getActivity().getApplicationContext(), "Le mot de passe doit contenir au moins 6 caract\u00e8res", Toast.LENGTH_SHORT).show();
                }
                else {
                    new NouvelEvenement().execute();
                }
            }
        }
        else{
            alert.showAlertDialog(getActivity(),getResources().getString(R.string.titre_boite_de_dialogue),getResources().getString(R.string.message_boite_de_dialogue), false);
        }

            }
        });

        builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });

        builder.setView(vue);
        builder.create().show();

    }
    //@Override
    public void onClick(View v) {

/*


        Pattern pDate = Pattern.compile("(0[1-9]|[1-2][0-9]|30|31)-(0[1-9]|1[0-2])-([0-9]{4})");      //Format de la date jj-mm-aaaa
        Matcher mDate = pDate.matcher(date.getText().toString());

        // Pour remplacer les espaces par des champs vides pour éviter les mauvaises insertions en base de données
        nomSaisi = Createur.getText().toString().replaceAll(" ","");
        titreSaisi = Nom_Evenement.getText().toString().replaceAll(" ","");
        motdepasseSaisi = pwd.getText().toString().replaceAll(" ","");

        DetectionConnexionInternet siInternet=new DetectionConnexionInternet(getActivity());    //Verification Connexion Internet

        if(siInternet.isConnectingToInternet()) {
            if (nomSaisi.equals("") || titreSaisi.equals("") || motdepasseSaisi.equals("") || date.getText().toString().equals("") ) {
                Toast.makeText(getActivity().getApplicationContext(), "Veuillez remplir tous les champs ", Toast.LENGTH_SHORT).show();
            }
            else {
                /***** Test pour le format de la date *****/
               /* if (!mDate.matches()) {
                    Toast.makeText(getActivity().getApplicationContext(), "Format date de naissance  invalide", Toast.LENGTH_SHORT).show();
                }
                if (motdepasseSaisi.length() < 6) {
                    Toast.makeText(getActivity().getApplicationContext(), "Le mot de passe doit contenir au moins 6 caract\u00e8res", Toast.LENGTH_SHORT).show();
                }
                else {
                    new NouvelEvenement().execute();
                }
            }
        }
        else{
            alert.showAlertDialog(getActivity(),getResources().getString(R.string.titre_boite_de_dialogue),getResources().getString(R.string.message_boite_de_dialogue), false);
        }*/
    }

    class NouvelEvenement extends AsyncTask<Void, Void, Void>
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

            int success;
            //replaceAll pour supprimer les espace
            String nomE = titreSaisi;
            String mdp = motdepasseSaisi;
            String dateE = DateEv;
            try {
                OkHttpClient client = new OkHttpClient();

                RequestBody formBody = new FormEncodingBuilder()//new FormBody.Builder()
                        .add("nomE", nomE)
                        .add("dateE", dateE)
                        .add("mdp",mdp)
                        .add("mail",mail)
                        .build();

                Request request = new Request.Builder()
                        .url("http://jukeboxv20.olympe.in/Naveck/Ajout_Evenement.php")
                        .build();
                Response responses = null;

                try {
                    responses = client.newCall(request).execute();
                } catch (IOException e) {
                    e.printStackTrace();
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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
