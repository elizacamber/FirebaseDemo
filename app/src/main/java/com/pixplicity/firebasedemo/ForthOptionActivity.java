package com.pixplicity.firebasedemo;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;

import java.util.List;

public class ForthOptionActivity extends BaseFirebaseActivity {

    private static final String TAG = ForthOptionActivity.class.getSimpleName();
    private FirebaseRecyclerAdapter<Student, StudentViewHolder> mFirebaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * Using Firebase's Recycler Adapter
         * Must be added as dependency
         */

        mFirebaseAdapter = new FirebaseRecyclerAdapter<Student, StudentViewHolder>(
                Student.class,
                R.layout.li_student,
                StudentViewHolder.class,
                mReference) {

            @Override
            protected void populateViewHolder(StudentViewHolder viewHolder, Student model, int position) {
                mStudents.add(model);
                mProgressBar.setVisibility(ProgressBar.INVISIBLE);
                viewHolder.mNameTV.setText(model.getName());
                viewHolder.mAgeTV.setText(Integer.toString(model.getAge()));
                viewHolder.mGradeTV.setText(Integer.toString(model.getGrade()));
            }
        };
        mRecyclerView.setAdapter(mFirebaseAdapter);


        /**
         * in some cases it can used instead of the listeners
         * TODO: Check what happens if you change the data from the device.
         * Have to be used in combination with Firebase's Adapter OR a
         * ChildAddedListener that would be instantly released after the initial loading
         */
        mReference.keepSynced(true);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFirebaseAdapter.cleanup();
    }
}
