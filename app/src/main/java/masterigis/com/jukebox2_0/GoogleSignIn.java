package masterigis.com.jukebox2_0;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
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

import m1geii.com.jukebox2_0.R;

public class GoogleSignIn extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener{

    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;
    Bundle mode=new Bundle();

    // Variables pour la connexion Google
    private GoogleApiClient mGoogleApiClient;
    String nomGoogle;
    String prenomGoogle;
    String dateDeNaissanceGoogle;
    String emailGoogle;

    // Données stockées
    SharedPreferences pref;

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
                nomGoogle = nomCompteGoogle[1];
                prenomGoogle = nomCompteGoogle[0];
                dateDeNaissanceGoogle = person.getBirthday();
                emailGoogle = acct.getEmail();

                // Pour afficher les informations dans le logcat
                Log.i(TAG, "--------------------------------");
                Log.i(TAG, "Display Name: " + person.getDisplayName());
                Log.i(TAG, "Gender: " + person.getGender());
                Log.i(TAG, "AboutMe: " + person.getAboutMe());
                Log.i(TAG, "Birthday: " + person.getBirthday());
                Log.i(TAG, "Current Location: " + person.getCurrentLocation());
                Log.i(TAG, "Language: " + person.getLanguage());
                Log.i(TAG, "Nom: " + nomGoogle);
                Log.i(TAG, "Prenom: " + prenomGoogle);

                handleSignInResult(result);
            }

            if(resultCode==RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "Vous devez accepter les autorisations", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            Toast.makeText(getApplicationContext(), "Connecté", Toast.LENGTH_SHORT).show();

            // Edition preferences
            this.getSharedPreferences(".siConnecte", MODE_PRIVATE)
                    .edit()
                    .putString("etat","dejaConnecte")
                    .commit();

            Intent i=new Intent(GoogleSignIn.this,Choix_roles.class);
            mode.putString("mode", "modeConnecte");
            i.putExtras(mode);
            finish();
            startActivity(i);
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
}
