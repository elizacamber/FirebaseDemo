package com.pixplicity.firebasedemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Useful Links :
 * https://www.firebase.com/docs/android/guide/retrieving-data.html
 * https://firebase.google.com/docs/database/android/lists-of-data
 * http://stackoverflow.com/questions/33776195/how-to-keep-track-of-listeners-in-firebase-on-android?answertab=votes#tab-top
 * https://www.sitepoint.com/creating-a-cloud-backend-for-your-android-app-using-firebase/
 */

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button firstBtn = (Button) findViewById(R.id.btn_first);
        Button secondBtn = (Button) findViewById(R.id.btn_second);
        Button thirdBtn = (Button) findViewById(R.id.btn_third);
        Button forthBtn = (Button) findViewById(R.id.btn_forth);

        firstBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO : Get rid of the activities?
                Intent intent = new Intent(MainActivity.this, FirstOptionActivity.class);
                startActivity(intent);
            }
        });

        secondBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SecondOptionActivity.class);
                startActivity(intent);
            }
        });

        thirdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ThirdOptionActivity.class);
                startActivity(intent);
            }
        });

        forthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ForthOptionActivity.class);
                startActivity(intent);
            }
        });
    }
}
