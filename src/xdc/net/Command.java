package xdc.net;

public class Command {
    private boolean isChatMessage;
    private String command;
    private String args;

    public Command(boolean chatMessage, String command, String args) {
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
        return (isChatMessage ? "<" : "$") + command + (isChatMessage ? "> " : " ") + args;
    }
}
