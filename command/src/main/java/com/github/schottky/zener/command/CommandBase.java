package com.github.schottky.zener.command;

import com.github.schottky.zener.localization.Language;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.*;

/**
 * The abstract base-class that all commands must extend
 */

@API(status = Status.EXPERIMENTAL)
public abstract class CommandBase implements CommandExecutor, TabCompleter {

    static {
        Language.addDefaults(
                "command.permission_denied", "&6You do not have permission",
                "command.not_executable_as_player", "&6You cannot execute this as a player",
                "command.not_executable_as_console", "&6You cannot execute this as console",
                "command.too_few_arguments", "&6Too few arguments! Provide at least {args}",
                "command.too_many_arguments", "&6Too many arguments!",
                "command.not_executable_as", "&6You cannot execute this command");
    }

    /**The permission a {@link org.bukkit.permissions.Permissible} must have to execute this command*/
    protected Permission permission;
    /**The minimum, resp. maximum number of arguments this command may have*/
    protected int minArgsLength, maxArgsLength;

    protected Set<String> aliases = new HashSet<>();

    public CommandBase() {
        this(false);
    }

    CommandBase(boolean methodBased) {
        if (!methodBased) injectCmdAnnotation();
        scanForSubCommands();
    }

    /**
     * Implementation of {@link CommandExecutor}
     * No class should override this method and instead override any sub-methods like
     * {@link CommandBase#onAcceptedCommand(CommandSender, Command, String, String[])}
     * @param sender The sender that executed this command
     * @param command The command that was sent (referring to the root CommandBase)
     * @param label The label under which this command was sent, referring to the SubCommand
     * @param arguments The arguments of this command, referring to the SubCommand
     * @return whether or not this command should display it's usage-message or not
     * @see #onAcceptedCommand(CommandSender, Command, String, String[])
     * @see #onPlayerCommand(Player, Command, String, String[])
     * @see #onConsoleCommand(ConsoleCommandSender, Command, String, String[])
     */

    @Override
    public boolean onCommand(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String label,
            @NotNull String[] arguments)
    {
        if (!sender.hasPermission(permission)) {
            sender.sendMessage(Language.current().translate("command.permission_denied"));
            return true;
        }
        CommandBase base = findSubCommand(arguments);
        String[] newArgs = ArrayUtil.popFirstN(arguments, base.computeDepth());
        if (newArgs.length < base.minArgsLength) {
            sender.sendMessage(base.tooFewArgumentsMessage(base.minArgsLength - newArgs.length));
            return true;
        } else if (base.maxArgsLength > -1 && newArgs.length > base.maxArgsLength) {
            sender.sendMessage(base.tooManyArgumentsMessage(newArgs.length - base.maxArgsLength));
            return true;
        }

        return base.onAcceptedCommand(sender, command, labelUsed(base, arguments, label), newArgs);
    }

    private String labelUsed(@NotNull CommandBase base, String[] args, String originalLabel) {
        int depth = base.computeDepth();
        return depth == 0 ? originalLabel : args[depth - 1];
    }

    /**
     * called when a command was accepted, meaning the sender had permission to execute this command
     * @param sender The sender that executed this command
     * @param command The command that was sent
     * @param label The label under which this command was sent
     * @param args The arguments
     * @return whether or not this command should display it's usage-message or not
     */

    public boolean onAcceptedCommand(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String label,
            @NotNull String[] args)
    {
        if (sender instanceof Player) {
            return this.onPlayerCommand((Player) sender, command, label, args);
        } else if (sender instanceof ConsoleCommandSender) {
            return this.onConsoleCommand((ConsoleCommandSender) sender, command, label, args);
        }
        return false;
    }

    /**
     * Called when this command gets called by a player. It can be assured that the player had permission
     * @param player The player that executed this command
     * @param command The command that was sent
     * @param label The label under which this command was sent
     * @param args The arguments
     * @return whether or not this command should display it's usage-message or not
     */

    public boolean onPlayerCommand(
            @NotNull Player player,
            @NotNull Command command,
            @NotNull String label,
            @NotNull String[] args)
    {
        player.sendMessage(Language.current().translate("command.not_executable_as_player"));
        return true;
    }

    /**
     * Called when this command gets called by the console
     * @param console The player that executed this command
     * @param command The command that was sent
     * @param label The label under which this command was sent
     * @param args The arguments
     * @return whether or not this command should display it's usage-message or not
     */

    public boolean onConsoleCommand(
            @NotNull ConsoleCommandSender console,
            @NotNull Command command,
            @NotNull String label,
            @NotNull String[] args)
    {
        console.sendMessage(Language.current().translate("command.not_executable_as_console"));
        return true;
    }

    /**
     * Called when the sender tab-completes a command.
     * Child classes should not directly override this command, if they branch into sub-commands.
     * If applicable, sub-commands should override the
     * {@link CommandBase#tabCompleteOptionsFor(CommandSender, Command, String, String[])}-method to
     * have this method handle the removal of infeasible commands (commands that do not begin with
     * the entered string)
     * @param sender The sender that executed this command
     * @param command The command that was sent
     * @param label The label under which this command was sent
     * @param args The arguments
     * @return a List of strings that the sender will see as tab-completable options
     */

