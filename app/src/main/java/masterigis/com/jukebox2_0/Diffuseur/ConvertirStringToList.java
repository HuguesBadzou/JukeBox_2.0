package masterigis.com.jukebox2_0.Diffuseur;

import java.util.ArrayList;

 // Created by hamila on 22/01/2016.

public class ConvertirStringToList {


    public ArrayList<NewItem> ConvertiToList(String msg)
    {
        ArrayList<NewItem> results = new ArrayList<NewItem>();
        String [] tab2;
        String [] tab1=msg.split(";,;,;");

        for(int i=0;i<tab1.length;i++){
            tab2=tab1[i].split("-,;,;");
            NewItem newsData = new NewItem();
            newsData.setId(tab2[0]);
            newsData.setTitre(tab2[1]);
            newsData.setArtist(tab2[2]);
            newsData.setVote(tab2[3]);
            results.add(newsData);
        }



        return  results;
    }
}
