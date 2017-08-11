package pe.geekadvice.zzleep.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import pe.geekadvice.zzleep.R;

/**
 * TODO: document your custom view class.
 */
public class StopItem extends LinearLayout implements View.OnClickListener
{

	TextView stopTitle;
	TextView stopAddress;
	ImageView iconStop;
	ImageView iconAlarm;
	ImageButton stopSwitch;

	String attrStopTitle = "Title";
	String attrStopAddress = "Toca para editar esta parada";
	Boolean attrActive;
	int iconStopResource;

	public StopItem(Context context)
	{
		super(context);
		init(null, 0);
	}

	public StopItem(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init(attrs, 0);
	}

	public StopItem(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		init(attrs, defStyle);
	}

	private void init(AttributeSet attrs, int defStyle)
	{
		final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.StopItem, defStyle, 0);

		attrStopTitle = a.getString(R.styleable.StopItem_stopTitle);
		attrStopAddress = a.getString(R.styleable.StopItem_stopAddress);
		attrActive = a.getBoolean(R.styleable.StopItem_active, false);
		iconStopResource = a.getResourceId(R.styleable.StopItem_stopIcon, R.mipmap.icon_targets);

		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.fragment_stop_item, this);
	}

	@Override
	protected void onFinishInflate()
	{
		super.onFinishInflate();
		stopTitle = (TextView) this.findViewById(R.id.stopTitle);
		stopAddress = (TextView) this.findViewById(R.id.stopAddress);
		stopSwitch = (ImageButton) this.findViewById(R.id.stopActive);
		iconStop = (ImageView) this.findViewById(R.id.iconStop);

		stopTitle.setText(attrStopTitle);
		stopAddress.setText(attrStopAddress);
		iconStop.setImageResource(iconStopResource);
		if(!attrActive){
			this.setAlpha(0.5F);
			stopSwitch.setImageResource(R.mipmap.switch_off);
		}

		stopSwitch.setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.stopActive:
				if(!attrActive)
				{
					setAlpha(0.5F);
					stopSwitch.setImageResource(R.mipmap.switch_off);
				} else{
					setAlpha(1F);
					stopSwitch.setImageResource(R.mipmap.switch_on);
				}
				attrActive = !attrActive;
				break;
		}
	}

}
