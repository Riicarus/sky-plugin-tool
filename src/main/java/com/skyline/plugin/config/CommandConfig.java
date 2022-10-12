package com.skyline.plugin.config;

import com.skyline.command.dispatch.CommandDispatchHandler;
import com.skyline.command.dispatch.DefaultCommandDispatchHandler;
import com.skyline.command.io.DefaultIOHandler;
import com.skyline.command.io.IOHandler;
import com.skyline.command.manage.CommandLoader;
import com.skyline.command.manage.DefaultCommandLoader;

/**
 * [FEATURE INFO]<br/>
 * 指令配置
 *
 * @author Skyline
 * @create 2022-10-12 0:50
 * @since 1.0.0
 */
public class CommandConfig {

    public static final String commandDefinitionClassPackage = "com.skyline.plugin.command";

    public static final String commandExecutionDefinitionClassPackage = "com.skyline.plugin.operation";

    public static final CommandLoader COMMAND_LOADER = new DefaultCommandLoader(
            commandDefinitionClassPackage,
            commandExecutionDefinitionClassPackage
    );

    public static final IOHandler IO_HANDLER = new DefaultIOHandler();

    public static final CommandDispatchHandler COMMAND_DISPATCH_HANDLER = new DefaultCommandDispatchHandler();

}
