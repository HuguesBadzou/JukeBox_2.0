package masterigis.com.jukebox2_0.ModeConnecte.FragmentsCo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import m1geii.com.jukebox2_0.R;
import masterigis.com.jukebox2_0.ModeConnecte.Uploader_dossier_diffuseur;

public class Fragment_Contenu_Evenement_Participant extends Fragment {

    FloatingActionButton btnFloatingUploader;
    Fragment fragment;



    public Fragment_Contenu_Evenement_Participant() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View deposerMusic=inflater.inflate(R.layout.fragment_contenu_evenement_participant, container, false);

        btnFloatingUploader = (FloatingActionButton)deposerMusic.findViewById(R.id.bouton_flottant_uploader);

        btnFloatingUploader.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Uploader_dossier_diffuseur.class);
                // On ne permet que la sélection de UN seul fichier
               /* intent.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false);
                intent.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, false);
                intent.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_FILE);*/
                //intent.putExtra(FilePickerActivity.EXTRA_START_PATH, Uploader_dossier_diffuseur.class);
                startActivity(intent);
            }
        });
        return deposerMusic;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
