package masterigis.com.jukebox2_0.Model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import m1geii.com.jukebox2_0.R;

public class AdapterAlbums extends BaseAdapter {

    private ArrayList<Album> arrayAlbums;   // ArrayList qui contiendra tous les row_albums
    private LayoutInflater albumInflater;   //

    // Constructeur album avec arguments
    public AdapterAlbums(Context c, ArrayList<Album> lesAlbums){
        arrayAlbums=lesAlbums;
        albumInflater=LayoutInflater.from(c);
    }

    @Override
    public int getCount() {
        return arrayAlbums.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayAlbums.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    static class ViewHolder{
        TextView titreAlbum;
        TextView artistView;
        TextView nombreMusiquesAlbum;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if(convertView==null){

            //map to album layout
            convertView = albumInflater.inflate(R.layout.row_albums,parent,false);
            holder = new ViewHolder();
            //get nom album and artist views
            holder.titreAlbum = (TextView) convertView.findViewById(R.id.titre_album);
            holder.artistView = (TextView) convertView.findViewById(R.id.album_artist);
            //set position as tag
            convertView.setTag(holder);
        }

        else{
            holder=(ViewHolder)convertView.getTag();
        }

        //get song using position
        Album chansonCourrante = arrayAlbums.get(position);
        //get nom album and artist strings
        holder.titreAlbum.setText(chansonCourrante.getTitle());
        holder.artistView.setText(chansonCourrante.getArtist());

        return convertView;
    }
}
