package com.skyline.plugin;

import com.skyline.command.SkyCommand;
import com.skyline.plugin.command.PluginCommand;
import com.skyline.plugin.config.CommandConfig;

/**
 * [FEATURE INFO]<br/>
 * 插件功能入口, 对外 API
 *
 * @author Skyline
 * @create 2022-10-12 0:51
 * @since 1.0.0
 */
public class SkyPlugin {

    private static final SkyCommand SKY_COMMAND = SkyCommand.getSkyCommand(CommandConfig.IO_HANDLER, CommandConfig.COMMAND_DISPATCHER);

    public SkyPlugin() {
    }

    public void enable() {
        SKY_COMMAND.startSkyCommand();
        new PluginCommand(SKY_COMMAND).defineCommand();
    }

}
