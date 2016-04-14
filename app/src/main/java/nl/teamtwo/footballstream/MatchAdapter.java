package nl.teamtwo.footballstream;

import android.content.Context;
import android.media.Image;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by marten on 11/04/16.
 */
public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.MatchViewHolder>{

    List<Match> matches;
    int layout;

    MatchAdapter(List<Match> matches, int layout){
        this.matches = matches;
        this.layout = layout;

    }


    public static class MatchViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView awayTeam;
        TextView homeTeam;
        ImageView awayTeamIcon;
        ImageView homeTeamIcon;

        MatchViewHolder(final View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            awayTeam = (TextView)itemView.findViewById(R.id.away_team);
            homeTeam = (TextView) itemView.findViewById(R.id.home_team);
            awayTeamIcon = (ImageView) itemView.findViewById(R.id.away_team_icon);
            homeTeamIcon = (ImageView) itemView.findViewById(R.id.home_team_icon);
        }
    }

    @Override
    public MatchViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(layout, viewGroup, false);
        MatchViewHolder pvh = new MatchViewHolder(v);

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

        matchViewHolder.homeTeam.setText(homeTeam.name);
        matchViewHolder.awayTeam.setText(awayTeam.name);
        matchViewHolder.awayTeamIcon.setImageResource(awayTeam.iconId);
        matchViewHolder.homeTeamIcon.setImageResource(homeTeam.iconId);

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


}