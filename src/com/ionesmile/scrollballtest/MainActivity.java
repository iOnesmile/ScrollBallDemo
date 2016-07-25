package com.ionesmile.scrollballtest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.ionesmile.scrollballtest.view.scrollball.RollorFrameUtil;
import com.ionesmile.scrollballtest.view.scrollball.ScrollBallView;
import com.ionesmile.scrollballtest.view.scrollball.ScrollBallView.OnScrollChangeListener;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private TextView mProgressTv;
	private ScrollBallView mScrollBallView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mProgressTv = (TextView) findViewById(R.id.tv_progress);
		mScrollBallView = (ScrollBallView) findViewById(R.id.scrollball);
		
		mScrollBallView.setOnScrollChangeListener(new OnScrollChangeListener() {
			
			@Override
			public void onScrollChange(int progress) {
				mProgressTv.setText(progress + "%");
			}
		});
	}
	
	public void saveImages(View view){
		RollorFrameUtil rollorFrameUtil = new RollorFrameUtil(this);
		Bitmap[] bitmaps = rollorFrameUtil.getBitmapFramesElegant(20, 300, 300);
		for (int i = 0; i < bitmaps.length; i++) {
			saveBitmap(bitmaps[i], "scrollball" + i + ".png");
		}
		Toast.makeText(this, "OK", 0).show();
	}

	private void saveBitmap(Bitmap bitmap, String fileName) {
		File file = new File(Environment.getExternalStorageDirectory(), fileName);
		FileOutputStream fileOS = null;
		try {
			fileOS = new FileOutputStream(file);
			// quality: Hint to the compressor, 0-100. 0 meaning compress for
			// small size, 100 meaning compress for max quality.
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOS);
			fileOS.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fileOS != null) {
				try {
					fileOS.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
