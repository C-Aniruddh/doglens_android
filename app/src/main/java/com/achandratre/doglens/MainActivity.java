package com.achandratre.doglens;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.achandratre.doglens.helper.AboutHelper;
import com.afollestad.ason.Ason;
import com.afollestad.bridge.Bridge;
import com.afollestad.bridge.BridgeException;
import com.afollestad.bridge.Callback;
import com.afollestad.bridge.MultipartForm;
import com.afollestad.bridge.Pipe;
import com.afollestad.bridge.ProgressCallback;
import com.afollestad.bridge.Request;
import com.afollestad.bridge.Response;
import com.afollestad.materialcamera.MaterialCamera;
import com.afollestad.materialdialogs.MaterialDialog;
import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

import es.dmoral.toasty.Toasty;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private final static int CAMERA_RQ = 6969;
    private FirebaseAnalytics mFirebaseAnalytics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(" ");
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        LottieAnimationView animationView = (LottieAnimationView) findViewById(R.id.animation_view);
        animationView.setAnimation("camera.json");
        animationView.loop(true);
        animationView.playAnimation();

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                //  Initialize SharedPreferences
                SharedPreferences getPrefs = PreferenceManager
                        .getDefaultSharedPreferences(getBaseContext());

                //  Create a new boolean and preference and set it to true
                boolean isFirstStart = getPrefs.getBoolean("firstStart", true);

                //  If the activity has never started before...
                if (isFirstStart) {

                    //  Launch app intro
                    Intent i = new Intent(MainActivity.this, IntroActivity.class);
                    startActivity(i);

                    //  Make a new preferences editor
                    SharedPreferences.Editor e = getPrefs.edit();

                    //  Edit preference to make it false because we don't want this to run again
                    e.putBoolean("firstStart", false);

                    //  Apply changes
                    e.apply();
                }
            }
        });

        // Start the thread
        t.start();

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/circular-book.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        File saveDir = null;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            // Only use external storage directory if permission is granted, otherwise cache directory is used by default
            saveDir = new File(Environment.getExternalStorageDirectory(), "DogLens");
            saveDir.mkdirs();
        }
        FloatingActionButton buttonOne = (FloatingActionButton) findViewById(R.id.fab);
        final File finalSaveDir = saveDir;
        buttonOne.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                new MaterialCamera(MainActivity.this)
                        .allowRetry(true)
                        .saveDir(finalSaveDir)
                        .videoPreferredAspect(16f/9f)
                        .stillShot()
                        .start(CAMERA_RQ);

            }
        });


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    private String readableFileSize(long size) {
        if (size <= 0) return size + " B";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.##").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    private String fileSize(File file) {
        return readableFileSize(file.length());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Received recording or error from MaterialCamera
        if (requestCode == CAMERA_RQ) {
            if (resultCode == RESULT_OK) {

                final File file = new File(data.getData().getPath());
                Toast.makeText(this, "Saved to: " + data.getDataString(), Toast.LENGTH_LONG).show();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);

                MultipartForm form = null;

                try {
                    form = new MultipartForm()
                            .add("song_upload", String.valueOf((new File(file.getName()))), Pipe.forFile(new File(file.getAbsolutePath())));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                /*Request request = Bridge
                        .post("http://159.203.185.68:5000/submit")
                        .body(form)
                        .request();*/
                    /*Response response = Bridge
                            .post(VC.SERVER + "submit")
                            .body(form)
                            .response();*/
                boolean showMinMax = false;
                final MaterialDialog dialog = new MaterialDialog.Builder(this)
                        .cancelable(false)
                        .title("Processing")
                        .customView(R.layout.upload_animation, true)
                        //.content("Please wait while we upload and process your file")
                        //.progress(false, 100, showMinMax)
                        .show();
                Bridge
                        .post(Constants.SERVER + "submit")
                        .cancellable(false)
                        .body(form)
                        .uploadProgress(new ProgressCallback() {
                            @Override
                            public void progress(Request request, int current, int total, int percent) {
                                //dialog.setProgress(percent);
                            }
                        })
                        .request(new Callback() {
                            @Override
                            public void response(Request request, Response response, BridgeException e) {
                                // Use response
                                Ason responseAsonObject = null;
                                try {
                                    if (response != null){
                                        responseAsonObject = response.asAsonObject();
                                    } else{
                                        dialog.dismiss();
                                        Snackbar.make(getWindow().getDecorView().getRootView(), "Could not reach the server", Snackbar.LENGTH_LONG)
                                                .setAction("Action", null).show();
                                    }
                                } catch (BridgeException e1) {

                                    e1.printStackTrace();
                                }
                                if (responseAsonObject != null){
                                    dialog.dismiss();
                                    String dog_breed = responseAsonObject.getString("breed_detected", "Dog");
                                    String predict_one = responseAsonObject.getString("predict_one", "Dog");
                                    String predict_two = responseAsonObject.getString("predict_two", "Dog");
                                    String file_path = file.getAbsolutePath();
                                    Intent redirectToBreed = new Intent(MainActivity.this, BreedActivity.class);
                                    redirectToBreed.putExtra("file_path", file_path);
                                    redirectToBreed.putExtra("dog_breed", dog_breed);
                                    redirectToBreed.putExtra("predict_one", predict_one);
                                    redirectToBreed.putExtra("predict_two", predict_two);
                                    redirectToBreed.putExtra("filename", String.valueOf((new File(file.getName()))));
                                    startActivity(redirectToBreed);
                                } else{
                                    Snackbar.make(getWindow().getDecorView().getRootView(), "Could not reach the server", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                }

                            }
                        })
                        .response();
                /*final File file = new File(data.getData().getPath());
                Toast.makeText(this, String.format("Saved to: %s, size: %s",
                        file.getAbsolutePath(), fileSize(file)), Toast.LENGTH_LONG).show();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);

                MultipartForm form = null;
                try {
                    form = new MultipartForm()
                            .add("song_upload", file.getName(), Pipe.forFile(new File(file.getAbsolutePath())));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                    boolean showMinMax = true;
                    final MaterialDialog dialog = new MaterialDialog.Builder(this)
                            .title("Uploading")
                            .content("Please wait while we upload and process your file...")
                            .progress(false, 100, showMinMax)
                            .show();
                    Bridge
                        .post(Constants.SERVER + "submit")
                        .body(form)
                        .uploadProgress(new ProgressCallback() {
                            @Override
                            public void progress(Request request, int current, int total, int percent) {
                                dialog.setProgress(percent);
                            }
                        })
                        .request(new Callback() {
                            @Override
                            public void response(Request request, Response response, BridgeException e) {
                                // Use response
                                Ason responseAsonObject = null;
                                try {
                                    if (response != null){
                                        responseAsonObject = response.asAsonObject();
                                    } else{
                                        dialog.dismiss();
                                        Snackbar.make(getWindow().getDecorView().getRootView(), "Could not reach the server", Snackbar.LENGTH_LONG)
                                                .setAction("Action", null).show();
                                    }
                                } catch (BridgeException e1) {

                                    e1.printStackTrace();
                                }
                                if (responseAsonObject != null){
                                    dialog.dismiss();

                                    String dog_breed = responseAsonObject.getString("breed_detected", "Dog");
                                    String predict_one = responseAsonObject.getString("predict_one", "Dog");
                                    String predict_two = responseAsonObject.getString("predict_two", "Dog");
                                    String file_path = file.getAbsolutePath();
                                    Intent redirectToBreed = new Intent(MainActivity.this, BreedActivity.class);
                                    redirectToBreed.putExtra("file_path", file_path);
                                    redirectToBreed.putExtra("dog_breed", dog_breed);
                                    redirectToBreed.putExtra("predict_one", predict_one);
                                    redirectToBreed.putExtra("predict_two", predict_two);
                                    redirectToBreed.putExtra("filename", String.valueOf((new File(file.getName()))));
                                    startActivity(redirectToBreed);
                                } else{
                                    Snackbar.make(getWindow().getDecorView().getRootView(), "Could not reach the server", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                }

                            }
                        })
                        .response();*/
            } else if (data != null) {
                Exception e = (Exception) data.getSerializableExtra(MaterialCamera.ERROR_EXTRA);
                if (e != null) {
                    e.printStackTrace();
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            //Do Nothing
        } else if (id == R.id.nav_list) {
            String url = "https://github.com/C-Aniruddh/dogLens/blob/master/DOGS.md";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        } else if (id == R.id.nav_request) {
            String url = "https://github.com/C-Aniruddh/dogLens/issues/1";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        } else if (id == R.id.nav_about) {
            Intent aboutAct = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(aboutAct);
        } else if (id == R.id.nav_feedback) {
            Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

            String aEmailList[] = { "c.aniruddh98@gmail.com" };
            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, aEmailList);
            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Dog Lens | App Feedback");

            emailIntent.setType("plain/text");
            emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "My message body.");
            startActivity(emailIntent);
        } else if (id == R.id.nav_demo){
            Intent demo = new Intent(MainActivity.this, IntroActivity.class);
            startActivity(demo);
        } else if (id == R.id.nav_report) {
            String url = "https://github.com/C-Aniruddh/dogLens/issues";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        } else if (id == R.id.nav_exit){
            System.exit(0);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
