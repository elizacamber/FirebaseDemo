package com.pixplicity.firebasedemo;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class ThirdOptionActivity extends BaseFirebaseActivity {

    private static final String TAG = ThirdOptionActivity.class.getSimpleName();
    private ValueEventListener mValueEventListener;
    private StudentsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * it will return every time the whole list
         * thus is not very data efficient. Instead we're using the addChildEventListener
         */

        mValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.v(TAG, "There are " + dataSnapshot.getChildrenCount() + " mStudents");
                mStudents.clear();
                for (DataSnapshot studentSnapshot : dataSnapshot.getChildren()) {
                    Student student = studentSnapshot.getValue(Student.class);
                    mStudents.add(student);
                    if (mAdapter == null) {
                        mAdapter = new StudentsAdapter(mStudents);
                        mRecyclerView.setAdapter(mAdapter);
                    }
                    mRecyclerView.getAdapter().notifyDataSetChanged();
                    Log.v(TAG, student.getName());

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mReference.addValueEventListener(mValueEventListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mReference.removeEventListener(mValueEventListener);
    }
}
