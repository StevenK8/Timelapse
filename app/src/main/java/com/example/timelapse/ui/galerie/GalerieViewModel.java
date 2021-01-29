package com.example.timelapse.ui.galerie;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GalerieViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public GalerieViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("la vue galerie pour afficher des images");
    }

    public LiveData<String> getText() {
        return mText;
    }
}