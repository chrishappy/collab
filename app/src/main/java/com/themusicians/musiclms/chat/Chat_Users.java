package com.themusicians.musiclms.chat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.themusicians.musiclms.R;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Chat_Users.java
 *
 * <p>Select specific user to send message to
 *
 * @todo Users' names
 * @author Shifan He Created by Shifan He on 2020-11-18
 */

public class Chat_Users extends AppCompatActivity {
  ListView usersList;
  TextView noUsersText;
  ArrayList<String> al = new ArrayList<>();
  int totalUsers = 0;
  ProgressDialog pd;
  FirebaseUser currentUser;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //setContentView(R.layout.activity_users);

   // usersList = (ListView) findViewById(R.id.usersList);
  //  noUsersText = (TextView) findViewById(R.id.noUsersText);
    currentUser = FirebaseAuth.getInstance().getCurrentUser();

    pd = new ProgressDialog(Chat_Users.this);
    pd.setMessage("Loading...");
    pd.show();

    String url = "https://musiclms---cmpt276.firebaseio.com/node__user/.json";

    StringRequest request =
        new StringRequest(
            Request.Method.GET,
            url,
            new Response.Listener<String>() {
              @Override
              public void onResponse(String s) {
                doOnSuccess(s);
              }
            },
            new Response.ErrorListener() {
              @Override
              public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError);
              }
            });

    RequestQueue rQueue = Volley.newRequestQueue(Chat_Users.this);
    rQueue.add(request);

    usersList.setOnItemClickListener(
        new AdapterView.OnItemClickListener() {
          @Override
          public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Chat_UserDetails.chatWith = al.get(position);
            startActivity(new Intent(Chat_Users.this, Chat.class));
          }
        });
  }

  public void doOnSuccess(String s) {
    try {
      JSONObject obj = new JSONObject(s);

      Iterator i = obj.keys();
      String key = "";

      while (i.hasNext()) {
        key = i.next().toString();

        if (!key.equals(Chat_UserDetails.username)) {
          al.add(key);
        }

        totalUsers++;
      }

    } catch (JSONException e) {
      e.printStackTrace();
    }

    if (totalUsers <= 1) {
      noUsersText.setVisibility(View.VISIBLE);
      usersList.setVisibility(View.GONE);
    } else {
      noUsersText.setVisibility(View.GONE);
      usersList.setVisibility(View.VISIBLE);
      usersList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, al));
    }

    pd.dismiss();
  }
}
