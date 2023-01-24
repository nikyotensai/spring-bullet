package com.github.nikyotensai.jdbc;

import com.github.nikyotensai.jdbc.annotation.ConnectionAttr;
import org.springframework.core.NamedThreadLocal;

public class ConnectionManager {

    private static final ThreadLocal<ConnectionAttr> currentConnectionAttr =
            new NamedThreadLocal<>("Current conn");


    public static ConnectionAttr getCurrentConnectionAttr() {
        return currentConnectionAttr.get();
    }

    public static void setCurrentConnectionAttrIfAbsent(ConnectionAttr connAttr) {
        if (getCurrentConnectionAttr() == null) {
            setCurrentConnectionAttr(connAttr);
        }
    }

    public static void setCurrentConnectionAttr(ConnectionAttr connAttr) {
        currentConnectionAttr.set(connAttr);
    }

    public static void removeCurrentConnectionAttr() {
        currentConnectionAttr.remove();
    }


}
