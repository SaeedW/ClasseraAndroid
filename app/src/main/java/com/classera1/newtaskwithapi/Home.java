package com.classera1.newtaskwithapi;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.classera1.newtaskwithapi.adapting.MenuAdapter;
import com.classera1.newtaskwithapi.database.DBHelper;
import com.classera1.newtaskwithapi.fragments.LessonDetail;
import com.classera1.newtaskwithapi.fragments.Profile;
import com.classera1.newtaskwithapi.oop.Courses;
import com.classera1.newtaskwithapi.oop.Lessons;
import com.classera1.newtaskwithapi.oop.User;
import com.classera1.newtaskwithapi.serviceside.connection.connectivity.Connection;
import com.classera1.newtaskwithapi.serviceside.requests.GetCoursesAndLessonsAsync;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //main fileds
    DrawerLayout drawer;
    DBHelper DB;

    //Expandable things
    ExpandableListView ex_of_item;
    ArrayList<Courses> coursesData;
    ArrayList<Lessons> lesonsData;
    List<String> listDataHeader, listChildren;
    HashMap<String, List<String>> listDataChild;
    MenuAdapter adapter;

    //Drawer Layout Headers Item
    TextView usernameHeader, emailHeader;
    ArrayList<User> userData;
    LinearLayout linear;
    SwipeRefreshLayout refreshlayout;
    ProgressBar dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        declareViews();
        setupHeaderDrwerLayout();
        showProgfileFragment();
        listenersViews();
        setAdapting();
    }

    private void setupHeaderDrwerLayout() {
        userData = DB.getUser();
        usernameHeader.setText(userData.get(0).getName());
        emailHeader.setText(userData.get(0).getEmail());
    }

    // let's play here bb
    private void setAdapting() {
        coursesData = DB.getCourses();
        listDataChild = new HashMap<String, List<String>>();
        listDataHeader = new ArrayList<String>();
        for (int i = 0; i < coursesData.size(); i++) {
            listDataHeader.add(coursesData.get(i).getName());
            listDataChild.put(listDataHeader.get(i), getChildren("" + (i + 1)));
        }
        adapter = new MenuAdapter(Home.this, listDataHeader, listDataChild);
        ex_of_item.setAdapter(adapter);
    }

    public List<String> getChildren(String id) {
        listChildren = new ArrayList<String>();
        lesonsData = DB.getLessonsById(id);
        for (int s = 0; s < lesonsData.size(); s++) {
            listChildren.add(lesonsData.get(s).getName());
        }
        return listChildren;
    }


    public void listenersViews() {
        // expandbale list
        ex_of_item.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                lesonsData = DB.getLessonsById(coursesData.get(groupPosition).getId());
                String lessonId = lesonsData.get(childPosition).getId();
                showLessonFragment(lessonId);
                return true;
            }
        });
        //Linear layout
        linear = (LinearLayout) findViewById(R.id.headerbb);
        linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgfileFragment();
            }
        });

        refreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // check internet first
                //call get data method
                // finish the refresh
                getData();
            }
        });
    }


    public void getData() {
        if (new Connection(Home.this).isOnline()) {
            //clean ur db
            DB.deleteCoursesTable();
            DB.deleteLessonsTable();
            dialog.setVisibility(View.GONE);
            //get new data
            GetCoursesAndLessonsAsync async = new GetCoursesAndLessonsAsync(Home.this, dialog);
            async.asyncDone(new GetCoursesAndLessonsAsync.asyncDoneLoading() {
                @Override
                public void onFinish(String s) {
                    if (s.equals("DONE")) {
                        // start the new activity
                        refreshlayout.setRefreshing(false);
                    } else {
                        Toast.makeText(Home.this, "SERVER ERROR", Toast.LENGTH_LONG).show();
                    }
                }
            });
            async.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            Toast.makeText(Home.this, "Check Your Connection!", Toast.LENGTH_LONG).show();
        }
    }


    public void showLessonFragment(String id) {
        // show fragment in the first view Profile Fragment
        Bundle b = new Bundle();
        LessonDetail lFrag = new LessonDetail();
        b.putString("lesson_id", id);
        FragmentTransaction lessonDetailFrag = getSupportFragmentManager().beginTransaction();
        lessonDetailFrag.replace(R.id.content, lFrag);
        lFrag.setArguments(b);
        lessonDetailFrag.commit();
        drawer.closeDrawer(GravityCompat.START);
    }

    public void showProgfileFragment() {
        // show fragment in the first view Profile Fragment
        FragmentTransaction profFrag = getSupportFragmentManager().beginTransaction();
        profFrag.replace(R.id.content, new Profile());
        profFrag.commit();
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            Intent x = new Intent(Intent.ACTION_MAIN);
            x.addCategory(Intent.CATEGORY_HOME);
            x.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            x.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(x);
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void declareViews() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ex_of_item = (ExpandableListView) findViewById(R.id.expandableListView);
        refreshlayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        DB = new DBHelper(Home.this);
        usernameHeader = (TextView) findViewById(R.id.namehead);
        emailHeader = (TextView) findViewById(R.id.emailhead);
        dialog = (ProgressBar) findViewById(R.id.progressBar2);
    }


    public void maybereused() {
           /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */
           /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
*/
    }
}
