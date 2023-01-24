package com.github.nikyotensai.jdbc.annotation;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.sql.Connection;
import java.sql.SQLException;

@Getter
@Setter
@EqualsAndHashCode
public class ConnectionAttr {


    public static ConnectionAttr from(Connectional conn) {
        ConnectionAttr connAttr = new ConnectionAttr();
        connAttr.setReadOnly(conn.readOnly());
        return connAttr;
    }

    public static ConnectionAttr from(Connection conn) throws SQLException {
        ConnectionAttr connAttr = new ConnectionAttr();
        connAttr.setReadOnly(conn.isReadOnly());
        return connAttr;
    }


    private boolean readOnly;


}
