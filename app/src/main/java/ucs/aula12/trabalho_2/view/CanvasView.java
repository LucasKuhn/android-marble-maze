package ucs.aula12.trabalho_2.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.graphics.Path;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ucs.aula12.trabalho_2.R;
import ucs.aula12.trabalho_2.model.Coordinates;

public class CanvasView extends View {

    private Bitmap mBitmap;
    private Canvas mCanvas;
    Context context;

    // Ball paint
    Paint ball = new Paint();
    public float ballX;    // Initial ball coordinates
    public float ballY;
    public float wallWidth;
    public float wallHeight;
    public int level;

    public int maze[][];
    private List<Coordinates> wallCoordinates = new ArrayList<Coordinates>();
    private Coordinates finalBlockCoordinates;

    private Bitmap beybladeBitmap;

    public CanvasView(Context c, AttributeSet attrs) {
        super(c, attrs);
        context = c;
        setBeybladeBitmap();
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setWallWidth(float displayWidth) {
        this.wallWidth = displayWidth / 15 ;
    }

    public void setWallHeight(float displayHeight) {
        this.wallHeight = displayHeight  / 7;
    }

    public void setInitialBallLocation() {
        this.ballX = wallWidth + ( wallWidth / 2);
        this.ballY = wallHeight + ( wallHeight / 2);
    }

    public void setMaze(int maze[][]) {
        this.maze = maze;
    }

    public void updateBall(float x, float y) {
        float new_x = ballX + y;
        float new_y = ballY + x;

        this.ballX = new_x;
        this.ballY = new_y;
    }

    // override onSizeChanged
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // your Canvas will draw onto the defined Bitmap
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    // override onDraw
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        wallCoordinates.clear();

        Paint p1 = new Paint();
        p1.setColor(Color.BLUE);

        Paint p2 = new Paint();
        p2.setColor(Color.YELLOW);


        for(int i = 0; i < maze.length; i++) {
            float y = i * this.wallHeight;

            for (int j = 0; j < maze[0].length; j++) {

                float x = j * this.wallWidth;

                // If the maze's position is a wall, draw a wall block
                if (maze[i][j] == 1) {
                    canvas.drawRect(x, y, x+wallWidth, y+wallHeight, p1);
                    wallCoordinates.add(new Coordinates(x, y, x+wallWidth, y+wallHeight));

                } else if (maze[i][j] == 2) { // If position of the maze is the final position, draw a yellow final block
                    canvas.drawRect(x, y, x+wallWidth, y+wallHeight, p2);
                    finalBlockCoordinates = new Coordinates(x, y, x+wallWidth, y+wallHeight);
                }

            }
        }

        //        ball.setColor(Color.RED);
        //        canvas.drawCircle(ballX, ballY, 40, ball);
        //        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.beyblade1);
        // Draw Beyblade
        Bitmap scaled = Bitmap.createScaledBitmap(beybladeBitmap, (int) (wallWidth), (int) (wallWidth), false);
        canvas.drawBitmap(scaled, (ballX - (scaled.getWidth()/2)), (ballY - (scaled.getHeight()/2)), ball);

        // Draw level
        p2.setColor(Color.WHITE);
        p2.setTextSize(90);
        canvas.drawText("Nível: ".concat(Integer.toString(level)), 50, 130, p2);
    }

    public void clearCanvas() {
        invalidate();
    }

    public boolean checkTouchedWall() {

        for(int i = 0; i < wallCoordinates.size(); i++) {
            if(ballX+20 >= wallCoordinates.get(i).getLeft() && ballX-20 <= wallCoordinates.get(i).getRight()
                    && ballY-20<= wallCoordinates.get(i).getBottom() && ballY+20>= wallCoordinates.get(i).getTop()) {
                return true;
            }
        }
        return false;
    }

    public boolean checkReachedEnd() {

        if(
            ballX+20 >= finalBlockCoordinates.getLeft()
            && ballX-20 <= finalBlockCoordinates.getRight()
            && ballY-20<= finalBlockCoordinates.getBottom()
            && ballY+20>= finalBlockCoordinates.getTop()
        ) {
            return true;
        } else {
            return false;
        }
    }

    public void setBeybladeBitmap() {
        int [] beyblades = {
                R.drawable.beyblade_1,
                R.drawable.beyblade_2,
                R.drawable.beyblade_3,
                R.drawable.beyblade_4,
                R.drawable.beyblade_5
        };
        int rnd = new Random().nextInt(beyblades.length);
        beybladeBitmap = BitmapFactory.decodeResource(getResources(), beyblades[rnd]);
    }




}