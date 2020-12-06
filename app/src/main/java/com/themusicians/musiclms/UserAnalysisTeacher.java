package com.themusicians.musiclms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

import static java.lang.Math.floor;

public class UserAnalysisTeacher extends AppCompatActivity {

  DatabaseReference reference;

  FirebaseUser currentUser;
  GraphView graphView;
  LineGraphSeries series;
  User currUser;
  String myName;
  long x;
  long y;
  ArrayList<Long> xs;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.user_analysis);

    graphView = findViewById(R.id.graphView);
    series = new LineGraphSeries();
    graphView.addSeries(series);
    GridLabelRenderer glr = graphView.getGridLabelRenderer();
    glr.setPadding(32);
    graphView.getViewport().setMinX(0);
    graphView.getViewport().setMinY(0);
    currentUser = FirebaseAuth.getInstance().getCurrentUser();
    currUser = new User(currentUser.getUid());
    xs = new ArrayList<>();

    reference = FirebaseDatabase.getInstance().getReference().child("node__user").child(currentUser.getUid()).child("chartTable");
    DatabaseReference nameRef = FirebaseDatabase.getInstance().getReference().child("node__user").child(currentUser.getUid()).child("name");
    nameRef.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        myName = snapshot.getValue().toString();
      }

      @Override
      public void onCancelled(@NonNull DatabaseError error) {

      }
    });

    reference.removeValue();
    setListeners();
  }

  private void setListeners() {
    DatabaseReference aRef = FirebaseDatabase.getInstance().getReference().child("node__assignment");

    aRef.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        for(DataSnapshot ds : snapshot.getChildren()){
          Assignment assignment = ds.getValue(Assignment.class);
          if(currentUser.getUid().equals(assignment.getUid()) && assignment.getAssignmentCompleteTime() != null){
            y = (long)assignment.getAssignmentCompleteTime() - (assignment.getDueDate()*1000);

            y = (long) (floor(((y/1000)/60)/60)/24);

            xs.add(y);
          }
        }

        reference.addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot snapshot) {
            for(int i = 0; i < xs.size(); i ++){
              int count = 1;
              for(int j = i+1; j < xs.size(); j++){
                if(xs.get(i) == xs.get(j)){
                  count++;
                }
              }

              boolean add = true;

              for(DataSnapshot ds : snapshot.getChildren()){
                if(xs.get(i) == ds.getValue(PointValue.class).getxValue()){
                  add = false;
                }
              }

              if(add) {
                PointValue pointValue = new PointValue(xs.get(i), (long) count);
                String id = reference.push().getKey();
                reference.child(id).setValue(pointValue);
              }
            }
          }
          @Override
          public void onCancelled(@NonNull DatabaseError error) {
          }
        });





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
          index++;
        }

        series.resetData(dp);
      }

      @Override
      public void onCancelled(@NonNull DatabaseError error) {

      }
    });
  }

  public void readKey(View view) {
    AlertDialog.Builder key = new AlertDialog.Builder(UserAnalysisTeacher.this);
    key.setTitle(R.string.key);
    key.setMessage("This graph represents the student's submission time relative to the due date. Each X value represents an assignment. The Y value 0 is the due date, all values above are how many days after the due date the assignment is submitted. All Y values below 0 are how early the student submits the assignment in days.");

    AlertDialog showKey = key.create();
    showKey.show();
  }

}