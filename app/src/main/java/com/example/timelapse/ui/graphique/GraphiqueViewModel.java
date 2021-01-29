package com.example.timelapse.ui.graphique;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GraphiqueViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public GraphiqueViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("c'est ici que seront affichés les graphiques (c'est bo le java)");
    }

    public LiveData<String> getText() {
        return mText;
    }
}