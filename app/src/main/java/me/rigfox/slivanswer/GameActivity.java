package me.rigfox.slivanswer;

import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity {
    Sensors sensors;
    Game game;
    Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent intent = getIntent();

        TextView screen = (TextView) findViewById(R.id.screen);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        sensors = new Sensors(this);

        game = new Game(screen, sensors, vibrator, intent, this);

        game.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensors.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensors.onPause();
    }
}
