package xdc.net;

public class Command {
    private static final String HUBNAME_COMMAND = "HubName";
    private static final String LOCK_COMMAND = "Lock";
    private static final String KEY_COMMAND = "Key";
    private static final String VALIDATE_NICK_COMMAND = "ValidateNick";

    private boolean isChatMessage;
    private String command;
    private String args;

    public static Command createKeyCommand(String key) {
        return new Command(false, KEY_COMMAND, key);
    }

    public static Command createValidateNickCommand(String nick) {
        return new Command(false, VALIDATE_NICK_COMMAND, nick);
    }

    Command(boolean chatMessage, String command, String args) {
        isChatMessage = chatMessage;
        this.command = command;
        this.args = args;
    }

    public boolean isChatMessage() {
        return isChatMessage;
    }

    public String getCommand() {
        return command;
    }

    public String getArgs() {
        return args;
    }

    public String toString() {
        String result = (isChatMessage ? "<" : "$") + command + (isChatMessage ? ">" : "");
        if (args != null && !args.equals("")) {
            result += " " + args;
        }
        return result;
    }

    public boolean isLockCommand() {
        return command.equals(LOCK_COMMAND);
    }

    public boolean isHubNameCommand() {
        return command.equals(HUBNAME_COMMAND);
    }
}
