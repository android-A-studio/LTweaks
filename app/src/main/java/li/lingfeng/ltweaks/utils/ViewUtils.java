package li.lingfeng.ltweaks.utils;

import android.content.Context;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import li.lingfeng.ltweaks.R;

/**
 * Created by smallville on 2017/2/9.
 */

public class ViewUtils {

    public static List<View> findAllViewByName(ViewGroup rootView, String containerName, String name) {
        if (containerName != null)
            rootView = findViewGroupByName(rootView, containerName);
        if (rootView == null)
            return new ArrayList<>();
        return findAllViewByName(rootView, name);
    }

    public static ViewGroup findViewGroupByName(final ViewGroup rootView, String name) {
        Queue<ViewGroup> views = new LinkedList<>();
        views.add(rootView);
        while (views.size() > 0) {
            ViewGroup view = views.poll();
            //Logger.v("findViewGroupByName " + view);
            if (view.getId() > 0) {
                String name_ = ContextUtils.getResNameById(view.getId());
                if (name.equals(name_)) {
                    return view;
                }
            }

            for (int i = 0; i < view.getChildCount(); ++i) {
                View child = view.getChildAt(i);
                if (child instanceof ViewGroup) {
                    views.add((ViewGroup) child);
                }
            }
        }
        return null;
    }

    public static List<View> findAllViewByName(ViewGroup rootView, String name) {
        Queue<View> views = new LinkedList<>();
        for (int i = 0; i < rootView.getChildCount(); ++i) {
            View child = rootView.getChildAt(i);
            views.add(child);
        }

        List<View> results = new ArrayList<>();
        while (views.size() > 0) {
            View view = views.poll();
            //Logger.v("findAllViewByName " + view);
            if (view.getId() > 0) {
                String name_ = ContextUtils.getResNameById(view.getId());
                if (name.equals(name_)) {
                    results.add(view);
                }
            }

            if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;
                for (int i = 0; i < viewGroup.getChildCount(); ++i) {
                    View child = viewGroup.getChildAt(i);
                    views.add(child);
                }
            }
        }
        return results;
    }

    public static <T extends View> List<T> findAllViewByType(ViewGroup rootView, Class<T> type) {
        Queue<View> views = new LinkedList<>();
        for (int i = 0; i < rootView.getChildCount(); ++i) {
            View child = rootView.getChildAt(i);
            views.add(child);
        }

        List<T> results = new ArrayList<>();
        while (views.size() > 0) {
            View view = views.poll();
            //Logger.v("findAllViewByType " + view);
            if (type.isAssignableFrom(view.getClass())) {
                results.add((T) view);
            }

            if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;
                for (int i = 0; i < viewGroup.getChildCount(); ++i) {
                    View child = viewGroup.getChildAt(i);
                    views.add(child);
                }
            }
        }
        return results;
    }

    public static Fragment findFragmentByPosition(FragmentManager fragmentManager, ViewPager viewPager, int position) {
        try {
            Method method = FragmentPagerAdapter.class.getDeclaredMethod("makeFragmentName", int.class, long.class);
            method.setAccessible(true);
            String tag = (String) method.invoke(viewPager.getAdapter(), viewPager.getId(), position);
            return fragmentManager.findFragmentByTag(tag);
        } catch (Exception e) {
            Logger.e("findFragmentByPosition error, " + e);
            return null;
        }
    }

    public static void showDialog(Context context, String message) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton(R.string.app_ok, null)
                .show();
    }

    public static void executeJs(WebView webView, String js) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.evaluateJavascript(js, null);
        } else {
            webView.loadUrl("javascript:" + js);
        }
    }
}
