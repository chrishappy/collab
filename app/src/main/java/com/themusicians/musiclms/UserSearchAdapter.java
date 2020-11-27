package com.themusicians.musiclms;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.themusicians.musiclms.entity.Node.User;
import java.util.ArrayList;

/**
 * The adapter for the UserSearch page
 *
 * @author Jerome Lau
 * @since Nov 19, 2020
 */

// FirebaseRecyclerAdapter is a class provided by
// FirebaseUI. it provides functions to bind, adapt and show
// database contents in a Recycler View
public class UserSearchAdapter extends RecyclerView.Adapter<UserSearchAdapter.MyViewHolder> {

  ArrayList<User> list;
  ArrayList<String> userList = new ArrayList<>();
  FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
  User currUser = new User(currentUser.getUid());
  Context context;

  DatabaseReference userRef =
      FirebaseDatabase.getInstance()
          .getReference()
          .child("node__user")
          .child(currentUser.getUid())
          .child("addedUsers");

  /**
   * Initialize variables
   *
   * @param list ListView of added user list
   * @param c Context of page
   */
  public UserSearchAdapter(@NonNull ArrayList<User> list, Context c) {
    context = c;
    this.list = list;
    /** Adds prior listed Firebase addedUsers to the added user list */
    userRef.addChildEventListener(
        new ChildEventListener() {
          @Override
          public void onChildAdded(
              @NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            /** Checks if user ID is already added */
            boolean input = true;
            String value = snapshot.getValue(String.class);
            for (int i = 0; i < userList.size(); i++) {
              if (value.equals(userList.get(i))) {
                input = false;
              }
            }
            if (input) {
              userList.add(value);
            }
          }

          @Override
          public void onChildChanged(
              @NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

          @Override
          public void onChildRemoved(@NonNull DataSnapshot snapshot) {}

          @Override
          public void onChildMoved(
              @NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

          @Override
          public void onCancelled(@NonNull DatabaseError error) {}
        });
  }

  // Function to tell the class about the Card view
  // which the data will be shown
  @NonNull
  @Override
  public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.user_card_holder, parent, false);
    return new MyViewHolder(view);
  }
  // Function to bind the view in Card view with data in
  // User class
  @Override
  public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
    holder.userName.setText(list.get(position).getName());
    holder.userEmail.setText(list.get(position).getEmail());
    holder.userRole.setText(list.get(position).getRole());
    holder.addUser.setVisibility(View.VISIBLE);
    holder.onClick(position);
  }

  @Override
  public int getItemCount() {
    return list.size();
  }

  // Sub Class to create references of the views in Crad
  // view (here "person.xml")
  class MyViewHolder extends RecyclerView.ViewHolder {
    TextView userName, userEmail, userRole;
    Button addUser;

    public MyViewHolder(@NonNull View itemView) {
      super(itemView);
      userName = itemView.findViewById(R.id.searchName);
      userEmail = itemView.findViewById(R.id.searchEmail);
      userRole = itemView.findViewById(R.id.searchRole);
      addUser = itemView.findViewById(R.id.searchAdd);
    }

    // Add button onClick
    public void onClick(int position) {
      addUser.setOnClickListener(
          new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              callAddUser(position);
            }
          });
    }
  }

  /**
   * On add button, get users ID and adds it to addedUsers in Firebase
   *
   * @param position position of cardView
   */
  public void callAddUser(int position) {
    currUser
        .getEntityDatabase()
        .child(currentUser.getUid())
        .addListenerForSingleValueEvent(
            new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot snapshot) {
                /** Checks if added user is new */
                boolean isNew = true;
                for (int i = 0; i < userList.size(); i++) {
                  if (list.get(position).getId(). equals(userList.get(i))) {
                    isNew = false;
                  }
                }

                if (isNew == false) {
                  Toast.makeText(
                          context,
                          list.get(position).getName() + " is already added",
                          Toast.LENGTH_SHORT)
                      .show();
                } else if (list.get(position).getId().equals(currUser.getId())) {
                  Toast.makeText(context, "Cannot add self", Toast.LENGTH_SHORT).show();
                } else {
                  /** Saves added users in Firebase */
                  currUser = snapshot.getValue(User.class);
                  userList.add(list.get(position).getId());
                  currUser.setAddedUsers(userList);
                  currUser.save();
                  Toast.makeText(
                          context, list.get(position).getName() + " added", Toast.LENGTH_SHORT)
                      .show();
                }
              }

              @Override
              public void onCancelled(@NonNull DatabaseError error) {}
            });
  }
}
