package ucs.aula12.trabalho_2.view;

import android.content.Context;
import android.graphics.Bitmap;
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

    public int maze[][];
    private List<Coordinates> wallCoordinates = new ArrayList<Coordinates>();

    public CanvasView(Context c, AttributeSet attrs) {
        super(c, attrs);
        context = c;
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
        boolean touchedWall;

        float new_x = ballX + 2*y ;
        float new_y = ballY + 2*x;
//        if (new_x < 0) {
//            new_x = 0;
//        }
//        if (new_y < 0) {
//            new_y = 0;
//        }
//        if ( new_x > this.getWidth() ) {
//            new_x = this.getWidth();
//        }
//        if ( new_y > this.getHeight() ) {
//            new_y = this.getHeight();
//        }

        // Checks if the new position of the ball invades the position of a wall
        touchedWall = checkTouchedWall(new_x, new_y);

        // If do not invade the position of a wall, update the ball's position
        if (!touchedWall) {
            this.ballX = new_x;
            this.ballY = new_y;
        }
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
                }

            }
        }

        ball.setColor(Color.RED);
        canvas.drawCircle(ballX, ballY, 40, ball);
    }

    public void clearCanvas() {
        invalidate();
    }

    public boolean checkTouchedWall(float new_x, float new_y) {

        for(int i = 0; i < wallCoordinates.size(); i++) {
            if(new_x+20 >= wallCoordinates.get(i).getLeft() && new_x-20 <= wallCoordinates.get(i).getRight()
                    && new_y-20<= wallCoordinates.get(i).getBottom() && new_y+20>= wallCoordinates.get(i).getTop()) {
                return true;
            }
        }

        return false;
    }


}