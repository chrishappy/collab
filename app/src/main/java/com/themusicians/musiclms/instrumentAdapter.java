package com.themusicians.musiclms;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.themusicians.musiclms.entity.Node.User;

import java.util.List;

// FirebaseRecyclerAdapter is a class provided by
// FirebaseUI. it provides functions to bind, adapt and show
// database contents in a Recycler View
public class instrumentAdapter extends FirebaseRecyclerAdapter<User, instrumentAdapter.instrumentsViewholder> {

  public instrumentAdapter(
    @NonNull FirebaseRecyclerOptions<User> options)
  {
    super(options);
  }

  // Function to bind the view in Card view(here
  // "person.xml") iwth data in
  // model class(here "person.class")
  @Override
  protected void
  onBindViewHolder(@NonNull instrumentsViewholder holder,
                   int position, @NonNull User model)
  {

    holder.instrument.setText(model.getInstrument());
  }

  // Function to tell the class about the Card view (here
  // "person.xml")in
  // which the data will be shown
  @NonNull
  @Override
  public instrumentsViewholder
  onCreateViewHolder(@NonNull ViewGroup parent,
                     int viewType)
  {
    View view
      = LayoutInflater.from(parent.getContext())
      .inflate(R.layout.user_instrument, parent, false);
    return new instrumentAdapter.instrumentsViewholder(view);
  }

  // Sub Class to create references of the views in Crad
  // view (here "person.xml")
  class instrumentsViewholder
    extends RecyclerView.ViewHolder {
    TextView instrument;
    public instrumentsViewholder(@NonNull View itemView)
    {
      super(itemView);

      instrument = itemView.findViewById(R.id.textView);
    }
  }
}