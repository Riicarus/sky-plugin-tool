package com.skyline.plugin.loader;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * [FEATURE INFO]<br/>
 * 单一插件加载器, 由文件夹插件加载器调用, 加载单个插件
 *
 * @author Skyline
 * @create 2022-10-7 0:10
 * @since 1.0.0
 */
public class SinglePluginLoader extends URLClassLoader {

    /**
     * @param url 插件所在的文件路径
     * @param parent 父类加载器
     */
    public SinglePluginLoader(URL url, ClassLoader parent) {
        super(new URL[]{url}, parent);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        return super.findClass(name);
    }
}
