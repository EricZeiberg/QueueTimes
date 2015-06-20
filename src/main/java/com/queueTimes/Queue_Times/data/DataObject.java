package com.queueTimes.Queue_Times.data;

public class DataObject {

    private static Object object;

    public static void setObject(Object object1){
        object = object1;
    }

    public static Object getObject(){
        return object;
    }
}
