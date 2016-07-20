package com.postel.user.logbook2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.postel.user.logbook2.config.Koneksi;
import com.postel.user.logbook2.config.RequestHandler;

import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

/**
 * Created by user on 6/30/2016.
 */
public class AddActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    //Mendefinisikan View Edit Text
    private EditText editTextNama;
    private EditText editTextEmail;
    private EditText editTextTlp;
    private EditText editTextPerusahaan;
    private EditText editTextKeterangan;
    // Mendefinisikan View Button
    private Button buttonAdd;
    private Button buttonView;

    private Spinner kep_spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        //Men Inisialisasi View Text dan Button
        editTextNama = (EditText) findViewById(R.id.editTextNama);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextTlp = (EditText) findViewById(R.id.editTextTlp);
        editTextPerusahaan = (EditText) findViewById(R.id.editTextPerusahaan);
        editTextKeterangan = (EditText) findViewById(R.id.editTextKetrangan);
        buttonAdd = (Button) findViewById(R.id.buttonAdd);
        buttonView=(Button) findViewById(R.id.buttonView);
        //Berikan event listeners Klik ke Button
        buttonAdd.setOnClickListener(this);
        buttonView.setOnClickListener(this);

        //Dorpdown Keperluan
        kep_spinner = (Spinner) findViewById(R.id.keperluan_spinner);

        // Create an ArrayAdapter using the string array and a default spinner
        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter
                .createFromResource(this, R.array.keperluan_array,
                        android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        staticAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        kep_spinner.setAdapter(staticAdapter);

        if(!isNetworkAvailable()){
            Toast.makeText(AddActivity.this, "Not Connection Internet...", Toast.LENGTH_LONG).show();
        }
    }

    private void TambahData(){
        // Ubah setiap View EditText ke tipe Data String
        final String nama = editTextNama.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        final String tlp = editTextTlp.getText().toString().trim();
        final String perusahan = editTextPerusahaan.getText().toString().trim();
        final String keterangan = editTextKeterangan.getText().toString().trim();
        final String keperluan = kep_spinner.getSelectedItem().toString();
        // Pembuatan Class AsyncTask yang berfungsi untuk koneksi ke Database Server
        //Toast.makeText(MainActivity.this, keperluan, Toast.LENGTH_LONG).show();
        class TambahData extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(AddActivity.this,"Proses Kirim Data...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();

                if(s == null){
                    Toast.makeText(AddActivity.this, "Something Wrong !!!", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(AddActivity.this, s, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();
                // Sesuaikan bagian ini dengan field di tabel Mahasiswa
                params.put(Koneksi.KEY_EMP_NAMA,nama);
                params.put(Koneksi.KEY_EMP_EMAIL,email);
                params.put(Koneksi.KEY_EMP_TLP,tlp);
                params.put(Koneksi.KEY_EMP_PERUSAHAAN,perusahan);
                params.put(Koneksi.KEY_EMP_KEPERLUAN,keperluan);
                params.put(Koneksi.KEY_EMP_KETERANGAN,keterangan);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Koneksi.URL_ADD, params);

                return res;
            }
        }
        // Jadikan Class TambahData Sabagai Object Baru
        TambahData ae = new TambahData();
        ae.execute();
    }

    private void cekRequired() {

        // Reset errors.
        editTextNama.setError(null);
        editTextEmail.setError(null);
        editTextTlp.setError(null);
        editTextPerusahaan.setError(null);
        editTextKeterangan.setError(null);

        // Store values at the time of the login attempt.
        String nama = editTextNama.getText().toString();
        String email = editTextEmail.getText().toString();
        String tlp = editTextTlp.getText().toString();
        String perusahaan = editTextPerusahaan.getText().toString();
        String keterangan = editTextKeterangan.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid nama.
        if (TextUtils.isEmpty(nama)) {
            editTextNama.setError(getString(R.string.error_field_required));
            focusView = editTextNama;
            cancel = true;
        }

        // Check for a valid email address.
        else if (TextUtils.isEmpty(email)) {
            editTextEmail.setError(getString(R.string.error_field_required));
            focusView = editTextEmail;
            cancel = true;
        } else if (!isEmailValid(email)) {
            editTextEmail.setError(getString(R.string.error_invalid_email));
            focusView = editTextEmail;
            cancel = true;
        }

        // Check for a valid perusahaan.
        else if (TextUtils.isEmpty(tlp)) {
            editTextTlp.setError(getString(R.string.error_field_required));
            focusView = editTextTlp;
            cancel = true;
        }

        // Check for a valid tlp.
        else if (TextUtils.isEmpty(perusahaan)) {
            editTextPerusahaan.setError(getString(R.string.error_field_required));
            focusView = editTextPerusahaan;
            cancel = true;
        }

        // Check for a valid tlp.
        else if (TextUtils.isEmpty(keterangan)) {
            editTextKeterangan.setError(getString(R.string.error_field_required));
            focusView = editTextKeterangan;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            if (!isNetworkAvailable()) {
                Toast.makeText(this, "Not Connection Internet...", Toast.LENGTH_LONG).show();
            }else if(isConnectedToServer(Koneksi.URL_ADD, 2000)){
                Toast.makeText(this, "Server Not Available...", Toast.LENGTH_LONG).show();
            } else{
                TambahData();
                editTextNama.setText("");
                editTextEmail.setText("");
                editTextTlp.setText("");
                editTextPerusahaan.setText("");
                editTextKeterangan.setText("");
            }

        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    //put this code in an asynctask and call it there
    public boolean isConnectedToServer(String url, int timeout) {
        try{
            URL myUrl = new URL(url);
            URLConnection connection = myUrl.openConnection();
            connection.setConnectTimeout(timeout);
            connection.connect();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void onClick(View v) {
        if(v == buttonAdd){
            cekRequired();
        }
        if(v == buttonView){
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
    }
}
