package masterigis.com.jukebox2_0.Participant;

import android.app.ProgressDialog;
import android.os.AsyncTask;

public class ProgressDialogRecherche extends AsyncTask<Void, Void, Void> {
    private ProgressDialog dialog;
    public ProgressDialogRecherche(Rejoindre_Playlist activity) {
        dialog = new ProgressDialog(activity);
    }

    @Override
    protected void onPreExecute() {
        dialog.setMessage("Recherche en cours...");
        dialog.show();
    }

    @Override
    protected void onPostExecute(Void result) {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }
}