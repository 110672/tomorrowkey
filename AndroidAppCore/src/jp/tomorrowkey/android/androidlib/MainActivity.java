package jp.tomorrowkey.android.androidlib;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {

	private TextView lblMessage;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		lblMessage = (TextView) findViewById(R.id.lblMessage);
		if (Util.isPaymentApp(this)) {
			lblMessage.setText("this is payment app");
		} else {
			lblMessage.setText("this is free app");
		}
	}
}