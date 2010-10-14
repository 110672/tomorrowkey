package jp.tomorrowkey.android.colorpallet;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity implements HSVCallback {

	private ImageView colorView;
	private TextView colorCode;
	private HueBarView hueBar;
	private SaturationBarView saturationBar;
	private ValueBarView valueBar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		colorView = (ImageView) findViewById(R.id.colorView);
		colorCode = (TextView) findViewById(R.id.colorCode);
		hueBar = (HueBarView) findViewById(R.id.hueBar);
		hueBar.setHSVCallback(this);
		saturationBar = (SaturationBarView) findViewById(R.id.saturationBar);
		saturationBar.setHSVCallback(this);
		valueBar = (ValueBarView) findViewById(R.id.valueBar);
		valueBar.setHSVCallback(this);
	}

	@Override
	public void onChangeHSV(float[] hsv) {
		int color = Color.HSVToColor(hsv);
		Bitmap bitmap = Bitmap.createBitmap(new int[] { color }, 1, 1, Config.ARGB_8888);
		colorView.setImageBitmap(bitmap);

		colorCode.setText(String.format("#%02x%02x%02x", Color.red(color), Color.green(color), Color.blue(color)));

		hueBar.setHSV(hsv);
		hueBar.invalidate();
		saturationBar.setHSV(hsv);
		saturationBar.invalidate();
		valueBar.setHSV(hsv);
		valueBar.invalidate();
	}
}