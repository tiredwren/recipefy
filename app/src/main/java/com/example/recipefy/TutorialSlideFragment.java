package com.example.recipefy;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TutorialSlideFragment extends Fragment {

    private int videoResource;

    public static TutorialSlideFragment newInstance(int videoResource) {
        TutorialSlideFragment fragment = new TutorialSlideFragment();
        Bundle args = new Bundle();
        args.putInt("videoResource", videoResource);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            videoResource = getArguments().getInt("videoResource");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tutorial, container, false);

        VideoView videoView = view.findViewById(R.id.videoView);
        videoView.setVideoURI(Uri.parse("android.resource://" + requireContext().getPackageName() + "/" + videoResource));
        videoView.start();

        return view;
    }
}
