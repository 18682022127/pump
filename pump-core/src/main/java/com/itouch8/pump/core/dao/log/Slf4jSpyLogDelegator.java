
package com.itouch8.pump.core.dao.log;

import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itouch8.pump.core.util.CoreUtils;
import com.itouch8.pump.core.util.logger.CommonLogger;


public class Slf4jSpyLogDelegator implements SpyLogDelegator {
    
    public Slf4jSpyLogDelegator() {}

    // logs for sql and jdbc

    
    private final Logger jdbcLogger = LoggerFactory.getLogger("jdbc.audit");

    
    private final Logger resultSetLogger = LoggerFactory.getLogger("jdbc.resultset");

    
    private final Logger sqlOnlyLogger = LoggerFactory.getLogger("jdbc.sqlonly");

    
    private final Logger sqlTimingLogger = LoggerFactory.getLogger("jdbc.sqltiming");

    
    private final Logger connectionLogger = LoggerFactory.getLogger("jdbc.connection");

    // admin/setup logging for log4jdbc.

    
    private final Logger debugLogger = LoggerFactory.getLogger("log4jdbc.debug");

    
    public boolean isJdbcLoggingEnabled() {
        return jdbcLogger.isErrorEnabled() || resultSetLogger.isErrorEnabled() || sqlOnlyLogger.isErrorEnabled() || sqlTimingLogger.isErrorEnabled() || connectionLogger.isErrorEnabled();
    }

    
    public void exceptionOccured(Spy spy, String methodCall, Exception e, String sql, long execTime) {
        String classType = spy.getClassType();
        Integer spyNo = spy.getConnectionNumber();
        String header = spyNo + ". " + classType + "." + methodCall;
        if (sql == null) {
            CommonLogger.error(header, e, jdbcLogger);
            CommonLogger.error(header, e, sqlOnlyLogger);
            CommonLogger.error(header, e, sqlTimingLogger);
        } else {
            sql = nl + processSql(sql);
            CommonLogger.error(header + " " + sql, e, jdbcLogger);

            // if at debug level, display debug info to error log
            if (sqlOnlyLogger.isDebugEnabled()) {
                CommonLogger.error(getDebugInfo() + nl + spyNo + ". " + sql, e, sqlOnlyLogger);
            } else {
                CommonLogger.error(header + " " + sql, e, sqlOnlyLogger);
            }

            // if at debug level, display debug info to error log
            if (sqlTimingLogger.isDebugEnabled()) {
                CommonLogger.error(getDebugInfo() + nl + spyNo + ". " + sql + " {FAILED after " + execTime + " msec}", e, sqlTimingLogger);
            } else {
                CommonLogger.error(header + " FAILED! " + sql + " {FAILED after " + execTime + " msec}", e, sqlTimingLogger);
            }
        }
    }

    
    public void methodReturned(Spy spy, String methodCall, String returnMsg) {
        String classType = spy.getClassType();
        Logger logger = ResultSetSpy.classTypeDescription.equals(classType) ? resultSetLogger : jdbcLogger;
        if (logger.isInfoEnabled()) {
            String header = spy.getConnectionNumber() + ". " + classType + "." + methodCall + " returned " + returnMsg;
            if (logger.isDebugEnabled()) {
                CommonLogger.debug(header + " " + getDebugInfo(), null, logger);
            } else {
                CommonLogger.info(header, null, logger);
            }
        }
    }

    
    public void constructorReturned(Spy spy, String constructionInfo) {
        // not used in this implementation -- yet
    }

