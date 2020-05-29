package ucs.aula12.trabalho_2.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.graphics.Path;

import ucs.aula12.trabalho_2.R;

public class CanvasView extends View {

    private Bitmap mBitmap;
    private Canvas mCanvas;
    Context context;

    // Ball paint
    Paint ball = new Paint();
    public float ballX = 0;
    public float ballY = 0;


    public CanvasView(Context c, AttributeSet attrs) {
        super(c, attrs);
        context = c;
    }

    public void updateBall(float x, float y) {
        float new_x = ballX + ( x * 3 );
        float new_y = ballY + ( y * 3 );
        if (new_x < 0) {
            new_x = 0;
        }
        if (new_y < 0) {
            new_y = 0;
        }
        if ( ( new_x + 100 ) > this.getWidth() ) {
            new_x = ( this.getWidth() - 100 );
        }
        if ( ( new_y + 100 ) > this.getHeight() ) {
            new_y = ( this.getHeight() - 100 );
        }
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

        ball.setColor(Color.RED);
//        canvas.drawCircle(ballX, ballX, 60, ball);
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.beyblade);
        canvas.drawBitmap(b, ballX, ballY, ball);
    }

    public void clearCanvas() {
        invalidate();
    }

}