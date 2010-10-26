package jp.tomorrowkey.android.delayedprogress;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	private Button btnWait1Second;
	private Button btnWait5Second;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		btnWait1Second = (Button) findViewById(R.id.btnWait1Second);
		btnWait1Second.setOnClickListener(this);
		btnWait5Second = (Button) findViewById(R.id.btnWait5Second);
		btnWait5Second.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.btnWait1Second) {
			WaitTask task = new WaitTask(this);
			task.execute(1000);
		} else if (id == R.id.btnWait5Second) {
			WaitTask task = new WaitTask(this);
			task.execute(5000);
		}
	}

	class WaitTask extends AsyncTask<Integer, Void, Integer> {

		/* プログレスが表示されるまでの閾値 */
		private static final int PROGRESS_DELAY = 2000;

		// Messageコード（数値は適当）
		private final int MESSAGE_WHAT = 100;

		private Context context;
		private ProgressDialog progressDialog = null;
		private Handler handler;

		public WaitTask(Context context) {
			this.context = context;
			handler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					progressDialog = new ProgressDialog(WaitTask.this.context);
					progressDialog.setMessage("please wait");
					progressDialog.show();
				}
			};
		}

		@Override
		protected Integer doInBackground(Integer... params) {

			Message msg = new Message();
			msg.what = MESSAGE_WHAT;
			handler.sendMessageDelayed(msg, PROGRESS_DELAY);

			try {
				Thread.sleep(params[0]);
			} catch (InterruptedException e) {
				Log.d("DelayedProgress", e.getMessage(), e);
			}
			return params[0];
		}

		@Override
		protected void onPostExecute(Integer result) {
			if (progressDialog != null && progressDialog.isShowing()) {
				progressDialog.dismiss();
			} else {
				handler.removeMessages(MESSAGE_WHAT);
			}
			Toast.makeText(context, String.format("%d second has passed", result), Toast.LENGTH_LONG).show();
		}

	}
}