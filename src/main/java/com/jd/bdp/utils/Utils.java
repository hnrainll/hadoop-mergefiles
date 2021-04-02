package com.jd.bdp.utils;

import com.jd.bdp.hdfs.mergefiles.Config;
import com.jd.bdp.hdfs.mergefiles.FileType;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.MD5Hash;
import org.apache.hadoop.security.UserGroupInformation;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by wubiao on 1/18/16.
 */
public class Utils {

    public static String USER = System.getenv("HADOOP_USER_NAME");

    static {
        if (StringUtils.isEmpty(USER)) {
            USER = System.getenv("USER");
            if (StringUtils.isEmpty(USER)) {
                USER = System.getProperty("user.name");
                if (StringUtils.isEmpty(USER)) {
                    try {
                        USER = UserGroupInformation.getCurrentUser().getUserName();
                    } catch (IOException e) {
                        System.err.println(ExceptionUtils.getFullStackTrace(e));
                        System.exit(-1);
                    }
                }
            }
        }
    }

    public static String makeMergeId(String path) {
        GregorianCalendar gc = new GregorianCalendar();
        return USER
                + "_"
                + String.format("%1$4d%2$02d%3$02d%4$02d%5$02d%6$02d", gc
                .get(Calendar.YEAR), gc.get(Calendar.MONTH) + 1, gc
                .get(Calendar.DAY_OF_MONTH), gc.get(Calendar.HOUR_OF_DAY), gc
                .get(Calendar.MINUTE), gc.get(Calendar.SECOND))
                + "_"
                + MD5Hash.digest(path);
    }

    public static String ts() {
        GregorianCalendar gc = new GregorianCalendar();
        return String.format("%1$4d%2$02d%3$02d%4$02d%5$02d%6$02d", gc
                .get(Calendar.YEAR), gc.get(Calendar.MONTH) + 1, gc
                .get(Calendar.DAY_OF_MONTH), gc.get(Calendar.HOUR_OF_DAY), gc
                .get(Calendar.MINUTE), gc.get(Calendar.SECOND));
    }

    public static String dt() {
        GregorianCalendar gc = new GregorianCalendar();
        return String.format("%1$4d%2$02d%3$02d", gc
                .get(Calendar.YEAR), gc.get(Calendar.MONTH) + 1, gc
                .get(Calendar.DAY_OF_MONTH), gc.get(Calendar.HOUR_OF_DAY), gc
                .get(Calendar.MINUTE));
    }

    public static String cutPrefix(String string, String prefix) {
        if (string.startsWith(prefix)) {
            string = string.substring(prefix.length());
        }

        return string;
    }

    public static String cutSuffix(String string, String suffix) {
        if (string.endsWith(suffix)) {
            string = string.substring(0, string.length() - suffix.length());
        }

        return string;
    }

    public static FileType getFileType(Path path, FileSystem fs) throws IOException {
        if (path.getName().endsWith(".lzo")) {
            return FileType.LZO;
        } else if (path.getName().endsWith(".lzo.index")) {
            return FileType.LZO_INDEX;
        } else if (path.getName().endsWith(".orc")) {
            return FileType.ORC;
        } else if (path.getName().endsWith(".avro")) {
            return FileType.AVRO;
        }
        // read file header
        // FSDataInputStream in = fs.open(path);


        if (Config.isWantNoneTypeToText()) {
            return FileType.TEXT;
        }
        return FileType.UNKNOWN;
    }
}
