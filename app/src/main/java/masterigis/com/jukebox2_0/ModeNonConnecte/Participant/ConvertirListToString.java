package masterigis.com.jukebox2_0.ModeNonConnecte.Participant;

import android.widget.ListView;

import java.util.ArrayList;

public class ConvertirListToString {

    public String convertirToString(InterfaceList interfaceList ){
        ArrayList<NewItem> results = interfaceList.image_details ;
        ListView listView = interfaceList.lv1 ;
        String msg = "";
        for(int i=0;i<results.size();i++){
            if((results.get(i).getVote().compareTo("Vote : 0")!=0 )&&(results.get(i).getVote().endsWith("*")==false)){
                    int vote;
                if(results.get(i).getVote().substring(0,8).compareTo("Vote : 1")==0){
                    vote = 5 ;
                }else if(results.get(i).getVote().substring(0,8).compareTo("Vote : 2")==0){
                    vote = 4 ;
                }else if(results.get(i).getVote().substring(0,8).compareTo("Vote : 3")==0){
                    vote = 3 ;
                }else if(results.get(i).getVote().substring(0,8).compareTo("Vote : 4")==0){
                    vote = 2;
                }else vote = 1;
                    msg += results.get(i).getId() + "-,;,;" + results.get(i).getTitre() + "-,;,;" + results.get(i).getArtist()
                            + "-,;,;" + results.get(i).getVote().substring(0,7)+vote + ";,;,;";
                ((NewItem)interfaceList.image_details.get(i)).setVote(results.get(i).getVote() + "*");
                ((ListView)interfaceList.lv1).invalidateViews();


            }

        }

        return msg ;
    }
}
