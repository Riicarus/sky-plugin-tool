package com.skyline.plugin.command;

import com.skyline.command.SkyCommand;
import com.skyline.command.argument.StringCommandArgumentType;
import com.skyline.command.command.BaseCommand;
import com.skyline.plugin.operation.BasicOperation;

/**
 * [FEATURE INFO]<br/>
 * 插件指令
 *
 * @author Skyline
 * @create 2022-10-12 0:28
 * @since 1.0.0
 */
public class PluginCommand extends BaseCommand {

    private final BasicOperation basicOperation = new BasicOperation();

    public PluginCommand(SkyCommand SKY_COMMAND) {
        super(SKY_COMMAND);
    }

    public void defineCommand() {
        SKY_COMMAND.register().execution("plugin").action("load")
                .option("all", "a")
                .option("dir", "d").argument("dir",  new StringCommandArgumentType())
                .executor(
                        (args) -> {
                            String dir = (String) args[0];
                            basicOperation.loadPluginFromDir(dir);
                        }
                );
        SKY_COMMAND.register().execution("plugin").action("unload")
                .option("all", "a")
                .executor(
                        (args) -> basicOperation.unloadAllPlugins()
                );
        SKY_COMMAND.register().execution("plugin").action("unload")
                .option("all", "a")
                .option("dir", "d").argument("dir", new StringCommandArgumentType())
                .executor(
                        (args) -> {
                            String dir = (String) args[0];
                            basicOperation.unloadPluginFromDir(dir);
                        }
                );
        SKY_COMMAND.register().execution("plugin").action("unload")
                .option("id", "i").argument("id", new StringCommandArgumentType())
                .executor(
                        (args) -> {
                            String id = (String) args[0];
                            basicOperation.unloadSinglePlugin(id);
                        }
                );
        SKY_COMMAND.register().execution("plugin").action("list")
                .option("all", "a")
                .executor(
                        (args) -> basicOperation.listAllPlugins()
                );
        SKY_COMMAND.register().execution("plugin").action("list")
                .option("all", "a")
                .option("dir", "d").argument("dir", new StringCommandArgumentType())
                .executor(
                        (args) -> {
                            String dir = (String) args[0];
                            basicOperation.listAllPluginsOfDir(dir);
                        }
                );
        SKY_COMMAND.register().execution("plugin").action("list")
                .option("detail", "D")
                .option("id", "i").argument("id", new StringCommandArgumentType())
                .executor(
                        (args) -> {
                            String id = (String) args[0];
                            basicOperation.listDetailOfPlugin(id);
                        }
                );
        SKY_COMMAND.register().execution("plugin").action("execute")
                .option("id", "i").argument("id", new StringCommandArgumentType())
                .option("args", "A").argument("args", new StringCommandArgumentType())
                .executor(
                        (args) -> {
                            String id = (String) args[0];
                            String runArgs = (String) args[1];
                            String[] argArr = runArgs == null ? null : runArgs.trim().split(",");
                            basicOperation.executePlugin(id, argArr);
                        }
                );
    }
}
