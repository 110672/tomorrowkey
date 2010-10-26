package jp.tomorrowkey.android.downloadimage;

/**
 * AsyncTaskのdoInBackgroundからonPostExecuteに渡す引数に仕様するクラス
 * 
 * @author tomorrowkey
 * 
 * @param <T>
 */
public class AsyncTaskResult<T> {
	/**
	 * AsyncTaskで取得したデータ
	 */
	private T content;
	/**
	 * エラーメッセージのリソースID
	 */
	private int resId;
	/**
	 * エラーならtrueが設定されている
	 */
	private boolean isError;

	/**
	 * コンストラクタ
	 * 
	 * @param content
	 *          AsyncTaskで取得したデータ
	 * @param isError
	 *          エラーならtrueを設定する
	 * @param resId
	 *          エラーメッセージのリソースIDを指定する
	 */
	private AsyncTaskResult(T content, boolean isError, int resId) {
		this.content = content;
		this.isError = isError;
		this.resId = resId;
	}

	/**
	 * AsyncTaskで取得したデータを返す
	 * 
	 * @return AsyncTaskで取得したデータ
	 */
	public T getContent() {
		return content;
	}

	/**
	 * エラーならtrueを返す
	 * 
	 * @return エラーならtrueを返す
	 */
	public boolean isError() {
		return isError;
	}

	/**
	 * stringリソースのIDを返す
	 * 
	 * @return stringリソースのIDを返す
	 */
	public int getResourceId() {
		return resId;
	}

	/**
	 * AsyncTaskが正常終了した場合の結果を作る
	 * 
	 * @param <T>
	 * @param content
	 *          AsyncTaskで取得したデータを指定する
	 * @return AsyncTaskResult
	 */
	public static <T> AsyncTaskResult<T> createNormalResult(T content) {
		return new AsyncTaskResult<T>(content, false, 0);
	}

	/**
	 * AsyncTaskが異常終了した場合の結果を作る
	 * 
	 * @param <T>
	 * @param resId
	 *          エラーメッセージのリソースIDを指定する
	 * @return AsyncTaskResult
	 */
	public static <T> AsyncTaskResult<T> createErrorResult(int resId) {
		return new AsyncTaskResult<T>(null, true, resId);
	}
}