package net.redlinesoft.taptaptapfree;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import mobi.vserv.android.ads.ViewNotEmptyException;
import mobi.vserv.android.ads.VservAd;
import mobi.vserv.android.ads.VservController;
import mobi.vserv.android.ads.VservManager;


public class MainActivity extends Activity {

    GameHelper gameHelper;
    SharedPreferences gameScore;

    ImageButton imageButton1, imageButton2;
    int ansButton = 0;
    int colorButton0 = 0;
    int colorButton1 = 0;
    int intLevel = 1;
    int intScore = 0;
    TextView txtScore;
    TextView txtLevel;
    private View decorView;
    Timer timer = new Timer();
    TimerTask timetask, timeremain;
    Handler handler = new Handler();
    Context context = this;

    // Vserv Ads
    private FrameLayout adView;
    private VservAd adObject;
    private VservController controller;
    private VservManager manager;
    // test zone
    //private static final String BANNER_ZONE = "20846";
    //private static final String BILLBOARD_ZONE = "8063";
    // my zone
    private static final String BANNER_ZONE = "009ec7c5";
    private static final String BILLBOARD_ZONE = "7799f753";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // hide action bar
        hideActionBar();

        // load ads
        adView = (FrameLayout) findViewById(R.id.container);
        /*
        manager = VservManager.getInstance(context);
        manager.setShowAt(AdPosition.START);
        manager.setCacheNextAd(true);
        manager.displayAd(BILLBOARD_ZONE, AdOrientation.LANDSCAPE);
        */

        if (adView != null) {
            adView.removeAllViews();
        }
        VservManager renderAdManager = VservManager.getInstance(context);
        try {
            controller = renderAdManager.renderAd(BANNER_ZONE, adView);
        } catch (ViewNotEmptyException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
        }

        // init var
        txtScore = (TextView) findViewById(R.id.txtScore);
        txtLevel = (TextView) findViewById(R.id.txtLevel);

        imageButton1 = (ImageButton) findViewById(R.id.imgButton0);
        imageButton2 = (ImageButton) findViewById(R.id.imgButton1);

        // load question
        loadData();

        // start timer
        doTimer();

        // click listener
        imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateScore(1);
            }
        });
        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateScore(2);
            }
        });
    }

    int GAME_TIMEOUT = 120, GAME_TIMETICK = 0;

    private void doTimer() {
        timetask = new TimerTask() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
        };

        timer.schedule(timetask, 0, (GAME_TIMEOUT * 1000));

        timeremain = new TimerTask() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //Log.d("Log", "TICK " + GAME_TIMETICK + "=" + GAME_TIMEOUT);
                        if (GAME_TIMETICK == GAME_TIMEOUT) {
                            stopTask();
                            //Toast.makeText(getApplicationContext(), "Booo!", Toast.LENGTH_SHORT).show();
                        }
                        setTimeOut(GAME_TIMETICK);
                        GAME_TIMETICK++;
                    }
                });
            }
        };
        timer.schedule(timeremain, 0, 1000);
    }

    public void stopTask() {
        showVibrate();
        setCurrentScore(intScore);
        timeremain.cancel();
        timer.cancel();
        timetask.cancel();
        showScoreDialog();
    }

    public void showVibrate() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(100);
    }

    public void showScoreDialog() {
        // show final score
        String messageScore = getString(R.string.dialog_message) + " " + String.valueOf(intScore);
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle(R.string.dialog_title)
                .setMessage(messageScore)
                .setCancelable(false)
                .setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).show();
    }

    public void setTimeOut(int timeOut) {
        txtLevel.setText(String.valueOf(GAME_TIMEOUT - timeOut) + "s");
    }

    private void setCurrentScore(int score) {
        gameScore = getSharedPreferences("SCORE", 0);
        SharedPreferences.Editor editor = gameScore.edit();
        editor.putInt("current", score);
        editor.commit();
    }

    private void hideActionBar() {
        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                    decorView.setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

                } else {
                    decorView.setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                                    | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

                }
            }
        });
    }

    private void loadData() {
        int randomNum1, randomNum2;
        ansButton = (int) (Math.random() * 2 + 1);
        randomNum1 = 0 + (int) (Math.random() * 4);
        randomNum2 = (randomNum1 + 5);

        String[] colorString = getResources().getStringArray(R.array.color);

        //Toast.makeText(getApplicationContext(),String.valueOf(ansButton), Toast.LENGTH_SHORT).show();

        if (ansButton == 1) {
            imageButton2.setBackgroundColor(Color.parseColor("#" + colorString[randomNum2]));
            imageButton1.setBackgroundColor(Color.parseColor("#" + colorString[randomNum1]));
        } else {
            imageButton2.setBackgroundColor(Color.parseColor("#" + colorString[randomNum1]));
            imageButton1.setBackgroundColor(Color.parseColor("#" + colorString[randomNum2]));
        }

    }


    private void updateScore(int ansIndex) {

        // update score
        if (ansIndex == (ansButton)) {
            //Toast.makeText(getApplicationContext(),"Yeah!", Toast.LENGTH_SHORT).show();
            intScore = intScore + 1;
        } else {
            //Toast.makeText(getApplicationContext(), "Booo!", Toast.LENGTH_SHORT).show();
            stopTask();
        }

        // show score 
        txtScore.setText(String.valueOf(intScore));

        // check game over
        if (intScore > 0) {
            // load new data
            loadData();
        } else {
            // reset data
            intLevel = 1;
            intScore = 0;
        }

    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus) {
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            );
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
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

}
