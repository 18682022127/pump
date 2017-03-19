package com.itouch8.pump.core.dao.util;

import java.sql.Connection;


public class DBHelp {

    
    public static final DriverUtilsImpl Driver = DriverUtilsImpl.getInstance();

    
    public static final MetaUtilsImpl Meta = MetaUtilsImpl.getInstance();

    
    public static final ConnectionUtilsImpl Connection = ConnectionUtilsImpl.getInstance();

    
    public static final StatementUtilsImpl Statement = StatementUtilsImpl.getInstance();

    
    public static final ResultSetUtilsImpl ResultSet = ResultSetUtilsImpl.getInstance();

    
    public static final CloseUtilsImpl Closer = CloseUtilsImpl.getInstance();

    
    public interface IConnectionCallback<T> {

        
        T callback(Connection conn);
    }
}
