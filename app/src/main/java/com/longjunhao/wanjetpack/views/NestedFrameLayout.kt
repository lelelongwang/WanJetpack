package com.longjunhao.wanjetpack.views

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.widget.FrameLayout
import kotlin.math.absoluteValue

/**
 * .NestedViewPager2
 *
 * 参考https://github.com/android/views-widgets-samples/tree/main/ViewPager2中的NestedScrollableHost实现
 * @author Admitor
 * @date 2021/06/24
 */
class NestedFrameLayout: FrameLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    private var touchSlop = 0
    private var initialX = 0f
    private var initialY = 0f
    var isTouched = false

    init {
        touchSlop = ViewConfiguration.get(context).scaledTouchSlop
    }

    /**
     * 处理viewpager2在RecyclerView中，不能滑动viewpager2的问题
     */
    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        handleInterceptTouchEvent(ev)
        return super.onInterceptTouchEvent(ev)
    }

    private fun handleInterceptTouchEvent(e: MotionEvent) {
        if (e.action == MotionEvent.ACTION_DOWN) {
            initialX = e.x
            initialY = e.y
            parent.requestDisallowInterceptTouchEvent(true)
        } else if (e.action == MotionEvent.ACTION_MOVE) {
            val dx = e.x - initialX
            val dy = e.y - initialY

            // assuming ViewPager2 touch-slop is 2x touch-slop of child
            val scaledDx = dx.absoluteValue * 1f
            val scaledDy = dy.absoluteValue * .5f

            if (scaledDx > touchSlop || scaledDy > touchSlop) {
                if (scaledDy > scaledDx) {
                    //事件拦截，让父类中的RecyclerView消费事件，执行上下滚动界面
                    parent.requestDisallowInterceptTouchEvent(false)
                } else {
                    //事件传递，比如banner中让viewpager2消费事件，执行banner翻页
                    parent.requestDisallowInterceptTouchEvent(true)
                }
            }
        }
    }

    /**
     * 新增isTouched变量，让Banner判断是否手指触摸viewpager，如果isTouched=true,则Banner不应该自动滚动
     */
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> isTouched = true
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> isTouched = false
        }
        return super.dispatchTouchEvent(ev)
    }
}