
package jp.tomorrowkey.android.shakelistener;

import java.util.Arrays;
import java.util.LinkedList;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * 端末が振られたことを検知するクラス
 * 
 * @author tomorrowkey@gmail.com
 */
public class ShakeListener implements SensorEventListener {

    public static final String LOG_TAG = ShakeListener.class.getSimpleName();

    /**
     * X方向に振られた時に{@link ShakeListener.OnShakeListener#onShaked(int)}でこのフラグが立ちます
     */
    public static final int DIRECTION_X = 0x0001;

    /**
     * Y方向に振られた時に{@link ShakeListener.OnShakeListener#onShaked(int)}でこのフラグが立ちます
     */
    public static final int DIRECTION_Y = 0x0010;

    /**
     * Z方向に振られた時に{@link ShakeListener.OnShakeListener#onShaked(int)}でこのフラグが立ちます
     */
    public static final int DIRECTION_Z = 0x0100;

    /**
     * 加速度の判定をするために最大いくつのデータを保持するか設定します
     */
    private static final int SAMPLING_SIZE = 50;

    /**
     * 端末が振られた事を検知するコールバックインターフェイスです
     */
    public interface OnShakeListener {
        /**
         * 端末が振られた時に呼ばれるメソッドです
         * 
         * @param direction 端末が振られた方向を表します
         * @see ShakeListener#DIRECTION_X
         * @see ShakeListener#DIRECTION_Y
         * @see ShakeListener#DIRECTION_Z
         */
        void onShaked(int direction);
    }

    /**
     * コールバッククラス
     */
    private OnShakeListener mOnShakeListener = null;

    /**
     * 振りが検知されなくてもコールバックを返すかを保持します
     */
    private boolean mIsCallbackAlways = false;

    /**
     * 加速度の平均値と、絶対値の差がどれだけ差があると振られた事にするか保持します
     */
    private int mDifferenceThreshold = 500;

    /**
     * 加速度の値を保持しておくリスト
     */
    private LinkedList<float[]> mAccelerometerList;

    /**
     * 加速度の値の合計<br>
     * サイズはx,y,z分で3つ<br>
     * 加速度センサーの反応の度に計算するとコストが高いのでメモリに保持しておく
     */
    private float[] mAccelerometerSamplingSums;

    /**
     * コンストラクタ
     */
    public ShakeListener() {
        mAccelerometerList = new LinkedList<float[]>();
        mAccelerometerSamplingSums = new float[] {
                0f, 0f, 0f
        };
    }

    /**
     * センサーを登録します<br>
     * センサー検知の頻度は{@link SensorManager#SENSOR_DELAY_FASTEST } が設定されます。
     * 振りを検知した時のみにコールバックメソッドが呼び出されます。
     * 
     * @param sensorManager
     * @param l コールバックリスナ
     */
    public void registerListener(SensorManager sensorManager, OnShakeListener l) {
        registerListener(sensorManager, l, SensorManager.SENSOR_DELAY_FASTEST, false);
    }

    /**
     * センサーを登録します<br>
     * センサー検知の頻度は{@link SensorManager#SENSOR_DELAY_FASTEST } が設定されます。
     * 
     * @param isCallbackAlways true を設定した場合、振りを検知しない場合でもコールバックメソッドを呼び出します。false
     *            を設定した場合、振りを検知した場合のみコールバックメソッドを呼び出します。
     * @param sensorManager
     * @param l コールバックリスナ
     */
    public void registerListener(SensorManager sensorManager, OnShakeListener l,
            boolean isCallbackAlways) {
        registerListener(sensorManager, l, SensorManager.SENSOR_DELAY_FASTEST, isCallbackAlways);
    }

    /**
     * センサーを登録します<br>
     * 振りを検知した時のみにコールバックメソッドが呼び出されます。
     * 
     * @param sensorManager
     * @param l コールバックリスナ
     * @param rate センサー検知の頻度を設定します。{@link SensorManager#SENSOR_DELAY_NORMAL}
     *            {@link SensorManager#SENSOR_DELAY_UI}
     *            {@link SensorManager#SENSOR_DELAY_GAME}
     *            {@link SensorManager#SENSOR_DELAY_FASTEST}
     */
    public void registerListener(SensorManager sensorManager, OnShakeListener l, int rate) {
        registerListener(sensorManager, l, rate, false);
    }

