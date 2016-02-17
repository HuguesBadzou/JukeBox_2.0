package masterigis.com.jukebox2_0.ModeNonConnecte.Participant;

import android.util.Log;

import java.io.DataOutputStream;

/**
 * Created by hamila on 19/01/2016.
 */
public class EnvoyerMsg extends Thread {
    DataOutputStream dOS;
    String msg;

    public EnvoyerMsg(DataOutputStream dOS, String msg){
        this.dOS=dOS;
        this.msg=msg;
    }
    public void run() {
        Log.i("msg je suis client", "client");
        try {

            if (dOS == null)
                Log.i("msg outPutStream est ", "null");
            else {
                Log.i("msg serveur avant write", "serveur");
                dOS.writeUTF(msg);
                dOS.flush();
                Log.i("msg serveur apres write", msg);
            }
        }catch(Exception e){
            Log.e("msg Exception","Client",e);
        }
    }

}

