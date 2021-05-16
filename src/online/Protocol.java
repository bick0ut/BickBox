package online;

public interface Protocol {
    /***
     * Protocols used by client to inform server. Format: P
     */
    public static final String NEW_MESSAGE = "!";

    public static final String USER_JOINED = "@";

    public static final String USER_LEFT = "#";
}
