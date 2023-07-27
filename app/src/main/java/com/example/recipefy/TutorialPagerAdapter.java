package com.example.recipefy;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

public class TutorialPagerAdapter extends FragmentStateAdapter {

    private List<TutorialSlideFragment> slideFragments;

    public TutorialPagerAdapter(List<TutorialSlideFragment> slideFragments, FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        this.slideFragments = slideFragments;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return slideFragments.get(position);
    }

    @Override
    public int getItemCount() {
        return slideFragments.size();
    }
}
