package com.skyline.plugin.loader;

import com.skyline.plugin.AbstractPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * [FEATURE INFO]<br/>
 * 文件夹插件加载器, 加载整个插件文件夹下的所有被允许加载的插件
 *
 * @author Skyline
 * @create 2022-10-7 16:33
 * @since 1.0.0
 */
public class DirPluginLoader extends ClassLoader {

    /**
     * 需要被加载的插件所在的文件目录, 使用绝对路径
     */
    private final String dir;

    /**
     * 允许加载的插件接口定义文件路径
     */
    private final static String CONFIG_PATH = "config/plugins.properties";

    /**
     * 禁止加载的插件集合
     */
    private final HashSet<Integer> disabledPluginIdSet = new HashSet<>();

    /**
     * 允许加载的插件接口集合, 由 CONFIG_PATH 下定义的属性决定
     */
    private final HashSet<Class<?>> admittedPluginInterfaceSet = new HashSet<>();

    /**
     * 被加载成功的插件集合
     */
    private final HashSet<AbstractPlugin> loadedPluginSet = new HashSet<>();

    public DirPluginLoader(String dir, ClassLoader parent) {
        super(parent);

        this.dir = dir;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        return super.findClass(name);
    }

    /**
     * 获取已被加载的插件注册表
     *
     * @return HashMap 已被加载的插件集合
     */
    public HashSet<AbstractPlugin> getLoadedPluginSet() {
        return loadedPluginSet;
    }

    /**
     * 加载文件夹下所有允许加载的插件
     */
    public void loadPlugin() {
        loadProperties();
        HashSet<Class<?>> pluginClsSet = loadPluginFromJars();
        installPlugins(pluginClsSet);
    }

    /**
     * 从 plugins.properties 文件中获取 允许被加载的接口
     */
    private void loadProperties() {
        admittedPluginInterfaceSet.clear();
        loadedPluginSet.clear();

        Properties properties = new Properties();

        InputStream in = DirPluginLoader.class.getClassLoader().getResourceAsStream(CONFIG_PATH);
        try {
            properties.load(in);
        } catch (IOException e) {
            System.out.println("从路径: " + CONFIG_PATH + " 加载配置文件失败.");
        }

        Enumeration<?> propertyNames = properties.propertyNames();
        while (propertyNames.hasMoreElements()) {
            String name = (String) propertyNames.nextElement();
            String className = properties.getProperty(name);

            Class<?> clazz;
            try {
                clazz = Class.forName(className);
            } catch (ClassNotFoundException e) {
                System.out.println("此类无法加载: " + className);
                continue;
            }

            admittedPluginInterfaceSet.add(clazz);
        }

        try {
            if (in != null) in.close();
        } catch (IOException e) {
            System.out.println("关闭配置文件读入流失败.");
        }
    }

    /**
     * 从传入的 dir 路径下加载所有的 Jar 包, 返回可用的 Jar
     *
     * @return 可用的 Jar, 但不一定是对应的插件 Jar
     */
    private HashSet<Class<?>> loadPluginFromJars() {
        File fileDir = new File(dir);
        File[] files = fileDir.listFiles();

        if (files == null) {
            System.out.println("没有可以加载的Jar包.");
            return new HashSet<>();
        }

        // 找到文件夹下所有的 jar
        HashSet<Class<?>> pluginClsSet = new HashSet<>();
        for (File file : files) {
            if (file.getName().endsWith(".jar")) {
                try {
                    JarFile jarFile = new JarFile(file);
                    Enumeration<JarEntry> entries = jarFile.entries();
                    while (entries.hasMoreElements()) {
                        // 返回对应 Jar 中的所有文件
                        JarEntry jarEntry = entries.nextElement();

                        if (!jarEntry.isDirectory() && jarEntry.getName().endsWith(".class")) {
                            String className = jarEntry.getName().split("\\.")[0].replace("/", ".");
                            Class<?> clazz;
                            try {
                                // 使用 SinglePluginLoader 加载对应插件 Jar
                                SinglePluginLoader singlePluginLoader = new SinglePluginLoader(file.toURI().toURL(), this);
                                clazz = singlePluginLoader.loadClass(className);
                                pluginClsSet.add(clazz);
                            } catch (ClassNotFoundException e) {
                                System.out.println("加载类 "+ className + "失败");
                            }
                        }
                    }

                    jarFile.close();
                } catch (IOException e) {
                    System.out.println("Jar包读取失败.");
                }
            }
        }

        return pluginClsSet;
    }

