package com.themusicians.musiclms.chat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.themusicians.musiclms.R;
import com.themusicians.musiclms.UserAddedAdapter;
import com.themusicians.musiclms.entity.Node.User;
import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {

  protected RecyclerView recyclerView;

  protected UserAddedAdapter userAddedAdapter;
  protected List<User> aUsers;

  FirebaseUser firebaseUser;
  DatabaseReference reference;

  protected List<String> userList;

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.fragment_chat, container, false);

    recyclerView = view.findViewById(R.id.recycler_view);
    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    userList = new ArrayList<>();

    reference = FirebaseDatabase.getInstance().getReference("chats");
    reference.addValueEventListener(
        new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            userList.clear();

            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
              ChatClass chat = snapshot.getValue(ChatClass.class);

              if (chat.getSender().equals(firebaseUser.getUid())) {
                userList.add(chat.getReceiver());
              }
              if (chat.getReceiver().equals(firebaseUser.getUid())) {
                userList.add(chat.getSender());
              }
            }
          }

          @Override
          public void onCancelled(@NonNull DatabaseError error) {}
        });
    // updateToken(FirebaseInstanceId.getInstance().getToken());
    return view;
  }

  protected void readChats() {
    aUsers = new ArrayList<>();

    reference = FirebaseDatabase.getInstance().getReference("Users");

    reference.addValueEventListener(
        new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            aUsers.clear();
            // display user from chat
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
              User user = snapshot.getValue(User.class);
              for (String id : userList) {
                if (user.getUid().equals(id)) {
                  if (aUsers.size() != 0) {
                    for (User user1 : aUsers) {
                      if (user.getUid().equals(user1.getUid())) {
                        aUsers.add(user);
                      }
                    }
                  } else {
                    aUsers.add(user);
                  }
                }
              }
            }

            userAddedAdapter = new UserAddedAdapter(getContext(), aUsers);
            recyclerView.setAdapter(userAddedAdapter);
          }

          @Override
          public void onCancelled(@NonNull DatabaseError error) {}
        });
  }
}
