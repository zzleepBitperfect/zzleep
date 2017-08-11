package pe.geekadvice.zzleep.components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import pe.geekadvice.zzleep.R;
import pe.geekadvice.zzleep.utils.Animate;
import pe.geekadvice.zzleep.utils.Metrics;

/**
 * Created by gerson on 23/09/16.
 */

public class InputLabel extends LinearLayout implements View.OnClickListener
{
	private String strLabel;
	private String strValue;
	private String strHint;
	private String strType;

	private EditText inputValue;
	private TextView inputLabel;

	public InputLabel(Context context)
	{
		super(context);
	}

	public InputLabel(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init(attrs, 0);
	}

	public InputLabel(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		init(attrs, defStyle);
	}

	private void init(AttributeSet attrs, int defStyle)
	{
		final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.InputLabel, defStyle, 0);

		strLabel = a.getString(R.styleable.InputLabel_textLabel);
		strValue = a.getString(R.styleable.InputLabel_textValue);
		strHint = a.getString(R.styleable.InputLabel_textHint);
		strType = a.getString(R.styleable.InputLabel_inputType);

		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.sample_input_label, this);
	}

	public void setText(String value)
	{
		strValue = value;

		updateData();
	}

	public String getText()
	{
		return inputValue.getText().toString();
	}

	private void updateData()
	{
		inputValue.setText(strValue);
		inputLabel.setText(strLabel);
	}

	@Override
	protected void onFinishInflate()
	{
		super.onFinishInflate();

		inputLabel = (TextView) this.findViewById(R.id.inputLabel);
		inputValue = (EditText) this.findViewById(R.id.inputValue);

		inputValue.setHint(strHint);
		int typeValue = 0;
		switch (strType) {
			case "textCapWords":
				typeValue = InputType.TYPE_TEXT_FLAG_CAP_WORDS;
				break;
			case "textEmailAddress":
				typeValue = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS;
				break;
			case "textPassword":
				typeValue = InputType.TYPE_TEXT_VARIATION_PASSWORD;
				break;
			case "textPersonName":
				typeValue = InputType.TYPE_TEXT_FLAG_CAP_WORDS;
				break;
			case "datetime":
				typeValue = InputType.TYPE_CLASS_DATETIME;
				break;
		}

		inputValue.setInputType(typeValue);

		this.findViewById(R.id.btnClean).setOnClickListener(this);
		this.findViewById(R.id.btnClean).setAlpha(0);

		initTextEvents();
		updateData();
	}

	private void initTextEvents()
	{
		inputValue.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{
			}

			@Override
			public void afterTextChanged(Editable s)
			{
			}

			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
				if (s.length() > 0) {
					changeToFullState();
				} else {
					changeToEmptyState();
				}
			}
		});
	}


	private void changeToFullState()
	{
		if (findViewById(R.id.btnClean).getAlpha() == 0) {
			Animate.fadeIn(findViewById(R.id.btnClean), 100);
			Animate.slideUp(inputLabel, 100);
		}

		ViewGroup.MarginLayoutParams s = (ViewGroup.MarginLayoutParams) inputValue.getLayoutParams();
		s.topMargin = getResources().getDimensionPixelSize(R.dimen.value_margin_top_full);
	}

	private void changeToEmptyState()
	{
		if (findViewById(R.id.btnClean).getAlpha() == 1) {
			Animate.fadeOut(findViewById(R.id.btnClean), 100);
		}

		inputLabel.setVisibility(View.GONE);
		inputLabel.setLayerType(View.LAYER_TYPE_NONE, null);
		ViewGroup.MarginLayoutParams s = (ViewGroup.MarginLayoutParams) inputValue.getLayoutParams();
		s.topMargin = getResources().getDimensionPixelSize(R.dimen.value_margin_top_empty);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId()) {
			case R.id.btnClean:
				strValue = "";
				inputValue.requestFocus();
				updateData();
				break;
		}
	}
}