    /**
     * センサーを登録します。
     * 
     * @param sensorManager
     * @param l コールバックリスナ
     * @param rate センサー検知の頻度を設定します。{@link SensorManager#SENSOR_DELAY_NORMAL}
     *            {@link SensorManager#SENSOR_DELAY_UI}
     *            {@link SensorManager#SENSOR_DELAY_GAME}
     *            {@link SensorManager#SENSOR_DELAY_FASTEST}
     * @param isCallbackAlways true を設定した場合、振りを検知しない場合でもコールバックメソッドを呼び出します。 false
     *            を設定した場合、振りを検知した場合のみコールバックメソッドを呼び出します。
     */
    public void registerListener(SensorManager sensorManager, OnShakeListener l, int rate,
            boolean isCallbackAlways) {
        if (l == null)
            throw new IllegalArgumentException("OnShakeListener is required");

        mOnShakeListener = l;
        mIsCallbackAlways = isCallbackAlways;
        mAccelerometerList.clear();
        mAccelerometerSamplingSums = new float[] {
                0f, 0f, 0f
        };
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), rate);
    }

    /**
     * センサー登録を解除します。
     * 
     * @param sensorManager
     */
    public void unregisterListener(SensorManager sensorManager) {
        sensorManager.unregisterListener(this);
        mOnShakeListener = null;
    }

    /**
     * 現在センサーが登録されているか判定します。
     * 
     * @return true の場合、登録されている。falseの場合、登録されていない。
     */
    public boolean isRegisteredListener() {
        return mOnShakeListener != null;
    }

    /**
     * 加速度の平均値と、絶対値の差がどれだけ差があると振られた事にするかを設定します<br>
     * この値を設定することで振りの検知の強弱を設定できます。 <br>
     * 初期値は500です。<br>
     * 最適な値は端末または使う人によって異なります
     * 
     * @param differenceThreshold
     */
    public void setDifferenceThreshold(int differenceThreshold) {
        mDifferenceThreshold = differenceThreshold;
    }

    // @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    // @Override
    public void onSensorChanged(SensorEvent event) {
        mAccelerometerSamplingSums[0] += event.values[SensorManager.DATA_X];
        mAccelerometerSamplingSums[1] += event.values[SensorManager.DATA_Y];
        mAccelerometerSamplingSums[2] += event.values[SensorManager.DATA_Z];
        mAccelerometerList.add(Arrays.copyOf(event.values, event.values.length));
        if (mAccelerometerList.size() > SAMPLING_SIZE) {
            float[] removedValues = mAccelerometerList.removeFirst();
            mAccelerometerSamplingSums[0] -= removedValues[SensorManager.DATA_X];
            mAccelerometerSamplingSums[1] -= removedValues[SensorManager.DATA_Y];
            mAccelerometerSamplingSums[2] -= removedValues[SensorManager.DATA_Z];
        }

        float xAverage = mAccelerometerSamplingSums[0] / mAccelerometerList.size();
        float yAverage = mAccelerometerSamplingSums[1] / mAccelerometerList.size();
        float zAverage = mAccelerometerSamplingSums[2] / mAccelerometerList.size();

        float xAbsTotal = 0;
        float yAbsTotal = 0;
        float zAbsTotal = 0;
        for (int i = 0; i < mAccelerometerList.size(); i++) {
            float[] values = mAccelerometerList.get(i);
            xAbsTotal += Math.abs(values[SensorManager.DATA_X] - xAverage);
            yAbsTotal += Math.abs(values[SensorManager.DATA_Y] - yAverage);
            zAbsTotal += Math.abs(values[SensorManager.DATA_Z] - zAverage);
        }

        int direction = 0;

        if (xAbsTotal > mDifferenceThreshold)
            direction |= DIRECTION_X;

        if (yAbsTotal > mDifferenceThreshold)
            direction |= DIRECTION_Y;

        if (zAbsTotal > mDifferenceThreshold)
            direction |= DIRECTION_Z;

        if (mOnShakeListener != null && (mIsCallbackAlways || direction != 0))
            mOnShakeListener.onShaked(direction);
    }
}
