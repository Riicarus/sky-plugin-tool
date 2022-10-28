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

    private static final SkyCommand SKY_COMMAND = SkyCommand.getSkyCommand(CommandConfig.IO_HANDLER);

    private volatile static SkyPlugin SKY_PLUGIN;

    private SkyPlugin() {
    }

    public static SkyPlugin getSkyPlugin() {
        if (SKY_PLUGIN == null) {
            synchronized (SkyPlugin.class) {
                if (SKY_PLUGIN == null) {
                    SKY_PLUGIN = new SkyPlugin();
                }
            }
        }

        return SKY_PLUGIN;
    }

    public void enable() {
        SKY_COMMAND.startSkyCommand();
        new PluginCommand(SKY_COMMAND).defineCommand();
    }

}
