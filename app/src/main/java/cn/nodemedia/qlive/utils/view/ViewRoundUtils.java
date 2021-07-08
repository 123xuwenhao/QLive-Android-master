package cn.nodemedia.qlive.utils.view;

import android.graphics.Outline;
import android.view.View;
import android.view.ViewOutlineProvider;

public class ViewRoundUtils {
    public static void clipViewCircle(View view) {
        view.setClipToOutline(true);
        view.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setOval(0, 0, view.getWidth(), view.getHeight());
            }
        });
    }

    public static void clipViewCornerByDp(View view, final int pixel) {
        view.setClipToOutline(true);
        view.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), pixel);
            }
        });
    }
}
