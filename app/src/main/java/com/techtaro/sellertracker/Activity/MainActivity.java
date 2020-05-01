package com.techtaro.sellertracker.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.techtaro.sellertracker.Adapter.TrackingAdapter;
import com.techtaro.sellertracker.DBTool.TrackingDBHelper;
import com.techtaro.sellertracker.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private TrackingDBHelper dbHelper;
    private TrackingAdapter adapter;
    DatePickerDialog picker;

    private String strSearch = "";
    private String strDateFrom = "";
    private String strDateTo = "";

    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 1000;
    public static final int MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE = 1001;
    public static final int MY_PERMISSIONS_REQUEST_VIBRATE = 1002;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        checkPermission();

        RelativeLayout RLDateFrom = findViewById(R.id.RLDateFrom);
        RelativeLayout RLDateTo = findViewById(R.id.RLDateTo);

        FloatingActionButton fabScanner = findViewById(R.id.fabScanner);

        recyclerView=(RecyclerView) findViewById(R.id.RVScanList);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);




        final TextView tvDateFrom = findViewById(R.id.tvDateFrom);
        final TextView tvDateTo = findViewById(R.id.tvDateTo);
        Calendar c = Calendar.getInstance();
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String strDate = sdf.format(c.getTime());
        tvDateFrom.setText(strDate);
        tvDateTo.setText(strDate);

        SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
        String output2 = formatter2.format(c.getTime());
        strDateFrom = output2;
        strDateTo = output2;


        RLDateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                Calendar calendar = Calendar.getInstance();
                                calendar.set(year, monthOfYear,dayOfMonth);

                                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                                String output = formatter.format(calendar.getTime());
                                tvDateFrom.setText(output);

                                SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
                                String output2 = formatter2.format(calendar.getTime());
                                strDateFrom = output2;

                                loadSQLiteAllTrackerList();
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        RLDateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(year, monthOfYear,dayOfMonth);

                                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                                String output = formatter.format(calendar.getTime());
                                tvDateTo.setText(output);

                                SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
                                String output2 = formatter2.format(calendar.getTime());
                                strDateTo = output2;

                                loadSQLiteAllTrackerList();
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        fabScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Here, thisActivity is the current activity
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {


                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.CAMERA},
                            MY_PERMISSIONS_REQUEST_CAMERA);

                } else {
                    // Permission has already been granted
                    Intent intent = new Intent(MainActivity.this, ActScanner.class);
                    startActivity(intent);
                }

            }
        });

        loadSQLiteAllTrackerList();
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu) {
        getMenuInflater().inflate( R.menu.searchview, menu);

        MenuItem myActionMenuItem = menu.findItem( R.id.action_search);
        final SearchView searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                strSearch = newText;
                loadSQLiteAllTrackerList();
                return true;
            }
        });
        return true;
    }

    void checkPermission(){
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {


            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_CAMERA);

        }
        else if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {


            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE);

        }
        else if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.VIBRATE)
                != PackageManager.PERMISSION_GRANTED) {


            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.VIBRATE},
                    MY_PERMISSIONS_REQUEST_VIBRATE);

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSQLiteAllTrackerList();
        adapter.notifyDataSetChanged();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Intent intent = new Intent(MainActivity.this, ActScanner.class);
                    startActivity(intent);
                }
                return;

            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    private void loadSQLiteAllTrackerList()
    {


        dbHelper = new TrackingDBHelper(this);
        adapter = new TrackingAdapter(dbHelper.trackerList(strSearch,strDateFrom,strDateTo), this, recyclerView);
        recyclerView.setAdapter(adapter);
    }



}
