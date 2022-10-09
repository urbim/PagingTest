package com.example.pagingtest.ui.exampledetails;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pagingtest.R;
import com.example.pagingtest.databinding.FragmentExampleDetailsBinding;

public class ExampleDetailsFragment extends Fragment {

    private FragmentExampleDetailsBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentExampleDetailsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        ExampleDetailsFragmentArgs args = ExampleDetailsFragmentArgs.fromBundle(getArguments());

        binding.detailId.setText(args.getUser().getId().toString());
        binding.detailName.setText(args.getUser().getName());

        return view;
    }
}