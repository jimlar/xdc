package xdc.net;

import java.util.StringTokenizer;

public class SearchResult {
    private static final String SEPARATOR = "" + (char) 5;
    private String nick;
    private String filename;
    private long fileSize;
    private int freeSlots;
    private int maxSlots;

    SearchResult(Command command) {
        StringTokenizer st = new StringTokenizer(command.getArgs(), SEPARATOR);
        String nickAndFile = st.nextToken();
        int i = nickAndFile.indexOf(" ");
        nick = nickAndFile.substring(0, i);
        filename = nickAndFile.substring(i + 1);

        String sizeAndSlots = st.nextToken();
        i = sizeAndSlots.indexOf(" ");
        try {
            fileSize = Long.parseLong(sizeAndSlots.substring(0, i));
        } catch (NumberFormatException e) {
            fileSize = -1;
        }

        sizeAndSlots = sizeAndSlots.substring(i + 1);
        i = sizeAndSlots.indexOf("/");
        freeSlots = Integer.parseInt(sizeAndSlots.substring(0, i));
        maxSlots = Integer.parseInt(sizeAndSlots.substring(i + 1));
    }

    public String getNick() {
        return nick;
    }

    public String getFilename() {
        return filename;
    }

    public long getFileSize() {
        return fileSize;
    }

    public int getFreeSlots() {
        return freeSlots;
    }

    public int getMaxSlots() {
        return maxSlots;
    }
}
