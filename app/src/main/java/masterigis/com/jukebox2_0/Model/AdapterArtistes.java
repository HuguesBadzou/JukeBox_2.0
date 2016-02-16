package masterigis.com.jukebox2_0.Model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import m1geii.com.jukebox2_0.R;

public class AdapterArtistes extends BaseAdapter {
    private ArrayList<Artiste> arrayArtists;   // ArrayList qui contiendra tous les row_albums
    private LayoutInflater artistInflater;   //

    // Constructeur artiste avec arguments
    public AdapterArtistes(Context c, ArrayList<Artiste> lesArtists){
        arrayArtists=lesArtists;
        artistInflater=LayoutInflater.from(c);
    }

    @Override
    public int getCount() {
        return arrayArtists.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayArtists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    static class ViewHolder{
        TextView nomArtiste;
        TextView nombreMusiquesArtiste;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if(convertView==null){
            convertView = artistInflater.inflate(R.layout.row_artistes,parent,false);
            holder=new ViewHolder();
            holder.nomArtiste = (TextView)convertView.findViewById(R.id.artist_name);
            holder.nombreMusiquesArtiste = (TextView)convertView.findViewById(R.id.nb_songs_artist);
            convertView.setTag(holder);
        }

        else{
            holder=(ViewHolder)convertView.getTag();
        }

        Artiste chansonCourrante = arrayArtists.get(position);
        holder.nomArtiste.setText(chansonCourrante.getNomArtiste());
        holder.nombreMusiquesArtiste.setText(chansonCourrante.getTotalChansons() + " chanson(s)");

        return convertView;
    }
}
