//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.sparkframework.sql;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class DB {
    private static Log log = LogFactory.getLog(DB.class);
    public boolean debugMode = true;
    public boolean safeMode = false;
    private DataSource dataSource;
    private DataSource forWriteDataSource;
    private static DB instance;

    static {
        if (!TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.initSynchronization();
        }

    }

    public DB() {
    }

    public DB(DataSource ds, boolean debugMode) {
        this.dataSource = ds;
        this.debugMode = debugMode;
        instance = this;
    }

    public static DB getInstance() {
        if (instance == null) {
            instance = new DB();
        }

        return instance;
    }

    public void setDataSource(DataSource ds) {
        this.dataSource = ds;
    }

    public void setDebugMode(boolean mode) {
        this.debugMode = mode;
    }

    public void setSafeMode(boolean mode) {
        this.safeMode = mode;
    }

    public DataSource getDataSource() {
        return this.dataSource;
    }

    public DataSource getForWriteDataSource() {
        return this.forWriteDataSource;
    }

    public void setForWriteDataSource(DataSource forWriteDataSource) {
        this.forWriteDataSource = forWriteDataSource;
    }

    public static void rollback() throws SQLException {
        getConnection().rollback();
    }

    public static void commit() throws SQLException {
        getConnection().commit();
    }

    public static Connection getConnection() {
        Connection conn = null;

        try {
            DataSource ds = getInstance().getDataSource();
            if (ds == null) {
                throw new RuntimeException("no datasource specified");
            }

            conn = DataSourceUtils.getConnection(ds);
        } catch (Exception e) {
            log.error("get connection failure!" + e.getMessage());
            e.printStackTrace();
        }

        return conn;
    }

    public static void closeConnection(Connection conn) {
        DataSourceUtils.releaseConnection(conn, getInstance().getDataSource());
    }

    private static void prepare(PreparedStatement statement, Object... parameters) throws SQLException {
        if (parameters.length != 0) {
            for(int i = 0; i < parameters.length; ++i) {
                statement.setObject(i + 1, parameters[i]);
            }

        }
    }

    protected static void prepare(CallableStatement paramCallableStatement, Parameter[] parameters) throws SQLException {
        if (parameters.length != 0) {
            for(int i = 0; i < parameters.length; ++i) {
                if (parameters[i].direction == ParameterDirection.OUT || parameters[i].direction == ParameterDirection.INOUT) {
                    paramCallableStatement.registerOutParameter(i + 1, parameters[i].type);
                }

                if (parameters[i].direction == ParameterDirection.IN || parameters[i].direction == ParameterDirection.INOUT) {
                    paramCallableStatement.setObject(i + 1, parameters[i].value);
                }
            }

        }
    }

    public static long exec(String command, Object... params) throws SQLException {
        Connection conn = getConnection();
        log(command, params);
        PreparedStatement preparedStatement = null;
        long ret = -1L;

        try {
            try {
                preparedStatement = conn.prepareStatement(command, 1);
            } catch (SQLException var11) {
                preparedStatement = conn.prepareStatement(command);
            }

            prepare(preparedStatement, params);
            ret = (long)preparedStatement.executeUpdate();
            ResultSet rs;
            if ((rs = preparedStatement.getGeneratedKeys()) != null && rs.next()) {
                ret = rs.getLong(1);
            }

            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            preparedStatement.close();
            closeConnection(conn);
        }

        return ret;
    }

    public static long exec(String command) throws SQLException {
        Connection conn = getConnection();
        log(command);
        PreparedStatement preparedStatement = null;
        long ret = -1L;

        try {
            try {
                preparedStatement = conn.prepareStatement(command, 1);
            } catch (SQLException var10) {
                preparedStatement = conn.prepareStatement(command);
            }

            ret = (long)preparedStatement.executeUpdate();
            ResultSet rs;
            if ((rs = preparedStatement.getGeneratedKeys()) != null && rs.next()) {
                ret = rs.getLong(1);
            }

            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }

            closeConnection(conn);
        }

        return ret;
    }

    public static List<Map<String, String>> query(String command, Object... params) throws SQLException, DataException {
        Connection conn = getConnection();
        List<Map<String, String>> list = null;

        try {
            PreparedStatement ps = conn.prepareStatement(command);
            log(command, params);
            if (params != null && params.length > 0) {
                prepare(ps, params);
            }

            ResultSet rs = ps.executeQuery();
            list = parseResult(rs);
            ps.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn);
        }

        return list;
    }

    public static List<Map<String, String>> query(String command) throws SQLException, DataException {
        Connection conn = getConnection();
        List<Map<String, String>> list = null;

        try {
            PreparedStatement ps = conn.prepareStatement(command);
            log(command);
            ResultSet rs = ps.executeQuery();
            list = parseResult(rs);
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn);
        }

        return list;
    }

    public static List<Map<String, String>> executeProcedure(String ProcedureName, List<Object> outParameterValues, Parameter... params) throws SQLException, DataException {
        Connection conn = getConnection();
        List<Map<String, String>> list = null;

        try {
            String sql = "{ call " + ProcedureName + "(";

            for(int i = 0; i < params.length; ++i) {
                sql = sql + (i > 0 ? ", ?" : "?");
            }

            sql = sql + ") }";
            log(sql, params);
            CallableStatement statement = conn.prepareCall(sql);
            prepare(statement, params);
            statement.execute();
            ResultSet rs = statement.getResultSet();
            if (rs != null) {
                list = parseResult(rs);
                rs.close();
            }

            for(int i = 0; i < params.length; ++i) {
                if (params[i].direction == ParameterDirection.OUT || params[i].direction == ParameterDirection.INOUT) {
                    outParameterValues.add(statement.getObject(i + 1));
                }
            }

            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn);
        }

        return list;
    }

    public static List<Object> executeProcedure(String ProcedureName, Parameter... params) throws SQLException, DataException {
        Connection conn = getConnection();
        List<Object> outParams = new ArrayList();

        try {
            String sql = "{ call " + ProcedureName + "(";

            for(int i = 0; i < params.length; ++i) {
                sql = sql + (i > 0 ? ", ?" : "?");
            }

            sql = sql + ") }";
            log(sql, params);
            CallableStatement statement = conn.prepareCall(sql);
            prepare(statement, params);
            statement.execute();

            for(int i = 0; i < params.length; ++i) {
                if (params[i].direction == ParameterDirection.OUT || params[i].direction == ParameterDirection.INOUT) {
                    outParams.add(statement.getObject(i + 1));
                }
            }

            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn);
        }

        return outParams;
    }

    public static List<Map<String, String>> executeProcedure(String ProcedureName, Object... params) throws SQLException, DataException {
        Connection conn = getConnection();
        List<Map<String, String>> list = null;

        try {
            String sql = "{ call " + ProcedureName + "(";

            for(int i = 0; i < params.length; ++i) {
                sql = sql + (i > 0 ? ", ?" : "?");
            }

            sql = sql + ") }";
            log(sql, params);
            CallableStatement statement = conn.prepareCall(sql);
            prepare((PreparedStatement)statement, (Object[])params);
            statement.execute();
            ResultSet rs = statement.getResultSet();
            if (rs != null) {
                list = parseResult(rs);
                rs.close();
            }

            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn);
        }

        return list;
    }

    private static List<Map<String, String>> parseResult(ResultSet rs) throws SQLException, DataException {
        ResultSetMetaData metaData = rs.getMetaData();
        int j = metaData.getColumnCount();
        List<Map<String, String>> list = new ArrayList();

        while(rs.next()) {
            Map<String, String> map = new HashMap();

            for(int i = 0; i < j; ++i) {
                String columnName = metaData.getColumnLabel(i + 1);
                String typeName = metaData.getColumnTypeName(i + 1);
                if (rs.getObject(i + 1) == null) {
                    map.put(columnName, null);
                } else if (typeName.equalsIgnoreCase("CLOB")) {
                    Clob clob = rs.getClob(i + 1);
                    BufferedReader br = new BufferedReader(clob.getCharacterStream());
                    String content = "";

                    try {
                        for(String strTmp = br.readLine(); strTmp != null; strTmp = br.readLine()) {
                            content = strTmp + content;
                        }

                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    map.put(columnName, content);
                } else if (typeName.equalsIgnoreCase("DATETIME")) {
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    map.put(columnName, df.format(rs.getTimestamp(i + 1)));
                } else {
                    map.put(columnName, rs.getObject(i + 1).toString());
                }
            }

            list.add(map);
        }

        return list;
    }

    private static void log(String command, Object... params) {
        if (params == null) {
            log(command);
        } else {
            if (getInstance().debugMode) {
                StringBuffer sb = new StringBuffer("[SQL]： " + command);
                sb.append("; params [");

                for(int i = 0; i < params.length; ++i) {
                    sb.append(params[i]);
                    if (i != params.length - 1) {
                        sb.append(",");
                    }
                }

                sb.append("]");
                log.info(sb.toString());
            }

        }
    }

    private static void log(String commond, Parameter[] params) {
        if (getInstance().debugMode) {
            StringBuffer sb = new StringBuffer("[SQL]:" + commond);
            sb.append("; params [");

            for(int i = 0; i < params.length; ++i) {
                sb.append(params[i].value);
                if (i != params.length - 1) {
                    sb.append(",");
                }
            }

            sb.append("]");
            log.info(sb.toString());
        }

    }

    private static void log(String commond) {
        if (getInstance().debugMode) {
            log.info("[SQL]: " + commond);
        }

    }
}
