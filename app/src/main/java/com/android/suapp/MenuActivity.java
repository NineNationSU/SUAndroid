package com.android.suapp;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;


public class MenuActivity extends AppCompatActivity {


   @Override
   protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_menu);
       BottomNavigationView mBottomNavigationItemView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
       mBottomNavigationItemView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
           @Override
           public boolean onNavigationItemSelected(@NonNull MenuItem item) {
               Fragment selectedFragment;
               switch (item.getItemId()) {
                   case R.id.nav_table:
                       selectedFragment = TableFragment.newInstance();
                       break;
                   case R.id.nav_user:
                       selectedFragment = UserFragment.newInstance();
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

   }

}
