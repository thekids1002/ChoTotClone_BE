package com.chototclone.Utils;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    /**
     * Thêm số ngày vào ngày hiện tại và trả về ngày mới.
     *
     * @param days Số ngày để thêm
     * @return Ngày mới
     */
    public static Date addDays(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, days);
        return calendar.getTime();
    }
}
