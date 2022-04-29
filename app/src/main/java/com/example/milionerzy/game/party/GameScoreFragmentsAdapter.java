package com.example.milionerzy.game.party;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class GameScoreFragmentsAdapter extends FragmentStateAdapter {

    public GameScoreFragmentsAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1) {
            return new ScoreFragment();
        }
        return new GameFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
