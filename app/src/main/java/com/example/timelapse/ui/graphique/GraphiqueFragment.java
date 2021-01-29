package com.example.timelapse.ui.graphique;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.timelapse.R;

public class GraphiqueFragment extends Fragment {

    private GraphiqueViewModel graphiqueViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        graphiqueViewModel =
                new ViewModelProvider(this).get(GraphiqueViewModel.class);
        View root = inflater.inflate(R.layout.fragment_graphique, container, false);
        final TextView textView = root.findViewById(R.id.text_graphique);
        graphiqueViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}