package jp.tomorrowkey.android.downloadimage;

import android.graphics.Bitmap;

/**
 * DownloadImageTaskのコールバックインターフェイス
 * 
 * @author tomorrowkey
 * 
 */
public interface DownloadImageTaskCallback {
	/**
	 * 画像のダウンロードが成功した時に呼ばれるメソッド
	 * 
	 * @param image
	 *          ダウンロードした画像
	 */
	void onSuccessDownloadImage(Bitmap image);

	/**
	 * 画像のダウンロードが失敗した時に呼ばれるメソッド
	 * 
	 * @param resId
	 *          エラーメッセージのリソースID
	 */
	void onFailedDownloadImage(int resId);
}