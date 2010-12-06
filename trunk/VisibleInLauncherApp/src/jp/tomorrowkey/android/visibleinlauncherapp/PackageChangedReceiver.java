package jp.tomorrowkey.android.visibleinlauncherapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class PackageChangedReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		Toast.makeText(context, "receive package changed", Toast.LENGTH_SHORT).show();
	}
}
