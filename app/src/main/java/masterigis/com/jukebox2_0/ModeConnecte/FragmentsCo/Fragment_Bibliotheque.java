package masterigis.com.jukebox2_0.ModeConnecte.FragmentsCo;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import m1geii.com.jukebox2_0.R;

// Fragment gérant le sytème d'onglet de l'application
public class Fragment_Bibliotheque extends Fragment {

    public static TabLayout mtabLayout;
    public static ViewPager mviewPager;
    public static int nb_section = 3 ;

    public Fragment_Bibliotheque() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.fragment_bibliotheque,null);
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
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Pour modifier les titres des tabs en fonction de la position
            switch (position) {
                case 0:
                    return "Artistes";
                case 1:
                    return "Albums";
                case 2:
                    return "Chansons";
            }
            return null;
        }
    }

}
