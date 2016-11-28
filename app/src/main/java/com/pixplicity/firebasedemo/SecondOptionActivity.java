package com.pixplicity.firebasedemo;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.List;

public class SecondOptionActivity extends BaseFirebaseActivity {

    private static final String TAG = SecondOptionActivity.class.getSimpleName();
    ChildEventListener childEventListener;
    StudentsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * add listeners to check for changes, additions, removals
         */
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Student student = dataSnapshot.getValue(Student.class);
                student.setId(dataSnapshot.getKey());
                mStudents.add(student);
                Log.v(TAG, student.getId());
                if (mAdapter == null) {
                    mAdapter = new StudentsAdapter(mStudents);
                    mRecyclerView.setAdapter(mAdapter);
                }
                mRecyclerView.getAdapter().notifyDataSetChanged();
            }

            // When onChildChanged is called, the dataSnapshot is the child that changed not the parent node that you set the listener on
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                for (Student st : mStudents) {
                    if (dataSnapshot.getKey().equals(st.getId())) {
                        mStudents.remove(st);
                        Student student = dataSnapshot.getValue(Student.class);
                        student.setId(dataSnapshot.getKey());
                        mStudents.add(student);
                    }
                }
                // If we're using query to sort data we need to sort them again here!
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                for (Student st : mStudents) {
                    if (dataSnapshot.getKey().equals(st.getId())) {
                        mStudents.remove(st);
                    }
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mReference.addChildEventListener(childEventListener);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mReference.removeEventListener(childEventListener);
    }
}
