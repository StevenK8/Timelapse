package com.example.timelapse.ui.galerie;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.timelapse.R;

public class GalerieFragment extends Fragment {

    private GalerieViewModel galerieViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galerieViewModel =
                new ViewModelProvider(this).get(GalerieViewModel.class);
        View root = inflater.inflate(R.layout.fragment_galerie, container, false);
        final TextView textView = root.findViewById(R.id.text_galerie);
        galerieViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        return root;


    }
}