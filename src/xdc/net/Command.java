package xdc.net;

public class Command {
    private static final String GETPASS_COMMAND = "GetPass";
    private static final String REVERSECONNECTTOME_COMMAND = "RevConnectToMe";
    private static final String CONNECTTOME_COMMAND = "ConnectToMe";
    private static final String FORCEMOVE_COMMAND = "ForceMove";
    private static final String QUIT_COMMAND = "Quit";
    private static final String GET_INFO_COMMAND = "GetINFO";
    private static final String TO_COMMAND = "To:";
    private static final String MYINFO_COMMAND = "MyINFO";
    private static final String OPLIST_COMMAND = "OpList";
    private static final String NICKLIST_COMMAND = "NickList";
    private static final String GET_NICKLIST_COMMAND = "GetNickList";
    private static final String VERSION_COMMAND = "Version";
    private static final String HELLO_COMMAND = "Hello";
    private static final String SEARCHRESULT_COMMAND = "SR";
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
        String userInfo = "$ALL " + user.getNick() + " " + user.getDescription() + "$ $" + user.getSpeed() + user.getSpeedCode() + "$" + user.getEmail() + "$" + user.getSharedSize() + "$";
        return new Command(false, MYINFO_COMMAND, userInfo);
    }

    public static Command createGetInfoCommand(User requestor, String requestInfoForNick) {
        return new Command(false, GET_INFO_COMMAND, requestInfoForNick + " " + requestor.getNick());
    }

    public static Command createHubMessage(User author, String message) {
        return new Command(true, null, "<" + author.getNick() + "> " + message);
    }

    public static Command createPrivateChatMessage(User fromUser, User toUser, String message) {
        return new Command(false, TO_COMMAND, toUser.getNick() + " From: " + fromUser.getNick() + " $" + message);
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
            if (result.equals("")) {
                result = args;
            } else {
                result += " " + args;
            }
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

    public boolean isQuitCommand() {
        return command.equals(QUIT_COMMAND);
    }

    public boolean isOpListCommand() {
        return command.equals(OPLIST_COMMAND);
    }

    public boolean isForceMoveCommand() {
        return command.equals(FORCEMOVE_COMMAND);
    }

    public boolean isSearchResultCommand() {
        return command.equals(SEARCHRESULT_COMMAND);
    }

    public boolean isConnectToMeCommand() {
        return command.equals(CONNECTTOME_COMMAND);
    }

    public boolean isReverseConnectToMeCommand() {
        return command.equals(REVERSECONNECTTOME_COMMAND);
    }

    public boolean isPasswordRequiredCommand() {
        return command.equals(GETPASS_COMMAND);
    }
}
