package net.redlinesoft.taptaptapfree;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.games.Games;

public class SplashActivity extends BaseGameActivity
        implements View.OnClickListener {

    SharedPreferences gameScore;
    int lastScore;
    private View decorView;
    int colorIndex=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        colorIndex =  (int)(Math.random()*4);
        String[] colorString = getResources().getStringArray(R.array.background);
        findViewById(R.id.splashView).setBackgroundColor(Color.parseColor("#" + colorString[colorIndex+5]));

        // hide action bar
        hideActionBar();

        // loadScore
        Log.d("Log", "Score = " + String.valueOf(loadCurrentScore()));

        // button view
        findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
        findViewById(R.id.imgButtonLeaderboard).setVisibility(View.GONE);
        findViewById(R.id.imgButtonArchivement).setVisibility(View.GONE);

        // button click
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.imgButtonStart).setOnClickListener(this);
        findViewById(R.id.imgButtonArchivement).setOnClickListener(this);
        findViewById(R.id.imgButtonLeaderboard).setOnClickListener(this);

    }

    private int loadCurrentScore() {
        gameScore = getSharedPreferences("SCORE", 0);
        lastScore = gameScore.getInt("current", 0);
        return lastScore;
    }


    private void setArchivement(int score) {

        if (getApiClient().isConnected())
            
        // Submit Leaderboard
        Games.Leaderboards.submitScore(getApiClient(),
                getString(R.string.leaderboard_tap_of_frame),score);

        // calculate score
        if (score >= 5) {
            Games.Achievements.unlock(getApiClient(), getString(R.string.achievement_n00b));
        }

        if (score >= 10) {
            Games.Achievements.unlock(getApiClient(), getString(R.string.achievement_busy_guy));
        }

        if (score >= 50) {
            Games.Achievements.unlock(getApiClient(), getString(R.string.achievement_business_man));
        }

        if (score >= 100) {
            Games.Achievements.unlock(getApiClient(), getString(R.string.achievement_professional));
        }

        if (score >= 110) {
            Games.Achievements.unlock(getApiClient(), getString(R.string.achievement_supper));
        }

        if (score >= 120) {
            Games.Achievements.unlock(getApiClient(), getString(R.string.achievement_excellent));
        }

        if (score >= 140) {
            Games.Achievements.unlock(getApiClient(), getString(R.string.achievement_incredible));
        }

        if (score >= 150) {
            Games.Achievements.unlock(getApiClient(), getString(R.string.achievement_brilliant));
        }

        if (score >= 190) {
            Games.Achievements.unlock(getApiClient(), getString(R.string.achievement_superb));
        }

        //Toast.makeText(this,"Sent Score",Toast.LENGTH_SHORT).show();
        Log.d("Log", "Sent Score!");

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
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.splash, menu);
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

    @Override
    public void onSignInFailed() {
        Toast.makeText(this, "Cannot sign in", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSignInSucceeded() {
        //Toast.makeText(this,"Sign in complete",Toast.LENGTH_SHORT).show();
        Log.d("Log", "Sign in complete");
        findViewById(R.id.sign_in_button).setVisibility(View.GONE);
        findViewById(R.id.imgButtonLeaderboard).setVisibility(View.VISIBLE);
        findViewById(R.id.imgButtonArchivement).setVisibility(View.VISIBLE);

        // set achievement
        setArchivement(loadCurrentScore());

        //loadGame();
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.sign_in_button) {
            beginUserInitiatedSignIn();
        } else if (view.getId() == R.id.imgButtonLeaderboard) {
            startActivityForResult(Games.Leaderboards.getLeaderboardIntent(
                    getApiClient(), getString(R.string.leaderboard_tap_of_frame)), 1);
        } else if (view.getId() == R.id.imgButtonArchivement) {
            startActivityForResult(Games.Achievements.getAchievementsIntent(
                    getApiClient()), 2);
        } else if (view.getId() == R.id.imgButtonStart) {
            loadGame();
        }
    }

    private void loadGame() {
        Log.d("Log", "Load Game");
        //Toast.makeText(this,"Load Game",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
