package pe.geekadvice.zzleep.components;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlacePicker;

import pe.geekadvice.zzleep.R;

/**
 * TODO: document your custom view class.
 */
public class StopMenuView extends LinearLayout implements View.OnClickListener {
	private StopMenuEvents listener;
	public Context ctx;

	public StopMenuView(Context context) {
		super(context);
		ctx = context;
		init(null, 0);
	}

	public StopMenuView(Context context, AttributeSet attrs) {
		super(context, attrs);
		ctx = context;
		init(attrs, 0);
	}

	public StopMenuView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		ctx = context;
		init(attrs, defStyle);
	}

	private void init(AttributeSet attrs, int defStyle) {

		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.sample_stop_menu_view, this);

		// Load attributes
		final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.StopMenuView, defStyle, 0);

//		this.findViewById(R.id.txtFindPlace).setOnClickListener(this);
//		this.findViewById(R.id.iconAlarma).setOnClickListener(this);
//		this.findViewById(R.id.btnAlarm).setOnClickListener(this);

		a.recycle();

	}

	public void setOnStopMenuListener(StopMenuEvents l) {
		listener = l;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		// TODO: consider storing these as member variables to reduce
		// allocations per draw cycle.
		int paddingLeft = getPaddingLeft();
		int paddingTop = getPaddingTop();
		int paddingRight = getPaddingRight();
		int paddingBottom = getPaddingBottom();

		int contentWidth = getWidth() - paddingLeft - paddingRight;
		int contentHeight = getHeight() - paddingTop - paddingBottom;

	}

	public void setAddress(String name) {
//		EditText txtF = (EditText) findViewById(R.id.txtSearchAddress);
//		txtF.setText(name);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
//			case R.id.txtFindPlace:
//				listener.onInteraction(v, 2);
//				break;
			case R.id.btnAlarm:
				listener.onInteraction(v, 1);
		}
	}
}
