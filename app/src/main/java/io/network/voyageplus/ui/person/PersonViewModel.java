package io.network.voyageplus.ui.person;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PersonViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public PersonViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is person fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}