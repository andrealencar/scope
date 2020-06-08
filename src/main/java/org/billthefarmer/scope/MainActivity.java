////////////////////////////////////////////////////////////////////////////////
//
//  Scope - An Android scope written in Java.
//
//  Copyright (C) 2014	Bill Farmer
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU General Public License for more details.
//
//  You should have received a copy of the GNU General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.
//
//  Bill Farmer	 william j farmer [at] yahoo [dot] co [dot] uk.
//
///////////////////////////////////////////////////////////////////////////////

package org.billthefarmer.scope;
import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import org.billthefarmer.scope.calc.CalcActivity;
import org.billthefarmer.scope.database.PostDatabase;
import org.billthefarmer.scope.database.UserDatabase;
import org.billthefarmer.scope.docs.docListActivity;
import org.billthefarmer.scope.models.Post;
import org.billthefarmer.scope.models.StrapiPost;
import org.billthefarmer.scope.models.User;
import org.billthefarmer.scope.network.GetDataService;
import org.billthefarmer.scope.network.RetrofitClientInstance;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


// MainActivity
public class MainActivity extends AppCompatActivity
{
    private static final String PREF_INPUT = "pref_input";
    private static final String PREF_SCREEN = "pref_screen";
    private static final String PREF_DARK = "pref_dark";

    private static final String TAG = "Scope";
    private static final String STATE = "state";

    private static final String SINGLE = "single";
    private static final String TIMEBASE = "timebase";

    private static final String START = "start";
    private static final String INDEX = "index";
    private static final String LEVEL = "level";
    PostDatabase mDb;



    private static final float values[] =
    {
        0.1f, 0.2f, 0.5f, 1.0f,
        2.0f, 5.0f, 10.0f, 20.0f,
        50.0f, 100.0f, 200.0f, 500.0f
    };

    private static final String strings[] =
    {
        "0.1 ms", "0.2 ms", "0.5 ms",
        "1.0 ms", "2.0 ms", "5.0 ms",
        "10 ms", "20 ms", "50 ms",
        "0.1 sec", "0.2 sec", "0.5 sec"
    };

    private static final int counts[] =
    {
        256, 512, 1024, 2048,
        4096, 8192, 16384, 32768,
        65536, 131072, 262144, 524288
    };

    private static final int REQUEST_PERMISSIONS = 1;

    protected static final int SIZE = 20;
    protected static final int DEFAULT_TIMEBASE = 3;
    protected static final float SMALL_SCALE = 200;
    protected static final float LARGE_SCALE = 200000;

    protected int timebase;
    private Scope scope;
    private XScale xscale;
    private YScale yscale;
    private Unit unit;
    private Audio audio;
    private Toast toast;
    private SubMenu submenu;
    private boolean dark;

    // On create
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getPreferences();

        if (!dark)
            setTheme(R.style.AppDarkTheme);

        setContentView(R.layout.activity_main);
        scope = findViewById(R.id.scope);
        xscale = findViewById(R.id.xscale);
        yscale = findViewById(R.id.yscale);
        unit = findViewById(R.id.unit);
        ActionBar actionBar = getSupportActionBar();
        //actionBar.hide();
        actionBar.setTitle("Osciloscópio");
        updtatePosts();

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        // Create audio
        audio = new Audio();

        if (scope != null)
            scope.audio = audio;

        // Set timebase index
        timebase = DEFAULT_TIMEBASE;

        // Set up scale
        if (scope != null && xscale != null && unit != null)
        {
            scope.scale = values[timebase];
            xscale.scale = scope.scale;
            xscale.step = 1000 * xscale.scale;
            unit.scale = scope.scale;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation ==Configuration.ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else if (newConfig.orientation ==Configuration.ORIENTATION_PORTRAIT){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    // onCreateOptionsMenu
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuItem item;

        // Inflate the menu; this adds items to the action bar if it
        // is present.
        getMenuInflater().inflate(R.menu.main, menu);

        // Timebase
        item = menu.findItem(R.id.timebase);
        if (timebase != DEFAULT_TIMEBASE)
        {
            if (item.hasSubMenu())
            {
                submenu = item.getSubMenu();
                item = submenu.getItem(timebase);
                if (item != null)
                    item.setChecked(true);
            }
        }

        return true;
    }

    // Restore state
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);

