package masterigis.com.jukebox2_0.Model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import m1geii.com.jukebox2_0.R;

/**
 * Created by naveck on 23/12/2015.
 */
public class AdapterListesdeMusiques extends ArrayAdapter<Chanson>  {

    Context context;
    public AdapterListesdeMusiques(Context context, ArrayList<Chanson> objects) {
        super(context,-1 ,objects);
        this.context = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;

        if(convertView==null){
            LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.listeitem_playlist, null);
        }
        else{
            view = convertView;
        }

        Chanson chanson = getItem(position);


        TextView titre = (TextView)view.findViewById(R.id.listItemPlaylist_titre);

        TextView artiste = (TextView)view.findViewById(R.id.listItemPlaylist_artiste);

       // TextView album = (TextView)view.findViewById(R.id.listItemPlaylist_album);

        titre.setText(chanson.getTitle());
        artiste.setText(chanson.getArtist());

        return view;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Chanson getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }




}
