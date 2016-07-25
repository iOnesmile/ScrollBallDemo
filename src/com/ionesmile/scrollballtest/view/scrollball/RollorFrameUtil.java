package com.ionesmile.scrollballtest.view.scrollball;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

import com.ionesmile.scrollballtest.R;

/**
 * 滚动进度条显示帧的处理工具类
 * @author iOnesmile	root@iOnesmile.com
 * @date 2016-7-11 上午11:59:06
 */
public class RollorFrameUtil {
	
	private static final int[] ROLLER_BITMAP_RES = new int[]{
		R.drawable.img_01, R.drawable.img_02, R.drawable.img_03,
		R.drawable.img_04, R.drawable.img_05, R.drawable.img_06,
		R.drawable.img_07, R.drawable.img_08, R.drawable.img_09}; 
	
	// 设置小横杠的排列顺序
	private static final int[] RACKS = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 8};
	private static final int SHOW_TIEM_NUMBER = ROLLER_BITMAP_RES.length;
	private static final float RATIO_ROLLOR_ITEM = 0.33f;
	private static final float RATIO_RADIUS_CUTCIRCLE = 0.963f;
	
	private Bitmap[] mRollerBitmaps;	// 小横杠的Bitmap
	private Bitmap mBgBitmap;	// 背景圆的Bitmap
	
	public RollorFrameUtil(Context context) {
		// 初始化加载小横杠的Bitmap
		if (mRollerBitmaps == null) {
			mRollerBitmaps = new Bitmap[ROLLER_BITMAP_RES.length];
			for (int i = 0; i < mRollerBitmaps.length; i++) {
				mRollerBitmaps[i] = BitmapFactory.decodeResource(context.getResources(), ROLLER_BITMAP_RES[i]);
			}
		}
		// 初始化加载背景圆的Bitmap
		mBgBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.img_tr);
	}

	/**
	 * 获取一组动画的图片
	 * @param frameCounter	总帧数
	 * @param width		图片宽度
	 * @param height	图片高度
	 * @return
	 */
	public Bitmap[] getBitmapFrames(int frameCounter, int width, int height){
		Bitmap[] bitmaps = new Bitmap[frameCounter];
		for (int i = 0; i < frameCounter; i++) {
			bitmaps[i] = getRollorFrame(i, frameCounter, width, height);
		}
		return bitmaps;
	}
	
	/**
	 * 获取一组动画的图片，相比{@link #getBitmapFrames()},此方法执行效率更高
	 * @param frameCounter	总帧数
	 * @param width		图片宽度
	 * @param height	图片高度
	 * @return
	 */
	public Bitmap[] getBitmapFramesElegant(int frameCounter, int width, int height){
		Bitmap[] bitmaps = new Bitmap[frameCounter];
		Bitmap scrollBitmap = createScrollBitmap(width, height);
		Rect contentRect = new Rect(0, 0, width, height);
		Rect srcRect = new Rect(0, 0, width, 0);
		Paint paint = new Paint();
		for (int i = 0; i < frameCounter; i++) {
			float offset = i/Float.valueOf(frameCounter);
	        Bitmap bmp = Bitmap.createBitmap(width, height, Config.ARGB_8888);
	        Canvas canvas = new Canvas(bmp); 
	        // 绘制一个圆
	        canvas.drawCircle(contentRect.centerX(), contentRect.centerY(), contentRect.width()/2*RATIO_RADIUS_CUTCIRCLE, paint);
	        // 设置合成模式
	        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
	        // 获取滚动横杠的Bitmap，然后添加到bmp中，与之前绘制的圆合成
	        // 改变@scrollBitmap的偏移，生成不同的Bitmap
	        srcRect.top = (int)(height/SHOW_TIEM_NUMBER*offset);
	        srcRect.bottom = height+(int)(height/SHOW_TIEM_NUMBER*offset);
	        canvas.drawBitmap(scrollBitmap, srcRect, contentRect, paint);
	        // 通过合成模式，生成圆在下，横杠在上的Bitmap
	        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OVER));
			canvas.drawBitmap(mBgBitmap, null, contentRect, paint);
			bitmaps[i] = bmp;
		}
		return bitmaps;
	}
	
	/**
	 * 获取一帧图片
	 * @param index		第N帧
	 * @param sumFrame	总帧数
	 * @param width		图片宽度
	 * @param height	图片高度
	 * @return
	 */
	public Bitmap getRollorFrame(int index, int sumFrame, int width, int height) {
//		Bitmap bmp = Bitmap.createBitmap(width, height, Config.ARGB_8888);
//		Rect contentRect = new Rect(0, 0, width, height);
//		Canvas canvas = new Canvas(bmp);
//		canvas.drawBitmap(mBgBitmap, null, contentRect, null);
//		canvas.drawBitmap(getMergeBitmap(width, height, index/Float.valueOf(sumFrame)), null, contentRect, null);
		// 通过合成模式，生成圆在下，横杠在上的Bitmap。与上面代码达到一致的效果
		Bitmap bmp = getMergeBitmap(width, height, index/Float.valueOf(sumFrame));
		Rect contentRect = new Rect(0, 0, width, height);
		Canvas canvas = new Canvas(bmp);
		Paint paint = new Paint();
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OVER));
		canvas.drawBitmap(mBgBitmap, null, contentRect, paint);
		return bmp;
	}
	
	// 得到一个与圆形背景切割了的帧图片
	private Bitmap getMergeBitmap(int width, int height, float offset){  
		Rect contentRect = new Rect(0, 0, width, height);
        Bitmap bmp = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Paint paint = new Paint();
        Canvas canvas = new Canvas(bmp); 
        // 绘制一个圆
        canvas.drawCircle(contentRect.centerX(), contentRect.centerY(), contentRect.width()/2*RATIO_RADIUS_CUTCIRCLE, paint);
        // 设置合成模式
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        // 获取滚动横杠的Bitmap，然后添加到bmp中，与之前绘制的圆合成
        Bitmap scrollBitmap = createScrollBitmap(width, height);
        // 改变@scrollBitmap的偏移，生成不同的Bitmap
        Rect srcRect = new Rect(0, (int)(height/SHOW_TIEM_NUMBER*offset), width, height+(int)(height/SHOW_TIEM_NUMBER*offset));
        canvas.drawBitmap(scrollBitmap, srcRect, contentRect, paint);
        return bmp;
    }

	// 绘制滚动条的Bitmap，总共SHOW_TIEM_NUMBER+1，方便实现一个刻度的拖动全动画
	private Bitmap createScrollBitmap(int width, int height) {
		float itemHeight = height/Float.valueOf(SHOW_TIEM_NUMBER); // 计算每一个小横杠的高度
		// 创建小横杠顺序的Bitmap
		Bitmap resultBitmap = Bitmap.createBitmap(width, (int) (height+itemHeight), Config.ARGB_8888);
		Canvas canvas = new Canvas(resultBitmap);
		int centerX = width / 2;
		float halfItemWidth;
		RectF itemRect = new RectF();
		// 循环，按照@RACKS的顺序将小横杠绘制到大的Bitmap中
		for (int i = 0; i < RACKS.length; i++) {
			itemRect.top = i * itemHeight;
			itemRect.bottom = (i+1)*itemHeight;
			halfItemWidth = getHalfItemWidth(RACKS.length, i+1, width);
			itemRect.left = centerX - halfItemWidth;
			itemRect.right = centerX + halfItemWidth;
			canvas.drawBitmap(mRollerBitmaps[RACKS[i]], null, itemRect, null);
		}
		return resultBitmap;
	}

	// 实现中间稍窄，获取宽度的一半
	private float getHalfItemWidth(int sum, int progress, int width) {
//		progress = ((progress > sum)  ? sum : progress);
		float halfWidth = (int) (width / 2 * RATIO_ROLLOR_ITEM);
		halfWidth = getInterpolation(progress/Float.valueOf(sum)) * halfWidth;
		return halfWidth;
	}

	// 实现中间稍窄，两头宽的Interpolator
	private float getInterpolation(float f) {
		if (f < 0.5) {
			return (float) (1 - f)/10*3 + 1f;
		} else {
			return (float) f/10*3 + 1f;
		}
	}
}
