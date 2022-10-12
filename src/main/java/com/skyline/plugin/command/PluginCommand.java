package com.skyline.plugin.command;

import com.skyline.command.annotation.*;
import com.skyline.command.data.CommandArgumentType;

/**
 * [FEATURE INFO]<br/>
 * 插件指令
 *
 * @author Skyline
 * @create 2022-10-12 0:28
 * @since 1.0.0
 */
@SkyCommandDefine
public class PluginCommand {

    /**
     * 加载所有插件指令, 直接加载对应 dir 下的所有插件<br/>
     * 参数: 插件所在 dir
     */
    @SkyCommand(
            name = "加载文件夹下所有插件",
            commandPatterns = {
                    @SkyCommandPattern(prefix = "plugin"),
                    @SkyCommandPattern(prefix = "--load", args = {
                            @SkyCommandArgument(type = CommandArgumentType.STRING)
                    })
            }
    )
    public static String load_plugins_from_dir;

    /**
     * 卸载所有插件指令<br/>
     * 参数: 无
     */
    @SkyCommand(
            name = "卸载所有插件",
            commandPatterns = {
                    @SkyCommandPattern(prefix = "plugin"),
                    @SkyCommandPattern(prefix = "--unload")
            }
    )
    public static String unload_plugins_of_dir;

    /**
     * 卸载对应文件夹下所有插件指令<br/>
     * 参数: 插件所在文件夹
     */
    @SkyCommand(
            name = "卸载文件夹下所有插件",
            commandPatterns = {
                    @SkyCommandPattern(prefix = "plugin"),
                    @SkyCommandPattern(prefix = "--unload", args = {
                            @SkyCommandArgument(type = CommandArgumentType.STRING)
                    })
            }
    )
    public static String unload_plugins;

    /**
     * 卸载单个插件, 卸载对应 id 的插件<br/>
     * 参数: 插件对应的 id
     */
    @SkyCommand(
            name = "卸载单个插件",
            commandPatterns = {
                    @SkyCommandPattern(prefix = "plugin"),
                    @SkyCommandPattern(prefix = "--unload"),
                    @SkyCommandPattern(prefix = "--id", args = {
                            @SkyCommandArgument(type = CommandArgumentType.INT)
                    })
            }
    )
    public static String unload_plugin_of_id;

    /**
     * 查看所有插件指令, 查看已加载的所有插件<br/>
     * 参数: 无
     */
    @SkyCommand(
            name = "查看所有已加载插件",
            commandPatterns = {
                    @SkyCommandPattern(prefix = "plugin"),
                    @SkyCommandPattern(prefix = "--list")
            }
    )
    public static String list_all_plugins;


    /**
     * 查看 dir 下所有插件指令, 查看 dir 下已加载的所有插件<br/>
     * 参数: dir 插件加载文件路径
     */
    @SkyCommand(
            name = "查看文件夹下所有已加载插件",
            commandPatterns = {
                    @SkyCommandPattern(prefix = "plugin"),
                    @SkyCommandPattern(prefix = "--list", args = {
                            @SkyCommandArgument(type = CommandArgumentType.STRING)
                    })
            }
    )
    public static String list_all_plugins_of_dir;

    /**
     * 查看插件详细信息指令, 查看对应 id 的插件的详细信息<br/>
     * 参数: 插件对应的 id
     */
    @SkyCommand(
            name = "查看插件详细信息",
            commandPatterns = {
                    @SkyCommandPattern(prefix = "plugin"),
                    @SkyCommandPattern(prefix = "--list"),
                    @SkyCommandPattern(prefix = "--detail", args = {
                            @SkyCommandArgument(type = CommandArgumentType.INT)
                    })
            }
    )
    public static String list_detail_of_plugin;
}
