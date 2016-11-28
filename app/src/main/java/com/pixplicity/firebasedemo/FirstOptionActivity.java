package com.pixplicity.firebasedemo;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;

import java.util.List;

public class FirstOptionActivity extends BaseFirebaseActivity {

    private static final String TAG = FirstOptionActivity.class.getSimpleName();
    private FirebaseRecyclerAdapter<Student, StudentViewHolder> mFirebaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * Using Firebase's Recycler Adapter + DataObserver.
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

        mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int studentsCount = mFirebaseAdapter.getItemCount();
                int lastVisiblePosition = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the user is at the bottom of the list, scroll
                // to the bottom of the list to show the newly added message.
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (studentsCount - 1) && lastVisiblePosition == (positionStart - 1))) {
                    mRecyclerView.scrollToPosition(positionStart);
                }
            }
        });

        mRecyclerView.setAdapter(mFirebaseAdapter);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFirebaseAdapter.cleanup();
    }
}
