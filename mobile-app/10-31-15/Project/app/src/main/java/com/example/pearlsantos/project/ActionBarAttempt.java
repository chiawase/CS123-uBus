package com.example.pearlsantos.project;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;;
import android.app.Activity;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.support.v7.app.ActionBar.Tab;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Locale;

public class ActionBarAttempt extends AppCompatActivity {

        private DrawerLayout mDrawerLayout;
        private NavigationView nvDrawer;
        Toolbar toolbar;
        ActionBar actionBar;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_action_bar_attempt);

            //setting the toolbar
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
            toolbar.setNavigationIcon(R.drawable.ic_action_menu);

            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            nvDrawer = (NavigationView) findViewById(R.id.left_drawer);
            setupDrawerContent(nvDrawer);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, new SearchFragment()).commit();



            //mDrawerList = (ListView) findViewById(R.id.left_drawer);


            // set up the drawer's list view with items and click listener
//            mDrawerList.setAdapter(new CustomAdapter(this,options, optionIcons));
//            mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

            // enable ActionBar app icon to behave as action to toggle nav drawer



            // ActionBarDrawerToggle ties together the the proper interactions
            // between the sliding drawer and the action bar app icon
//            mDrawerToggle = new ActionBarDrawerToggle(
//                    this,                  /* host Activity */
//                    mDrawerLayout,         /* DrawerLayout object */
//                    R.drawable.ic_action_menu,  /* nav drawer image to replace 'Up' caret */
//                    R.string.drawer_open,  /* "open drawer" description for accessibility */
//                    R.string.drawer_close  /* "close drawer" description for accessibility */
//            )
//            {
//                public void onDrawerClosed(View view) {
//                    actionBar.setTitle(mTitle);
//                    invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
//                }
//
//                public void onDrawerOpened(View drawerView) {
//                    actionBar.setTitle(mDrawerTitle);
//                    invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
//                }
//            };
//            mDrawerLayout.setDrawerListener(mDrawerToggle);


//            if (savedInstanceState == null) {
//                selectItem(0);
//            }
        }

        private void setupDrawerContent(NavigationView navigationView){
            navigationView.setNavigationItemSelectedListener(
                    new NavigationView.OnNavigationItemSelectedListener(){
                        @Override
                        protected Object clone() throws CloneNotSupportedException {
                            return super.clone();
                        }

                        @Override
                        public boolean onNavigationItemSelected(MenuItem menuItem) {
                            selectDrawerItem(menuItem);
                            return true;
                        }
                    }
            );
        }

    public void selectDrawerItem(MenuItem menuItem){
        Fragment fragment = null;

        Class fragmentClass;
        switch(menuItem.getItemId()){
//            case R.id.search:
//                fragmentClass = SearchFragment.class;
//                break;
            case R.id.editProfile:
                fragmentClass = ChangeInfoFragment.class;
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setHomeButtonEnabled(true);
                actionBar.setDisplayShowTitleEnabled(true);
                toolbar.setNavigationIcon(R.drawable.ic_action_menu);
                break;
            case R.id.viewPastTrips:
                fragmentClass = PastTrips.class;
                break;
            case R.id.settings:
                //settings
                fragmentClass = Settings.class;
                break;
            case R.id.logout:
                //logout
                fragmentClass = Help.class;
                break;
            default:
                fragmentClass = SearchFragment.class;
                break;
        }

        try{
            fragment = (Fragment) fragmentClass.newInstance();
        }catch(Exception e){
            e.printStackTrace();

        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        // update selected item and title, then close the drawer
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawerLayout.closeDrawers();
    }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_action_bar_attempt, menu);
            //write here your filter method
            return super.onCreateOptionsMenu(menu);
        }

    /* Called whenever we call invalidateOptionsMenu() */
        @Override
        public boolean onPrepareOptionsMenu(Menu menu) {
            // If the nav drawer is open, hide action items related to the content view
//            boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
//            menu.findItem(R.id.action_search).setVisible(!drawerOpen);
            return super.onPrepareOptionsMenu(menu);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // The action bar home/up action should open or close the drawer.
            // ActionBarDrawerToggle will take care of this.
//            if (mDrawerToggle.onOptionsItemSelected(item)) {
//                return true;
//            }

            switch(item.getItemId()){
                case android.R.id.home:
                    mDrawerLayout.openDrawer(GravityCompat.START);
                    return true;
            }
//            Fragment fragment = null;
//            Bundle args = new Bundle();
//            FragmentManager fragmentManager = null;
//            // Handle action buttons
//            switch(item.getItemId()) {
//                case R.id.action_search:
//                    //insert what needs to be done when chosen
//                    return true;
//                case R.id.action_aboutUs:
//                    fragment = new About();
//                    fragmentManager = getSupportFragmentManager();
//                    fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
//                    return true;
//                case R.id.action_help:
//                    fragment = new Help();
//                    fragmentManager = getSupportFragmentManager();
//                    fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
//                    return true;
//                case R.id.action_settings:
//                    //insert what needs to be done when chosen
//                    return true;
//                default:
//                    return super.onOptionsItemSelected(item);
//            }

            return super.onOptionsItemSelected(item);
        }

//    /* The click listner for ListView in the navigation drawer */
//        private class DrawerItemClickListener implements ListView.OnItemClickListener {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                selectItem(position);
//            }
//        }

//    private void selectItem(int position) {
//        // update the main content by replacing fragments
//        Fragment fragment = null;
//        Bundle args = new Bundle();
//
//        switch(position){
//            case 0:
//                fragment = new SearchFragment();
//                break;
//            case 1:
//                fragment = new Schedules();
//                break;
//            case 2:
//                fragment = new ChangeInfoFragment();
//                break;
//            case 3:
//                fragment = new PastTrips();
//                break;
//            case 4:
//                //settings
//                fragment = new Help();
//                break;
//            case 5:
//                //logout
//                fragment = new Help();
//                break;
//            default:
//                break;
//        }
//
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
//
//        // update selected item and title, then close the drawer
//        mDrawerList.setItemChecked(position, true);
//        setTitle(options[position]);
//        mDrawerLayout.closeDrawer(mDrawerList);
//    }
//
//    @Override
//    public void setTitle(CharSequence title) {
//        mTitle = title;
//        getSupportActionBar().setTitle(mTitle);
//    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        //mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        //mDrawerToggle.onConfigurationChanged(newConfig);
    }


    }


    class CustomAdapter extends BaseAdapter {
        String[] options;
        Context context;
        int[] imageID;
        private static LayoutInflater inflater = null;

        public CustomAdapter(Context mainActivity,String[] optionsList, int[] optionIcons){
            options = optionsList;
            context = mainActivity;
            imageID = optionIcons;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public class Holder{
            ImageView img;
            TextView tv;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder = new Holder();
            View rowView;
            rowView = inflater.inflate(R.layout.custom_draw_item, null);
            holder.tv = (TextView) rowView.findViewById(R.id.nav_item);
            holder.img = (ImageView) rowView.findViewById(R.id.list_icon);
            holder.tv.setText(options[position]);
            holder.img.setImageResource(imageID[position]);
            return rowView;
        }
    }
















