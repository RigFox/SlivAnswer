package me.rigfox.slivanswer;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class Sensors {
    public static final int ScreenUP = 1;
    public static final int ScreenDown = -1;
    public static final int ScreenFace = 0;

    Context ctx;
    SensorManager sensorManager;
    Sensor sensorAccel;

    private static final int CRIT_AXES = 6;

    Sensors(Context context) {
        ctx = context;
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensorAccel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    public int getScreenPosition() {
        float zAxes = valuesAccelGravity[2];

        if ((zAxes > -CRIT_AXES) && (zAxes < CRIT_AXES)) {
            return ScreenFace;
        } else {
            if (zAxes < -CRIT_AXES) {
                return ScreenDown;
            } else {
                return ScreenUP;
            }
        }
    }

    public void onResume() {
        sensorManager.registerListener(listener, sensorAccel,
                SensorManager.SENSOR_DELAY_GAME);
    }

    public void onPause() {
        sensorManager.unregisterListener(listener);
    }

    float[] valuesAccelGravity = new float[3];

    SensorEventListener listener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent event) {
            switch (event.sensor.getType()) {
                case Sensor.TYPE_ACCELEROMETER:
                    for (int i = 0; i < 3; i++) {
                        valuesAccelGravity[i] = (float) (0.1 * event.values[i] + 0.9 * valuesAccelGravity[i]);
                    }
                    break;
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}
    };
}
