package com.itouch8.pump.core.dao.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DataSourceUtils;

import com.itouch8.pump.core.dao.dialect.IDialect;
import com.itouch8.pump.core.dao.dialect.impl.MySQL;
import com.itouch8.pump.core.dao.dialect.impl.Oracle;
import com.itouch8.pump.core.dao.jndi.JndiManager;
import com.itouch8.pump.core.dao.util.DBHelp.IConnectionCallback;
import com.itouch8.pump.core.util.CoreUtils;
import com.itouch8.pump.core.util.exception.Throw;
import com.itouch8.pump.core.util.logger.CommonLogger;


public abstract class ConnectionUtilsImpl {

    private static final ConnectionUtilsImpl instance = new ConnectionUtilsImpl() {};

    private ConnectionUtilsImpl() {}

    static ConnectionUtilsImpl getInstance() {
        return instance;
    }

    
    public Connection get(DataSource dataSource) {
        try {
            return DataSourceUtils.getConnection(dataSource);
        } catch (Exception e) {
            throw Throw.createRuntimeException(e);
        }
    }

    
    public void release(Connection conn, DataSource dataSource) {
        DataSourceUtils.releaseConnection(conn, dataSource);
    }

    
    public Connection get(String driver, String url, String username, String password) {
        try {
            DBHelp.Driver.load(driver);
            return DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            throw Throw.createRuntimeException(e);
        }
    }

    
    public void commit(Connection conn) {
        try {
            if (null != conn) {
                conn.commit();
            }
        } catch (SQLException e) {
            throw Throw.createRuntimeException(e);
        }
    }

    
    public void rollback(Connection conn) {
        try {
            if (null != conn) {
                conn.rollback();
            }
        } catch (SQLException e) {
            throw Throw.createRuntimeException(e);
        }
    }

    
    public void beginTransaction(Connection conn, boolean newTransaction) {
        if (newTransaction) {
            this.commit(conn);
            this.setAutoCommit(conn, false);
        } else {
            this.setAutoCommit(conn, false);
        }
    }

    
    public void endTransaction(Connection conn) {
        this.commit(conn);
        this.setAutoCommit(conn, true);
    }

    
    public void setAutoCommit(Connection conn, boolean autoCommit) {
        try {
            if (null != conn) {
                conn.setAutoCommit(autoCommit);
            }
        } catch (SQLException e) {
            throw Throw.createRuntimeException(e);
        }
    }

    
    public <T> T doInConnection(DataSource dataSource, IConnectionCallback<T> callback) {
        Connection conn = null;
        try {
            conn = DataSourceUtils.getConnection(dataSource);
            return callback.callback(conn);
        } finally {
            DataSourceUtils.releaseConnection(conn, dataSource);
        }
    }

    
    public void executeResourceLocation(Connection connection, String resourceLocation) {
        CommonLogger.info("execute resourceName: " + resourceLocation);
        String sqlStatement = null;
        try {
            Exception exception = null;
            byte[] bytes = readInputStream(resourceLocation);
            String ddlStatements = new String(bytes);
            IDialect dialect = JndiManager.getDialect(connection);

            // Special DDL handling for certain databases
            if (dialect.equals(MySQL.getSingleInstance())) {
                DatabaseMetaData databaseMetaData = connection.getMetaData();
                int majorVersion = databaseMetaData.getDatabaseMajorVersion();
                int minorVersion = databaseMetaData.getDatabaseMinorVersion();
                CommonLogger.info("Found MySQL: majorVersion=" + majorVersion + " minorVersion=" + minorVersion);

                // Special care for MySQL < 5.6
                if (majorVersion <= 5 && minorVersion < 6) {
                    ddlStatements = updateDdlForMySqlVersionLowerThan56(ddlStatements);
                }
            }

            BufferedReader reader = new BufferedReader(new StringReader(ddlStatements));
            String line = readNextTrimmedLine(reader);
            boolean inOraclePlsqlBlock = false;
            while (line != null) {
                if (line.startsWith("#")) {
                    CommonLogger.debug(line.substring(1).trim());

                } else if (line.startsWith("--")) {
                    CommonLogger.debug(line.substring(2).trim());

                } else if (line.length() > 0) {

                    if (dialect.equals(Oracle.getSingleInstance()) && line.startsWith("begin")) {
                        inOraclePlsqlBlock = true;
                        sqlStatement = addSqlStatementPiece(sqlStatement, line);

                    } else if ((line.endsWith(";") && inOraclePlsqlBlock == false) || (line.startsWith("/") && inOraclePlsqlBlock == true)) {

                        if (inOraclePlsqlBlock) {
                            inOraclePlsqlBlock = false;
                        } else {
                            sqlStatement = addSqlStatementPiece(sqlStatement, line.substring(0, line.length() - 1));
                        }

                        Statement jdbcStatement = connection.createStatement();
                        try {
                            // no logging needed as the connection will log it
                            CommonLogger.debug("SQL: " + sqlStatement);
                            jdbcStatement.execute(sqlStatement);
                            jdbcStatement.close();
                        } catch (Exception e) {
                            if (exception == null) {
                                exception = e;
                            }
                            CommonLogger.error("problem during statement " + sqlStatement, e);
                        } finally {
                            sqlStatement = null;
                        }
                    } else {
                        sqlStatement = addSqlStatementPiece(sqlStatement, line);
                    }
                }

                line = readNextTrimmedLine(reader);
            }

            if (exception != null) {
                throw exception;
            }

            CommonLogger.debug("execute successful");

        } catch (Exception e) {
            Throw.throwRuntimeException(e);
        }
    }

    private byte[] readInputStream(String inputStreamName) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[16 * 1024];
        InputStream inputStream = null;
        try {
            inputStream = CoreUtils.getResource(inputStreamName).getInputStream();
            int bytesRead = inputStream.read(buffer);
            while (bytesRead != -1) {
                outputStream.write(buffer, 0, bytesRead);
                bytesRead = inputStream.read(buffer);
            }
        } catch (Exception e) {
            Throw.throwRuntimeException("couldn't read input stream " + inputStreamName, e);
        } finally {
            CoreUtils.closeQuietly(inputStream);
        }
        return outputStream.toByteArray();
    }

    private String updateDdlForMySqlVersionLowerThan56(String ddlStatements) {
        return ddlStatements.replace("timestamp(3)", "timestamp").replace("datetime(3)", "datetime").replace("TIMESTAMP(3)", "TIMESTAMP").replace("DATETIME(3)", "DATETIME");
    }

    private String addSqlStatementPiece(String sqlStatement, String line) {
        if (sqlStatement == null) {
            return line;
        }
        return sqlStatement + " \n" + line;
    }

    private String readNextTrimmedLine(BufferedReader reader) throws IOException {
        String line = reader.readLine();
        if (line != null) {
            line = line.trim();
        }
        return line;
    }
}
