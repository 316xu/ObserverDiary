package hust.xujifa.observerdiary.ui;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import hust.xujifa.observerdiary.R;

/**
 * Created by xujifa on 2015/11/28.
 */
public class Recoder extends View{
    float scale=getResources().getDisplayMetrics().density;
    Paint ring1;
    Paint circle;
    Path p;
    Bitmap mic;
    Handler h=null;
    boolean playing=false;
    boolean exp=false;
    ImageView imageView;
    float radius=0f;
    float radius2=50*scale;
    public Recoder(Context context) {
        this(context, null);
    }

    public Recoder(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public void setH(Handler h){
        this.h=h;
    }
    public Recoder(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ring1=new Paint();
        ring1.setColor(getResources().getColor(R.color.blue2));
        ring1.setStyle(Paint.Style.FILL);
        ring1.setAlpha(150);
        ring1.setStrokeWidth(10f);

        circle=new Paint();
        circle.setColor(getResources().getColor(R.color.blue2));

        circle.setStyle(Paint.Style.STROKE);
        circle.setStrokeWidth(7f);


        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inSampleSize=4;
        mic=BitmapFactory.decodeResource(getResources(),R.mipmap.microphone,options);
        imageView=new ImageView(context);
        imageView.setMaxWidth((int) (100 * scale));
        imageView.setMaxHeight((int) (100 * scale));
        imageView.setImageBitmap(mic);
    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        float dw=150*scale;
        float dh=150*scale;
        setMeasuredDimension((int) dw, (int) dh);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle((getRight() - getLeft()) / 2, (getBottom() - getTop()) / 2, radius2, circle);
        canvas.drawCircle((getRight() - getLeft()) / 2, (getBottom() - getTop()) / 2,radius, ring1);

        canvas.drawBitmap(mic, 75 * scale - mic.getWidth() / 2, 75 * scale - mic.getWidth() / 2, circle);


        if(playing){
            if(radius<50*scale) {
                radius += 4;
                if(radius>=50*scale)exp=true;
            }
            else if(exp){
                radius+=1;
                radius2+=1;
                if(radius>=70*scale)exp=false;
            }else if(!exp){
                radius-=1;
                radius2-=1;
                if(radius<=50*scale)exp=true;
            }
            invalidate();
        }else if(radius>0){
            if(radius>50*scale){
                radius-=1;
                radius2-=1;
            }else{
                radius2=50*scale;
                radius-=4;
                if(radius<0)radius=0;
            }
            invalidate();
        }


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(event.getAction()==MotionEvent.ACTION_DOWN){
            playing=true;
            h.sendEmptyMessage(1);
            invalidate();
        }else if(event.getAction()==MotionEvent.ACTION_UP){
            playing=false;
            h.sendEmptyMessage(0);

        }
        return true;
    }
    public void setHandler(Handler h){
        this.h=h;

    }

}
