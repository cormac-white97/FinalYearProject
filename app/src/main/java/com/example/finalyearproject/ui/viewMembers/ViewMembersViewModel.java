package com.example.finalyearproject.ui.viewMembers;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ViewMembersViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ViewMembersViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}