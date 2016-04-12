package nl.teamtwo.footballstream;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by marten on 11/04/16.
 */
public class RVAdapter extends RecyclerView.Adapter<RVAdapter.PersonViewHolder>{

    List<Team> teams;

    RVAdapter(List<Team> teams){
        this.teams = teams;
    }


    public static class PersonViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView teamName;
        ImageView teamIcon;

        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            teamName = (TextView)itemView.findViewById(R.id.team_name);
            teamIcon = (ImageView)itemView.findViewById(R.id.team_icon);
        }
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
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
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


}