package com.example.milionerzy.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.milionerzy.model.Team;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiresApi(api = Build.VERSION_CODES.N)
public class TeamsListService {
    private final Context context;
    private List<Team> teams;

    public TeamsListService(Context context) {
        this.context = context;
        getTeamsFromSP();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void getTeamsFromSP() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SetTeamsService.TEAMS_FROM_SP, Context.MODE_PRIVATE);
        Set<String> teamsSet = sharedPreferences.getStringSet(SetTeamsService.TEAMS_FROM_SP, new HashSet<>());

        teams = convertFromStringSetToTeamList(teamsSet);
    }

    private List<Team> convertFromStringSetToTeamList(Set<String> teamsSet) {
        List<String> tmpList = new ArrayList<>(teamsSet);
        List<Team> teamsList = new ArrayList<>();
        for (byte i = 0; i < tmpList.size(); i++) {
            Team team = new Team(i, tmpList.get(i), 0);
            teamsList.add(team);
        }
        Log.i("TeamsListService", teamsList.toString());
        return teamsList;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Team addPointToTeam(Team team) {
        int currentScore = team.getScore();
        team.setScore(currentScore + 1);
        return team;
    }

    public List<Team> getTeams() {
        return teams;
    }

}