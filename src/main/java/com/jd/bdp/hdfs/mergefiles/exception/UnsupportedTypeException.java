package com.jd.bdp.hdfs.mergefiles.exception;

/**
 * Created by wubiao on 1/24/16.
 */
public class UnsupportedTypeException extends Exception {

    public UnsupportedTypeException() {
        super();
    }

    public UnsupportedTypeException(String message) {
        super(message);
    }

    public UnsupportedTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnsupportedTypeException(Throwable cause) {
        super(cause);
    }


    @Override
    public String getMessage() {
        return "Not support file type";
    }
}
