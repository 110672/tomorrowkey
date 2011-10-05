
package jp.tomorrowkey.android.fontmetrics;

import android.graphics.Paint;

/**
 * ユーティリティクラス
 * 
 * @author tomorrowkey
 */
public class Util {
    /**
     * Paintを作成します
     * 
     * @param color 色
     * @return Paint
     */
    public static Paint getPaint(int color) {
        return getPaint(color, true);
    }

    /**
     * Paintを作成します
     * 
     * @param color 色
     * @param antiAlias true アンチエイリアスを有効にする/false アンチエイリアスを無効にする
     * @return Paint
     */
    public static Paint getPaint(int color, boolean antiAlias) {
        return getPaint(color, antiAlias, 12);
    }

    /**
     * Paintを作成します Paintを作成します
     * 
     * @param color 色
     * @param antiAlias true アンチエイリアスを有効にする/false アンチエイリアスを無効にする
     * @param textSize テキストサイズ
     * @return
     */
    public static Paint getPaint(int color, boolean antiAlias, int textSize) {
        Paint paint = new Paint();
        paint = new Paint();
        paint.setAntiAlias(antiAlias);
        paint.setColor(color);
        paint.setTextSize(textSize);
        return paint;
    }
}
