package masterigis.com.jukebox2_0.ModeConnecte.FragmentsCo;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import m1geii.com.jukebox2_0.R;

public class Creation_evenement extends Fragment {
    private ProgressDialog pDialog;

    String nomSaisi, nomEvenSaisi, motdepasseSaisi, mail, DateEv;

    ArrayList<String> arrayEvenements = new ArrayList<>();
    AlertDialogManager alert = new AlertDialogManager();
    ListView liste_evenements;
    ArrayAdapter<String> adapter;
    FloatingActionButton btnFloatingAjoutEvt;

    //  EditText createur, nomeven,dateeven;
    LayoutInflater inf;

    SharedPreferences donneesSession;
    int succesRequete;
    String id_diffuseur;


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

        View view = inflater.inflate(R.layout.fragment_liste_evenements, container, false);

        btnFloatingAjoutEvt = (FloatingActionButton) view.findViewById(R.id.bouton_flottant_ajouter_evenement);
        liste_evenements = (ListView) view.findViewById(R.id.listview_liste_evenements);
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_selectable_list_item, arrayEvenements);

        donneesSession= getActivity().getSharedPreferences(".donneesUtilisateur", Context.MODE_PRIVATE);
        id_diffuseur = donneesSession.getString("idDiffuseur","");
        mail = donneesSession.getString("emailUtilisateur", "");

        btnFloatingAjoutEvt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                afficherAlertDialog();
            }
        });

        return view;
    }


    public void afficherAlertDialog() {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View vue = inflater.inflate(R.layout.fragment_creation_evenement, null);
        final EditText nomeven = (EditText) vue.findViewById(R.id.titre);
        final EditText dateeven = (EditText) vue.findViewById(R.id.date);
        final EditText pwd = (EditText) vue.findViewById(R.id.mdp);
        pwd.setTypeface(Typeface.DEFAULT);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.titre_alert_dialog_creation_evenement));
        builder.setCancelable(false);


        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Format de la date jj-mm-aaaa
                Pattern pDate = Pattern.compile("(0[1-9]|[1-2][0-9]|30|31)-(0[1-9]|1[0-2])-([0-9]{4})");
                Matcher mDate = pDate.matcher(dateeven.getText().toString());

                // Pour remplacer les espaces par des champs vides pour éviter les mauvaises insertions en base de données
                nomEvenSaisi = nomeven.getText().toString().trim(); // Pour retirer les espaces à la fin ou au début
                motdepasseSaisi = pwd.getText().toString().replaceAll(" ", "");
                DateEv = dateeven.getText().toString();
                DetectionConnexionInternet siInternet = new DetectionConnexionInternet(getActivity());    //Verification Connexion Internet

                if (siInternet.isConnectingToInternet()) {
                    if (nomEvenSaisi.equals("") || motdepasseSaisi.equals("") || dateeven.getText().toString().equals("")) {
                        Toast.makeText(getActivity().getApplicationContext(), "Veuillez remplir tous les champs ", Toast.LENGTH_SHORT).show();
                    } else {
                        // Test pour le format de la date
                        if (!mDate.matches()) {
                            Toast.makeText(getActivity().getApplicationContext(), "Format date de naissance  invalide", Toast.LENGTH_SHORT).show();
                        }
                        if (motdepasseSaisi.length() < 6) {
                            Toast.makeText(getActivity().getApplicationContext(), "Le mot de passe doit contenir au moins 6 caract\u00e8res", Toast.LENGTH_SHORT).show();
                        } else {
                            new NouvelEvenement().execute(nomEvenSaisi,motdepasseSaisi,DateEv);
                        }
                    }
                } else {
                    alert.showAlertDialog(getActivity(), getResources().getString(R.string.titre_boite_de_dialogue), getResources().getString(R.string.message_boite_de_dialogue), false);
                }

                //Toast.makeText(getActivity(),nomEvenSaisi+" "+motdepasseSaisi+" "+DateEv,Toast.LENGTH_SHORT).show();

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

    }

    class NouvelEvenement extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Chargement...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            //replaceAll pour supprimer les espace

            try {
                OkHttpClient client = new OkHttpClient();

                RequestBody formBody = new FormEncodingBuilder()//new FormBody.Builder()
                        .add("id_diffuseur",id_diffuseur)
                        .add("nom_evenement", params[0])
                        .add("date_evenement", params[2])
                        .add("mdp_evenement", params[1])
                        .build();

                Request request = new Request.Builder()
                        .url("http://jukeboxv20.olympe.in/android/ajout_evenement.php")
                        .post(formBody)
                        .build();

                Response responses = client.newCall(request).execute();

                String jsonData = responses.body().string();
                JSONObject jobject = new JSONObject(jsonData);
                succesRequete = jobject.getInt("success");

                if (succesRequete == 1) {
                    Log.i("msg id evenement  ", jobject.get("id_evenement").toString());
                    Log.i("msg nom evenement  ", jobject.get("nom_evenement").toString());

                    return jobject.getString("message");
                }

                else
                {
                    return jobject.getString("message");
                }
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            pDialog.dismiss();
            if (file_url != null) {
                Toast.makeText(getActivity().getApplicationContext(), file_url, Toast.LENGTH_LONG).show();
            }
        }
    }
}
