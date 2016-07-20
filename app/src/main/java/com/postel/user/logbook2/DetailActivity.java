package com.postel.user.logbook2;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.postel.user.logbook2.config.Config;
import com.postel.user.logbook2.config.ImageLoader;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by user on 6/30/2016.
 */
public class DetailActivity extends AppCompatActivity {

    // LogCat tag
    private static final String TAG = DetailActivity.class.getSimpleName();

    String id, nama, status, email, tlp, perusahaan, keterangan, keperluan, waktu, foto, ktp, ttd;
    //ImageLoader imageLoader = new ImageLoader(DetailActivity.this);

    // Camera activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    private Uri fileUri; // file url to store image/video

    TextView viewNama;
    TextView viewWaktu;
    TextView viewStatus;
    TextView viewTlp;
    TextView viewEmail;
    TextView viewPerusahaan;
    TextView viewKeperluan;
    TextView viewKeterangan;
    ImageView viewFoto;
    ImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        callData();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();

                captureImage();
            }
        });



        // Checking camera availability
        if (!isDeviceSupportCamera()) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! Your device doesn't support camera",
                    Toast.LENGTH_LONG).show();
            // will close the app if the device does't have camera
            finish();
        }
    }

    private void callData(){
        imageLoader = new ImageLoader(DetailActivity.this);
        // Locate the ImageView in activity_detail.xml
        viewNama = (TextView) findViewById(R.id.listNamaDetail);
        viewWaktu = (TextView) findViewById(R.id.listWaktuDetail);
        viewStatus = (TextView) findViewById(R.id.listStatusDetail);
        viewTlp = (TextView) findViewById(R.id.listTlpDetail);
        viewEmail = (TextView) findViewById(R.id.listEmailDetail);
        viewPerusahaan = (TextView) findViewById(R.id.listPerusahaanDetail);
        viewKeperluan = (TextView) findViewById(R.id.listKeperluanDetail);
        viewKeterangan = (TextView) findViewById(R.id.listKeteranganDetail);
        viewFoto = (ImageView) findViewById(R.id.listFotoDetail);

        // Get the result of nama
        Intent i = getIntent();
        id = i.getStringExtra("id");
        nama = i.getStringExtra("nama");
        status = i.getStringExtra("status");
        tlp = i.getStringExtra("tlp");
        email = i.getStringExtra("email");
        perusahaan = i.getStringExtra("perusahaan");
        keterangan = i.getStringExtra("keterangan");
        keperluan = i.getStringExtra("keperluan");
        waktu = i.getStringExtra("waktu");
        foto = i.getStringExtra("foto");
        ktp = i.getStringExtra("ktp");
        ttd = i.getStringExtra("ttd");

        // Capture position and set results to the ImageView
        // Passes flag images URL into ImageLoader.class
        viewNama.setText(nama);
        viewWaktu.setText(waktu);
        viewStatus.setText(status);
        viewTlp.setText(tlp);
        viewEmail.setText(email);
        viewPerusahaan.setText(perusahaan);
        viewKeperluan.setText(keperluan);
        viewKeterangan.setText(keterangan);
        imageLoader.DisplayImage(foto, viewFoto);
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        //recreate();
        callData();
    }



    /**
     * Checking device has camera hardware or not
     * */
    private boolean isDeviceSupportCamera() {
        if (getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    /**
     * Launching camera app to capture image
     */
    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        //Toast.makeText(this, id, Toast.LENGTH_LONG).show();

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
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
    }*/

    /**
     * Here we store the file url as it will be null after returning from camera
     * app
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on screen orientation
        // changes
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }

    /**
     * Receiving activity result method will be called after closing the camera
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                // successfully captured the image
                // launching upload activity
                launchUploadActivity(true);


            } else if (resultCode == RESULT_CANCELED) {

                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();

            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }

        } else if (requestCode == CAMERA_CAPTURE_VIDEO_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                // video successfully recorded
                // launching upload activity
                launchUploadActivity(false);

            } else if (resultCode == RESULT_CANCELED) {

                // user cancelled recording
                Toast.makeText(getApplicationContext(),
                        "User cancelled video recording", Toast.LENGTH_SHORT)
                        .show();

            } else {
                // failed to record video
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to record video", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    private void launchUploadActivity(boolean isImage) {
        Intent i = new Intent(DetailActivity.this, UploadFotoActivity.class);
        i.putExtra("filePath", fileUri.getPath());
        i.putExtra("isImage", isImage);
        i.putExtra("id", id);
        startActivity(i);
    }

    /**
     * ------------ Helper Methods ----------------------
     * */

    /**
     * Creating file uri to store image/video
     */
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * returning image / video
     */
    private static File getOutputMediaFile(int type) {

        // External sdcard location
         File mediaStorageDir = new File(
                 android.os.Environment.getExternalStoragePublicDirectory(android.os.Environment.DIRECTORY_PICTURES),
               Config.IMAGE_DIRECTORY_NAME);

        //File mediaStorageDir = new File(getFilesDir(), Config.IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            //mediaStorageDir.mkdirs();
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "Oops! Failed create "
                        + Config.IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
        //return mediaStorageDir;
    }
}
