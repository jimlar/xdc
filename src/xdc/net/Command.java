package xdc.net;

public class Command {
    private static final String GET_INFO_COMMAND = "GetINFO";
    private static final String TO_COMMAND = "To:";
    private static final String MYINFO_COMMAND = "MyINFO";
    private static final String NICKLIST_COMMAND = "NickList";
    private static final String GET_NICKLIST_COMMAND = "GetNickList";
    private static final String VERSION_COMMAND = "Version";
    private static final String HELLO_COMMAND = "Hello";
    private static final String SEARCH_COMMAND = "Search";
    private static final String HUBNAME_COMMAND = "HubName";
    private static final String LOCK_COMMAND = "Lock";
    private static final String KEY_COMMAND = "Key";
    private static final String VALIDATE_NICK_COMMAND = "ValidateNick";

    private boolean isHubMessage;
    private String command;
    private String args;

    public static Command createKeyCommand(String key) {
        return new Command(false, KEY_COMMAND, key);
    }

    public static Command createValidateNickCommand(String nick) {
        return new Command(false, VALIDATE_NICK_COMMAND, nick);
    }

    public static Command createVersionCommand(String version) {
        return new Command(false, VERSION_COMMAND, version);
    }

    public static Command createGetNickListCommand() {
        return new Command(false, GET_NICKLIST_COMMAND, null);
    }

    public static Command createMyInfoCommand(User user) {
        String userInfo = "$ALL " + user.getNick() + " " + user.getDescription() + "$ $" + user.getSpeed() + user.getSpeedCode() + "$ $" + user.getSharedSize() + "$";
        return new Command(false, MYINFO_COMMAND, userInfo);
    }

    public static Command createGetInfoCommand(User requestor, String requestInfoForNick) {
        return new Command(false, GET_INFO_COMMAND, requestInfoForNick + " " + requestor.getNick());
    }

    Command(boolean isHubMessage, String command, String args) {
        this.isHubMessage = isHubMessage;
        this.command = command;
        this.args = args;
    }

    public boolean isHubMessage() {
        return isHubMessage;
    }

    public String getCommand() {
        return command;
    }

    public String getArgs() {
        return args;
    }

    public String toString() {
        String result = "";
        if (command != null || !isHubMessage) {
            result += (isHubMessage ? "<" : "$") + command + (isHubMessage ? ">" : "");
        }
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

    public boolean isSearchCommand() {
        return command.equals(SEARCH_COMMAND);
    }

    public boolean isHelloCommand() {
        return command.equals(HELLO_COMMAND);
    }

    public boolean isPrivateChatCommand() {
        return command.equals(TO_COMMAND);
    }

    public boolean isNickListCommand() {
        return command.equals(NICKLIST_COMMAND);
    }

    public boolean isMyInfoCommand() {
        return command.equals(MYINFO_COMMAND);
    }
}
