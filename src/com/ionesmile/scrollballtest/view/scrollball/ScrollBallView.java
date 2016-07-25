package com.ionesmile.scrollballtest.view.scrollball;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.ionesmile.scrollballtest.R;

/**
 * 滚动进度条
 * @author iOnesmile	root@iOnesmile.com
 * @date 2016-7-11 上午11:30:53
 */
public class ScrollBallView extends View {
	
	private static final int SHOW_FRAME_NUMBER = 20;	// 滑动一个刻度描述的帧数
	private static final int MAX_SCROLL_VALUE = 1000;	// 最大的滚动值
	private RollorFrameUtil mRollorFrameUtil;
	private Bitmap[] mTrackballBitmaps;	// 帧数的Bitmap
	private Paint mPaint;

	private int mViewWidth, mViewHeight;
	private int mScrollIndex;	// 显示第N帧

	private boolean downOnScroll = false;
	private int lastTouchY = 0;
	
	// 当前滚动条的进度值
	private int mScrollValue = MAX_SCROLL_VALUE;
	// 用来缓存滚动条滚动时的进度值，由此得到@mScrollIndex，初始值为Integer的一半（消除滚动条为与手滑动方向不一致的问题）
	private int mScrollIndexSum = Integer.MAX_VALUE / 2;

	public ScrollBallView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// 初始化View高宽
		Drawable bgDrawable = getResources().getDrawable(R.drawable.img_tr);
		mViewWidth = bgDrawable.getIntrinsicWidth();
		mViewHeight = bgDrawable.getIntrinsicHeight();
		// 初始化一组动画帧的图片
		mRollorFrameUtil = new RollorFrameUtil(context);
		mTrackballBitmaps = mRollorFrameUtil.getBitmapFramesElegant(SHOW_FRAME_NUMBER, mViewWidth, mViewHeight);
		// 初始化笔
		mPaint = new Paint();
		mPaint.setAlpha(100);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawBitmap(mTrackballBitmaps[mScrollIndex], null, new Rect(0, 0, mViewWidth, mViewHeight), mPaint);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		int x = (int) event.getX();
		int y = (int) event.getY();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			if (isTouchScroll(x, y)) {
				downOnScroll = true;
				lastTouchY = y;
				return true;
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if (downOnScroll) {
				// dy 与上一次MOVE的偏移
				int dy = y - lastTouchY;
				lastTouchY = y;
				setScroll(dy);
				invalidate();
				if (mOnScrollChangeListener != null) {
					mOnScrollChangeListener.onScrollChange(getProgress100());
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			if (downOnScroll) {
				downOnScroll = false;
				lastTouchY = 0;
				mScrollIndexSum = Integer.MAX_VALUE / 2;
				invalidate();
			}
			break;
		}
		return super.onTouchEvent(event);
	}

	private boolean isTouchScroll(int x, int y) {
		return true;
	}

	/**
	 * 处理滚动时的事件
	 * @param dy	移动的距离
	 */
	public void setScroll(int dy) {
//		dy = dy > 0 ? dy*dy : -dy*dy;   
		mScrollValue = mScrollValue + dy;
		// 当滚动值大于最大值或小于0时，限制值，并不做更新
		if (mScrollValue > MAX_SCROLL_VALUE) {
			mScrollValue = MAX_SCROLL_VALUE;
		} else if (mScrollValue < 0) {
			mScrollValue = 0;
		} else {
			mScrollIndex = getScrollIndex(dy);
			invalidate();
		}
	}

	/**
	 * 获取滑动帧的Index
	 * @param dy
	 * @return
	 */
	private int getScrollIndex(int dy) {
		if (dy > 0) {
			mScrollIndexSum = mScrollIndexSum - dy * dy;
		} else {
			mScrollIndexSum = mScrollIndexSum + dy * dy;
		}
		return  Math.abs(mScrollIndexSum / 4 % SHOW_FRAME_NUMBER);
	}

	/**
	 * 返回最大值为100的进度
	 * @return
	 */
	public int getProgress100() {
		return (int) (mScrollValue / Float.valueOf(MAX_SCROLL_VALUE) * 100);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(mViewWidth, mViewHeight);
	}
	
	// ===================== Callback ======================
	private OnScrollChangeListener mOnScrollChangeListener;
	
	public void setOnScrollChangeListener(OnScrollChangeListener onScrollChangeListener){
		this.mOnScrollChangeListener = onScrollChangeListener;
	}
	
	public static interface OnScrollChangeListener {
		void onScrollChange(int progress);
	}
}
