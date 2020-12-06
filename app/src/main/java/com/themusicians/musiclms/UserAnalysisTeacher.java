package com.themusicians.musiclms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.themusicians.musiclms.entity.Node.Assignment;
import com.themusicians.musiclms.entity.Node.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.floor;

/**
 * Display the users submission analysis
 *
 * @contributors Nathan Tsai
 * @author Jerome Lau
 * @since Dec 3, 2020
 *
 */
public class UserAnalysisTeacher extends AppCompatActivity {

  DatabaseReference reference;

  FirebaseUser currentUser;
  GraphView graphView;
  LineGraphSeries series;
  User currUser;
  long x;
  long y;
  ArrayList<Long> store;
  Map<Long, Long> xs;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.user_analysis);

    graphView = findViewById(R.id.graphView);
    series = new LineGraphSeries();
    graphView.addSeries(series);
    GridLabelRenderer glr = graphView.getGridLabelRenderer();
    glr.setPadding(32);
    currentUser = FirebaseAuth.getInstance().getCurrentUser();
    currUser = new User(currentUser.getUid());
    xs = new HashMap<>();
    store = new ArrayList<>();

    reference = FirebaseDatabase.getInstance().getReference().child("node__user").child(currentUser.getUid()).child("chartTable");

    reference.removeValue();
    setListeners();
  }

  /** Checks Firebase for assignments the teacher made
   *  Gets time difference from submission and due date and counts how many
   *  Stores in users chartTable
   */
  private void setListeners() {
    DatabaseReference aRef = FirebaseDatabase.getInstance().getReference().child("node__assignment");

    aRef.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        for(DataSnapshot ds : snapshot.getChildren()){
          Assignment assignment = ds.getValue(Assignment.class);
          if(currentUser.getUid().equals(assignment.getUid()) && assignment.getAssignmentCompleteTime() != null){
            y = (long)assignment.getAssignmentCompleteTimeLong() - assignment.getDueDate();

            y = (long) (floor(((y/1000)/60)/60)/24);

            store.add(y);

          }
        }

        Collections.sort(store);

        for(int i = 0; i < store.size(); i ++){
          long count = 1;
          for(int j = i+1; j < store.size(); j++){
            if(store.get(i) == store.get(j)){
              count++;
            }
          }
          if(xs.get(store.get(i)) == null) {
            xs.put(store.get(i), count);
          }
        }

        for(int i = 0; i < xs.size(); i ++){
          PointValue pointValue = new PointValue(store.get(i), xs.get(store.get(i)));
          String id = reference.push().getKey();
          reference.child(id).setValue(pointValue);
        }
      }

      @Override
      public void onCancelled(@NonNull DatabaseError error) {

      }
    });
  }

  /** Fetches data from Firebase and displays it in a graph */
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
          index++;
        }

        series.resetData(dp);
      }

      @Override
      public void onCancelled(@NonNull DatabaseError error) {

      }
    });
  }

  /** Displays graph key */
  public void readKey(View view) {
    AlertDialog.Builder key = new AlertDialog.Builder(UserAnalysisTeacher.this);
    key.setTitle(R.string.key);
    key.setMessage(R.string.teacher_key);

    AlertDialog showKey = key.create();
    showKey.show();
  }

}