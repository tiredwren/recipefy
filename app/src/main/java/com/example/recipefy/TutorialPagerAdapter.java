package com.example.recipefy;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TutorialPagerAdapter extends FragmentPagerAdapter {

    public TutorialPagerAdapter(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new TutorialFragment("Add Items", "Instructions on how to add items.");
            case 1:
                return new TutorialFragment("Delete Items", "Instructions on how to delete items.");
            case 2:
                return new TutorialFragment("Select Items", "Instructions on how to select items.");
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3; // Three tutorial screens
    }
}
