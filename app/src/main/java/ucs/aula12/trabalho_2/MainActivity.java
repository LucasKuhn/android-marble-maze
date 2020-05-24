package ucs.aula12.trabalho_2;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import ucs.aula12.trabalho_2.view.CanvasView;

public class MainActivity extends AppCompatActivity {
    private CanvasView customCanvas;
    private SensorManager sensorManager;
    private Sensor accelerometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the root Linearlayout object.
        customCanvas = (CanvasView) findViewById(R.id.signature_canvas);

        // Declare accelerometer sensor manager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(new Accelerometer(), accelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    class Accelerometer implements SensorEventListener {

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        public void onSensorChanged(SensorEvent event) {
            float x = - event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            customCanvas.updateBall(x,y);
            customCanvas.clearCanvas();
        }
    }
}


