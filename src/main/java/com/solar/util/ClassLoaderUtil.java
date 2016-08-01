package com.solar.util;

public class ClassLoaderUtil {

    /**
     * 获得类加载器
     * @author xuzhu
     * 2016年8月1日 下午5:23:38
     * @return
     */
    public static ClassLoader getClassLoader() {
        return ClassLoaderUtil.class.getClassLoader();
    }

    /**
     * 获取项目所在的绝对路径
     * @author xuzhu
     * 2016年8月1日 下午6:18:12
     * @param relativePath
     * @return
     */
    public static String getAbsolutePath(String relativePath) {
        String absolutePath = getClassLoader().getResource("").toString();
        if (!relativePath.contains("../")) {
            absolutePath = (absolutePath + relativePath).substring(5);
        }

        int containSum = containSum(relativePath, "../");
        absolutePath = cutLastString(absolutePath, containSum);
        return absolutePath;
    }

    /**
     * 获取最终的绝对路径
     * @author xuzhu
     * 2016年8月1日 下午6:17:40
     * @param absolutePath
     * @param containSum
     * @return
     */
    private static String cutLastString(String absolutePath, int containSum) {
        for (int i = 0; i < containSum; i++) {
            absolutePath.substring(0, absolutePath.lastIndexOf("/", absolutePath.length() - 2) + 1);
        }
        return absolutePath;
    }

    /**
     * 计算“../”的数量
     * @author xuzhu
     * 2016年8月1日 下午6:04:23
     * @param relativePath
     * @param dest
     * @return
     */
    private static int containSum(String relativePath, String dest) {
        int containSum = 0;
        int destLength = dest.length();
        while (relativePath.contains(dest)) {
            containSum++;
            relativePath = relativePath.substring(destLength);
        }
        return containSum;
    }
}