    private static String nl = System.getProperty("line.separator");

    
    private boolean shouldSqlBeLogged(String sql) {
        if (sql == null) {
            return false;
        }
        sql = sql.trim();

        if (sql.length() < 6) {
            return false;
        }
        sql = sql.substring(0, 6).toLowerCase();
        return (DriverSpy.DumpSqlSelect && "select".equals(sql)) || (DriverSpy.DumpSqlInsert && "insert".equals(sql)) || (DriverSpy.DumpSqlUpdate && "update".equals(sql)) || (DriverSpy.DumpSqlDelete && "delete".equals(sql)) || (DriverSpy.DumpSqlCreate && "create".equals(sql));
    }

    
    public void sqlOccured(Spy spy, String methodCall, String sql) {
        if (!DriverSpy.DumpSqlFilteringOn || shouldSqlBeLogged(sql)) {
            if (sqlOnlyLogger.isDebugEnabled()) {
                CommonLogger.debug(getDebugInfo() + nl + spy.getConnectionNumber() + ". " + processSql(sql), null, sqlOnlyLogger);
            } else if (sqlOnlyLogger.isInfoEnabled()) {
                CommonLogger.info(processSql(sql), null, sqlOnlyLogger);
            }
        }
    }

    
    private String processSql(String sql) {
        if (sql == null) {
            return null;
        }

        if (DriverSpy.TrimSql) {
            sql = sql.trim();
        }

        StringBuilder output = new StringBuilder();

        if (DriverSpy.DumpSqlMaxLineLength <= 0) {
            output.append(sql);
        } else {
            // insert line breaks into sql to make it more readable
            StringTokenizer st = new StringTokenizer(sql);
            String token;
            int linelength = 0;

            while (st.hasMoreElements()) {
                token = (String) st.nextElement();

                output.append(token);
                linelength += token.length();
                output.append(" ");
                linelength++;
                if (linelength > DriverSpy.DumpSqlMaxLineLength) {
                    output.append("\n");
                    linelength = 0;
                }
            }
        }

        if (DriverSpy.DumpSqlAddSemicolon) {
            output.append(";");
        }

        String stringOutput = output.toString();

        if (DriverSpy.TrimExtraBlankLinesInSql) {
            stringOutput = "\nSQL=" + CoreUtils.formatWhitespace(stringOutput) + "\n";
            // LineNumberReader lineReader = new LineNumberReader(new StringReader(stringOutput));
            //
            // output = new StringBuilder();
            //
            // int contiguousBlankLines = 0;
            // try
            // {
            // while (true)
            // {
            // String line = lineReader.readLine();
            // if (line==null)
            // {
            // break;
            // }
            //
            // // is this line blank?
            // if (line.trim().length() == 0)
            // {
            // contiguousBlankLines ++;
            // // skip contiguous blank lines
            // if (contiguousBlankLines > 1)
            // {
            // continue;
            // }
            // }
            // else
            // {
            // contiguousBlankLines = 0;
            // output.append(line);
            // }
            // output.append("\n");
            // }
            // }
            // catch (IOException e)
            // {
            // // since we are reading from a buffer, this isn't likely to happen,
            // // but if it does we just ignore it and treat it like its the end of the stream
            // }
            // stringOutput = "\n"+output.toString();
        }

        return stringOutput;
    }

    
    public void sqlTimingOccured(Spy spy, long execTime, String methodCall, String sql) {
        if (sqlTimingLogger.isErrorEnabled() && (!DriverSpy.DumpSqlFilteringOn || shouldSqlBeLogged(sql))) {
            if (DriverSpy.SqlTimingErrorThresholdEnabled && execTime >= DriverSpy.SqlTimingErrorThresholdMsec) {
                CommonLogger.error(buildSqlTimingDump(spy, execTime, methodCall, sql, sqlTimingLogger.isDebugEnabled()), null, sqlTimingLogger);
            } else if (sqlTimingLogger.isWarnEnabled()) {
                if (DriverSpy.SqlTimingWarnThresholdEnabled && execTime >= DriverSpy.SqlTimingWarnThresholdMsec) {
                    CommonLogger.warn(buildSqlTimingDump(spy, execTime, methodCall, sql, sqlTimingLogger.isDebugEnabled()), null, sqlTimingLogger);
                } else if (sqlTimingLogger.isDebugEnabled()) {
                    CommonLogger.debug(buildSqlTimingDump(spy, execTime, methodCall, sql, true), null, sqlTimingLogger);
                } else if (sqlTimingLogger.isInfoEnabled()) {
                    CommonLogger.info(buildSqlTimingDump(spy, execTime, methodCall, sql, false), null, sqlTimingLogger);
                }
            }
        }
    }

    
    private String buildSqlTimingDump(Spy spy, long execTime, String methodCall, String sql, boolean debugInfo) {
        StringBuffer out = new StringBuffer();

        if (debugInfo) {
            out.append(getDebugInfo());
            out.append(nl);
            out.append(spy.getConnectionNumber());
            out.append(". ");
        }

        // NOTE: if both sql dump and sql timing dump are on, the processSql
        // algorithm will run TWICE once at the beginning and once at the end
        // this is not very efficient but usually
        // only one or the other dump should be on and not both.

        sql = processSql(sql);

        out.append(sql);
        out.append(" {executed in ");
        out.append(execTime);
        out.append(" msec}");

        return out.toString();
    }

    
    private static String getDebugInfo() {
        Throwable t = new Throwable();
        t.fillInStackTrace();

        StackTraceElement[] stackTrace = t.getStackTrace();

        if (stackTrace != null) {
            String className;

            StringBuffer dump = new StringBuffer();

            
            if (DriverSpy.DumpFullDebugStackTrace) {
                boolean first = true;
                for (int i = 0; i < stackTrace.length; i++) {
                    className = stackTrace[i].getClassName();
                    if (!className.startsWith("com.itouch8.pump.core.dao.log")) {
                        if (first) {
                            first = false;
                        } else {
                            dump.append("  ");
                        }
                        dump.append("at ");
                        dump.append(stackTrace[i]);
                        dump.append(nl);
                    }
                }
            } else {
                dump.append(" ");
                int firstLog4jdbcCall = 0;
                int lastApplicationCall = 0;

                for (int i = 0; i < stackTrace.length; i++) {
                    className = stackTrace[i].getClassName();
                    if (className.startsWith("com.itouch8.pump.core.dao.log")) {
                        firstLog4jdbcCall = i;
                    } else if (DriverSpy.TraceFromApplication && className.startsWith(DriverSpy.DebugStackPrefix)) {
                        lastApplicationCall = i;
                        break;
                    }
                }
                int j = lastApplicationCall;

                if (j == 0) // if app not found, then use whoever was the last guy that called a
                            // log4jdbc class.
                {
                    j = 1 + firstLog4jdbcCall;
                }

                dump.append(stackTrace[j].getClassName()).append(".").append(stackTrace[j].getMethodName()).append("(").append(stackTrace[j].getFileName()).append(":").append(stackTrace[j].getLineNumber()).append(")");
            }

            return dump.toString();
        } else {
            return null;
        }
    }

    
    public void debug(String msg) {
        CommonLogger.debug(msg, null, debugLogger);
    }

    
    public void connectionOpened(Spy spy) {
        if (connectionLogger.isDebugEnabled()) {
            CommonLogger.info(spy.getConnectionNumber() + ". Connection opened " + getDebugInfo(), null, connectionLogger);
            CommonLogger.debug(ConnectionSpy.getOpenConnectionsDump(), null, connectionLogger);
        } else {
            CommonLogger.info(spy.getConnectionNumber() + ". Connection opened", null, connectionLogger);
        }
    }

    
    public void connectionClosed(Spy spy) {
        if (connectionLogger.isDebugEnabled()) {
            CommonLogger.info(spy.getConnectionNumber() + ". Connection closed " + getDebugInfo(), null, connectionLogger);
            CommonLogger.debug(ConnectionSpy.getOpenConnectionsDump(), null, connectionLogger);
        } else {
            CommonLogger.info(spy.getConnectionNumber() + ". Connection closed", null, connectionLogger);
        }
    }
}
