package nl.teamtwo.footballstream;

import android.content.Context;
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
public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.TeamViewHolder>{

    List<Team> teams;
    int layout;

    TeamAdapter(List<Team> teams, int layout){
        this.teams = teams;
        this.layout = layout;

    }


    public static class TeamViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView teamName;
        ImageView teamIcon;
        CheckBox teamCheckbox;
        String savedTeams;
        List<String> savedTeamsList;

        TeamViewHolder(final View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            teamName = (TextView)itemView.findViewById(R.id.team_name);
            teamIcon = (ImageView)itemView.findViewById(R.id.team_icon);
            teamCheckbox = (CheckBox)itemView.findViewById(R.id.checkBox);

//            PreferenceManager.getDefaultSharedPreferences(itemView.getContext()).edit().putString("TEAMS", "").commit();

            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (teamCheckbox.isChecked()) {
                        removeMatch();
                    } else {
                        addMatch();
                    }
                }
            });

            teamCheckbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (teamCheckbox.isChecked()) {
                        addMatch();
                    } else {
                        removeMatch();
                    }
                }
            });
        }

        public void addMatch() {
            savedTeams  = PreferenceManager.getDefaultSharedPreferences(itemView.getContext()).getString("TEAMS", "");
            if (savedTeams.length() == 0) {
                savedTeamsList = new ArrayList<String>();
            } else {
                savedTeamsList = new ArrayList<String>(Arrays.asList(savedTeams.split(",")));
            }

            savedTeamsList.add(teamCheckbox.getTag().toString());
            teamCheckbox.setChecked(true);

            savedTeams = TextUtils.join(",", savedTeamsList);
            PreferenceManager.getDefaultSharedPreferences(itemView.getContext()).edit().putString("TEAMS", savedTeams).commit();
        }

        public  void removeMatch() {
            savedTeams  = PreferenceManager.getDefaultSharedPreferences(itemView.getContext()).getString("TEAMS", "");
            if (savedTeams.length() == 0) {
                savedTeamsList = new ArrayList<String>();
            } else {
                savedTeamsList = new ArrayList<String>(Arrays.asList(savedTeams.split(",")));
            }

            List<String> newList = new ArrayList<String>();
            String idToRemove = teamCheckbox.getTag().toString();

            for (int i = 0; i < savedTeamsList.size(); i++ ) {
                String listId = savedTeamsList.get(i);
                if (!idToRemove.equals(listId)) {
                    newList.add(listId);
                }
            }

            savedTeamsList = newList;
            teamCheckbox.setChecked(false);

            savedTeams = TextUtils.join(",", savedTeamsList);
            PreferenceManager.getDefaultSharedPreferences(itemView.getContext()).edit().putString("TEAMS", savedTeams).commit();
        }


    }

    private Context context;

    @Override
    public TeamViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(layout, viewGroup, false);
        TeamViewHolder pvh = new TeamViewHolder(v);
        context = viewGroup.getContext();

        return pvh;
    }

    @Override
    public int getItemCount() {
        return teams.size();
    }

    @Override
    public void onBindViewHolder(TeamViewHolder teamViewHolder, int i) {
        teamViewHolder.teamName.setText(teams.get(i).name);
        teamViewHolder.teamIcon.setImageResource(teams.get(i).iconId);
        teamViewHolder.teamCheckbox.setTag(teams.get(i).teamId);
        teamViewHolder.teamCheckbox.setChecked(false);

        String savedTeams;
        List<String> savedTeamsList;

        String team_id = Integer.toString(teams.get(i).teamId);
        savedTeams  = PreferenceManager.getDefaultSharedPreferences(context).getString("TEAMS", "");
        if (savedTeams.length() == 0) {
            savedTeamsList = new ArrayList<String>();
        } else {
            savedTeamsList = new ArrayList<String>(Arrays.asList(savedTeams.split(",")));
        }

        if (savedTeamsList.contains(team_id)) {
            teamViewHolder.teamCheckbox.setChecked(true);
        }

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


}