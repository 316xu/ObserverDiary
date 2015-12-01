package hust.xujifa.observerdiary.ui

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup

/**
 * Created by xujifa on 2015/11/12.
 */
class FloatingActionButtonMenu:ViewGroup, ValueAnimator.AnimatorUpdateListener{

    var show=true;
    var start=false;
    val scale=resources.displayMetrics.density
    constructor(context: Context,attrs: AttributeSet?=null):super(context,attrs){}
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int  , b: Int) {
        var i=0
        var height:Int=paddingTop
        while(i<childCount){
            val child: View =getChildAt(i)

            child.layout(paddingLeft,height.toInt(),(paddingLeft+56*scale).toInt(),(height+56*scale).toInt())
            height+=(66*scale).toInt()
            i++
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val count=childCount
        var i=0
        var height:Float=0f
        while(i<count){
            val child: View =getChildAt(i)
            i++
            measureChild(child,widthMeasureSpec,heightMeasureSpec)
            height+=child.measuredHeight
            height+=10*scale
        }
        setMeasuredDimension((paddingLeft+paddingRight+56*scale).toInt(),(paddingTop+paddingBottom+height).toInt())
    }



    fun start(duration:Int){
        val anim:ValueAnimator=ValueAnimator.ofInt(0,100)
        anim.setDuration(duration.toLong())
        anim.addUpdateListener(this)
        anim.start()
    }
    override fun onAnimationUpdate(animation: ValueAnimator) {
        val value=animation.animatedValue as Int
        if(value==0) start=true
        var i=0;
        while(i<childCount-1){
            val child=getChildAt(i)

            if(show)
                child.translationY=value.toFloat()*(childCount-i-1)*1.97f
            else child.translationY=(100-value.toFloat())*(childCount-i-1)*1.97f
            i++
        }
        val child=getChildAt(childCount-1)
        if(show)
            child.rotation=45-45*value.toFloat()/100
        else child.rotation=45*value.toFloat()/100
        if(value==100){
            show=!show
            start=false
        }
    }
}