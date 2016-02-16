package masterigis.com.jukebox2_0.Diffuseur;

import android.util.Log;

import java.io.DataOutputStream;

/**
 * Created by hamila on 16/01/2016.
 */

// Classe de test pour l'envoie de String aux clients

public class EnvoyerMsg extends Thread {
    DataOutputStream dOS;
    String msg;

    public EnvoyerMsg(DataOutputStream dOS, String msg){
        this.dOS=dOS;
        this.msg=msg;
    }
    public void run() {
        Log.i("msg je suis serveur", "serveur");
        try {

            if (dOS == null)
                Log.i("msg outPutStream est ", "null");
            else {
                Log.i("msg serveur avant write", "serveur");
                dOS.writeUTF(msg);
                dOS.flush();
                Log.i("msg serveur apres write", "serveur");
            }
        }catch(Exception e){
            Log.e("msg", Log.getStackTraceString(e));
        }
    }

}

