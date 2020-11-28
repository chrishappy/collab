package com.themusicians.musiclms.chat;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.themusicians.musiclms.R;
import com.themusicians.musiclms.entity.Node.User;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {

  public static final int MSG_TYPE_LEFT = 0;
  public static final int MSG_TYPE_RIGHT = 1;
  private List<ChatClass> list;
  FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
  Context context;

  /**
   * Initialize variables
   *
   * @param list ListView of added user list
   * @param c Context of page
   */
  public ChatAdapter(@NonNull List<ChatClass> list, Context c) {
    this.context = c;
    this.list = list;
  }

  // Function to tell the class about the Card view
  // which the data will be shown
  @NonNull
  @Override
  public ChatAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view;
    if(viewType == MSG_TYPE_RIGHT){
      view = LayoutInflater.from(context).inflate(R.layout.chat_item_right, parent, false);
    } else {
      view = LayoutInflater.from(context).inflate(R.layout.chat_item_left, parent, false);
    }
    return new MyViewHolder(view);

  }
  // Function to bind the view in Card view with data in
  // User class
  @Override
  public void onBindViewHolder(@NonNull ChatAdapter.MyViewHolder holder, int position) {

    ChatClass chat = list.get(position);
    holder.showMessage.setText(chat.getMessage());
  }

  @Override
  public int getItemCount() {
    return list.size();
  }

  // Sub Class to create references of the views in Card
  // view (here "person.xml")
  class MyViewHolder extends RecyclerView.ViewHolder {
    TextView showMessage;

    public MyViewHolder(@NonNull View itemView) {
      super(itemView);
      showMessage = itemView.findViewById(R.id.showMessage);
    }
  }

  @Override
  public int getItemViewType(int position) {
    if(list.get(position).getSender().equals(currentUser.getUid())){
      return MSG_TYPE_RIGHT;
    }else{
      return MSG_TYPE_LEFT;
    }
  }
}
