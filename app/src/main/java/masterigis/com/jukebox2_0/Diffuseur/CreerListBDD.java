package masterigis.com.jukebox2_0.Diffuseur;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import masterigis.com.jukebox2_0.Model.BDAdapter;

// Classe de création de la liste à partir des données contenu dans la BD


public class CreerListBDD {
    BDAdapter base;
    Cursor curseurBase;
    Context c ;

    public CreerListBDD(Context c){
        this.c = c ;
    }
    public ArrayList<NewItem>  getList(String nomList) {
        ArrayList<NewItem> results = new ArrayList<>();
        base = new BDAdapter(c);
        base.ouvrirBase();
        curseurBase = base.ObtenirTousLesTitres(nomList);

        if (curseurBase != null && curseurBase.moveToFirst()) {
            do {
                NewItem newsData = new NewItem();
                newsData.setVote("Vote : 0");
                //recuperation le nom du music
                String name = curseurBase.getString(1);
                newsData.setTitre(name);
                //recuperation le nom d'artist
                String art = curseurBase.getString(2);
                newsData.setArtist(art);
                //recuperation identifiant du music
                String ids = curseurBase.getString(3);
                newsData.setId(ids);
                results.add(newsData);
            }
            while (curseurBase.moveToNext());

            curseurBase.close();
        }
        return results ;
    }
}
