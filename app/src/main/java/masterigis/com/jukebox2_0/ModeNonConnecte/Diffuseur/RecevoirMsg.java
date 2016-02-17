package masterigis.com.jukebox2_0.ModeNonConnecte.Diffuseur;

import android.os.Handler;
import android.util.Log;
import android.widget.ListView;

import java.io.DataInputStream;
import java.util.ArrayList;
import java.util.Collections;
//Created by hamila on 16/01/2016.

// Classe contenant les méthode de reception des liste par les participants
public class RecevoirMsg {

    String line = null ;
    // Handler handler = new Handler() ;
    ArrayList<NewItem> results;

    // Méthode de lecture des données
    public void lireMsg(final Handler handler ,final DataInputStream dIS,final InterfacePartage intList,final ListView lv1)
    {
        new Thread(new Runnable()
        {
            public void run() {
                boolean  erreur = true;
                while (dIS != null && erreur)
                {
                    try
                    {
                        Log.i("msg bonjour  ", "client");
                        line = dIS.readUTF();
                        results= new ConvertirStringToList().ConvertiToList(line);
                        displayMsg(handler, lv1, results, intList);
                        Log.i("msg ", line);

                    } catch (Exception e)
                    {
                        erreur = false;
                        Log.e("msg", Log.getStackTraceString(e));                    }
                }
            }
        }).start();
    }


    // Méthode d'affichage de la liste reçu par les participants
    public void displayMsg(Handler handler, final ListView lv1, final ArrayList<NewItem> results, final InterfacePartage intList) {
        String [] tab1,tab2;
        ArrayList<NewItem> image_details;
        image_details = (ArrayList<NewItem>) intList.image_details;
        Log.i("msg ",image_details.size()+"");
        Log.i("msg ",results.size()+"");
        int somme;
        for(int i=0;i < results.size(); i++) {
            int j = 0;
            while (results.get(i).getId().compareTo(image_details.get(j).getId()) != 0 && j < image_details.size()) {
                j++;
            }
            if (j < image_details.size()) {
               tab1 = results.get(i).getVote().split(":");
                tab1=tab1[1].split(" ");
                tab2 = image_details.get(j).getVote().split(":");
                tab2=tab2[1].split(" ");
                somme=Integer.parseInt(tab1[1])+Integer.parseInt(tab2[1]);
                image_details.get(j).setVote("Vote : "+somme);
                //pour faire le trie de la liste
                Collections.sort(image_details);
            }
        }

        handler.post(new Runnable() {
            @Override
            public void run() {
                lv1.invalidateViews();

            }
        });

    }
}
