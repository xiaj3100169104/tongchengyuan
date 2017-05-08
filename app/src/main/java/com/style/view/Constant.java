package com.style.view;

public class Constant {

    /**
     * 反射获得资源文件
     *
     * @param className "layout"  或者      "id"  等
     * @param idName    资源名称
     * @return
     */
    public static int getCompentID(String className, String idName) {
        int id = 0;
        try {
            Class<?> cls = Class
                    .forName("com.same.city.love.R$"
                            + className);
            id = cls.getField(idName).getInt(cls);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    public static int[] getCompentIDs(String className, String idName) {
        int[] id = {};
        try {
            Class<?> cls = Class
                    .forName("com.same.city.love.R$"
                            + className);
            id = (int[]) cls.getField(idName).get(cls);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }


}
