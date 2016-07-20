package com.postel.user.logbook2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.postel.user.logbook2.adapter.ListViewAdapter;
import com.postel.user.logbook2.config.Koneksi;
import com.postel.user.logbook2.config.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // Declare Variables
    JSONObject jsonobject;
    JSONArray jsonarray;
    ListView listview;
    ListViewAdapter adapter;
    ProgressDialog mProgressDialog;
    ArrayList<HashMap<String, String>> arraylist;
    private SwipeRefreshLayout swipeRefreshLayout;

    // Variabel untuk format String JSON
    private String JSON_STRING;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);

        // Inisialiasi ListView
        listview = (ListView) findViewById(R.id.listview);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(!isNetworkAvailable()){
                    Toast.makeText(MainActivity.this, "Not Connection Internet...", Toast.LENGTH_LONG).show();
                }

                //Checking Connection
                if (!isNetworkAvailable()) {
                    Toast.makeText(MainActivity.this, "Not Connection Internet...", Toast.LENGTH_LONG).show();
                }else if(isConnectedToServer(Koneksi.URL_GET_ALL, 2000)){
                    Toast.makeText(MainActivity.this, "Server Not Available...", Toast.LENGTH_LONG).show();
                } else{
                    //new DownloadJSON().execute();
                    getJSON();
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if(!isNetworkAvailable()){
            Toast.makeText(MainActivity.this, "Not Connection Internet...", Toast.LENGTH_LONG).show();
        }

        //Checking Connection
        if (!isNetworkAvailable()) {
            Toast.makeText(MainActivity.this, "Not Connection Internet...", Toast.LENGTH_LONG).show();
        }else if(isConnectedToServer(Koneksi.URL_GET_ALL, 2000)){
            Toast.makeText(MainActivity.this, "Server Not Available...", Toast.LENGTH_LONG).show();
        } else{
            //new DownloadJSON().execute();
            getJSON();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        recreate();
    }

    // Buat Methode untuk ambil data dari Server
    private void TampilData(){
        // Data dalam bentuk Array kemudian akan kita ubah menjadi JSON Object
        JSONObject jsonObject = null;
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        try {

            if(JSON_STRING == ""){
                Toast.makeText(MainActivity.this, "Get data failed...!!! ", Toast.LENGTH_LONG).show();
            }

            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Koneksi.TAG_JSON_ARRAY);
            // FOR untuk ambil data
            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                // TAG_ID dan TAG_NAME adalah variabel yang ada di Class Config.java,

                String status = null;
                String ket = jo.getString(Koneksi.TAG_STATUS);
                if( Integer.parseInt(ket) == 1){
                    status = "Sign In";
                }else{
                    status = "Sign Out";
                }

                //String nama = jo.getString(Koneksi.TAG_NAMA);
                //String jam_masuk = jo.getString(Koneksi.TAG_JAM_MASUK);
                //String foto = jo.getString(Koneksi.TAG_FOTO);

                HashMap<String,String> map = new HashMap<>();
                map.put(Koneksi.TAG_ID, jo.getString("id"));
                map.put(Koneksi.TAG_NAMA, jo.getString("nama"));
                map.put(Koneksi.TAG_EMAIL, jo.getString("email"));
                map.put(Koneksi.TAG_TLP, jo.getString("tlp"));
                map.put(Koneksi.TAG_PERUSAHAAN, jo.getString("perusahaan"));
                map.put(Koneksi.TAG_KEPERLUAN, jo.getString("keperluan"));
                map.put(Koneksi.TAG_KETERANGAN, jo.getString("keterangan"));
                map.put(Koneksi.TAG_STATUS, status);
                map.put(Koneksi.TAG_WAKTU, jo.getString("jam_masuk")+" s/d "+jo.getString("jam_keluar"));
                map.put(Koneksi.TAG_FOTO, jo.getString("foto"));
                map.put(Koneksi.TAG_KTP, jo.getString("ktp"));
                map.put(Koneksi.TAG_TTD, jo.getString("ttd"));
                list.add(map);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Tampilkan datanya dalam Layout Lihat Data
        /*ListAdapter adapter = new SimpleAdapter(
                LihatData.this, list, R.layout.lihatdata,
                new String[]{Config.TAG_ID,Config.TAG_NAME},
                new int[]{R.id.id, R.id.name});*/
        ListViewAdapter adapter = new ListViewAdapter(MainActivity.this, list);
        // Tampilkan dalam bentuk ListView
        listview.setAdapter(adapter);
    }

    // Methode ambil data JSON yang kita definisikan dalam bentuk AsyncTask
    private void getJSON(){
        class GetJSON extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MainActivity.this,"Pengambilan Data","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                // Panggil method tampil data
                TampilData();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                // Proses nya sesuai alamat URL letak script PHP yang kita set di Class Config.java
                String s = rh.sendGetRequest(Koneksi.URL_GET_ALL);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_add) {
            startActivity(new Intent(this, AddActivity.class));
        }
        /*else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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


}
