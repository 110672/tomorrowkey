package jp.tomorrowkey.android.androidlib;

import android.content.Context;

public class Util {

	private static final String PAY_APP_PACKAGE_NAME = "jp.tomorrowkey.android.androidpayapp";

	public static boolean isPaymentApp(Context context) {
		String packageName = context.getPackageName();
		return PAY_APP_PACKAGE_NAME.equals(packageName);
	}
}
