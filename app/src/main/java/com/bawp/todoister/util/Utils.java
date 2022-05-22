package com.bawp.todoister.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
    public static String formattedDate(Date dueDate) {
        SimpleDateFormat simpleDateFormat = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
        simpleDateFormat.applyPattern("EEE, MM d");
        return simpleDateFormat.format(dueDate);
    }
}
