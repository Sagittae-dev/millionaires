package com.example.milionerzy.game.party;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.milionerzy.R;
import com.example.milionerzy.adapters.TeamsScoreListAdapter;
import com.example.milionerzy.services.TeamsListService;

public class ScoreFragment extends Fragment {

    public ScoreFragment() {
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_score, container, false);

        TeamsListService teamsListService = new TeamsListService(getContext());
        TeamsScoreListAdapter teamsScoreListAdapter = new TeamsScoreListAdapter(teamsListService.getTeams());
        RecyclerView teamList_RecyclerView = view.findViewById(R.id.groupsListRecyclerView_ScoreFragment);
        teamList_RecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        teamList_RecyclerView.setAdapter(teamsScoreListAdapter);
        teamsScoreListAdapter.notifyDataSetChanged();
        return view;
    }
}