package com.example.diabeticdiary1;


import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/*  The ListView uses a ArrayAdapter to build up the list.
    It uses another layout of a textView in a xml file to append and get all the data
*/

public class HistoryActivity extends AppCompatActivity {

            DatabaseReference myRef1;
            ListView list;
            ArrayAdapter<String> adapter;

            List<String> slist;

            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.res_tracker);

                slist = new ArrayList<>();

                list = findViewById(R.id.list);

                //Here it is using the TextView from sample_layout1.xml to create the view
                adapter = new ArrayAdapter<String>(this, R.layout.sample_layout1, R.id.t1, slist);

                //Getting the value from database
                myRef1= FirebaseDatabase.getInstance().getReference("Data");
                myRef1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                        {
                            Diabetes res =dataSnapshot1.getValue(Diabetes.class);
                            //The values are added to the list
                            slist.add(res.date+"     "+res.time+"    "+res.type+"\t   "+res.level);

                        }
                        //the values are added to the adapter and subsequently to the List
                        list.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }

