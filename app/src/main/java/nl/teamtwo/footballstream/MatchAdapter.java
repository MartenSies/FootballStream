package nl.teamtwo.footballstream;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.pierry.simpletoast.SimpleToast;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by marten on 11/04/16.
 */
public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.MatchViewHolder>{

    List<Match> matches;
    int layout;
    HomeActivity ha;
    Context context;


    MatchAdapter(List<Match> matches, int layout, HomeActivity ha){
        this.matches = matches;
        this.layout = layout;
        this.ha = ha;
    }

    public static class MatchViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView awayTeam;
        TextView homeTeam;
        ImageView awayTeamIcon;
        ImageView homeTeamIcon;
        TextView date;
        TextView competitionLabel;
        TextView stadion;
        TextView homeScore;
        TextView awayScore;
        String savedMatches;
        List<String> savedMatchesList;

        MatchViewHolder(final View itemView, HomeActivity ha) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            awayTeam = (TextView)itemView.findViewById(R.id.away_team);
            homeTeam = (TextView) itemView.findViewById(R.id.home_team);
            awayTeamIcon = (ImageView) itemView.findViewById(R.id.away_team_icon);
            homeTeamIcon = (ImageView) itemView.findViewById(R.id.home_team_icon);

            homeScore = (TextView) itemView.findViewById(R.id.home_team_score);
            awayScore = (TextView) itemView.findViewById(R.id.away_team_score);

            date = (TextView) itemView.findViewById(R.id.date);
            stadion = (TextView) itemView.findViewById(R.id.stadion);
            competitionLabel = (TextView) itemView.findViewById(R.id.competitionLabel);
            final HomeActivity homeActivity = ha;

//            PreferenceManager.getDefaultSharedPreferences(itemView.getContext()).edit().putString("MATCHES", "").commit();

            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    savedMatches  = PreferenceManager.getDefaultSharedPreferences(v.getContext()).getString("MATCHES", "");
                    if (savedMatches.length() == 0) {
                        savedMatchesList = new ArrayList<>();
                    } else {
                        savedMatchesList = new ArrayList<>(Arrays.asList(savedMatches.split(",")));
                    }

                    String matchId = cv.getTag().toString();
                    if (savedMatchesList.contains(matchId)) {
                        List<String> newMatchIdList = new ArrayList<>();
                        for (int i = 0; i < savedMatchesList.size(); i++ ) {
                            String listId = savedMatchesList.get(i);

                            if (!matchId.equals(listId)) {
                                newMatchIdList.add(listId);
                            }
                        }
                        savedMatchesList = newMatchIdList;
                        SimpleToast.ok(v.getContext(), "Stopped casting", "");
                        cv.setCardBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.castNotActive));
                    } else {
                        savedMatchesList.add(matchId);
                        SimpleToast.info(v.getContext(), "Casting match", "{fa-check}");
                        cv.setCardBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.castActive));
                    }

                    savedMatches = TextUtils.join(",", savedMatchesList);
                    PreferenceManager.getDefaultSharedPreferences(v.getContext()).edit().putString("MATCHES", savedMatches).commit();
                    homeActivity.sendMatchesToChromecast();
                }
            });
        }

    }

    @Override
    public MatchViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(layout, viewGroup, false);
        MatchViewHolder pvh = new MatchViewHolder(v, ha);
        context = viewGroup.getContext();
        return pvh;
    }

    @Override
    public int getItemCount() {
        return matches.size();
    }

    MatchViewHolder mvh;

    @Override
    public void onBindViewHolder(MatchViewHolder matchViewHolder, int i) {
        mvh = matchViewHolder;

        Team homeTeam = matches.get(i).homeTeam;
        Team awayTeam = matches.get(i).awayTeam;

        String homeScore = matches.get(i).homeScore;
        String awayScore = matches.get(i).awayScore;

        Date matchDate = matches.get(i).date;
        DateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.ENGLISH);
        String date = format.format(matchDate);

        String stadion = matches.get(i).stadion;
        String competition = matches.get(i).competition;

        matchViewHolder.homeTeam.setText(homeTeam.name);
        matchViewHolder.awayTeam.setText(awayTeam.name);
        matchViewHolder.awayTeamIcon.setImageResource(awayTeam.iconId);
        matchViewHolder.homeTeamIcon.setImageResource(homeTeam.iconId);
        matchViewHolder.homeScore.setText(homeScore);
        matchViewHolder.awayScore.setText(awayScore);
        matchViewHolder.date.setText(date);
        matchViewHolder.stadion.setText(stadion);
        matchViewHolder.competitionLabel.setText(competition);
        matchViewHolder.competitionLabel.setBackgroundColor(getCompetitionColor(competition));
        matchViewHolder.cv.setTag(matches.get(i).id);

        //set color if matches saved
        String savedMatches  = PreferenceManager.getDefaultSharedPreferences(context).getString("MATCHES", "");
        List<String> savedMatchesList;
        if (savedMatches.length() == 0) {
            savedMatchesList = new ArrayList<>();
        } else {
            savedMatchesList = new ArrayList<>(Arrays.asList(savedMatches.split(",")));
        }

        if (savedMatchesList.contains(Integer.toString(matches.get(i).id))) {
            matchViewHolder.cv.setCardBackgroundColor(ContextCompat.getColor(context, R.color.castActive));
        } else {
            matchViewHolder.cv.setCardBackgroundColor(ContextCompat.getColor(context, R.color.castNotActive));
        }
    }

    public int getCompetitionColor(String competition) {
        int color;
        switch (competition) {
            case "Netherlands - Eredivisie":
                color = ContextCompat.getColor(context, R.color.colorEredivisie);
                break;
            case "England - Premier League":
                color = ContextCompat.getColor(context, R.color.colorPremierLeague);
                break;
            case "France - Ligue 1":
                color = ContextCompat.getColor(context, R.color.colorLigue1);
                break;
            case "Spain - Primera División":
                color = ContextCompat.getColor(context, R.color.colorPrimeraDivision);
                break;
            case "Portugal - Primeira Liga":
                color = ContextCompat.getColor(context, R.color.colorPrimeiraLiga);
                break;
            case "Germany - Bundesliga":
                color = ContextCompat.getColor(context, R.color.colorBundesliga);
                break;
            case "Italy - Serie A":
                color = ContextCompat.getColor(context, R.color.colorSerieA);
                break;
            default:
                color = 0;
                break;
        }
        return color;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


}