package com.summation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        selectItem(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_restart) {
            selectItem(1);
            return true;
        } else if (id == android.R.id.home) {
            selectItem(0);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void selectItem(int position) {
        Fragment fragment;

        switch (position) {
            case 0:
                fragment = new StartFragment();
                break;
            case 1:
                fragment = new PlaygroundFragment();
                break;
            default:
                fragment = new ScoreFragment();
                break;
        }

        getSupportFragmentManager().
                beginTransaction().
                replace(R.id.content_frame, fragment).
                commit();
    }
}
