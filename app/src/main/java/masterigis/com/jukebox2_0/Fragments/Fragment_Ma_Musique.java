package masterigis.com.jukebox2_0.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import m1geii.com.jukebox2_0.R;

// Fragment gérant le sytème d'onglet de l'application
public class Fragment_Ma_Musique extends Fragment {

    public static TabLayout mtabLayout;
    public static ViewPager mviewPager;
    public static int nb_section = 4 ;



    public Fragment_Ma_Musique(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /*@Override
    public void onResume(){
        super.onResume();
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("Ma Musique");

        View v =  inflater.inflate(R.layout.fragment_ma_musique,null);
        mtabLayout = (TabLayout) v.findViewById(R.id.tabs_artistes);
        mviewPager = (ViewPager) v.findViewById(R.id.pager);

        mviewPager.setAdapter(new SectionsPagerAdapter(getChildFragmentManager()));

         mtabLayout.setupWithViewPager(mviewPager);

        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position) {
                case 0:
                    return new Fragment_Artistes();
                case 1:
                    return new Fragment_Albums();
                case 2:
                    return new Fragment_Chansons();
                case 3:
                    return new Fragment_Listes_de_Lecture();
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return nb_section;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Pour modifier les titres des tabs en fonction de la position
            switch (position) {
                case 0:
                    return "ARTISTES";
                case 1:
                    return "ALBUMS";
                case 2:
                    return "CHANSONS";
                case 3:
                    return "PLAYLISTS";
            }
            return null;
        }
    }

}