    @Override
    public @Nullable List<String> onTabComplete(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String label,
            @NotNull String[] args)
    {
        if (args.length == 0) return null;
        CommandBase base = findSubCommand(args);
        if (!sender.hasPermission(base.permission)) return Collections.emptyList();
        if (base == this) {
            List<String> options = tabCompleteOptionsFor(sender, command, label, args);
            if (options == null) return null;
            options.removeIf(name -> !name.startsWith(args[0]));
            return options;
        } else {
            return base.onTabComplete(sender, command, labelUsed(base, args, label), ArrayUtil.popFirstN(args, base.computeDepth()));
        }
    }

    /**
     * returns a modifiable List of Strings that the sender will see as tab-completable options.
     * This list doesn't need to be filtered, if the <code>onTabComplete</code>-method hasn't been
     * overridden.
     * @param sender The sender that executed this command
     * @param command The command that was sent
     * @param label The label under which this command was sent
     * @param args The arguments
     * @return a modifiable list of strings that the sender will see as tab-completable options, or null
     * if the default behavior should be displayed (the player-names of online players)
     */

    protected @Nullable List<String> tabCompleteOptionsFor(
            CommandSender sender,
            Command command,
            String label,
            String[] args)
    {
        return CollectionUtil.modifiableListUsing(subCommands, subCommand -> subCommand.name);
    }

    // ------- SUB-COMMANDS -------

    /**A list of sub-commands to this command*/
    protected final Set<SubCommand<?>> subCommands = new HashSet<>();

    /**
     * finds the last sub-command that can be matched with the given String-input.
     * Child classes should never override this method since it depends on a recursive
     * scheme to find those commands
     * @param args The arguments that were entered.
     * @return The Command that is the best match considering the arguments
     */

    CommandBase findSubCommand(String[] args) {
        if (this.subCommands.isEmpty() || args.length == 0) return this;
        for (SubCommand<?> cmd : subCommands) {
            if (cmd.name.equalsIgnoreCase(args[0]) ||
                    cmd.aliases.contains(args[0].toLowerCase())) {
                return cmd.findSubCommand(ArrayUtil.popFirstN(args, 1));
            }
        }
        return this;
    }

    /**
     * returns the depth of this command, meaning how many parent-commands this command
     * has.
     * The only class that should override this is the abstract {@link SubCommand}-class
     * so that this class may correctly compute the number of nodes that follow a certain sub-command
     * @return The number of parents this command has. Since this is the base-class, this will return 0
     */

    int computeDepth() { return 0; }

    /**
     * injects the {@link Cmd}-Annotation into this command
     */

    private void injectCmdAnnotation() {
        Cmd cmd = ReflectionUtil.annotationFor(this.getClass(), Cmd.class)
                .orElseThrow(() -> new RuntimeException("No Annotation 'Cmd' present at Command-class" + this.getClass()));

        this.name = cmd.name();
        String permission = cmd.permission().isEmpty() ?
                Objects.requireNonNull(Bukkit.getPluginCommand(name)).getPermission() :
                cmd.permission();
        this.permission = new Permission(Objects.requireNonNull(permission), cmd.permDefault());
        this.minArgsLength = cmd.minArgs();
        this.maxArgsLength = cmd.maxArgs();
        this.aliases = new HashSet<>();
        for (String alias: cmd.aliases()) {
            aliases.add(alias.toLowerCase());
        }
        this.simpleDescription = cmd.desc();
    }

    private void scanForSubCommands() {
        for (final Method method: this.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(SubCmd.class)) {
                MethodBasedSubCommand<?> methodBasedSubCommand = new MethodBasedSubCommand<>(method, this);
                this.subCommands.add(methodBasedSubCommand);
            }
        }
    }

    /**
     * registers any Sub-command of this command
     * @param subCommands The sub-commands to register
     */

    public void registerSubCommands(SubCommand<?>... subCommands) {
        this.subCommands.addAll(Arrays.asList(subCommands));
    }


    /**The name of this command*/
    protected String name;

    /**
     * returns the name of this command, which is equivalent to the name that has to be entered
     * (case-insensitively) to execute this command
     * @return The name of this command
     */

    public String name() {
        return name;
    }

    protected String simpleDescription;

    public String simpleDescription() {
        return Language.isValidIdentifier(simpleDescription) ?
                Language.current().translate(simpleDescription) :
                simpleDescription;
    }

    /**
     * The message that will be sent to the command-sender if he entered too few command-arguments
     * If child-classes wish to enable custom missing-arguments messages, they should override this method
     * A call to the super-method is not necessary
     * @param missing The amount of arguments that are missing
     * @return The message to be sent to the sender if he entered too few arguments
     */

    public String tooFewArgumentsMessage(int missing) {
        return Language.current().translateWithExtra("command.too_few_arguments",
                "args", this.minArgsLength);
    }

    /**
     * The message that will be sent to the command-sender if he entered too many command-arguments
     * If child-classes wish to enable custom too-many-arguments messages, they should override this method
     * A call to the super-method is not necessary
     * @param tooMany The additional amount of arguments that were passed
     * @return The message to be sent to the sender if he entered too many arguments
     */

    public String tooManyArgumentsMessage(int tooMany) {
        return Language.current().translate("command.too_many_arguments");
    }

    @Override
    public String toString() {
        return "CommandBase{" +
                "name='" + name + '\'' +
                '}';
    }
}
