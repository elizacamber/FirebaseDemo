package com.pixplicity.firebasedemo;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class BaseFirebaseActivity extends AppCompatActivity {

    public DatabaseReference mReference;
    public static FirebaseDatabase mFirebaseDatabase;
    private Button mSendBtn, mUpdateBtn;
    public RecyclerView mRecyclerView;
    public ProgressBar mProgressBar;
    public LinearLayoutManager mLinearLayoutManager;
    public List<Student> mStudents;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_base);

        // By enabling persistence, any data that we sync while online will be
        // persisted to disk and available offline, even when we restart the app.
        if (mFirebaseDatabase == null) {
            mFirebaseDatabase = FirebaseDatabase.getInstance();
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
        // Reference for Database
        mReference = mFirebaseDatabase.getReference("students");

        mSendBtn = (Button) findViewById(R.id.btn_send);
        mUpdateBtn = (Button) findViewById(R.id.btn_update);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_student_list);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

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

        mStudents = new ArrayList<>();

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

    public class StudentsAdapter extends RecyclerView.Adapter<StudentViewHolder> {

        private List<Student> students;

        public StudentsAdapter(List<Student> students) {
            this.students = students;
        }

        @Override
        public StudentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.li_student, parent, false);
            StudentViewHolder holder = new StudentViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(StudentViewHolder holder, int position) {
            Student student = students.get(position);
            holder.mNameTV.setText(student.getName());
            holder.mAgeTV.setText(String.valueOf(student.getAge()));
            holder.mGradeTV.setText(String.valueOf(student.getGrade()));
        }

        @Override
        public int getItemCount() {
            return students.size();
        }
    }
}
