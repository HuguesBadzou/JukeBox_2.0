package masterigis.com.jukebox2_0.Participant;

import android.os.Handler;
import android.util.Log;
import android.widget.ListView;

import java.io.DataInputStream;
import java.util.ArrayList;

/**
 * Created by hamila on 16/01/2016.
 */
public class RecevoirMsg {

    String line = null ;
   // Handler handler = new Handler() ;
   ArrayList<NewItem> results;

    public void lireMsg(final Handler handler ,final DataInputStream dIS,final InterfaceList intList,final ListView lv1)
    {
        new Thread(new Runnable()
        {
            public void run() {
                boolean  erreur = true;
                while (dIS != null && erreur == true)
                 {
                     try
                     {

                         Log.i("msg bonjour  ", "client");
                         if (dIS == null) {
                             Log.i("msg inputStream est ", "null");
                         } else {
                             int i = 1;
                             line = dIS.readUTF();
                             results= new ConvertirStringToList().ConvertiToList(line);
                             displayMsg(handler,lv1,results,intList);
                             Log.i("msg ", line);
                             i++;

                         }

                     } catch (Exception e)
                     {
                         erreur = false;
                         Log.e("msg Exception", "client", e);
                     }
                 }
            }
        }).start();
    }


    public void displayMsg(Handler handler, final ListView lv1, final ArrayList<NewItem> results, final InterfaceList intList) {

        handler.post(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                intList.image_details = results;
                lv1.setAdapter(new CustomListAdapter(intList, results));

            }
        });

    }
}
