package com.themusicians.musiclms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.themusicians.musiclms.Notifications.Data;

public class UserAnalysis extends AppCompatActivity {

  DatabaseReference reference;

  FirebaseUser currentUser;
  GraphView graphView;
  LineGraphSeries series;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.user_analysis);

    graphView = findViewById(R.id.graphView);
    series = new LineGraphSeries();
    graphView.addSeries(series);
    currentUser = FirebaseAuth.getInstance().getCurrentUser();

    reference = FirebaseDatabase.getInstance().getReference().child("node__user").child(currentUser.getUid()).child("chartTable");

    //setListeners();

  }

  private void setListeners() {
    String id = reference.push().getKey();

    reference.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        int x = (int) snapshot.getChildrenCount();
        int y = 2;
        PointValue pointValue = new PointValue(x,y);
        reference.child(id).setValue(pointValue);
      }

      @Override
      public void onCancelled(@NonNull DatabaseError error) {

      }
    });
  }

  @Override
  protected void onStart() {
    super.onStart();

    reference.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        DataPoint[] dp = new DataPoint[(int) snapshot.getChildrenCount()];
        int index = 0;

        for(DataSnapshot myDataSnapshot : snapshot.getChildren()){
          PointValue pointValue = myDataSnapshot.getValue(PointValue.class);
          dp[index] = new DataPoint(pointValue.getxValue(), pointValue.getyValue());
          index ++;
        }

        series.resetData(dp);
      }

      @Override
      public void onCancelled(@NonNull DatabaseError error) {

      }
    });
  }
}