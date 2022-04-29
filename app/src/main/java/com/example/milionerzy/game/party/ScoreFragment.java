package com.example.milionerzy.game.party;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.milionerzy.R;
import com.example.milionerzy.adapters.TeamsScoreListAdapter;

public class ScoreFragment extends Fragment {

    private RecyclerView teamList_RecyclerView; //not yet implemented

    public ScoreFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_score, container, false);
        teamList_RecyclerView = view.findViewById(R.id.groupsListRecyclerView_ScoreFragment);
//        TeamsScoreListAdapter teamsScoreListAdapter = new TeamsScoreListAdapter(getActivity(), getActivity);
        return view;
    }
}