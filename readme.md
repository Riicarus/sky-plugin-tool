# SkyPlugin
version: 1.0

## 概述
### 功能
SkyPlugin 是一个支持插件注册/卸载/热插拔的工具, 同时支持以命令行方式进行相关功能的管理和执行.  

### 特点
- 热插拔
- 自定义加载许可
- 命令行控制, 内置插件控制指令

## 使用
### 插件开发
#### 插件抽象类定义
创建一个工程, 作为定义插件抽象类的工程. 后续的自定义插件的开发都需要引入这个工程的 Jar 包(相当于在这里定义后续开发插件的抽象类).  
需要引入插件基础定义类的 Jar 包:  
```xml
<dependency>
    <groupId>com.skyline</groupId>
    <artifactId>sky-plugin-base</artifactId>
    <version>1.0</version>
</dependency>
```
然后定义相关的抽象类, 继承 `AbstractPlugin` 抽象类, 作为后续插件开发的抽象类定义.  
```java
public abstract class AbstractStorePlugin extends AbstractPlugin {
}
```

#### 插件开发
新建一个工程, 并引入刚才开发好的插件抽象类定义依赖:  
```xml
<dependency>
    <groupId>com.skyline</groupId>
    <artifactId>sky-plugin-abstract-permitted</artifactId>
    <version>1.0</version>
</dependency>
```
继承已定义好的抽象类, 重写对应的方法:  
```java
public class ServerStorePlugin extends AbstractStorePlugin {

    @Override
    public Object run(Object[] objects) {
        System.out.println(pluginInfo);

        System.out.println(Arrays.toString(objects));

        return null;
    }

    @Override
    public boolean install(Object[] objects) {
        System.out.println("插件信息: ");
        System.out.println(pluginInfo);

        return true;
    }

    @Override
    public boolean uninstall() {
        return true;
    }
}
```
`AbstractPlugin#run()` 方法是插件方法的主要执行入口, 加载插件后可以自定义其执行时机.  
`AbstractPlugin#install()` 方法会在插件被加载时调用, 可能会进行一些初始话操作.  
`AbstractPlugin#unistall()` 方法会在插件被卸载时调用, 可能会执行一些结束的方法. 我们建议在这里关闭插件占用的文件流等资源.  
加载和卸载插件方法需要返回 `true`, 才会进行后续的加载和卸载流程.  

同时, 需要在路径: `resources/config` 下创建 `plugin.properties` 配置文件, 需要在里面定义一些插件的基础信息. 有一些信息是必要的, 如下:  
```properties
version=1.0.0
name=服务器存储插件
interfaceName=com.skyline.plugin.AbstractStorePlugin
author=skyline
```
插件在被加载时, 会将其读入自己的 `PluginInfo` 中, 如果无法读入, 会停止对应插件的加载流程.

### 工具使用
引入依赖:  
```xml
<dependency>
    <groupId>com.skyline</groupId>
    <artifactId>sky-plugin-tool</artifactId>
    <version>1.0</version>
</dependency>
```
编写配置:  
在路径: `resources/config` 下创建 `plugins.properties` 配置文件, 配置允许加载的插件类型, 格式为: `插件抽象类名称=插件抽象类全限定类名`  
```properties
## 在这里配置允许加载的插件类型, 使用对应接口的全限定类名
SearchPlugin=com.skyline.plugin.permitted.AbstractSearchPlugin
StorePlugin=com.skyline.plugin.permitted.AbstractStorePlugin
```
启动插件工具的服务, 在启动插件的同时, 也会同时启动命令行工具对应的线程:  
```java
public class PluginStarter {

    public static void main(String[] args) {
        // 获取工具 API 单例
        SkyPlugin skyPlugin = SkyPlugin.getSkyPlugin();
        skyPlugin.enable();
    }
}
```

### 插件指令
控制台输入 `plugin -h` 或 `plugin --help` 可以获取插件相关的所有指令, 目前包括:  
```bash
plugin -h
Format: exe act sub-act [opt] <arg>
plugin [help]
plugin load [all] [dir] <dir>
plugin unload [all] [dir] <dir>
plugin unload [id] <id>
plugin list [all] [dir] <dir>
plugin list [detail] [id] <id>
```
使用时, 只需要注意插件放置的目录的绝对路径.