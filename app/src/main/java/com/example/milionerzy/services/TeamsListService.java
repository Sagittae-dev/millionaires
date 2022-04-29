package com.example.milionerzy.services;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.milionerzy.model.Team;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class TeamsListService {
    private final Context context;
    private List<Team> teams = new ArrayList<>();

    public TeamsListService(Context context) {
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void getTeamsFromSP() {
        SharedPreferences sharedPreferences = ((Activity) context).getPreferences(Context.MODE_PRIVATE);
        Set<String> teamsSet = sharedPreferences.getStringSet(SetTeamsService.TEAMS_FROM_SP, new HashSet<>());

        teamsSet.forEach(t -> {
            Team team = new Team(t, 0);
            teams.add(team);
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Team addPointToTeam(Team team){
        int currentScore = team.getScore();
        team.setScore( currentScore + 1 );
        return team;
//        Optional<Team> optionalTeam = teams.stream().filter(t -> t.getTeamName().equals(team.getTeamName())).findAny();
    }

    public List<Team> getTeams(){
        return teams;
    }

}
