package masterigis.com.jukebox2_0.ModeConnecte.ParticipantCo;

import android.util.Log;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *  Created by hamila on 16/01/2016.
 * Listing 16-26: Creating a client Socket
 *
 */
public class CreerSocketClient {
    DataInputStream dIS = null;
    Socket socket = null;

    public void initiateServerSocket(final String hostAddress, final InterfaceList interfaceList) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket= new Socket(hostAddress, 1030);
                    if(socket != null) {
                        Log.i("Socket client ", "est creé avec succes ");
                        try {
                            interfaceList.oos = new DataOutputStream(socket.getOutputStream());
                            interfaceList.in = new DataInputStream(socket.getInputStream());
                            if (interfaceList.in != null) {
                                Log.i("msg DataInputStream", "est creé avec succes");
                                new RecevoirMsg().lireMsg(interfaceList.handler, interfaceList.in,interfaceList, interfaceList.lv1);
                            }
                            if (interfaceList.oos != null)
                                Log.i("msg DataOutStream", "est creé avec succes");
                        } catch (Exception e) {
                            Log.i("msg ", "erreur");

                        }

                    }
                    else
                        Toast.makeText(interfaceList.getApplicationContext(), "erreur au niveau de la creation de socket ", Toast.LENGTH_LONG).show();


                } catch (UnknownHostException e) {
                    Log.e("msg", Log.getStackTraceString(e));
                } catch (Exception e) {
                    Log.e("msg", Log.getStackTraceString(e));
                }
            }
        }).start();

    }
}
