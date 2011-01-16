package jp.tomorrowkey.android.webviewapp;

import android.content.Context;
import android.widget.Toast;

public class Toaster {

	private Context context;

	public Toaster(Context context) {
		this.context = context;
	}

	public void show(String text) {
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}
}
