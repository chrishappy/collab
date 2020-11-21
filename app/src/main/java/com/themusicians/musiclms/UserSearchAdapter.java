package com.themusicians.musiclms;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
public class UserSearchAdapter extends RecyclerView.Adapter<UserSearchAdapter.MyViewHolder>{

  ArrayList<User> list;
  public UserSearchAdapter(ArrayList<User> list){
    this.list = list;
  }

  // Function to tell the class about the Card view
  // which the data will be shown
  @NonNull
  @Override
  public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_card_holder,parent,false);
    return new MyViewHolder(view);
  }
  // Function to bind the view in Card view with data in
  // User class
  @Override
  public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
    holder.userName.setText(list.get(position).getName());
    holder.userEmail.setText(list.get(position).getEmail());
    holder.userRole.setText(list.get(position).getRole());
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
    //ListView userInstruments;
    public MyViewHolder(@NonNull View itemView) {
      super(itemView);
      userName = itemView.findViewById(R.id.searchName);
      userEmail = itemView.findViewById(R.id.searchEmail);
      userRole = itemView.findViewById(R.id.searchRole);
      addUser = itemView.findViewById(R.id.searchAdd);
      //userInstruments = itemView.findViewById(R.id.searchInstruments);
    }
  }

}
