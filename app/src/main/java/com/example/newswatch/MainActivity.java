package com.example.newswatch;

import android.os.Bundle;

import com.example.newswatch.fragment.AboutFragment;
import com.example.newswatch.fragment.CategoryFragment;
import com.example.newswatch.fragment.TopheadlinesFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
{
    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            displaySelectedScreen(item.getItemId());

            return true;
        }
    };

    private void displaySelectedScreen(int itemId)
    {
        Fragment fragment = null;
        switch (itemId)
        {
            case R.id.navigation_home:
                fragment = new TopheadlinesFragment();
                break;

            case R.id.navigation_dashboard:
                fragment = new CategoryFragment();
                break;

            case R.id.navigation_notifications:
                fragment = new AboutFragment();
                break;

            default:
                fragment = new TopheadlinesFragment();
                break;
        }

        if (fragment != null)
        {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment);
            fragmentTransaction.commit();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        mTextMessage = findViewById(R.id.message);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, new TopheadlinesFragment());
        fragmentTransaction.commit();
    }

}
