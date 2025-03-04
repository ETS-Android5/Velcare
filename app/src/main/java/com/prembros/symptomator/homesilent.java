package com.prembros.symptomator;

import android.Manifest;
import android.app.FragmentManager;
import android.app.NotificationManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TimePicker;

import com.prembros.symptomator.adapters.JobsAdapter;
import com.prembros.symptomator.db.JobsViewModel;
import com.prembros.symptomator.fragments.NewJobDialog;
import com.prembros.symptomator.muteService.MuteJobsModel;
import com.prembros.symptomator.utils.NotificationsUtils;
import com.prembros.symptomator.utils.ServiceUtils;


import java.util.ArrayList;

public class homesilent extends AppCompatActivity implements JobsAdapter.OnJobClickListener, NewJobDialog.OnSetJobBtnClicked {

    //public final static String TAG = MainActivity.class.getSimpleName();
    public static final int PERMISSION_CALENDAR_READ = 221;

    private RecyclerView mRecyclerView;
    private JobsAdapter mAdapter;
    private JobsViewModel mViewModel;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coordinated_main_activity);

        //Toolbar mToolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(mToolbar);

        initViewModel();

        mRecyclerView = findViewById(R.id.jobs_recyclerview);
        mAdapter = new JobsAdapter(this, new ArrayList<>(), this);
        initializeRecyclerView();

        fragmentManager = getFragmentManager();

        checkForPermissions();
        NotificationsUtils.createNotificationChannel(this);
    }

    void checkCalendarPermission() {
        if (ActivityCompat.checkSelfPermission(getApplication(), android.Manifest.permission.READ_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions
                    (this,
                            new String[]{Manifest.permission.READ_CALENDAR},
                            200);
        } else {
            ServiceUtils.setUriObserver(this);
            syncWithCalendar();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 200:
                ServiceUtils.setUriObserver(this);
                syncWithCalendar();
                break;
        }
    }

    void initializeRecyclerView(){
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager
                (this, LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();

        mRecyclerView.setItemAnimator(itemAnimator);
        mRecyclerView.setAdapter(mAdapter);

        if(mAdapter.getItemCount() > 0){
            findViewById(R.id.no_jobs_message).setVisibility(View.GONE);
        }

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new
            ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
                @Override
                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                    ArrayList<MuteJob> list = mAdapter.getmJobsList();
                    MuteJob job = list.get(viewHolder.getAdapterPosition());
                    MuteJobsModel.removeJob(job,mViewModel,getApplicationContext());
                }
            };

    void initViewModel(){
        mViewModel = ViewModelProviders.of(this).get(JobsViewModel.class);
        mViewModel.getAllJobs().observe(this, jobs -> {
            mAdapter.setJobs(jobs);
            if(jobs.size() > 0) {
                findViewById(R.id.no_jobs_message).setVisibility(View.GONE);
            } else {
                findViewById(R.id.no_jobs_message).setVisibility(View.VISIBLE);
            }
        });
    }

    void checkForPermissions(){
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        assert notificationManager != null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && !notificationManager.isNotificationPolicyAccessGranted()) {

            Intent intent = new Intent(
                    android.provider.Settings
                            .ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);

            startActivity(intent);
        }
    }

    @Override
    public void onJobClick(int position) {
        MuteJob job = mAdapter.getmJobsList().get(position);
        NewJobDialog newFragment = new NewJobDialog(this, job,mViewModel, this);
        newFragment.show(fragmentManager, "datePicker");
    }

    public void addJobs(View view) {
        NewJobDialog newFragment = new NewJobDialog(this, null,mViewModel, this);
        newFragment.show(fragmentManager, "datePicker");
    }

    @Override
    public void onSetJobBtnClicked(int startDayOffset, int endDayOffset, TimePicker start, TimePicker end, boolean isChecked,
                                   CheckBox[] checkBoxes, NewJobDialog fragment) {
        MuteJobsModel.addMuteJob(null,startDayOffset,
                endDayOffset, start,end, isChecked, checkBoxes,this);
        fragment.dismiss();
    }

    public void syncWithCalendar(){

        ArrayList<UserCalendar> calendars = ServiceUtils.getCalendars(this);
        ServiceUtils.buildCalendarsDialog(calendars,this);
    }

    public void syncJobs(View view) {
        log("syncing calendar");
        checkCalendarPermission();
    }

    public void log(String s) {

    }

}



