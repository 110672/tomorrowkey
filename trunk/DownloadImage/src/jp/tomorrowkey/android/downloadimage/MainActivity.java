package jp.tomorrowkey.android.downloadimage;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener, DownloadImageTaskCallback {

	/* 画像のURL */
	private static final String IMAGE_URL = "http://upload.wikimedia.org/wikipedia/commons/4/47/PNG_transparency_demonstration_1.png";

	/* ダウンロードボタン */
	private Button btnDownload;

	/* 画像を表示する部分 */
	private ImageView imgViewer;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		btnDownload = (Button) findViewById(R.id.btnDownload);
		btnDownload.setOnClickListener(this);
		imgViewer = (ImageView) findViewById(R.id.imgViewer);
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		if (id == R.id.btnDownload) {
			// ダウンロードを開始する
			DownloadImageTask task = new DownloadImageTask(this, this);
			task.execute(IMAGE_URL);
		}
	}

	@Override
	public void onSuccessDownloadImage(Bitmap image) {
		// ダウンロードした画像を設定する
		imgViewer.setImageBitmap(image);
	}

	@Override
	public void onFailedDownloadImage(int resId) {
		// エラーの内容をトーストに表示する
		Toast.makeText(this, resId, Toast.LENGTH_LONG).show();
	}
}