package xdc.net;

public class User {
    private String nick;
    private String email;
    private String description;
    private String speed;
    private char speedCode;
    private long sharedSize;
    private boolean isOperator;

    public User(String nick) {
        this(nick, 0);
    }

    public User(String nick, long sharedSize) {
        this.nick = nick;
        this.sharedSize = sharedSize;
        this.email = "";
        this.description = "";
        this.speed = "DSL";
        this.speedCode = '\1';
        this.isOperator = false;
    }

    public String getNick() {
        return nick;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public char getSpeedCode() {
        return speedCode;
    }

    public void setSpeedCode(char speedCode) {
        this.speedCode = speedCode;
    }

    public long getSharedSize() {
        return sharedSize;
    }

    public void setSharedSize(long sharedSize) {
        this.sharedSize = sharedSize;
    }

    public boolean isOperator() {
        return isOperator;
    }

    public void setOperator(boolean operator) {
        isOperator = operator;
    }

    public String toString() {
        return "[User nick=" + nick + "]";
    }
}
