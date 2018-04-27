package com.esilife.fb;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.esilife.fbglib.nofity.FBObserverable;
import com.esilife.fbglib.nofity.Observer;

/**
 * Created by siy on 18-4-27.
 */
public class BaseActivity extends AppCompatActivity implements Observer {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FBObserverable.getInstance().registerOnlyOneObserver(this);
    }
    
    @Override
    public void update(boolean fg) {
        android.util.Log.e("BaseActivity", "fg: " + fg);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FBObserverable.getInstance().removeOnlyOneObserver();
    }
}
