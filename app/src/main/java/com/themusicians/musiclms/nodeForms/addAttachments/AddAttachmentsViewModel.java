package com.themusicians.musiclms.nodeForms.addAttachments;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * A sample view model for All Attachments, currently not used
 *
 * @author Nathan Tsai
 * @since Nov 23, 2020
 */
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
