package com.skyline.plugin.manage;

import com.skyline.plugin.loader.DirPluginLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * [FEATURE INFO]<br/>
 * 类加载器管理器
 *
 * @author Skyline
 * @create 2022-10-12 11:01
 * @since 1.0.0
 */
public class LoaderManager {

    protected static final HashMap<String, DirPluginLoader> dirPluginLoaderMap = new HashMap<>();

    /**
     * 注册类加载器
     *
     * @param dir 类加载路径
     * @param dirPluginLoader 文件夹类加载器
     */
    public void add(String dir, DirPluginLoader dirPluginLoader) {
        if (dir == null || dirPluginLoader == null) {
            System.out.println("目录或目录类加载器不能为null.");
            return;
        }

        dirPluginLoaderMap.put(dir, dirPluginLoader);
    }

    /**
     * 获取类加载器
     *
     * @param dir 类加载路径
     * @return 文件夹类加载器
     */
    public DirPluginLoader getLoader(String dir) {
        return dirPluginLoaderMap.get(dir);
    }

    /**
     * 获取所有文件夹类加载器
     *
     * @return List, 所有文件夹类加载器
     */
    public List<DirPluginLoader> listAllLoader() {
        return new ArrayList<>(dirPluginLoaderMap.values());
    }
}
