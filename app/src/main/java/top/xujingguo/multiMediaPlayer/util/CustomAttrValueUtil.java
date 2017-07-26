package top.xujingguo.multiMediaPlayer.util;

import android.content.Context;
import android.content.res.TypedArray;

/**
 * Created by xujingguo on 2017/7/24.
 */

public class CustomAttrValueUtil {

    /**
     * 动态获取当前主题中的自定义颜色属性值
     *
     * @param attr
     *         e.g R.attr.colorAccent
     * @param defaultColor
     *         默认颜色值
     */
    public static int getAttrColorValue(int attr, int defaultColor, Context context) {

        int[] attrsArray = {attr};
        TypedArray typedArray = context.obtainStyledAttributes(attrsArray);
        int value = typedArray.getColor(0, defaultColor);
        typedArray.recycle();
        return value;
    }
}
