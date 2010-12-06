package jp.tomorrowkey.android.visibleinlauncherapp;

import android.content.ComponentName;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class SettingActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.setting);

		int state = getPackageManager().getComponentEnabledSetting(new ComponentName(this, SettingActivity.class));
		if (state == PackageManager.COMPONENT_ENABLED_STATE_ENABLED) {
			PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("visible_in_launcher_1", true);
		} else {
			PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("visible_in_launcher_1", false);
		}

		state = getPackageManager().getComponentEnabledSetting(new ComponentName(this, SettingActivity.class));
		if (state == PackageManager.COMPONENT_ENABLED_STATE_ENABLED) {
			PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("visible_in_launcher_2", true);
		} else {
			PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("visible_in_launcher_2", false);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		int newState;
		if (sharedPreferences.getBoolean(key, true)) {
			newState = PackageManager.COMPONENT_ENABLED_STATE_ENABLED;
		} else {
			newState = PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
		}
		String packageName = getPackageName();
		ComponentName componentName;
		if (key.equals("visible_in_launcher_1")) {
			componentName = new ComponentName(packageName, packageName + ".SettingActivity");
		} else {
			componentName = new ComponentName(packageName, packageName + ".SettingActivityAlias");
		}
		PackageManager packageManager = getPackageManager();
		packageManager.setComponentEnabledSetting(componentName, newState, PackageManager.DONT_KILL_APP);
	}
}
