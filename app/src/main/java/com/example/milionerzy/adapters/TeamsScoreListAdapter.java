
package com.example.milionerzy.adapters;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.milionerzy.R;
import com.example.milionerzy.model.Team;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TeamsScoreListAdapter extends RecyclerView.Adapter<TeamsScoreListAdapter.ViewHolder> {
    private final List<Team> teams;

    public TeamsScoreListAdapter(List<Team> teams) {
        this.teams = teams;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.teams_item_list, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        List<Team> sortedTeamsList = teams.stream().sorted(Comparator.comparingInt(Team::getScore).reversed()).collect(Collectors.toList());
        holder.scoreTextView.setText(String.valueOf(sortedTeamsList.get(position).getScore()));
        holder.teamNameTextView.setText(sortedTeamsList.get(position).getTeamName());
    }

    @Override
    public int getItemCount() {
        return teams.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView teamNameTextView, scoreTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            teamNameTextView = itemView.findViewById(R.id.teamNameTextView_TeamsList);
            scoreTextView = itemView.findViewById(R.id.scoreTextView_TeamsList);
        }
    }
}
