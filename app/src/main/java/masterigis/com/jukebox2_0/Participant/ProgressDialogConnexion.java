package masterigis.com.jukebox2_0.Participant;

import android.app.ProgressDialog;
import android.os.AsyncTask;

/**
 * Created by hamila on 20/01/2016.
 */
public class ProgressDialogConnexion extends AsyncTask<Void, Void, Void> {
    private ProgressDialog dialog;
    public ProgressDialogConnexion(InterfaceList activity) {
        dialog = new ProgressDialog(activity);
    }

    @Override
    protected void onPreExecute() {
        dialog.setMessage("Connexion en cours..");
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
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

}
