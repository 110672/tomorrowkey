package jp.tomorrowkey.android.downloadimage;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

/**
 * ネットワークから画像をダウンロードする非同期タスク
 * 
 * @author tomorrowkey
 * 
 */
public class DownloadImageTask extends AsyncTask<String, Void, AsyncTaskResult<Bitmap>> {

	private Context context;
	private DownloadImageTaskCallback callback;
	private ProgressDialog progressDialog;

	public DownloadImageTask(Context context, DownloadImageTaskCallback callback) {
		this.context = context;
		this.callback = callback;
	}

	@Override
	public void onProgressUpdate(Void... values) {
	}

	public void onPreExecute() {
		// プログレスを表示する
		progressDialog = new ProgressDialog(context);
		progressDialog.setMessage(context.getText(R.string.dialog_message_downloading));
		progressDialog.show();
	}

	@Override
	public AsyncTaskResult<Bitmap> doInBackground(String... params) {
		try {
			// 画像をダウンロードする
			HttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet(new URI(params[0]));
			HttpResponse response = client.execute(get);
			int statusCode = response.getStatusLine().getStatusCode();
			switch (statusCode) {
			case HttpStatus.SC_OK:
				// 画像のダウンロードが成功した場合
				InputStream is = response.getEntity().getContent();
				Bitmap bitmap = BitmapFactory.decodeStream(is);
				is.close();
				return AsyncTaskResult.createNormalResult(bitmap);
			case HttpStatus.SC_NOT_FOUND:
				// 画像がなかった場合
				// 画像がありません
				return AsyncTaskResult.createErrorResult(R.string.toast_not_found);
			default:
				// その他エラーで画像がダウンロードできなかった場合
				// サーバーエラー
				return AsyncTaskResult.createErrorResult(R.string.toast_server_error);
			}
		} catch (URISyntaxException e) {
			// URLが不正です
			return AsyncTaskResult.createErrorResult(R.string.toast_uri_syntax_error);
		} catch (ClientProtocolException e) {
			// ネットワークエラー
			return AsyncTaskResult.createErrorResult(R.string.toast_network_error);
		} catch (IllegalStateException e) {
			// 不明なエラー
			return AsyncTaskResult.createErrorResult(R.string.toast_unkown_error);
		} catch (IOException e) {
			// 不明なエラー
			return AsyncTaskResult.createErrorResult(R.string.toast_unkown_error);
		}
	}

	public void onPostExecute(AsyncTaskResult<Bitmap> result) {
		// プログレスを閉じる
		progressDialog.dismiss();

		if (result.isError()) {
			// エラーをコールバックで返す
			callback.onFailedDownloadImage(result.getResourceId());
		} else {
			// ダウンロードした画像コールバックでを返す
			callback.onSuccessDownloadImage(result.getContent());
		}
	}
}
