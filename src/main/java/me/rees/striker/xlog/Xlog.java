package me.rees.striker.xlog;

import java.io.IOException;
import java.net.*;
import java.lang.management.ManagementFactory;
import java.time.LocalDateTime;
import me.rees.striker.constants.Constants;

public class Xlog {

    private static final String SYSLOG_ADDRESS = "192.168.0.27";
    private static final int SYSLOG_PORT = 10514;
    private static final int SYSLOG_MSG_MAX = 1024;
    private static final int SYSLOG_FACILITY = 1 << 3; // USER facility

    // Severity levels
    public static final int SYSLOG_EMERG = 0;
    public static final int SYSLOG_ALERT = 1;
    public static final int SYSLOG_CRIT = 2;
    public static final int SYSLOG_ERR = 3;
    public static final int SYSLOG_WARNING = 4;
    public static final int SYSLOG_NOTICE = 5;
    public static final int SYSLOG_INFO = 6;
    public static final int SYSLOG_DEBUG = 7;

    private static DatagramSocket socket;
    private static InetAddress syslogHost;
    private static String hostname = "blackjack";
    private static int pid;

    static {
        try {
            String jvmName = ManagementFactory.getRuntimeMXBean().getName();
            pid = Integer.parseInt(jvmName.split("@")[0]);
            hostname = InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            pid = 0;
        }
    }

    public static boolean initSyslog(String remoteHost, int port) {
        try {
            socket = new DatagramSocket();
            syslogHost = InetAddress.getByName(remoteHost);
            return true;
        } catch (IOException e) {
            System.err.println("Failed to initialize syslog: " + e.getMessage());
            return false;
        }
    }

    public static void closeSyslog() {
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    }

    private static void xlogSyslog(int severity, String message) {
        if (socket == null || socket.isClosed()) return;

        int priority = SYSLOG_FACILITY + severity;
        String packet = String.format("<%d>%s: [version=%s] [PID=%d] | %s",
                priority, Constants.STRIKER_WHO_AM_I, Constants.STRIKER_VERSION, pid, message);

        if (packet.length() > SYSLOG_MSG_MAX) {
            packet = packet.substring(0, SYSLOG_MSG_MAX);
        }

        byte[] data = packet.getBytes();
        DatagramPacket datagramPacket = new DatagramPacket(data, data.length, syslogHost, SYSLOG_PORT);
        try {
            socket.send(datagramPacket);
        } catch (IOException e) {
            System.err.println("Failed to send syslog message: " + e.getMessage());
        }
    }

    public static void logInfo(String format, Object... args) {
        xlogSyslog(SYSLOG_INFO, String.format(format, args));
    }

    public static void logError(String format, Object... args) {
        xlogSyslog(SYSLOG_ERR, String.format(format, args));
    }

    public static void logFatal(String format, Object... args) {
        xlogSyslog(SYSLOG_CRIT, String.format(format, args));
    }
}

