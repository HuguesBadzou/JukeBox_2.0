package masterigis.com.jukebox2_0.ModeNonConnecte.Participant;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import m1geii.com.jukebox2_0.R;

/**
 * Created by hamila on 21/01/2016.
 */
public class CustomListAdapter extends BaseAdapter {
    private ArrayList<NewItem> listData;
    private LayoutInflater layoutInflater;

    public CustomListAdapter(Context aContext, ArrayList<NewItem> listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
    }
    public String getArray(){
        String a="ee";
        return a;
    }
    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.p_list_row_layout, null);
            holder = new ViewHolder();
            holder.headlineView = (TextView) convertView.findViewById(R.id.titre_chanson);
            holder.reporterNameView = (TextView) convertView.findViewById(R.id.artiste_chanson);
            holder.reportedDateView = (TextView) convertView.findViewById(R.id.nombre_de_votes);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.headlineView.setText(listData.get(position).getTitre());
        holder.reporterNameView.setText("De, " + listData.get(position).getArtist());
        holder.reportedDateView.setText(listData.get(position).getVote());
        if(holder.reportedDateView.getText().toString().endsWith("*")==true)
            convertView.setBackgroundColor(Color.GREEN);
        else
            convertView.setBackgroundColor(Color.WHITE);



        return convertView;
    }

    static class ViewHolder {
        TextView headlineView;
        TextView reporterNameView;
        TextView reportedDateView;
    }

}