        // Get saved state bundle
        Bundle bundle = savedInstanceState.getBundle(STATE);

        // Timebase
        timebase = bundle.getInt(TIMEBASE, DEFAULT_TIMEBASE);
        setTimebase(timebase, false);

        // Start
        scope.start = bundle.getFloat(START, 0);
        xscale.start = scope.start;
        xscale.postInvalidate();

        // Index
        scope.index = bundle.getFloat(INDEX, 0);

        // Level
        yscale.index = bundle.getFloat(LEVEL, 0);
        yscale.postInvalidate();
    }

    // Save state
    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);

        // State bundle
        Bundle bundle = new Bundle();

        // Timebase
        bundle.putInt(TIMEBASE, timebase);

        // Start
        bundle.putFloat(START, scope.start);

        // Index
        bundle.putFloat(INDEX, scope.index);

        // Level
        bundle.putFloat(LEVEL, yscale.index);

        // Save bundle
        outState.putBundle(STATE, bundle);
    }

    // On options item
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Get id
        int id = item.getItemId();
        switch (id)
        {

        // Timebase
        case R.id.timebase:
            if (item.hasSubMenu())
                submenu = item.getSubMenu();
            break;

        // 0.1 ms
        case R.id.tb0_1ms:
            timebase = 0;
            item.setChecked(true);
            setTimebase(timebase, true);
            break;

        // 0.2 ms
        case R.id.tb0_2ms:
            timebase = 1;
            item.setChecked(true);
            setTimebase(timebase, true);
            break;

        // 0.5 ms
        case R.id.tb0_5ms:
            timebase = 2;
            item.setChecked(true);
            setTimebase(timebase, true);
            break;

        // 1.0 ms
        case R.id.tb1_0ms:
            timebase = 3;
            item.setChecked(true);
            setTimebase(timebase, true);
            break;

        // 2.0 ms
        case R.id.tb2_0ms:
            timebase = 4;
            item.setChecked(true);
            setTimebase(timebase, true);
            break;

        // 5.0 ms
        case R.id.tb5_0ms:
            timebase = 5;
            item.setChecked(true);
            setTimebase(timebase, true);
            break;

        // 10 ms
        case R.id.tb10ms:
            timebase = 6;
            item.setChecked(true);
            setTimebase(timebase, true);
            break;

        // 20 ms
        case R.id.tb20ms:
            timebase = 7;
            item.setChecked(true);
            setTimebase(timebase, true);
            break;

        // 50 ms

        case R.id.tb50ms:
            timebase = 8;
            item.setChecked(true);
            setTimebase(timebase, true);
            break;

        // 0.1 sec
        case R.id.tb0_1sec:
            timebase = 9;
            item.setChecked(true);
            setTimebase(timebase, true);
            break;

        // 0.2 sec
        case R.id.tb0_2sec:
            timebase = 10;
            item.setChecked(true);
            setTimebase(timebase, true);
            break;

        // 0.5 sec
        case R.id.tb0_5sec:
            timebase = 11;
            item.setChecked(true);
            setTimebase(timebase, true);
            break;


        //Calc
            case R.id.action_calc:
                return onCalcClick(item);

        //Docs
            case R.id.action_docs:
                return onDocsClick(item);

        // Start
        case R.id.start:
            if (scope != null && xscale != null)
            {
                scope.start = 0;
                scope.index = 0;
                xscale.start = 0;
                xscale.postInvalidate();
                yscale.index = 0;
                yscale.postInvalidate();
            }
            break;

        // End
        case R.id.end:
            if (scope != null && xscale != null)
            {
                while (scope.start < audio.length)
                    scope.start += xscale.step;
                scope.start -= xscale.step;
                xscale.start = scope.start;
                xscale.postInvalidate();
            }
            break;

        // Spectrum
        case R.id.action_spectrum:
            return onSpectrumClick(item);

        // Help
        case R.id.action_help:
            return onHelpClick(item);

        // Settings
        case R.id.action_settings:
            return onSettingsClick(item);

        default:
            return false;
        }

        return true;
    }

    void updtatePosts(){

        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<List<StrapiPost>> call = service.ListPosts();
        mDb = PostDatabase.getInstance(getApplicationContext());
        call.enqueue(new Callback<List<StrapiPost>>() {
            @Override
            public void onResponse(Call<List<StrapiPost>> call, Response<List<StrapiPost>> response) {

                List<StrapiPost> posts = response.body();
                for (int i = 0; i < posts.size(); i++) {

                    Post post   = new Post();
                    post.id     = posts.get(i).getId();
                    post.title  = posts.get(i).getTitle();
                    post.text   = posts.get(i).getText();

                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            Post PostFind = mDb.postDao().findById(post.id);
                            String id = PostFind.id;
                            if(id == null){
                                mDb.postDao().insertPost(post);
                                Log.d("ADD post -->>", PostFind.title);
                            }else{
                                post.uid = PostFind.uid;
                                mDb.postDao().updatePost(post);
                                Log.d("UPdate post -->>", post.title);
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<StrapiPost>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Sem concexão com a Internet!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    // On docs click
    private boolean onDocsClick(MenuItem item)
    {
        Intent intent = new Intent(this, docListActivity.class);
        startActivity(intent);
        return true;
    }

    // On calc click
    private boolean onCalcClick(MenuItem item)
    {
        Intent intent = new Intent(this, CalcActivity.class);
        startActivity(intent);
        return true;
    }

    // On settings click
    private boolean onSettingsClick(MenuItem item)
    {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);

        return true;
    }

    // On help click
    private boolean onHelpClick(MenuItem item)
    {
        Intent intent = new Intent(this, HelpActivity.class);
        startActivity(intent);
        return true;
    }

    // On spectrum click
    private boolean onSpectrumClick(MenuItem item)
    {
        Intent intent = new Intent(this, SpectrumActivity.class);
        startActivity(intent);

        return true;
    }

    // Set timebase
    void setTimebase(int timebase, boolean show)
    {
        if (scope != null && xscale != null && unit != null)
        {
            // Set up scale
            scope.scale = values[timebase];
            xscale.scale = scope.scale;
            xscale.step = 1000 * xscale.scale;
            unit.scale = scope.scale;

            // Set up scope points
            scope.points = timebase == 0;

            // Reset start
            scope.start = 0;
            xscale.start = 0;

            // Update display
            xscale.postInvalidate();
            unit.postInvalidate();
        }

        // Show timebase
        if (show)
            showTimebase(timebase);
    }

    // Show timebase
    void showTimebase(int timebase)
    {
        String text = "Timebase: " + strings[timebase];
        showToast(text);
    }

    // Show toast
    void showToast(int key)
    {
        Resources resources = getResources();
        String text = resources.getString(key);

        showToast(text);
    }

    // Show toast
    void showToast(String text)
    {
        // Cancel the last one
        if (toast != null)
            toast.cancel();

        // Make a new one
        toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    // On Resume
    @Override
    protected void onResume()
    {
        super.onResume();

        boolean theme = dark;

        // Get preferences
        getPreferences();

        if (theme != dark && Build.VERSION.SDK_INT != Build.VERSION_CODES.M)
            recreate();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (checkSelfPermission(Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]
                    {Manifest.permission.RECORD_AUDIO}, REQUEST_PERMISSIONS);

                return;
            }
        }

        // Start the audio thread
        audio.start();
    }

    // onRequestPermissionsResult
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults)
    {
        if (requestCode == REQUEST_PERMISSIONS)
            for (int i = 0; i < grantResults.length; i++)
                if (permissions[i].equals(Manifest.permission.RECORD_AUDIO) &&
                    grantResults[i] == PackageManager.PERMISSION_GRANTED)
                {
                    // Granted, recreate or start audio thread
                    if (Build.VERSION.SDK_INT != Build.VERSION_CODES.M)
                        recreate();

                    else
                        audio.start();
                }
    }

    // On pause
    @Override
    protected void onPause()
    {
        super.onPause();

        // Save preferences
        savePreferences();

        // Stop audio thread
        audio.stop();
    }

    // Get preferences
    void getPreferences()
    {
        SharedPreferences preferences =
            PreferenceManager.getDefaultSharedPreferences(this);

        // Set preferences
        if (audio != null)
        {
            audio.input =
                Integer.parseInt(preferences.getString(PREF_INPUT, "0"));
        }

        boolean screen = preferences.getBoolean(PREF_SCREEN, false);

        // Check screen
        Window window = getWindow();
        if (screen)
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        else
            window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        dark = preferences.getBoolean(PREF_DARK, false);
    }

    // Save preferences
    void savePreferences()
    {
        SharedPreferences preferences =
            PreferenceManager.getDefaultSharedPreferences(this);

        // TODO
    }

    // Show alert
    void showAlert(int appName, int errorBuffer)
    {
        // Create an alert dialog builder
        AlertDialog.Builder builder =
            new androidx.appcompat.app.AlertDialog.Builder(this);

        // Set the title, message and button
        builder.setTitle(appName);
        builder.setMessage(errorBuffer);
        builder.setNeutralButton(android.R.string.ok,
                                 (dialog, which) ->
        {
            // Dismiss dialog
            dialog.dismiss();
        });

        // Create the dialog
        AlertDialog dialog = builder.create();

        // Show it
        dialog.show();
    }

    // Audio
    protected class Audio implements Runnable
    {
        // Preferences
        protected boolean bright;
        protected boolean single;
        protected boolean trigger;

        protected int input;
        protected int sample;

        // Data
        protected Thread thread;
        protected short data[];
        protected long length;

        // Private data
        private static final int SAMPLES = 524288;
        private static final int FRAMES = 4096;

        private static final int INIT = 0;
        private static final int FIRST = 1;
        private static final int NEXT = 2;
        private static final int LAST = 3;

        private AudioRecord audioRecord;
        private short buffer[];

        // Constructor
        protected Audio()
        {
            buffer = new short[FRAMES];
            data = new short[SAMPLES];
        }

        // Start audio
        protected void start()
        {
            // Start the thread
            thread = new Thread(this, "Audio");
            thread.start();
        }

        // Run
        @Override
        public void run()
        {
            processAudio();
        }

        // Stop
        protected void stop()
        {
            // Stop and release the audio recorder
            cleanUpAudioRecord();

            Thread t = thread;
            thread = null;

            // Wait for the thread to exit
            while (t != null && t.isAlive())
                Thread.yield();
        }

        // Stop and release the audio recorder
        private void cleanUpAudioRecord()
        {
            if (audioRecord != null &&
                    audioRecord.getState() == AudioRecord.STATE_INITIALIZED)
            {
                try
                {
                    if (audioRecord.getRecordingState() ==
                            AudioRecord.RECORDSTATE_RECORDING)
                        audioRecord.stop();

                    audioRecord.release();
                }
                catch (Exception e)
                {
                }
            }
        }

        // Process Audio
        protected void processAudio()
        {
            // Assume the output sample rate will work on the input as
            // there isn't an AudioRecord.getNativeInputSampleRate()
            sample =
                AudioTrack.getNativeOutputSampleRate(AudioManager.STREAM_MUSIC);

            // Get buffer size
            int size =
                AudioRecord.getMinBufferSize(sample,
                                             AudioFormat.CHANNEL_IN_MONO,
                                             AudioFormat.ENCODING_PCM_16BIT);
            // Give up if it doesn't work
            if (size == AudioRecord.ERROR_BAD_VALUE ||
                    size == AudioRecord.ERROR ||
                    size <= 0)
            {
                runOnUiThread(() -> showAlert(R.string.app_name,
                                              R.string.error_buffer));

                thread = null;
                return;
            }

            // Create the AudioRecord object
            try
            {
                audioRecord =
                    new AudioRecord(input, sample,
                                    AudioFormat.CHANNEL_IN_MONO,
                                    AudioFormat.ENCODING_PCM_16BIT,
                                    size);
            }

            // Exception
            catch (Exception e)
            {
                runOnUiThread(() -> showAlert(R.string.app_name,
                                              R.string.error_init));

                thread = null;
                return;
            }

            // Check audiorecord
            // Check state
            int state = audioRecord.getState();

            if (state != AudioRecord.STATE_INITIALIZED)
            {
                runOnUiThread(() -> showAlert(R.string.app_name,
                                              R.string.error_init));

                audioRecord.release();
                thread = null;
                return;
            }

            // Start recording
            audioRecord.startRecording();

            int index = 0;
            int count = 0;

            state = INIT;
            short last = 0;

            // Continue until the thread is stopped
            while (thread != null)
            {
                // Read a buffer of data
                size = audioRecord.read(buffer, 0, FRAMES);

                // Stop the thread if no data or error state
                if (size <= 0)
                {
                    thread = null;
                    break;
                }

                // State machine for sync and copying data to display buffer
                switch (state)
                {
                // INIT: waiting for sync
                case INIT:

                    index = 0;

                    if (bright)
                        state++;

                    else
                    {
                        if (single && !trigger)
                            break;

                        // Calculate sync level
                        float level = -yscale.index * scope.yscale;

                        // Initialise sync
                        int dx;

                        // Sync polarity
                        if (level < 0)
                        {
                            for (int i = 0; i < size; i++)
                            {
                                dx = buffer[i] - last;

                                if (dx < 0 && last > level && buffer[i] < level)
                                {
                                    index = i;
                                    state++;
                                    break;
                                }

                                last = buffer[i];
                            }
                        }
                        else
                        {
                            for (int i = 0; i < size; i++)
                            {
                                dx = buffer[i] - last;

                                if (dx > 0 && last < level && buffer[i] > level)
                                {
                                    index = i;
                                    state++;
                                    break;
                                }

                                last = buffer[i];
                            }
                        }
                    }

                    // No sync, try next time
                    if (state == INIT)
                        break;

                    // Reset trigger
                    if (single && trigger)
                        trigger = false;

                // FIRST: First chunk of data
                case FIRST:

                    // Update count
                    count = counts[timebase];
                    length = count;

                    // Copy data
                    System.arraycopy(buffer, index, data, 0, size - index);
                    index = size - index;

                    // If done, wait for sync again
                    if (index >= count)
                        state = INIT;

                    // Else get some more data next time
                    else
                        state++;
                    break;

                // NEXT: Subsequent chunks of data
                case NEXT:

                    // Copy data
                    System.arraycopy(buffer, 0, data, index, size);
                    index += size;

                    // Done, wait for sync again
                    if (index >= count)
                        state = INIT;

                    // Else if last but one chunk, get last chunk next time
                    else if (index + size >= count)
                        state++;
                    break;

                // LAST: Last chunk of data
                case LAST:

                    // Copy data
                    System.arraycopy(buffer, 0, data, index, count - index);

                    // Wait for sync next time
                    state = INIT;
                    break;
                }

                // Update display
                scope.postInvalidate();
            }

            // Stop and release the audio recorder
            cleanUpAudioRecord();
        }
    }
}