    /**
     * 从所有读取出来的类文件集合中, 加载插件
     *
     * @param pluginClsSet 从对应路径读取出的可用的类文件集合
     */
    private void installPlugins(HashSet<Class<?>> pluginClsSet) {
        for (Class<?> clazz : pluginClsSet) {
            for (Class<?> cls : admittedPluginInterfaceSet) {
                if (cls.isAssignableFrom(clazz)) {
                    AbstractPlugin plugin;
                    try {
                        plugin = (AbstractPlugin) clazz.newInstance();
                        ((SinglePluginLoader) plugin.getClass().getClassLoader()).close();
                    } catch (InstantiationException | IllegalAccessException e) {
                        System.out.println("实例化类 "+ clazz.getName() + "失败");
                        continue;
                    } catch (IOException e) {
                        System.out.println("关闭类加载器失败.");
                        continue;
                    }

                    if (disabledPluginIdSet.contains(plugin.getPluginInfo().getId())) {
                        System.out.println(plugin.getPluginInfo().getName() + "[" + plugin.getPluginInfo().getVersion() + "] " + "已被禁用, 不做加载.");
                    }

                    System.out.println(plugin.getPluginInfo().getName() + "[" + plugin.getPluginInfo().getVersion() + "] " + "正在加载 ......");
                    if (plugin.install(null)) {
                        System.out.println(plugin.getPluginInfo().getName() + "[" + plugin.getPluginInfo().getVersion() + "] " + "加载成功.");
                        loadedPluginSet.add(plugin);
                    }
                    else {
                        System.out.println(plugin.getPluginInfo().getName() + "[" + plugin.getPluginInfo().getVersion() + "] " + "加载失败.");
                    }

                    System.out.println();
                }
            }
        }
    }

    /**
     * 卸载所有插件
     */
    public void unloadAll() {
        Iterator<AbstractPlugin> iterator = loadedPluginSet.iterator();
        while (iterator.hasNext()) {
            AbstractPlugin plugin = iterator.next();
            if (uninstallPlugin(plugin)) {
                iterator.remove();
            }
        }
    }

    /**
     * 卸载插件
     *
     * @param clazz 插件对应的接口类型
     * @param name 插件名称, 如果为 null, 则卸载对应类型下的所有插件
     * @param version 插件版本号, 如果为 null, 则卸载对应类型和名称下的所有插件
     */
    public void unloadPlugin(Class<?> clazz, String name, String version) {
        if (loadedPluginSet.isEmpty()) {
            System.out.println("没有加载对应的插件, 无法卸载.");
            return;
        }

        if (!admittedPluginInterfaceSet.contains(clazz)) {
            System.out.println("该插件接口没有加载任何插件.");
            return;
        }

        // 只传入 class, 卸载对应类型下所有插件
        if (name == null || "".equals(name.trim())) {
            Iterator<AbstractPlugin> iterator = loadedPluginSet.iterator();
            while (iterator.hasNext()) {
                AbstractPlugin plugin = iterator.next();
                if (clazz.isAssignableFrom(plugin.getClass())) {
                    if (uninstallPlugin(plugin)) {
                        iterator.remove();
                    }
                }
            }

            return;
        }

        // 只传入 class 和 name, 删除对应类型和名称下的所有插件
        if (version == null || "".equals(version.trim())) {
            Iterator<AbstractPlugin> iterator = loadedPluginSet.iterator();
            while (iterator.hasNext()) {
                AbstractPlugin plugin = iterator.next();
                if (clazz.isAssignableFrom(plugin.getClass()) && plugin.getPluginInfo().getName().equals(name)) {
                    if (uninstallPlugin(plugin)) {
                        iterator.remove();
                    }
                }
            }

            return;
        }

        // 删除对应类型, 名称, 版本的插件
        Iterator<AbstractPlugin> iterator = loadedPluginSet.iterator();
        while (iterator.hasNext()) {
            AbstractPlugin plugin = iterator.next();
            if (clazz.isAssignableFrom(plugin.getClass()) && plugin.getPluginInfo().getName().equals(name) && plugin.getPluginInfo().getVersion().equals(version)) {
                if (uninstallPlugin(plugin)) {
                    iterator.remove();
                }
            }
        }
    }

    /**
     * 卸载插件
     *
     * @param plugin AbstractPlugin 需要被卸载的插件
     */
    public void unloadPlugin(AbstractPlugin plugin) {
        Iterator<AbstractPlugin> iterator = loadedPluginSet.iterator();
        while (iterator.hasNext()) {
            AbstractPlugin p = iterator.next();
            if (p.equals(plugin)) {
                if (uninstallPlugin(plugin)) {
                    iterator.remove();
                }
            }
        }
    }

    /**
     * 卸载插件<br/>
     * 此方法效率较低
     *
     * @param pluginId Integer, 插件id
     */
    public void unloadPlugin(int pluginId) {
        Iterator<AbstractPlugin> iterator = loadedPluginSet.iterator();
        while (iterator.hasNext()) {
            AbstractPlugin plugin = iterator.next();
            if (plugin.getPluginInfo().getId().equals(pluginId)) {
                if (uninstallPlugin(plugin)) {
                    iterator.remove();
                }
            }
        }
    }

    /**
     * 卸载插件
     *
     * @param plugin AbstractPlugin 插件
     */
    private boolean uninstallPlugin(AbstractPlugin plugin) {
        boolean success;

        if (plugin.uninstall()) {
            disabledPluginIdSet.add(plugin.getPluginInfo().getId());
            success = true;
            System.out.println(plugin.getPluginInfo().getName() + "[" + plugin.getPluginInfo().getVersion() + "] " + "卸载成功.");
        } else {
            success = false;
            System.out.println(plugin.getPluginInfo().getName() + "[" + plugin.getPluginInfo().getVersion() + "] " + "卸载失败.");
        }

        System.out.println();

        return success;
    }
}
