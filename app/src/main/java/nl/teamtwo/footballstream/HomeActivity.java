package nl.teamtwo.footballstream;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DataInterface {

    private List<Match> matches = new ArrayList<>();
    private MatchAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, CompetitionsActivity.class));
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        RecyclerView rv = (RecyclerView)findViewById(R.id.rv);
        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        adapter = new MatchAdapter(matches, R.layout.match_card);
        rv.setAdapter(adapter);

    }

    @Override
    public void notify(JSONObject data) {

        try {
            JSONArray matchesArray = data.getJSONArray("matches");
            List<Integer> matchIdList = new ArrayList<Integer>();
            for (int i = 0; i < matchesArray.length(); i++) {
                JSONObject match = matchesArray.getJSONObject(i);

                int matchId = match.getInt("id");
                String date = match.getString("date_start");
                String stadion = match.getString("venue");
                String competition = match.getString("competition");

                JSONObject homeTeamObject = match.getJSONObject("home_team");
                JSONObject awayTeamObject = match.getJSONObject("away_team");

                String homeTeamName = homeTeamObject.getString("name");
                Integer homeTeamId = homeTeamObject.getInt("id");
                int homeTeamLogo = getResources().getIdentifier(homeTeamName.replaceAll(" ", "_").replaceAll("'", "").toLowerCase(), "drawable", getApplication().getPackageName());


                String awayTeamName = awayTeamObject.getString("name");
                Integer awayTeamId = awayTeamObject.getInt("id");
                int awayTeamLogo = getResources().getIdentifier(awayTeamName.replaceAll(" ", "_").replaceAll("'", "").toLowerCase(), "drawable", getApplication().getPackageName());


                Team homeTeam = new Team(homeTeamName, homeTeamLogo, homeTeamId);
                Team awayTeam = new Team(awayTeamName, awayTeamLogo, awayTeamId);

                if (!matchIdList.contains(matchId)) {
                    matchIdList.add(matchId);
                    matches.add(new Match(homeTeam, awayTeam, date, stadion, competition));
                }
                
                Collections.sort(matches);
                adapter.notifyItemInserted(matches.size()-1);

            }
        } catch (JSONException e) {

        }

    }

    private void updateFootballMatches(String url) {
        new FootballStreamTask(this, url).execute();
    }


    @Override
    protected void onStart() {
        super.onStart();

        String followedTeams = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("TEAMS", "");
        List<String> followedTeamsList;
        if (followedTeams.length() == 0) {
            followedTeamsList = new ArrayList<String>();
        } else {
            followedTeamsList = new ArrayList<String>(Arrays.asList(followedTeams.split(",")));
        }

        Log.d("teams", followedTeams.toString());

        matches.clear();
        updateFootballMatches("/matches?team_id=" + TextUtils.join(",", followedTeamsList));


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar team_card clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view team_card clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
