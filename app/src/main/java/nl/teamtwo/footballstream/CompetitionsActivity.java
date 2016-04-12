package nl.teamtwo.footballstream;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CompetitionsActivity extends AppCompatActivity implements DataInterface {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private JSONObject competitions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competitions);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);

        //Get football competitions
        updateFootballCompetitions();
    }

    private void updateFootballCompetitions() {
        new FootballStreamTask(this, "/competitions").execute();
    }

    @Override
    public void notify(JSONObject data) {
        this.competitions = data;
        createTabs();
    }

    public void createTabs() {

        try {
            JSONArray competitionArray = this.competitions.getJSONArray("competitions");

            // Create the adapter that will return a fragment for each of the three
            // primary sections of the activity.
            mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), competitionArray.length());

            // Set up the ViewPager with the sections adapter.
            TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
            for(int i = 0; i < competitionArray.length(); i++) {
                JSONObject competition = competitionArray.getJSONObject(i);

                String region = competition.get("region").toString();
                String name = competition.get("name").toString();

                String competitionName;
                if (region.equals("International")) {
                    competitionName = name;
                } else {
                    competitionName = region + " - " + name;
                }

                tabLayout.addTab(tabLayout.newTab().setText(competitionName));

            }
            tabLayout.setTabGravity(TabLayout.MODE_SCROLLABLE);

            mViewPager = (ViewPager) findViewById(R.id.container);
            mViewPager.setAdapter(mSectionsPagerAdapter);
            mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        } catch (JSONException e) {

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_competitions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startActivity(new Intent(this, HomeActivity.class));
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private JSONArray teamsArray;
        private List<Team> teams = new ArrayList<>();
        private RVAdapter adapter = new RVAdapter(teams);

        public PlaceholderFragment() {
        }


        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber, JSONArray teams) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putString("teams",teams.toString());
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            teams.clear();
            try {
                teamsArray = new JSONArray(getArguments().getString("teams"));
                for(int i = 0; i < teamsArray.length(); i++) {
                    JSONObject team = teamsArray.getJSONObject(i);

                    String id = team.get("id").toString();
                    String name = team.get("name").toString();
                    int logo = getResources().getIdentifier(name.replaceAll(" ", "_").replaceAll("'", "").toLowerCase(), "drawable", getActivity().getPackageName());

                    teams.add(new Team(name, logo));
                    adapter.notifyItemInserted(teams.size()-1);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            View rootView = inflater.inflate(R.layout.fragment_competitions, container, false);
            RecyclerView rv = (RecyclerView)rootView.findViewById(R.id.rv);
            rv.setHasFixedSize(true);

            LinearLayoutManager llm = new LinearLayoutManager(getActivity());
            rv.setLayoutManager(llm);

            rv.setAdapter(adapter);

            return rootView;
        }




    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private int count;
        private List<Fragment> fragments = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager fm, int count) {
            super(fm);
            this.count = count;
            createFragments();
        }

        public void createFragments() {
            try {
                JSONArray competitionsArray = competitions.getJSONArray("competitions");
                for(int i = 0; i < competitionsArray.length(); i++) {
                    JSONObject competition = competitionsArray.getJSONObject(i);
                    JSONArray teams = competition.getJSONArray("teams");
                    Fragment fragment = PlaceholderFragment.newInstance(i + 1, teams);
                    fragments.add(fragment);
                }
            } catch (JSONException e) {

            }

        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return count;
        }

    }
}
