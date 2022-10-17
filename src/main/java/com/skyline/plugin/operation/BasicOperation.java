package com.skyline.plugin.operation;

import com.skyline.plugin.AbstractPlugin;
import com.skyline.plugin.loader.DirPluginLoader;
import com.skyline.plugin.manage.LoaderManager;

import java.util.HashSet;
import java.util.List;

/**
 * [FEATURE INFO]<br/>
 * 插件基础操作
 *
 * @author Skyline
 * @create 2022-10-12 0:49
 * @since 1.0.0
 */
public class BasicOperation implements Operation {

    protected final LoaderManager loaderManager = new LoaderManager();

    public void loadPluginFromDir(String dir) {
        DirPluginLoader dirPluginLoader = new DirPluginLoader(dir, DirPluginLoader.class.getClassLoader());
        loaderManager.add(dir, dirPluginLoader);

        dirPluginLoader.loadPlugin();
    }

    public void unloadAllPlugins() {
        List<DirPluginLoader> loaders = loaderManager.listAllLoader();

        loaders.forEach(DirPluginLoader::unloadAll);
    }

    public void unloadPluginFromDir(String dir) {
        DirPluginLoader dirPluginLoader = loaderManager.getLoader(dir);
        if (dirPluginLoader == null) {
            System.out.println("没有对应的文件夹类加载器.");
            return;
        }

        dirPluginLoader.unloadAll();
    }

    public void unloadSinglePlugin(String id) {
        Integer _id = Integer.parseInt(id);

        DirPluginLoader pluginLoader = null;

        List<DirPluginLoader> loaders = loaderManager.listAllLoader();

        loop: for (DirPluginLoader loader : loaders) {
            HashSet<AbstractPlugin> loadedPluginSet = loader.getLoadedPluginSet();

            for (AbstractPlugin abstractPlugin : loadedPluginSet) {
                if (abstractPlugin.getPluginInfo().getId().equals(_id)) {
                    pluginLoader = loader;
                    break loop;
                }
            }
        }

        if (pluginLoader != null) {
            pluginLoader.unloadPlugin(_id);
            return;
        }

        System.out.println("没有对应插件.");
    }

    public void listAllPlugins() {
        List<DirPluginLoader> loaders = loaderManager.listAllLoader();

        loaders.forEach(loader -> {
            HashSet<AbstractPlugin> loadedPluginSet = loader.getLoadedPluginSet();
            loadedPluginSet.forEach(plugin -> System.out.println(plugin.getPluginInfo()));
        });
    }

    public void listAllPluginsOfDir(String dir) {
        DirPluginLoader dirPluginLoader = loaderManager.getLoader(dir);

        HashSet<AbstractPlugin> loadedPluginSet = dirPluginLoader.getLoadedPluginSet();
        loadedPluginSet.forEach(plugin -> System.out.println(plugin.getPluginInfo()));
    }

    public void listDetailOfPlugin(String id) {
        Integer _id = Integer.parseInt(id);

        List<DirPluginLoader> loaders = loaderManager.listAllLoader();

        loaders.forEach(loader -> {
            HashSet<AbstractPlugin> loadedPluginSet = loader.getLoadedPluginSet();
            loadedPluginSet.forEach(plugin -> {
                if (plugin.getPluginInfo().getId().equals(_id)) {
                    System.out.println(plugin.getPluginInfo());
                }
            });
        });
    }
}
