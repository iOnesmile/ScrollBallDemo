package com.ionesmile.scrollballtest.view.wheel;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;

import com.ionesmile.scrollballtest.R;


public class SmileTimePickerView extends FrameLayout{
	
    private String[] hoursArray = { "00", "01","02", "03", "04", "05", "06", "07", "08", "09", "10", 
            "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"};
    private String[] minsArray = { "00", "01","02", "03", "04", "05", "06", "07", "08", "09", "10", 
            "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23",
            "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37",
            "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50",
            "51", "52", "53", "54", "55", "56", "57", "58", "59"};

	private WheelView wvHour, wvMinute;
	private Context mContext;
	
	private NumberAdapter hourAdapter;
	    
	private NumberAdapter minAdapter;
	
	private TosAdapterView.OnItemSelectedListener callBack;
	
	public SmileTimePickerView(Context context) {
		super(context);
		init(context);
	}
	
	public SmileTimePickerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public SmileTimePickerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		this.mContext = context;
		LayoutInflater.from(context).inflate(R.layout.view_smile_time_picker, this);
		wvHour = (WheelView) findViewById(R.id.wheelview_hour);
		wvMinute = (WheelView) findViewById(R.id.wheelview_minute);

        wvHour.setScrollCycle(true);
        wvMinute.setScrollCycle(true);
        
        hourAdapter = new NumberAdapter(hoursArray, false);
        minAdapter = new NumberAdapter(minsArray, true);

        wvHour.setAdapter(hourAdapter);
        wvMinute.setAdapter(minAdapter);
        
        wvHour.setSelection(7, true);
        wvMinute.setSelection(30, true);
        
        ((WheelTextView)wvHour.getSelectedView()).setTextSize(30);
        ((WheelTextView)wvMinute.getSelectedView()).setTextSize(30);
        

        wvHour.setOnItemSelectedListener(mListener);
        wvMinute.setOnItemSelectedListener(mListener);
        
        wvHour.setUnselectedAlpha(0.5f);
        wvMinute.setUnselectedAlpha(0.5f);
	}
	
	public void setHour(int hour){
		wvHour.setSelection(hour, true);
	}
	
	public void setMinute(int minute){
		wvMinute.setSelection(minute, true);
	}
	
	public int getHour(){
		return wvHour.getSelectedItemPosition();
	}
	
	public int getMinute(){
		return wvMinute.getSelectedItemPosition();
	}
	
	/**
	 * 返回  10:30 类型的时间
	 * @return
	 */
	public String getFormatDate(){
		int hour = wvHour.getSelectedItemPosition();;
		int minute = wvMinute.getSelectedItemPosition();
		String result = String.format("%d:%d", hour, minute);
		return result;
	}
	
	public void setCallback(TosAdapterView.OnItemSelectedListener callBack){
		this.callBack = callBack;
	}
	
    public static int dipToPx(Context context, int dipValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, context
                .getResources().getDisplayMetrics());
    }

    private class NumberAdapter extends BaseAdapter {
        int mHeight = 50;
        String[] mData = null;
        private boolean isLeft = true;

        public NumberAdapter(String[] data, boolean isLeft) {
            mHeight = (int) dipToPx(mContext, mHeight);
            this.mData = data;
            this.isLeft = isLeft;
        }

        @Override
        public int getCount() {
            return (null != mData) ? mData.length : 0;
        }

        @Override
        public View getItem(int arg0) {
            return getView(arg0, null, null);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }
        
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            WheelTextView textView = null;

            if (null == convertView) {
                convertView = new WheelTextView(mContext);
                convertView.setLayoutParams(new TosGallery.LayoutParams(-1, mHeight));
                textView = (WheelTextView) convertView;
                textView.setTextSize(25);
                if (isLeft) {
                	textView.setPadding((int) dipToPx(mContext, 24), 0, 0, 0);
                	textView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
				} else {
					textView.setPadding(0, 0, (int) dipToPx(mContext, 24), 0);
                	textView.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
				}
            }
            
            String text = mData[position];
            if (null == textView) {
                textView = (WheelTextView) convertView;
            }
            
            textView.setText(text);
            return convertView;
        }
    }

    private TosAdapterView.OnItemSelectedListener mListener = new TosAdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(TosAdapterView<?> parent, View view, int position, long id) {
            ((WheelTextView)view).setTextSize(30);
            
            int index = Integer.parseInt(view.getTag().toString());
            int count = parent.getChildCount();
            if(index < count-1){
                ((WheelTextView)parent.getChildAt(index+1)).setTextSize(25);
            }
            if(index>0){
                ((WheelTextView)parent.getChildAt(index-1)).setTextSize(25);
            }
            if (callBack != null) {
            	callBack.onItemSelected(parent, view, position, id);
			}
        }
        
        @Override
        public void onNothingSelected(TosAdapterView<?> parent) {
        	if (callBack != null) {
            	callBack.onNothingSelected(parent);
			}
        }
    };

}
