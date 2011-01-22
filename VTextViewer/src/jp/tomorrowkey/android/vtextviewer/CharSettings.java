
package jp.tomorrowkey.android.vtextviewer;

public class CharSettings {
    /**
     * 文字
     */
    public final String charcter;

    /**
     * 回転角度
     */
    public final float angle;

    /**
     * xの差分<br />
     * Paint#getFontSpacing() * xが足される<br />
     * -0.5fが設定された場合、1/2文字分左にずれる
     */
    public final float x;

    /**
     * xの差分<br />
     * Paint#getFontSpacing() * yが足される<br />
     * -0.5fが設定された場合、1/2文字分上にずれる
     */
    public final float y;

    public CharSettings(String charcter, float angle, float x, float y) {
        super();
        this.charcter = charcter;
        this.angle = angle;
        this.x = x;
        this.y = y;
    }

    public static final CharSettings[] settings = {
            new CharSettings("、", 0.0f, 0.7f, -0.6f), new CharSettings("。", 0.0f, 0.7f, -0.6f),
            new CharSettings("「", 90.0f, -1.0f, -0.3f), new CharSettings("」", 90.0f, -1.0f, 0.0f),
            new CharSettings("ぁ", 0.0f, 0.1f, -0.1f), new CharSettings("ぃ", 0.0f, 0.1f, -0.1f),
            new CharSettings("ぅ", 0.0f, 0.1f, -0.1f), new CharSettings("ぇ", 0.0f, 0.1f, -0.1f),
            new CharSettings("ぉ", 0.0f, 0.1f, -0.1f), new CharSettings("っ", 0.0f, 0.1f, -0.1f),
            new CharSettings("ゃ", 0.0f, 0.1f, -0.1f), new CharSettings("ゅ", 0.0f, 0.1f, -0.1f),
            new CharSettings("ょ", 0.0f, 0.1f, -0.1f), new CharSettings("ァ", 0.0f, 0.1f, -0.1f),
            new CharSettings("ィ", 0.0f, 0.1f, -0.1f), new CharSettings("ゥ", 0.0f, 0.1f, -0.1f),
            new CharSettings("ェ", 0.0f, 0.1f, -0.1f), new CharSettings("ォ", 0.0f, 0.1f, -0.1f),
            new CharSettings("ッ", 0.0f, 0.1f, -0.1f), new CharSettings("ャ", 0.0f, 0.1f, -0.1f),
            new CharSettings("ュ", 0.0f, 0.1f, -0.1f), new CharSettings("ョ", 0.0f, 0.1f, -0.1f),
            new CharSettings("ー", -90.0f, 0.0f, 0.9f), new CharSettings("a", 90.0f, 0.0f, -0.1f),
            new CharSettings("b", 90.0f, 0.0f, -0.1f), new CharSettings("c", 90.0f, 0.0f, -0.1f),
            new CharSettings("d", 90.0f, 0.0f, -0.1f), new CharSettings("e", 90.0f, 0.0f, -0.1f),
            new CharSettings("f", 90.0f, 0.0f, -0.1f), new CharSettings("g", 90.0f, 0.0f, -0.1f),
            new CharSettings("h", 90.0f, 0.0f, -0.1f), new CharSettings("i", 90.0f, 0.0f, -0.1f),
            new CharSettings("j", 90.0f, 0.0f, -0.1f), new CharSettings("k", 90.0f, 0.0f, -0.1f),
            new CharSettings("l", 90.0f, 0.0f, -0.1f), new CharSettings("m", 90.0f, 0.0f, -0.1f),
            new CharSettings("n", 90.0f, 0.0f, -0.1f), new CharSettings("o", 90.0f, 0.0f, -0.1f),
            new CharSettings("p", 90.0f, 0.0f, -0.1f), new CharSettings("q", 90.0f, 0.0f, -0.1f),
            new CharSettings("r", 90.0f, 0.0f, -0.1f), new CharSettings("s", 90.0f, 0.0f, -0.1f),
            new CharSettings("t", 90.0f, 0.0f, -0.1f), new CharSettings("u", 90.0f, 0.0f, -0.1f),
            new CharSettings("v", 90.0f, 0.0f, -0.1f), new CharSettings("w", 90.0f, 0.0f, -0.1f),
            new CharSettings("x", 90.0f, 0.0f, -0.1f), new CharSettings("y", 90.0f, 0.0f, -0.1f),
            new CharSettings("z", 90.0f, 0.0f, -0.1f), new CharSettings("A", 90.0f, 0.0f, -0.1f),
            new CharSettings("B", 90.0f, 0.0f, -0.1f), new CharSettings("C", 90.0f, 0.0f, -0.1f),
            new CharSettings("D", 90.0f, 0.0f, -0.1f), new CharSettings("E", 90.0f, 0.0f, -0.1f),
            new CharSettings("F", 90.0f, 0.0f, -0.1f), new CharSettings("G", 90.0f, 0.0f, -0.1f),
            new CharSettings("H", 90.0f, 0.0f, -0.1f), new CharSettings("I", 90.0f, 0.0f, -0.1f),
            new CharSettings("J", 90.0f, 0.0f, -0.1f), new CharSettings("K", 90.0f, 0.0f, -0.1f),
            new CharSettings("L", 90.0f, 0.0f, -0.1f), new CharSettings("M", 90.0f, 0.0f, -0.1f),
            new CharSettings("N", 90.0f, 0.0f, -0.1f), new CharSettings("O", 90.0f, 0.0f, -0.1f),
            new CharSettings("P", 90.0f, 0.0f, -0.1f), new CharSettings("Q", 90.0f, 0.0f, -0.1f),
            new CharSettings("R", 90.0f, 0.0f, -0.1f), new CharSettings("S", 90.0f, 0.0f, -0.1f),
            new CharSettings("T", 90.0f, 0.0f, -0.1f), new CharSettings("U", 90.0f, 0.0f, -0.1f),
            new CharSettings("V", 90.0f, 0.0f, -0.1f), new CharSettings("W", 90.0f, 0.0f, -0.1f),
            new CharSettings("X", 90.0f, 0.0f, -0.1f), new CharSettings("Y", 90.0f, 0.0f, -0.1f),
            new CharSettings("Z", 90.0f, 0.0f, -0.1f), new CharSettings("ａ", 90.0f, 0.0f, -0.1f),
            new CharSettings("ｂ", 90.0f, 0.0f, -0.1f), new CharSettings("ｃ", 90.0f, 0.0f, -0.1f),
            new CharSettings("ｄ", 90.0f, 0.0f, -0.1f), new CharSettings("ｅ", 90.0f, 0.0f, -0.1f),
            new CharSettings("ｆ", 90.0f, 0.0f, -0.1f), new CharSettings("ｇ", 90.0f, 0.0f, -0.1f),
            new CharSettings("ｈ", 90.0f, 0.0f, -0.1f), new CharSettings("ｉ", 90.0f, 0.0f, -0.1f),
            new CharSettings("ｊ", 90.0f, 0.0f, -0.1f), new CharSettings("ｋ", 90.0f, 0.0f, -0.1f),
            new CharSettings("ｌ", 90.0f, 0.0f, -0.1f), new CharSettings("ｍ", 90.0f, 0.0f, -0.1f),
            new CharSettings("ｎ", 90.0f, 0.0f, -0.1f), new CharSettings("ｏ", 90.0f, 0.0f, -0.1f),
            new CharSettings("ｐ", 90.0f, 0.0f, -0.1f), new CharSettings("ｑ", 90.0f, 0.0f, -0.1f),
            new CharSettings("ｒ", 90.0f, 0.0f, -0.1f), new CharSettings("ｓ", 90.0f, 0.0f, -0.1f),
            new CharSettings("ｔ", 90.0f, 0.0f, -0.1f), new CharSettings("ｕ", 90.0f, 0.0f, -0.1f),
            new CharSettings("ｖ", 90.0f, 0.0f, -0.1f), new CharSettings("ｗ", 90.0f, 0.0f, -0.1f),
            new CharSettings("ｘ", 90.0f, 0.0f, -0.1f), new CharSettings("ｙ", 90.0f, 0.0f, -0.1f),
            new CharSettings("ｚ", 90.0f, 0.0f, -0.1f), new CharSettings("Ａ", 90.0f, 0.0f, -0.1f),
            new CharSettings("Ｂ", 90.0f, 0.0f, -0.1f), new CharSettings("Ｃ", 90.0f, 0.0f, -0.1f),
            new CharSettings("Ｄ", 90.0f, 0.0f, -0.1f), new CharSettings("Ｅ", 90.0f, 0.0f, -0.1f),
            new CharSettings("Ｆ", 90.0f, 0.0f, -0.1f), new CharSettings("Ｇ", 90.0f, 0.0f, -0.1f),
            new CharSettings("Ｈ", 90.0f, 0.0f, -0.1f), new CharSettings("Ｉ", 90.0f, 0.0f, -0.1f),
            new CharSettings("Ｊ", 90.0f, 0.0f, -0.1f), new CharSettings("Ｋ", 90.0f, 0.0f, -0.1f),
            new CharSettings("Ｌ", 90.0f, 0.0f, -0.1f), new CharSettings("Ｍ", 90.0f, 0.0f, -0.1f),
            new CharSettings("Ｎ", 90.0f, 0.0f, -0.1f), new CharSettings("Ｏ", 90.0f, 0.0f, -0.1f),
            new CharSettings("Ｐ", 90.0f, 0.0f, -0.1f), new CharSettings("Ｑ", 90.0f, 0.0f, -0.1f),
            new CharSettings("Ｒ", 90.0f, 0.0f, -0.1f), new CharSettings("Ｓ", 90.0f, 0.0f, -0.1f),
            new CharSettings("Ｔ", 90.0f, 0.0f, -0.1f), new CharSettings("Ｕ", 90.0f, 0.0f, -0.1f),
            new CharSettings("Ｖ", 90.0f, 0.0f, -0.1f), new CharSettings("Ｗ", 90.0f, 0.0f, -0.1f),
            new CharSettings("Ｘ", 90.0f, 0.0f, -0.1f), new CharSettings("Ｙ", 90.0f, 0.0f, -0.1f),
            new CharSettings("Ｚ", 90.0f, 0.0f, -0.1f), new CharSettings("：", 90.0f, 0.0f, -0.1f),
            new CharSettings("；", 90.0f, 0.0f, -0.1f), new CharSettings("／", 90.0f, 0.0f, -0.1f),
            new CharSettings("", 90.0f, 0.0f, -0.1f), new CharSettings(":", 90.0f, 0.0f, -0.1f),
            new CharSettings(";", 90.0f, 0.0f, -0.1f), new CharSettings("/", 90.0f, 0.0f, -0.1f),
            new CharSettings(".", 90.0f, 0.0f, -0.1f),
    };

    public static CharSettings getSetting(String character) {
        for (int i = 0; i < settings.length; i++) {
            if (settings[i].charcter.equals(character)) {
                return settings[i];
            }
        }
        return null;
    }

    private static final String[] PUNCTUATION_MARK = {
            "、", "。", "「", "」"
    };

    public static boolean isPunctuationMark(String s) {
        for (String functuantionMark : PUNCTUATION_MARK) {
            if (functuantionMark.equals(s)) {
                return true;
            }
        }
        return false;
    }

}
