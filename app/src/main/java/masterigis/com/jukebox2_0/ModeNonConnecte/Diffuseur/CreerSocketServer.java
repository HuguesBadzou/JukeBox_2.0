package masterigis.com.jukebox2_0.ModeNonConnecte.Diffuseur;

import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

// Created by hamila on 16/01/2016.
// Listing 16-25: Creating a Server Socket
public class CreerSocketServer
{
    DataOutputStream dOS=null;
    ServerSocket socketserver = null ;
    Socket socketduserveur = null ;
   InterfacePartage interf;

   public CreerSocketServer(ServerSocket socketserver,InterfacePartage interf)
   {
       this.socketserver = socketserver;
       this.interf=interf;
   }

   public void initiateServerSocket() {

       new Thread(new Runnable() {
           @Override
           public void run() {
               try {
                   while(true) {

                       //attendre la connexion du client
                       socketduserveur = socketserver.accept();
                       interf.socket[interf.numbreClientConnecte] = socketduserveur;
                       Log.i("msg : ", interf.numbreClientConnecte + "");
                       if (interf.socket[interf.numbreClientConnecte] != null) {
                           Log.i("msg", "socket num" + interf.numbreClientConnecte);

                           //creer DataOutStream
                           interf.oos[interf.numbreClientConnecte] = new DataOutputStream(interf.socket[interf.numbreClientConnecte].getOutputStream());

                           //creer DataInputStream
                           interf.in[interf.numbreClientConnecte] = new DataInputStream(interf.socket[interf.numbreClientConnecte].getInputStream());

                           //creer un ecouteur pour lire les msg envoyer par ce client
                           new RecevoirMsg().lireMsg(interf.handler, interf.in[interf.numbreClientConnecte], interf, interf.lv1);

                           //convertir la liste de music a une chaine de carreccter
                           String msg1 = new ConvertirListToString().convertirToString(interf.image_details);

                           //envoyer la chaine a ce participant
                           new EnvoyerMsg( interf.oos[interf.numbreClientConnecte], msg1).start();
                           interf.numbreClientConnecte++;
                       }
                   }

               } catch (Exception e) {
                   Log.e("msg", Log.getStackTraceString(e));
               }
           }
       }).start();

       if (dOS == null)
           Log.i("msg", "DataOutputStream echec ");
       else
           Log.i("msg", "DataOutputStream est créé avec succés ");
   }
}
