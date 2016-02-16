package masterigis.com.jukebox2_0.Participant;

import android.widget.ListView;

import java.util.ArrayList;

public class ConvertirListToString {

    public String convertirToString(InterfaceList interfaceList ){
        ArrayList<NewItem> results = interfaceList.image_details ;
        ListView listView = interfaceList.lv1 ;
        String msg = "";
        for(int i=0;i<results.size();i++){
            if((results.get(i).getVote().compareTo("Vote : 0")!=0 )&&(results.get(i).getVote().endsWith("*")==false)){

                    msg += results.get(i).getId() + "-,;,;" + results.get(i).getTitre() + "-,;,;" + results.get(i).getArtist()
                            + "-,;,;" + results.get(i).getVote() + ";,;,;";
                ((NewItem)interfaceList.image_details.get(i)).setVote(results.get(i).getVote() + "*");
                ((ListView)interfaceList.lv1).invalidateViews();


            }

        }

        return msg ;
    }
}
