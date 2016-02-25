package masterigis.com.jukebox2_0.ModeConnecte.ParticipantCo;

/**
 * Created by Akretche on 20/01/2016.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import m1geii.com.jukebox2_0.R;
//Created by Hugues on 15/01/2016.

public class DeviceAdapter extends ArrayAdapter<Device>{

    public DeviceAdapter(Context context, List<Device> listeAppareils) {
        super(context, 0,listeAppareils);
    }

    class DeviceViewHolder{
        public TextView nomAppareil;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.p_row_device,parent,false);
        }

        DeviceViewHolder viewHolder=(DeviceViewHolder)convertView.getTag();

        if(viewHolder==null){
            viewHolder=new DeviceViewHolder();
            viewHolder.nomAppareil=(TextView)convertView.findViewById(R.id.device_name);
        }

        // Pour récupérer la position de l'item de la liste<device>
        Device appareil=getItem(position);

        // On rempli ensuite la vue
        viewHolder.nomAppareil.setText(appareil.getDeviceName());
        return convertView;
    }
}

