package jp.tomorrowkey.android.webviewapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

public class MainActivity extends Activity {

	private WebView browser;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// WebView取得
		browser = (WebView) findViewById(R.id.browser);

		// javascript有効化
		browser.getSettings().setJavaScriptEnabled(true);

		// Toastを表示するjavascript interfaceを追加
		browser.addJavascriptInterface(new Toaster(this), Toaster.class.getSimpleName());

		// assetのindex.htmlを読み込み
		browser.loadUrl("file:///android_asset/index.html");
	}

	private String[] MESSAGES = { "HogeHoge", "FugaFuga", "PiyoPiyo" };

	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("change message");
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		// メッセージを選択
		String message = MESSAGES[(int) Math.floor(Math.random() * MESSAGES.length)];

		// javascriptを呼び出し
		browser.loadUrl("javascript:change_message('" + message + "')");
		return true;
	}
}