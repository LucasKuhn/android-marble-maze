package ucs.aula12.trabalho_2;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import ucs.aula12.trabalho_2.utils.MazeGenerator;
import ucs.aula12.trabalho_2.view.CanvasView;

public class MainActivity extends AppCompatActivity {
    private CanvasView customCanvas;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private SensorEventListener accelerometerEventListener;
    private MazeGenerator maze;
    private MediaPlayer backgroundMP = new MediaPlayer();
    private MediaPlayer effectsMP = new MediaPlayer();
    private boolean game_started = false;
    private View overlayScreen;
    private TextView overlayText;
    private Button startButton;
    private int level = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide status bar
        getSupportActionBar().hide();

        // Keep screen always on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_main);

        overlayScreen = findViewById(R.id.overlay_screen);
        overlayText = findViewById(R.id.overlay_text);

        // Get the root Linearlayout object.
        customCanvas = (CanvasView) findViewById(R.id.signature_canvas);

        // Instantiates a MazeGenerator with the fixed size of the maze
        maze = new MazeGenerator(7, 15);

        // Setup canvas according to maze;
        setupCanvas();

        startButton = findViewById(R.id.start_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(game_started) {
                    restartGame();
                } else {
                    startGame();
                    startButton.setText("Reiniciar");
                }
            }
        });
    }

    public void startGame() {
        overlayScreen.setVisibility(View.INVISIBLE);
        overlayText.setVisibility(View.INVISIBLE);

        setupCanvas();
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        accelerometerEventListener = new Accelerometer();
        sensorManager.registerListener(accelerometerEventListener, accelerometer, SensorManager.SENSOR_DELAY_UI);

        game_started = true;

        // Play initial song
        gameStartSong();
    }

    public void restartGame() {
        level = 1;
        setupCanvas();
        game_started = true;

        // Play initial song
        gameStartSong();
    }

    public void setupCanvas() {
        // Calls the function to generate the maze matrix
        maze.generateMaze();

        // Set the maze matrix in customCanvas
        customCanvas.setMaze(maze.getMaze());
        customCanvas.setLevel(level);

        // Set walls width and height based on device size
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        customCanvas.setWallHeight(displayMetrics.heightPixels);
        customCanvas.setWallWidth(displayMetrics.widthPixels);

        // Get a random beyblade bitmap
        customCanvas.setBeybladeBitmap();

        // Set ball location now that we now the size of the walls
        customCanvas.setInitialBallLocation();
    }

    class Accelerometer implements SensorEventListener {

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            // The ball gets faster as you pass more levels
            customCanvas.updateBall((level * 2) * x,(level * 2) * y);

            // If the ball touched a wall
            if (customCanvas.checkTouchedWall()) {
                wallHitSong();
                vibrateDevice();
                // Kick back the ball away from the wall
                customCanvas.updateBall(-3*(level * 2)*x,-3*(level * 2)*y);
            }

            if (customCanvas.checkReachedEnd()) {
                endReachedSong();
                nextLevel();
            }

            customCanvas.clearCanvas();
        }
    }

    public void nextLevel() {
        sensorManager.unregisterListener(accelerometerEventListener);
        overlayScreen.setVisibility(View.VISIBLE);
        overlayText.setText("Vitória!");
        overlayText.setVisibility(View.VISIBLE);
        startButton.setText("Próximo Nível!");
        game_started = false;
        level++;
    }

    public void gameStartSong() {
        if ( backgroundMP.isPlaying() ) {
            backgroundMP.stop();
            backgroundMP.release();
        }
        backgroundMP = MediaPlayer.create(getApplicationContext(), R.raw.vai_beyblade);
        backgroundMP.start();
    }

    public void endReachedSong() {
        if( !effectsMP.isPlaying() ){
            effectsMP = MediaPlayer.create(getApplicationContext(), R.raw.vitoria_total);
            effectsMP.start();
        }
    }

    public void wallHitSong() {
        if( !effectsMP.isPlaying() ){
            effectsMP = MediaPlayer.create(getApplicationContext(), R.raw.hit);
            effectsMP.start();
        }
    }

    public void vibrateDevice() {
        if (Build.VERSION.SDK_INT >= 26) {
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(150, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(150);
        }
    }
}


