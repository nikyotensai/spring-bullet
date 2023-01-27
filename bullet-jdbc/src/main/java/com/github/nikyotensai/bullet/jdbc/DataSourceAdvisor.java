package com.github.nikyotensai.bullet.jdbc;

import com.github.nikyotensai.bullet.jdbc.annotation.ConnectionAttr;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.sql.Connection;
import java.sql.SQLException;

public class DataSourceAdvisor implements MethodInterceptor {

    private static final String CONN_ATTR_CHANGED = "connAttrChanged";

    private final Object lock = new Object();

    private volatile ConnectionAttr defaultConnAttr;


    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object result = invocation.proceed();
        if (result instanceof Connection) {
            Connection conn = (Connection) result;

            ConnectionAttr defaultConnAttr = generateConnectionAttr(conn);
            ConnectionAttr annotedConnAttr = ConnectionManager.getCurrentConnectionAttr();

            if (annotedConnAttr != null) {
                changeConnAttr(conn, annotedConnAttr);
            } else if (connAttrChanged(conn)) {
                changeConnAttr(conn, defaultConnAttr);
            }
        }
        return result;
    }


    private void changeConnAttr(Connection conn, ConnectionAttr connAttr) throws SQLException {
        conn.setReadOnly(connAttr.isReadOnly());
        boolean changed = connAttr != defaultConnAttr;
        conn.setClientInfo(CONN_ATTR_CHANGED, Boolean.toString(changed));
    }

    private boolean connAttrChanged(Connection conn) throws SQLException {
        String connAttrChanged = conn.getClientInfo(CONN_ATTR_CHANGED);
        return Boolean.toString(true).equals(connAttrChanged);
    }


    public ConnectionAttr generateConnectionAttr(Connection conn) throws SQLException {
        if (defaultConnAttr == null) {
            synchronized (lock) {
                if (defaultConnAttr == null) {
                    defaultConnAttr = ConnectionAttr.from(conn);
                }
            }
        }
        return defaultConnAttr;
    }

}
