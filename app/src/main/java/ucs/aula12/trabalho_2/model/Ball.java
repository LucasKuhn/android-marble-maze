// Not using it for now, just and idea for refactoring

package ucs.aula12.trabalho_2.model;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Ball {
    float x;
    float y;

    public float getX() {
        return x;
    }
    public void setX(float x) {
        this.x = x;
    }
    public float getY() {
        return y;
    }
    public void setY(float y) {
        this.y = y;
    }

    public void draw(Canvas canvas, Paint paint) {
        paint.setStrokeWidth(3);
        paint.setColor(Color.BLUE);
        canvas.drawCircle(this.getX(), this.getY(), 10, paint);
    }

    public void update(float dx, float dy) {
        this.setX(this.getX()+dx);
        this.setY(this.getY()+dy);
    }
}

//    https://github.com/bittokazi/Ball-Balance-Game-Android-Java/blob/master/src/us/bitto/kazi/com/savetheball/GameThread.java
//    https://github.com/bittokazi/Ball-Balance-Game-Android-Java/blob/master/src/us/bitto/kazi/com/savetheball/Ball.java
//    @Override
//    public void onSensorChanged(SensorEvent event) {
//        int type = event.sensor.getType();
//        if (type == Sensor.TYPE_ACCELEROMETER) {
//            float gx = event.values[0];
//            if(gx>1 && (ball.getX()-ball.getRadious())>0 && !game_over) {
//                ball.setX((ball.getX()-(gx)));
//            }
//            else if(gx<1 && (ball.getX()+ball.getRadious())<VIEW_WIDTH && !game_over) {
//                ball.setX((ball.getX()-(gx)));
//            }
//        }
//    }