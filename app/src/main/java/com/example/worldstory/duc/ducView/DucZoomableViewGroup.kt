package com.example.worldstory.duc.ducView

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.widget.FrameLayout

class DucZoomableViewGroup @JvmOverloads constructor(
    context: Context,attrs: AttributeSet?=null
): FrameLayout(context,attrs) {

    private var scaleFactor = 1.0f
    private val scaleGestureDetector = ScaleGestureDetector(context, object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            scaleFactor *= detector.scaleFactor
            scaleFactor = scaleFactor.coerceIn(0.5f, 3.0f) // Giới hạn zoom từ 0.5x đến 3.0x
            scaleX = scaleFactor
            scaleY = scaleFactor
            return true
        }
    })

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return scaleGestureDetector.onTouchEvent(event) || super.onTouchEvent(event)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return scaleGestureDetector.onTouchEvent(ev) || super.onInterceptTouchEvent(ev)
    }
}