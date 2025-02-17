package com.getarrays.server.utils;

public class Constantes {

    public static final String[] SERVER_IMAGES = {
            "server1.jpg", "server2.jpg", "server3.jpg", "server4.jpg"
    };
    public static final String IMAGE_PATH = "/server/image/";
    public static final int PING_TIMEOUT_MS = 10000;

    
    public static final String DATA_KEY_SERVER = "server";
    public static final String DATA_KEY_SERVERS = "servers";
    public static final String DATA_KEY_DELETED = "deleted";
    public static final int DEFAULT_PAGE_SIZE = 30;

    public static final String MSG_SERVERS_RETRIEVED = "Servers retrieved";
    public static final String MSG_SERVER_CREATED = "Server created";
    public static final String MSG_SERVER_RETRIEVED = "Server retrieved";
    public static final String MSG_SERVER_DELETED = "Server deleted";
    public static final String MSG_PING_SUCCESS = "Ping success";
    public static final String MSG_PING_FAILED = "Ping failed";

    public static final String IMAGES_PATH = System.getProperty("user.home") + "/Downloads/images/";


}
