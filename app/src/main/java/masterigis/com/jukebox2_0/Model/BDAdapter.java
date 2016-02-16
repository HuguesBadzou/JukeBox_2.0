package masterigis.com.jukebox2_0.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by naveck on 11/01/2016.
 */

public class BDAdapter {

    static final String TAG             = "BDAdapter";

    //Colonnes table de liste de listes
    //static final String Colonne_Idl     = "l_id";
    static final String NomListe        = "nom_l";
    static final String NomTable_l      = "ListeDeListes";



    //Colonnes table de liste de piste
    static final String Colonne_Idm     = "m_id";
    static final String Titre           = "nom_t";
    static final String Artiste         = "artiste";
    static final String NomTable_C      = "Tablechansons";
    static final String NombreDeVotes   = "NbVotes";
    static final String Id_liste        = "liste_id";
    static final String Id_chanson       = "identifiant_chanson";


    static  String DB_NAME = "JBoxBdD.db";
    static  int DB_VERSION = 8;


    static final String CREATE_TBL = "CREATE TABLE "+ NomTable_l    + "("
                                                    //+ Colonne_Idl   + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                                                    + NomListe      + " TEXT NOT NULL UNIQUE PRIMARY KEY"
                                                    +");";

    static final String CREATE_TBM = "CREATE TABLE "+ NomTable_C    + "("
                                                    + Colonne_Idm   + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                                                    + Titre         + " TEXT NOT NULL,"
                                                    + Artiste       + " TEXT NOT NULL,"
                                                    + Id_chanson    + " TEXT NOT NULL,"
                                                    + NombreDeVotes + " INTEGER,"
                                                    + Id_liste      + " TEXT NOT NULL,"
                                                    + "FOREIGN KEY ("+Id_liste+") REFERENCES "+NomTable_l+" ("+NomListe+") ON DELETE CASCADE);";

    final Context c;
    SQLiteDatabase db;
    BDHelper helper;


    public BDAdapter(Context ctx) {
        this.c = ctx ;
        helper = new BDHelper(c) ;
    }


    private static class BDHelper extends SQLiteOpenHelper {
        public BDHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);

        }


        @Override
        public void onCreate(SQLiteDatabase db) {

            try{

               db.execSQL(CREATE_TBL);
               db.execSQL(CREATE_TBM);

            }
            catch (SQLException e) {

                e.printStackTrace();

            }
        }

        @Override
        public void onOpen(SQLiteDatabase db) {
            super.onOpen(db);
            db.execSQL("PRAGMA foreign_keys = ON");
        }


        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            Log.w(TAG, "Mise à jour de la table de base données");
            db.execSQL("DROP TABLE IF EXISTS " + NomTable_C);
            db.execSQL("DROP TABLE IF EXISTS " + NomTable_l);

            onCreate(db);
        }
    }

    public BDAdapter ouvrirBase() {
        try {
                db = helper.getWritableDatabase();

        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return this;
    }

    public void fermer(){
        helper.close();
    }

    public long ajoutLdl(String name)
    {

        try{
            ContentValues cv = new ContentValues();
            cv.put(NomListe, name);

            BDHelper helper = new BDHelper(c);
            SQLiteDatabase db = helper.getWritableDatabase();
            //return db.insert(NomTable_l, Colonne_Idl, cv);
            return db.insert(NomTable_l, null, cv);
        }catch(SQLException e){
            e.printStackTrace();
        }
        return 0;
    }

    public long ajoutLdm(String titre, String artiste, String liste, String idchanson)
    {

        try{
            ContentValues cv = new ContentValues();
            cv.put(Titre, titre);
            cv.put(Artiste, artiste);
            cv.put(Id_liste,liste);
            cv.put(Id_chanson,idchanson);


            BDHelper helper = new BDHelper(c);
            SQLiteDatabase db = helper.getWritableDatabase();
            return db.insert(NomTable_C, Colonne_Idm, cv);
        }catch(SQLException e){
            e.printStackTrace();
        }
        return 0;
    }


   public Cursor ObtenirToutesLesListes()
    {
        SQLiteDatabase db = helper.getReadableDatabase();
        String[] colonnes = {NomListe};
        Cursor cursor = db.query(NomTable_l, colonnes, null, null, null, null, null );

        return cursor;
    }

    // Curseur permettant d'obtenir tous les titre de la liste de lecture selectionnée
    public Cursor ObtenirTousLesTitres(String id)
    {

        SQLiteDatabase db = helper.getReadableDatabase();
        String[] colonnes = {Colonne_Idm, Titre, Artiste, Id_chanson};  // Colonnes qu'on veut  obtenir

        // Table de chanson je veux les colonnes Id de la chanson dans la liste, son titre, son artiste et son id mp3 pour la lecture
        Cursor cursor = db.query(NomTable_C, colonnes, Id_liste + "=? " , new String[] {id}, null, null, null );

        return cursor;
    }


    public void resetTables_L() {
        try{

            SQLiteDatabase db = helper.getWritableDatabase();
            // Delete All Rows
            db.delete(NomTable_l, null, null);
            db.close();

        }
       catch (SQLException e){
           e.printStackTrace();
       }
    }

    public void resetTables_M() {
        try{
            SQLiteDatabase db = helper.getWritableDatabase();
            // Delete All Rows
            db.delete(NomTable_C, null, null);
            db.close();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void SupprimerLdl (String nom){

        SQLiteDatabase db = helper.getWritableDatabase();

        db.delete(NomTable_l, NomListe+ "=?" , new String[] {nom});

    }


    // Il faut la supprimer en fonction de la clé primaire
    public void Supprimerm (String id){
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete(NomTable_C,Colonne_Idm + "=?" ,new String[] {id});
    }

}
