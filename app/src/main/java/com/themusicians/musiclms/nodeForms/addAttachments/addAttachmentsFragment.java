package com.themusicians.musiclms.nodeForms.addAttachments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.themusicians.musiclms.R;

public class addAttachmentsFragment extends Fragment {

  private addAttachmentsViewModel homeViewModel;

  public View onCreateView(
      @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    homeViewModel = new ViewModelProvider(this).get(addAttachmentsViewModel.class);
    View root = inflater.inflate(R.layout.fragment_add_attachments, container, false);
    //        final TextView textView = root.findViewById(R.id.text_home);
    //        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
    //            @Override
    //            public void onChanged(@Nullable String s) {
    //                textView.setText(s);
    //            }
    //        });
    return root;
  }
}
