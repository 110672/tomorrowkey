
package jp.tomorrowkey.android.repeatpushbutton;

import jp.tomorrowkey.android.repeatpushbutton.RepeatButton.OnRepeatClickListener;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener, OnRepeatClickListener {

    private TextView txtNumber;

    private RepeatButton btnIncrement;

    private RepeatButton btnDecrement;

    private int number = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        txtNumber = (TextView)findViewById(R.id.txtNumber);
        txtNumber.setText(String.valueOf(number));
        btnIncrement = (RepeatButton)findViewById(R.id.btnIncrement);
        btnIncrement.setOnClickListener(this);
        btnIncrement.setOnRepeatClickListener(this);
        btnDecrement = (RepeatButton)findViewById(R.id.btnDecrement);
        btnDecrement.setOnClickListener(this);
        btnDecrement.setOnRepeatClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnIncrement:
                increment();
                break;
            case R.id.btnDecrement:
                decrement();
                break;
        }
    }

    @Override
    public void onRepeartClick(View v) {
        switch (v.getId()) {
            case R.id.btnIncrement:
                increment();
                break;
            case R.id.btnDecrement:
                decrement();
                break;
        }
    }

    private void increment() {
        number++;
        txtNumber.setText(String.valueOf(number));
    }

    private void decrement() {
        number--;
        txtNumber.setText(String.valueOf(number));
    }
}
