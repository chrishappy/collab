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

import static java.lang.Math.floor;

/**
 * Display the users submission analysis
 *
 * @contributors Nathan Tsai
 * @author Jerome Lau
 * @since Dec 3, 2020
 *
 */
public class UserAnalysisStudent extends AppCompatActivity {

  DatabaseReference reference;

  FirebaseUser currentUser;
  GraphView graphView;
  LineGraphSeries series;
  User currUser;
  String myName;
  long x;
  long y;

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

    /** Gets the users name as a reference */
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

    x=0;
    y=0;
    reference.removeValue();
    PointValue pointValue = new PointValue(x,y);
    String id = reference.push().getKey();
    reference.child(id).setValue(pointValue);
    setListeners();
  }

  /** Checks Firebase for assignments the student has
   *  Gets time difference from submission and due date
   *  Stores in users chartTable
   */
  private void setListeners() {
    DatabaseReference aRef = FirebaseDatabase.getInstance().getReference().child("node__assignment");

    aRef.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        for(DataSnapshot ds : snapshot.getChildren()){
          Assignment assignment = ds.getValue(Assignment.class);
          if(myName.equals(assignment.getClassId()) && assignment.getAssignmentCompleteTime() != null){

            x++;
            y = (long) assignment.getAssignmentCompleteTimeLong() - assignment.getDueDate();

            y = (long) (floor(((y/1000)/60)/60)/24);
            PointValue pointValue = new PointValue(x,y);
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
    AlertDialog.Builder key = new AlertDialog.Builder(UserAnalysisStudent.this);
    key.setTitle(R.string.key);
    key.setMessage(R.string.student_key);

    AlertDialog showKey = key.create();
    showKey.show();
  }

}