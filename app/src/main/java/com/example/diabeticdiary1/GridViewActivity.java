package com.example.diabeticdiary1;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/*  The Grid uses a ArrayAdapter to build up the list.
    It uses another layout of a textView in a xml file to append and get all the data.
    The grid is Combined of four columns
 */

public class GridViewActivity extends AppCompatActivity {

    DatabaseReference myRef1;
    GridView grid;
    ArrayAdapter<String> adapter;

    List<String> slist;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid_res);

        slist = new ArrayList<>();

        grid = findViewById(R.id.grid);
        //Initializing the Headings of the columns
        slist.add("Date");
        slist.add("Time");
        slist.add("Type");
        slist.add("Level");

        //The gridView is using the sample_layout.xml for Build up
        adapter = new ArrayAdapter<String>(this, R.layout.sample_layout, R.id.t1, slist);

        //Getting the value from database
        myRef1= FirebaseDatabase.getInstance().getReference("Data");
        myRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    Diabetes res =dataSnapshot1.getValue(Diabetes.class);
                    //The values are added to the list
                    slist.add(res.date);
                    slist.add(res.time);
                    slist.add(res.type);
                    slist.add(res.level);
                }
                //the values are added to the adapter and subsequently to the List
                grid.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
