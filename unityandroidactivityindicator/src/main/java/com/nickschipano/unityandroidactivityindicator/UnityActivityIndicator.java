package com.nickschipano.unityandroidactivityindicator;

import android.content.Context;
import android.view.*;
import android.app.Activity;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.util.Log;
import android.app.Activity;
import java.lang.Runnable;

public class UnityActivityIndicator
{
    private Context _context;
    private static String tag = "UnityActivityIndicator";

    public UnityActivityIndicator(Context context)
    {
        // context passed in to ctor should point to the com.unity3d.player.UnityPlayer activity
        this._context = context;
    }

    public void MoveToCenterOfScreen()
    {
        // This needs to happen on the ui thread because that's
        // where Unity instantiates the layout.
        final Activity activity = (Activity)_context;
        activity.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                View view = activity.findViewById(android.R.id.content);
                ViewGroup vg = (ViewGroup)view;
                SetChildProgressBarGravity(vg, Gravity.CENTER);
            }
        });
    }

    public void SetGravity(final int g)
    {
        final Activity activity = (Activity)_context;
        activity.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                View view = activity.findViewById(android.R.id.content);
                ViewGroup vg = (ViewGroup)view;
                SetChildProgressBarGravity(vg, g);
            }
        });
    }

    private void SetChildProgressBarGravity(ViewGroup vg, int g)
    {
        final int count = vg.getChildCount();
        for (int i=0; i<count; i++)
        {
            View v = vg.getChildAt(i);

            if (v instanceof ProgressBar)
            {
                LayoutParams lp = v.getLayoutParams();
                Log.i(tag, "Found ProgressBar!");

                // I'm sure there's a more elegant way to do this
                if (lp instanceof FrameLayout.LayoutParams)
                {
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) v.getLayoutParams();
                    params.gravity = g;
                    v.setLayoutParams(params);
                }
                else if (lp instanceof LinearLayout.LayoutParams)
                {
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) v.getLayoutParams();
                    params.gravity = g;
                    v.setLayoutParams(params);
                }
                else {
                    // shrug!
                    Log.w(tag, "Unknown layout for ProgressBar!");
                }
                break;
            }
            else if (v instanceof ViewGroup)
            {
                // we found a ViewGroup, look at its children too
                SetChildProgressBarGravity((ViewGroup)v, g);
            }
        }
    }
}
