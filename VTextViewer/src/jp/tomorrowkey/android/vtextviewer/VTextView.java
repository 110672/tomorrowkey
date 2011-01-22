
package jp.tomorrowkey.android.vtextviewer;

import java.io.File;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.View;

public class VTextView extends View {

    private static final String FONT_PATH = Environment.getExternalStorageDirectory()
            .getAbsolutePath()
            + File.separator + "ipam.ttf";

    private static final int TOP_SPACE = 18;

    private static final int BOTTOM_SPACE = 18;

    private static final int FONT_SIZE = 18;

    private Typeface mFace;

    private Paint mPaint;

    private String text = "";

    private int width;

    private int height;

    public VTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mFace = Typeface.createFromFile(FONT_PATH);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(FONT_SIZE);
        mPaint.setColor(Color.BLACK);
        mPaint.setTypeface(mFace);
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        width = getWidth();
        height = getHeight();
    }

    @Override
    public void onDraw(Canvas canvas) {
        float fontSpacing = mPaint.getFontSpacing();
        float lineSpacing = fontSpacing * 2;
        float x = width - lineSpacing;
        float y = TOP_SPACE + fontSpacing * 2;
        String[] s = text.split("");
        boolean newLine = false;

        for (int i = 1; i <= text.length(); i++) {
            newLine = false;

            CharSettings setting = CharSettings.getSetting(s[i]);
            if (setting == null) {
                // 文字設定がない場合、そのまま描画
                canvas.drawText(s[i], x, y, mPaint);
            } else {
                // 文字設定が見つかったので、設定に従い描画
                canvas.save();
                canvas.rotate(setting.angle, x, y);
                canvas.drawText(s[i], x + fontSpacing * setting.x, y + fontSpacing * setting.y,
                        mPaint);
                canvas.restore();
            }

            if (y + fontSpacing > height - BOTTOM_SPACE) {
                // もう文字が入らない場合
                newLine = true;
            } else {
                // まだ文字が入る場合
                newLine = false;
            }

            if (newLine) {
                // 改行処理
                x -= lineSpacing;
                y = TOP_SPACE + fontSpacing;
            } else {
                // 文字を送る
                y += fontSpacing;
            }
        }
    }
}
