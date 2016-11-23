package com.pixplicity.firebasedemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

/**
 * Useful Links :
 * https://www.firebase.com/docs/android/guide/retrieving-data.html
 * https://firebase.google.com/docs/database/android/lists-of-data
 * http://stackoverflow.com/questions/33776195/how-to-keep-track-of-listeners-in-firebase-on-android?answertab=votes#tab-top
 * https://www.sitepoint.com/creating-a-cloud-backend-for-your-android-app-using-firebase/
 */

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private Button mSendBtn, mUpdateBtn;
    private DatabaseReference mReference;
    private RecyclerView mRecyclerView;
    private FirebaseRecyclerAdapter<Student, StudentViewHolder> mFirebaseAdapter;
    private ProgressBar mProgressBar;
    private LinearLayoutManager mLinearLayoutManager;
    private List<Student> students;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSendBtn = (Button) findViewById(R.id.btn_send);
        mUpdateBtn = (Button) findViewById(R.id.btn_update);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_student_list);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        students = new ArrayList<Student>();

        // By enabling persistence, any data that we sync while online will be
        // persisted to disk and available offline, even when we restart the app.
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        // Reference for Database
        mReference = FirebaseDatabase.getInstance().getReference("students");

        /**
         * Sorting according a specific value (ex. their age)
         *  Query queryRef = mReference.orderByChild("age");
         */

        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Student student = new Student("Mary", 5, 6);
                mReference.push().setValue(student);
            }
        });

        mUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mReference.child("studentC").child("name").setValue("Josh");
            }
        });

        /**
         * OPTION 1
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
                mProgressBar.setVisibility(ProgressBar.INVISIBLE);
                viewHolder.mNameTV.setText(model.getName());
                viewHolder.mAgeTV.setText(Integer.toString(model.getAge()));
                viewHolder.mGradeTV.setText(Integer.toString(model.getGrade()));
            }
        };

        /**
         * OPTION 1
         *
         mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
        @Override public void onItemRangeInserted(int positionStart, int itemCount) {
        super.onItemRangeInserted(positionStart, itemCount);
        int friendlyMessageCount = mFirebaseAdapter.getItemCount();
        int lastVisiblePosition = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
        // If the recycler view is initially being loaded or the user is at the bottom of the list, scroll
        // to the bottom of the list to show the newly added message.
        if (lastVisiblePosition == -1 ||
        (positionStart >= (friendlyMessageCount - 1) && lastVisiblePosition == (positionStart - 1))) {
        mRecyclerView.scrollToPosition(positionStart);
        }
        }
        });
         */
        mRecyclerView.setAdapter(mFirebaseAdapter);

        /**
         * OPTION 2
         * add listeners to check for changes, additions, removals
         *

         mReference.addChildEventListener(new ChildEventListener() {
         //mReference.addChildEventListener(new ChildEventListener() {
        @Override public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        Student student = dataSnapshot.getValue(Student.class);
        student.setId(dataSnapshot.getKey());
        students.add(student);
        Log.v(TAG, student.getId());
        }

        // When onChildChanged is called, the dataSnapshot is the child that changed not the parent node that you set the listener on
        @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        for (Student st : students) {
        if (dataSnapshot.getKey().equals(st.getId())) {
        students.remove(st);
        Student student = dataSnapshot.getValue(Student.class);
        student.setId(dataSnapshot.getKey());
        students.add(student);
        }
        }
        // TODO: check if the age is changed. If yes we need to sort again the students list
        }

        @Override public void onChildRemoved(DataSnapshot dataSnapshot) {
        for (Student st : students) {
        if (dataSnapshot.getKey().equals(st.getId())) {
        students.remove(st);
        }
        }
        }

        @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override public void onCancelled(DatabaseError databaseError) {

        }
        });

         */

        /**
         * OPTION 3
         * it will return every time the whole list
         * thus is not very data efficient. Instead we're using the addChildEventListener

         mReference.addValueEventListener(new ValueEventListener() {
        @Override public void onDataChange(DataSnapshot dataSnapshot) {
        Log.v(TAG, "There are " + dataSnapshot.getChildrenCount() + " students");
        for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
        Student student = studentSnapshot.getValue(Student.class);
        students.add(student);
        Log.v(TAG, student.getName());
        }
        }

        @Override public void onCancelled(DatabaseError databaseError) {

        }
        });
         */

        /**
         * OPTION 4
         * in some cases it can used instead of the listeners
         * TODO: Check what happens if you change the data from the device.
         * Have to be used in combination with Firebase's Adapter [without the DataObserver] OR a
         * ChildAddedListener that would be instantly released after the initial loading
         */
        mReference.keepSynced(true);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // If we use FirebaseAdapter : mFirebaseAdapter.cleanup();
        // If we use Listeners : mReference.removeEventListener(childEventListener);
    }

    // if it's not static it causes a NoSuchMethodException
    public static class StudentViewHolder extends RecyclerView.ViewHolder {
        public TextView mNameTV;
        public TextView mAgeTV;
        public TextView mGradeTV;

        public StudentViewHolder(View itemView) {
            super(itemView);
            mNameTV = (TextView) itemView.findViewById(R.id.tv_name);
            mAgeTV = (TextView) itemView.findViewById(R.id.tv_age);
            mGradeTV = (TextView) itemView.findViewById(R.id.tv_grade);
        }
    }
}
