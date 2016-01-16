/*
 * Copyright 2015. Alex Zhang aka. ztc1997
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zprogrammer.tool.util;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Pair;
import android.view.View;
import android.view.Window;

import com.b.a.V;
import com.zprogrammer.tool.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import cn.bmob.v3.a.b.This;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.XListener;
import cn.bmob.v3.requestmanager.of;

public class UiUtil {
    public static void startActivity(Activity activity, Intent intent, Pair<? extends View, String>... pairs) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ArrayList<Pair<? extends View, String>> pairList = new ArrayList<>(Arrays.asList(pairs));

            View statusbar = activity.findViewById(android.R.id.statusBarBackground);
            if (statusbar != null)
                pairList.add(Pair.create(statusbar, Window.STATUS_BAR_BACKGROUND_TRANSITION_NAME));

            View navbar = activity.findViewById(android.R.id.navigationBarBackground);
            if (navbar != null)
                pairList.add(Pair.create(navbar, Window.NAVIGATION_BAR_BACKGROUND_TRANSITION_NAME));

            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity, pairList.toArray(new Pair[pairList.size()]));
            activity.startActivity(intent, options.toBundle());
        } else {
            activity.startActivity(intent);
        }
        activity.overridePendingTransition(R.anim.slide_up, R.anim.scale_down);
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static void loadImageThumbnail(Context context, BmobFile file, int width, int height, int quality) {
        JSONObject json = new JSONObject();

        try {
            JSONObject json1;
            (json1 = new JSONObject()).put("image", file.getFileUrl(context));
            json1.put("mode", 5);
            json1.put("quality", quality);
            json1.put("width", width);
            json1.put("height", height);
            json1.put("outType", 1);
            json.put("data", json1);
            json.put("c", "Fack");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("params", json);
        This width1 = new This(context, 1, "api", "/8/thumbnail", map);
        of.I(context).V(width1, new XListener() {
            public final void onSuccess(V data) {
                byte[] data1 = Base64.decode(data.cA().ap("file").getAsString(), 0);
                Bitmap bitmap = BitmapFactory.decodeByteArray(data1, 0, data1.length);
            }

            public final void onFailure(int code, String e) {
            }
        });
    }
}
