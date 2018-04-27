package com.esilife.fb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.esilife.fbglib.nofity.FBObserver;
import com.esilife.fbglib.nofity.FBObserverable;
import com.esilife.fbglib.nofity.Observer;

public class MainActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_to_second).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SecondActivity.class));
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void update(boolean fg) {
        super.update(fg);
        android.util.Log.e("siy", "main-fg : " + fg);
    }
}
