package xdc.net;

public class User {
    private String nick;
    private String email;
    private String description;
    private String speed;
    private char speedCode;
    private long sharedSize;

    public User(String nick) {
        this.nick = nick;
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

    public String toString() {
        return "[User nick=" + nick + "]";
    }
}
