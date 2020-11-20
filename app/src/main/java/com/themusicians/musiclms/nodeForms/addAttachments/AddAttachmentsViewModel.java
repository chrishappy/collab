package com.themusicians.musiclms.nodeForms.addAttachments;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddAttachmentsViewModel extends ViewModel {

  private MutableLiveData<String> mText;

  public AddAttachmentsViewModel() {
    mText = new MutableLiveData<>();
    mText.setValue("This is home fragment");
  }

  public LiveData<String> getText() {
    return mText;
  }
}
