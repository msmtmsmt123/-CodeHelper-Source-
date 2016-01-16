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

import com.google.gson.Gson;
import com.zprogrammer.tool.MyApplication;
import com.zprogrammer.tool.bean.Data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FavoritesUtil {
    public static final String FAVORITES_PATH = MyApplication.FILES_PATH + File.separator + "FAVORITES" + File.separator;
    private static final Gson gson = new Gson();

    public static Data readDataFromFavorites(String id) {
        try {
            String json = FileUtil.readString(FAVORITES_PATH + id);
            return gson.fromJson(json, Data.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Data readDataFromFavorites(File file) {
        try {
            String json = FileUtil.readString(file.getPath());
            return gson.fromJson(json, Data.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean writeDataToFavorites(Data data) {
        try {
            String json = gson.toJson(data);
            FileUtil.writeString(FAVORITES_PATH + data.getObjectId(), json);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean deleteDataFromFavorites(String id) {
        File file = new File(FAVORITES_PATH + id);
        return file.delete();
    }

    public static List<Data> readDataListFromFavorites() {
        List<File> files = FileUtil.getFileSort(FAVORITES_PATH);
        List<Data> datas = new ArrayList<>();
        for (File f : files) {
            Data data = readDataFromFavorites(f);
            if (data != null) datas.add(data);
        }
        return datas;
    }
}
