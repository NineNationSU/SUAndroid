package com.android.suapp;

import android.app.FragmentManager;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.android.suapp.messages.NotificationsFragment;
import com.android.suapp.notes.NotesFragment;


public class MenuActivity extends AppCompatActivity {

    private Fragment selectedFragment = null;


    @Override
   protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_menu);
       setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);



        BottomNavigationView mBottomNavigationItemView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
       mBottomNavigationItemView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
           @Override
           public boolean onNavigationItemSelected(@NonNull MenuItem item) {

               switch (item.getItemId()) {
                   case R.id.nav_user:
                       selectedFragment = UserFragment.newInstance();
                       break;
                   case R.id.nav_table:
                       selectedFragment = TableFragment.newInstance();
                       break;
                   case R.id.nav_notice:
                       selectedFragment = NotificationsFragment.newInstance();
                       break;
                   case R.id.nav_notes:
                       selectedFragment = NotesFragment.newInstance();
                       break;
                   default:
                       selectedFragment = UserFragment.newInstance();
               }
               FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
               transaction.replace(R.id.fragment_container, selectedFragment);
               transaction.commit();
               return true;
           }
       });
       if(selectedFragment == null){
           FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
           transaction.replace(R.id.fragment_container, TableFragment.newInstance());
           transaction.commit();
       }

   }

}
