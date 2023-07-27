package com.example.recipefy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

public class TutorialDialogFragment extends DialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_tutorial, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewPager2 viewPager2 = view.findViewById(R.id.viewPager);
        TutorialPagerAdapter pagerAdapter = new TutorialPagerAdapter(getTutorialSlides(), requireActivity());
        viewPager2.setAdapter(pagerAdapter);
    }

    private List<Fragment> getTutorialSlides() {
        List<Fragment> slides = new ArrayList<>();
        slides.add(TutorialSlideFragment.newInstance(R.raw.tutorial_slide_one_video)); // Replace with your video resource ID
        slides.add(TutorialSlideFragment.newInstance(R.raw.tutorial_slide_two_video)); // Replace with your video resource ID
        slides.add(TutorialSlideFragment.newInstance(R.raw.tutorial_slide_three_video)); // Replace with your video resource ID
        // Add more slides if needed

        return slides;
    }

    private static class TutorialPagerAdapter extends FragmentStateAdapter {

        private List<Fragment> tutorialSlides;

        public TutorialPagerAdapter(List<Fragment> tutorialSlides, FragmentActivity fragmentActivity) {
            super(fragmentActivity);
            this.tutorialSlides = tutorialSlides;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return tutorialSlides.get(position);
        }

        @Override
        public int getItemCount() {
            return tutorialSlides.size();
        }
    }
}
