package nl.teamtwo.footballstream;

import android.content.Intent;
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
public class RVAdapter extends RecyclerView.Adapter<RVAdapter.PersonViewHolder>{

    List<Team> teams;
    int layout;

    RVAdapter(List<Team> teams, int layout){
        this.teams = teams;
        this.layout = layout;
    }


    public static class PersonViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView teamName;
        ImageView teamIcon;
        CheckBox teamCheckbox;
        String savedTeams;
        List<String> savedTeamsList;

        PersonViewHolder(final View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            teamName = (TextView)itemView.findViewById(R.id.team_name);
            teamIcon = (ImageView)itemView.findViewById(R.id.team_icon);
            teamCheckbox = (CheckBox)itemView.findViewById(R.id.checkBox);

            PreferenceManager.getDefaultSharedPreferences(itemView.getContext()).edit().putString("TEAMS", "").commit();

//            savedTeams  = PreferenceManager.getDefaultSharedPreferences(itemView.getContext()).getString("TEAMS", "");
//            savedTeamsList = new ArrayList<String>(Arrays.asList(savedTeams.split(",")));


            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    savedTeams  = PreferenceManager.getDefaultSharedPreferences(view.getContext()).getString("TEAMS", "");
                    if (savedTeams.length() == 0) {
                        savedTeamsList = new ArrayList<String>();
                    } else {
                        savedTeamsList = new ArrayList<String>(Arrays.asList(savedTeams.split(",")));
                    }

                    if (teamCheckbox.isChecked()) {
                        savedTeamsList.remove(teamCheckbox.getTag().toString());
                        teamCheckbox.setChecked(false);
                        Log.d("index", savedTeamsList.indexOf(teamCheckbox.getTag().toString()));
                        Log.d("saved_teams", savedTeamsList.toString());
                    } else {
                        savedTeamsList.add(teamCheckbox.getTag().toString());
                        teamCheckbox.setChecked(true);
                        Log.d("saved_teams", savedTeamsList.toString());
                    }

                    PreferenceManager.getDefaultSharedPreferences(view.getContext()).edit().putString("TEAMS", TextUtils.join(",", savedTeamsList)).commit();
                }
            });
        }
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(layout, viewGroup, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public int getItemCount() {
        return teams.size();
    }

    @Override
    public void onBindViewHolder(PersonViewHolder personViewHolder, int i) {
        personViewHolder.teamName.setText(teams.get(i).name);
        personViewHolder.teamIcon.setImageResource(teams.get(i).iconId);
        personViewHolder.teamCheckbox.setTag(teams.get(i).teamId);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


}