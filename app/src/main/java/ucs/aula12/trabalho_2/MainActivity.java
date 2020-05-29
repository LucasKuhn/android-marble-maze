package ucs.aula12.trabalho_2;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import ucs.aula12.trabalho_2.utils.MazeGenerator;
import ucs.aula12.trabalho_2.view.CanvasView;

public class MainActivity extends AppCompatActivity {
    private CanvasView customCanvas;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private MazeGenerator maze;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide status bar
        getSupportActionBar().hide();

        // Keep screen always on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_main);

        // Get the root Linearlayout object.
        customCanvas = (CanvasView) findViewById(R.id.signature_canvas);

        // Instantiates a MazeGenerator with the fixed size of the maze
        maze = new MazeGenerator(7, 15);

        // Calls the function to generate the maze matrix
        maze.generateMaze();

        // Set the maze matrix in customCanvas
        customCanvas.setMaze(maze.getMaze());

        // Set walls width and height based on device size
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        customCanvas.setWallHeight(displayMetrics.heightPixels);
        customCanvas.setWallWidth(displayMetrics.widthPixels);

        // Set ball location now that we now the size of the walls
        customCanvas.setInitialBallLocation();

        Button startButton = findViewById(R.id.start_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame();
            }
        });
    }

    public void startGame() {
        // Declare accelerometer sensor manager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(new Accelerometer(), accelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    class Accelerometer implements SensorEventListener {

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            customCanvas.updateBall(x,y);
            customCanvas.clearCanvas();
        }
    }
}


