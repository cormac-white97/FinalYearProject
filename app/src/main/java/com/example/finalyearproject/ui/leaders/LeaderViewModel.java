package com.example.finalyearproject.ui.leaders;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LeaderViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public LeaderViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is send fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}