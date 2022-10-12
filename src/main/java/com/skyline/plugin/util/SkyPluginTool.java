package com.skyline.plugin.util;

import com.skyline.command.util.SkyCommandUtil;
import com.skyline.plugin.config.CommandConfig;

/**
 * [FEATURE INFO]<br/>
 * 工具类, 插件功能入口
 *
 * @author Skyline
 * @create 2022-10-12 0:51
 * @since 1.0.0
 */
public class SkyPluginTool {

    private final SkyCommandUtil skyCommandUtil;

    public SkyPluginTool() {
        skyCommandUtil = SkyCommandUtil.startSkyCommand(
                CommandConfig.COMMAND_LOADER,
                CommandConfig.COMMAND_DISPATCH_HANDLER,
                CommandConfig.IO_HANDLER
        );
    }

}
