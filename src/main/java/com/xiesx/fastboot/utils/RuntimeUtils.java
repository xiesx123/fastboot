package com.xiesx.fastboot.utils;

import java.net.URL;

import cn.hutool.core.text.CharSequenceUtil;

public class RuntimeUtils {

    /**
     * @return 当前操作系统是否为Windows系统
     */
    public static boolean isWindows() {
        return System.getProperties().getProperty("os.name").toUpperCase().indexOf("WINDOWS") != -1;
    }

    /**
     * @return 获取应用根路径（若WEB工程则基于.../WEB-INF/返回，若普通工程则返回类所在路径）
     */
    public static String getRootPath() {
        return getRootPath(true);
    }

    /**
     * @param safe 若WEB工程是否保留WEB-INF
     * @return 返回应用根路径
     */
    public static String getRootPath(boolean safe) {
        // D:/projects/xxx/target/classes/
        // www/wwwroot/xxx/WEB-INF
        // www/wwwroot/xxx/webapps
        String rootPath = null;
        URL _rootURL = RuntimeUtils.class.getClassLoader().getResource("/");
        if (_rootURL == null) {
            _rootURL = RuntimeUtils.class.getClassLoader().getResource("");
            if (_rootURL != null) {
                rootPath = _rootURL.getPath();
            }
        } else {
            rootPath = CharSequenceUtil.removeSuffix(CharSequenceUtil.subBefore(_rootURL.getPath(), safe ? "classes/" : "WEB-INF/", false), "/");
        }
        if (rootPath != null) {
            rootPath = CharSequenceUtil.replace(rootPath, "%20", " ");
            if (isWindows()) {
                rootPath = CharSequenceUtil.removePrefix(rootPath, "/");
            }
            rootPath = CharSequenceUtil.trimToEmpty(rootPath);
        }
        return rootPath;
    }
}
