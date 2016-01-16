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

import com.zprogrammer.tool.bean.Data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.datatype.BmobDate;

public class QueryUtil {
    public static BmobDate parseTime(String time, int offset) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        try {
            Date date = sdf.parse(time);
            date.setTime(date.getTime() + offset);
            return new BmobDate(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int countDatasByCreateAt(List<Data> datas, String createAt) {
        int count = 0;
        for (Data data : datas) {
            if (createAt.equals(data.getCreatedAt()))
                count++;
        }
        return count;
    }
}
