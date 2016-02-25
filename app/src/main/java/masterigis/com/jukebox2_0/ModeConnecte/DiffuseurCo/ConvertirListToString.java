package masterigis.com.jukebox2_0.ModeConnecte.DiffuseurCo;

import java.util.ArrayList;
// Created by hamila on 21/01/2016.

public class ConvertirListToString {

    public String convertirToString(ArrayList<NewItem> results ){

        String msg = "";
        for(int i=0;i<results.size();i++){
            msg +=results.get(i).getId()+"-,;,;"+results.get(i).getTitre()+"-,;,;"+results.get(i).getArtist()
                    +"-,;,;"+"Vote : 0"+";,;,;";
        }

        return msg ;
    }
}
