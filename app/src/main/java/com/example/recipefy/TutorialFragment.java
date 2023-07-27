package com.example.recipefy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TutorialFragment extends Fragment {

    private String title;
    private String description;

    public TutorialFragment(String title, String description) {
        this.title = title;
        this.description = description;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tutorial, container, false);

        TextView titleTextView = rootView.findViewById(R.id.titleTextView);
        TextView descriptionTextView = rootView.findViewById(R.id.descriptionTextView);

        titleTextView.setText(title);
        descriptionTextView.setText(description);

        return rootView;
    }
}
