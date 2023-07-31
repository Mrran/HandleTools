package io.emqx.mqtt;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class MyTest {


    public static void main(String[] args) {
        Date currDate = new Date();
        System.out.println(currDate.toString());
        // 已经@Deprecated
        System.out.println(currDate.toLocaleString());
        // 已经@Deprecated
        System.out.println(currDate.toGMTString());
    }


}
