package masterigis.com.jukebox2_0.ModeConnecte;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import m1geii.com.jukebox2_0.R;
import masterigis.com.jukebox2_0.Choix_roles;

public class GoogleSignIn extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener{

    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;
    private static ProgressDialog pDialog;                 //Fenêtre de dialogue de progression de la tâche
    Bundle mode=new Bundle();

    // Variables pour la connexion Google
    private GoogleApiClient mGoogleApiClient;
    String nomGoogle;
    String prenomGoogle;
    String dateDeNaissanceGoogle;
    String emailGoogle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_sign_in);

        TextView texteGoogleSignIn=(TextView)findViewById(R.id.google_signin_label);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/HelveticaNeue.ttf");
        texteGoogleSignIn.setTypeface(typeface);

        // Button listeners
        findViewById(R.id.sign_in_button).setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(Scopes.PLUS_LOGIN))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addApi(Plus.API)
                .build();

        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
    }

    // Listener
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    // Méthode de connexion
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    // Methode de déconnexion
    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                    }
                });
    }

    // Méthode de révocation
    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {

                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {

            if(resultCode==RESULT_OK) {
                // Récupération des infos de l'utilisateur
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                GoogleSignInAccount acct = result.getSignInAccount();
                Person person = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);

                String[] nomCompteGoogle = person.getDisplayName().split(" ");

                if(person.getBirthday()==null){
                    dateDeNaissanceGoogle="0000-00-00";
                }
                else {
                    dateDeNaissanceGoogle = person.getBirthday();
                }

                if(nomCompteGoogle[1]==null){
                    nomGoogle="inconnu";
                }

                else{
                    nomGoogle = nomCompteGoogle[1];
                }
                prenomGoogle = nomCompteGoogle[0];
                emailGoogle = acct.getEmail();

                // Pour afficher les informations dans le logcat
                Log.i(TAG, "--------------------------------");
                Log.i(TAG, "Infos: Display Name: " + person.getDisplayName());
                Log.i(TAG, "Infos Gender: " + person.getGender());
                Log.i(TAG, "Infos AboutMe: " + person.getAboutMe());
                Log.i(TAG, "Infos Birthday: " + person.getBirthday());
                Log.i(TAG, "Infos Current Location: " + person.getCurrentLocation());
                Log.i(TAG, "Infos Language: " + person.getLanguage());
                Log.i(TAG, "Infos Nom: " + nomGoogle);
                Log.i(TAG, "Infos Prenom: " + prenomGoogle);
                Log.i(TAG, "Infos Email: " + emailGoogle);

                handleSignInResult(result);
            }

            if(resultCode==RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "Vous devez accepter les autorisations", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        String s="coucou";
        if (result.isSuccess()) {
            // Appel de la classe fesant la connexion
            GoogleConnect gSignIn=new GoogleConnect();
            gSignIn.execute(nomGoogle, prenomGoogle, dateDeNaissanceGoogle, emailGoogle);
        }
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    // On le déconnecte une fois les informations récupérées
    @Override
    public void onStop() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            signOut();
        }
        super.onStop();
    }

    // Tâche Asynchrone
    class GoogleConnect extends AsyncTask<String,String,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(GoogleSignIn.this);
            pDialog.setMessage("Connexion...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                int succesRequete;
                OkHttpClient client = new OkHttpClient();


                // Envoie des paramètres
                RequestBody requestBody = new FormEncodingBuilder()
                    .add("nom_google", params[0])
                    .add("prenom_google",params[1])
                    .add("date_de_naissance_google", params[2])
                    .add("email_google", params[3])
                    .build();

                // Url d'envoie
                Request request=new Request.Builder().url("http://jukeboxv20.olympe.in/android/connexion_jukebox_google.php").post(requestBody).build();

                // String ou seront stockées les informations des l'utilisateur
                String id_diffuseur, id_participant;
                String nom_utilisateur;
                String prenom_utilisateur;
                String date_de_naissance_utilisateur;
                String email_utilisateur;

                Response response = client.newCall(request).execute();
                String jsonData = response.body().string();
                JSONObject jobject = new JSONObject(jsonData);
                succesRequete=jobject.getInt("success");

                // Si la requête s'est bien passé nous
                if(succesRequete==1) {
                    Log.i("msg id diffuseur  ", jobject.get("id_diffuseur").toString());
                    Log.i("msg id participant  ", jobject.get("id_participant").toString());
                    Log.i("msg nom  ", jobject.get("nom_utilisateur").toString());
                    Log.i("msg prenom  ", jobject.get("prenom_utilisateur").toString());
                    Log.i("msg dateNaissance  ", jobject.get("date_de_naissance_utilisateur").toString());
                    Log.i("msg mail  ", jobject.get("email_utilisateur").toString());

                    id_diffuseur=jobject.get("id_diffuseur").toString();
                    id_participant=jobject.get("id_participant").toString();
                    nom_utilisateur=jobject.get("nom_utilisateur").toString();
                    prenom_utilisateur=jobject.get("prenom_utilisateur").toString();
                    date_de_naissance_utilisateur=jobject.get("date_de_naissance_utilisateur").toString();
                    email_utilisateur=jobject.get("email_utilisateur").toString();

                    // Edition preferences
                    GoogleSignIn.this.getSharedPreferences(".donneesUtilisateur", MODE_PRIVATE)
                            .edit()
                            .putString("etat","dejaConnecte")
                            .putString("idDiffuseur",id_diffuseur)
                            .putString("idParticipant",id_participant)
                            .putString("nomUtilisateur",nom_utilisateur)
                            .putString("prenomUtilisateur",prenom_utilisateur)
                            .putString("emailUtilisateur",email_utilisateur)
                            .putString("dateNaissance",date_de_naissance_utilisateur)
                            .commit();

                    return jobject.getString("message");
                }

                else {
                    return jobject.getString("message");
                }
            }
            catch(Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            pDialog.dismiss();
            if(file_url !=null) {
                Toast.makeText(getApplicationContext(),file_url, Toast.LENGTH_LONG).show();

                Intent i=new Intent(GoogleSignIn.this,Choix_roles.class);
                mode.putString("mode", "modeConnecte");
                i.putExtras(mode);
                //finish();
                startActivity(i);
            }
        }
    }


}
