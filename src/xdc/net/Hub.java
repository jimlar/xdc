package xdc.net;

public class Hub extends Object {
    private String name;
    private String host;
    private String description;
    private int users;

    public Hub(String host) {
        this("", host, "", 0);
    }

    public Hub(String name, String host, String description, int users) {
        this.name = name;
        this.host = host;
        this.description = description;
        this.users = users;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return 411;
    }

    public String getDescription() {
        return description;
    }

    public int getUsers() {
        return users;
    }

    public String toString() {
        return "[Hub name=" + name + ", host=" + host + ", description=" + description + ", users=" + users + "]";
    }
}
