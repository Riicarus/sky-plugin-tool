package com.skyline.plugin.config;

import com.skyline.command.manage.CommandDispatcher;
import com.skyline.command.manage.ConsoleIOHandler;
import com.skyline.command.manage.IOHandler;

/**
 * [FEATURE INFO]<br/>
 * 指令配置
 *
 * @author Skyline
 * @create 2022-10-12 0:50
 * @since 1.0.0
 */
public class CommandConfig {

    public static final IOHandler IO_HANDLER = new ConsoleIOHandler();

    public static final CommandDispatcher COMMAND_DISPATCHER = new CommandDispatcher();

}
