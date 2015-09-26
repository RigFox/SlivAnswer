package me.rigfox.slivanswer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity {
    Sensors sensors;
    Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        TextView screen = (TextView) findViewById(R.id.screen);

        sensors = new Sensors(this);

        game = new Game(screen, sensors, this);

        game.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensors.onResume();
    }

    @Override
    protected void onPause() {
        super.onStop();
        sensors.onPause();
    }
}
