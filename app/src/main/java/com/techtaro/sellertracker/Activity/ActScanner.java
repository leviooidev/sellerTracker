package com.techtaro.sellertracker.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.zxing.Result;
import com.techtaro.sellertracker.DBTool.DBInit;
import com.techtaro.sellertracker.DBTool.TrackingDBHelper;
import com.techtaro.sellertracker.R;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ActScanner extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_scanner);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        mScannerView = new ZXingScannerView(this);
        contentFrame.addView(mScannerView);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();

    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();

    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        // Toast.makeText(this, "Contents = " + rawResult.getText() +
        //          ", Format = " + rawResult.getBarcodeFormat().toString(), Toast.LENGTH_SHORT).show();
        String strScannedText=rawResult.getText();

        //Import Sqlite data into details
        SQLiteOpenHelper database = new DBInit(this);
        SQLiteDatabase db = database.getReadableDatabase();
        String query = "SELECT exists(SELECT * FROM TrackerList WHERE code = '"+strScannedText+"' ) AS exist;";
        Cursor c = db.rawQuery(query, null);

        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            String strCount = c.getString(c.getColumnIndex("exist"));

            if (strCount.equalsIgnoreCase("1")) {

                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

                // Vibrate for 400 milliseconds
                if (v != null) {
                    v.vibrate(400);
                }
                Toast.makeText(getApplicationContext(), "Already Scanned", Toast.LENGTH_SHORT).show();


            } else {
                // Get instance of Vibrator from current Context
                addScannedCode(strScannedText);

                Toast.makeText(getApplicationContext(), "New Record Inserted", Toast.LENGTH_SHORT).show();
                ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
                tg.startTone(ToneGenerator.TONE_PROP_ACK);
            }

            // Note:
            // * Wait 2 seconds to resume the preview.
            // * On older devices continuously stopping and resuming camera preview can result in freezing the app.
            // * I don't know why this is the case but I don't have the time to figure out.
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScannerView.resumeCameraPreview(ActScanner.this);
                }
            }, 300);
        }



    }

    //SAVE
    private void addScannedCode(String strScannedCode)
    {


        TrackingDBHelper db=new TrackingDBHelper(this);

        //OPEN
        db.openDB();

        //INSERT TEMPDELIVERYZONE
        long result=db.addScannedJob(strScannedCode);


        if(result>0)
        {
            Toast.makeText(getApplicationContext(),"Insert Successful",Toast.LENGTH_LONG).show();

        }
        else
        {
            Toast.makeText(getApplicationContext(),"Unable to insert",Toast.LENGTH_LONG).show();
        }


        //CLOSE
        db.close();

    }
}
