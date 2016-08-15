package com.louisgeek.louisareaselectdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.TextView;

import com.louisgeek.louisareaselectdemo.Bean.SelectAreaBean;

import java.util.List;

/**
 * Created by louisgeek on 2016/8/14.
 */
public class MyPinyinBannerView extends View{
    private static final String TAG = "MyPinyinBannerView";
    Context mContext;
    Paint mPaint;
    int width=80;
    int height=1000;
    int textHeight;
    int textSpacing=dp2px(5);
    int choosePos=-1;
    final int  DEFAULT_BACKGROUNDCOLOR=0x55555555;
    final int  DEFAULT_TEXTCOLOR=0xFFffffff;
    int  backgroundColor=DEFAULT_BACKGROUNDCOLOR;
    int  textColor=DEFAULT_TEXTCOLOR;
    boolean isSliding=false;
    private int yDown, yMove,ScaledTouchSlop;

    private  String sectionStr = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public void setTextShowBox(TextView textShowBox) {
        this.textShowBox = textShowBox;
    }

    TextView textShowBox;
    public MyPinyinBannerView(Context context) {
        this(context,null,0);
    }

    public MyPinyinBannerView(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public MyPinyinBannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext=context;
        initView();
    }

    private void initView() {
        ScaledTouchSlop = ViewConfiguration.get(mContext).getScaledTouchSlop();
        mPaint=new Paint();

        mPaint.setAntiAlias(true);
        mPaint.setTextSize(40);//提供测量
        mPaint.setTypeface(Typeface.DEFAULT_BOLD);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //当xml未设置固定值时候需要显示的宽度和高度
        int textWidth= (int) mPaint.measureText("A");
        textHeight= (int) Math.abs(mPaint.descent() + mPaint.ascent());
        Log.d(TAG, "onMeasure:textWidth: "+textWidth);
        Log.d(TAG, "onMeasure:textHeight: "+textHeight);
        int w_my_when_no_size_configed= textWidth+this.getPaddingLeft()+this.getPaddingRight();
        int h_my_when_no_size_configed=textHeight*sectionStr.length()+textSpacing*(sectionStr.length()-1)+this.getPaddingTop()+this.getPaddingBottom();
        int w=resolveSize(w_my_when_no_size_configed,widthMeasureSpec);
        int h=resolveSize(h_my_when_no_size_configed,heightMeasureSpec);
        setMeasuredDimension(w,h);
        width=w;
        height=h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
       // width=getWidth();
     //   height=getHeight();
        mPaint.setColor(backgroundColor);
        canvas.drawRect(0,0,width,height,mPaint);

        mPaint.setColor(textColor);

        for (int i = 0; i < sectionStr.length(); i++) {
            // 选中的状态
            if (i == choosePos) {
                mPaint.setColor(ContextCompat.getColor(mContext,R.color.colorAccent));
               // mPaint.setFakeBoldText(true);
            }else{
                mPaint.setColor(textColor);
            }
            int textWidth= (int) mPaint.measureText(sectionStr.charAt(i)+"");
            //依托测量后的height来设定文本
           // int y=this.getPaddingTop()+(textHeight)*(i+1)+textSpacing*i;
            int newTextSpacing=(height-this.getPaddingTop()-this.getPaddingBottom()-sectionStr.length()*textHeight)/(sectionStr.length()-1);
                canvas.drawText(sectionStr.charAt(i)+"",(width-textWidth)/2,this.getPaddingTop()+textHeight*(i+1)+newTextSpacing*i,mPaint);
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int y = (int) event.getY();// 点击y坐标
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                yDown =y;
                Log.d(TAG, "dispatchTouchEvent: ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                yMove =y;
                int dy = yMove - yDown;
                Log.d(TAG, "dispatchTouchEvent: ACTION_MOVE");
                //如果是竖直方向滑动
                if (Math.abs(dy) > ScaledTouchSlop) {
                    isSliding = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "dispatchTouchEvent: ACTION_UP");
                isSliding=false;
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int y = (int) event.getY();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "onTouchEvent: ACTION_DOWN");
                backgroundColor=0xFF676767;
                choosePos= y / (height / sectionStr.length());
                choosePos=choosePos>sectionStr.length()-1?sectionStr.length()-1:choosePos;
                invalidate();
                showTextBox();
                onSlidedListener.onSlided(sectionStr.charAt(choosePos)+"");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "onTouchEvent: ACTION_MOVE isSliding:"+isSliding);
                if (isSliding) {
                    backgroundColor=0xFF676767;
                    choosePos= y / (height / sectionStr.length());
                    choosePos=choosePos>sectionStr.length()-1?sectionStr.length()-1:choosePos;
                  invalidate();
                  showTextBox();
                  onSlidedListener.onSlided(sectionStr.charAt(choosePos)+"");
                }
                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "onTouchEvent: ACTION_UP");
                backgroundColor=DEFAULT_BACKGROUNDCOLOR;
               /* choosePos= y / (height / sectionStr.length());
                choosePos=choosePos>sectionStr.length()-1?sectionStr.length()-1:choosePos;*/
                choosePos=-1;
                invalidate();
                /*if (isSliding){
                    showTextBox();
                }else{*/
                    hideTextBox();
                //}
                break;
            default:
                break;
        }
        return  true;//消费掉事件
        //return super.onTouchEvent(event);
    }

   /* private int getPositionByPinyinFirst(String pinyinFirst) {
    }*/

    private void showTextBox() {
        if (textShowBox != null){
           //   textShowBox.clearAnimation();
                textShowBox.setText(sectionStr.charAt(choosePos)+"");
                textShowBox.setVisibility(View.VISIBLE);
                   /* ObjectAnimator anim=ObjectAnimator.ofFloat(textShowBox,"Alpha",0f,100f);
                    anim.setDuration(3000);
                    anim.start();*/
         }
    }
    private void hideTextBox() {
        if (textShowBox != null){
            textShowBox.setVisibility(GONE);
           /* textShowBox.clearAnimation();
            ObjectAnimator anim=ObjectAnimator.ofFloat(textShowBox,"Alpha",100f,0f);
            anim.setDuration(300);
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    textShowBox.setVisibility(View.GONE);
                }
            });
            anim.start();*/
        }
    }
    public int dp2px(int dpValue){
        int px= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dpValue,getResources().getDisplayMetrics());
        return px;
    }

    public void setSelectAreaBeanList(List<SelectAreaBean> selectAreaBeanList) {
       // StringBuilder stringBuilder=new StringBuilder();
        sectionStr="";
        if (selectAreaBeanList!=null&&selectAreaBeanList.size()>0){
            for (int i = 0; i < selectAreaBeanList.size(); i++) {
                String firstPinyin=selectAreaBeanList.get(i).getPinyin().charAt(0)+"";
                if (!sectionStr.contains(firstPinyin)){
                    sectionStr+=firstPinyin;
                }
            }
        }

    }

    public  interface  OnSlidedListener{
        void  onSlided(String pinyinFirst);
    }

    public void setOnSlidedListener(OnSlidedListener onSlidedListener) {
        this.onSlidedListener = onSlidedListener;
    }

    OnSlidedListener onSlidedListener;
}
