package tools;

import java.sql.*;
import java.util.ArrayList;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel; 

/**
 * Do database-related events with this class tool.
 * @author Arnesfield
 */
public final class DatabaseTool {
    
    private DatabaseTool() {}
    
    private static String URL = null;
    private static String CLASS_NAME = null;
    private static String USER = null;
    private static String PASSWORD = null;
    
    /**
     * Sets class name.
     * @param className the class name to be set
     */
    public static final void setClassName(String className) {
        CLASS_NAME = className;
    }
    
    /**
     * Creates a connection to database.
     * @param url url of database
     * @param user user of database
     * @param password password of user
     */
    public static final void setConnection(String url, String user, String password) {
        URL = url;
        USER = user;
        PASSWORD = password;
    }
    
    /**
     * Establishes a connection.
     * @return database connection
     */
    public static final Connection getConnection() {
        Connection con = null;
        try {
            if (CLASS_NAME != null)
                Class.forName(CLASS_NAME);
            con = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch(ClassNotFoundException | SQLException e) {}
        return con;
    }
    
    /**
     * Gets list of rows based on query.
     * @param query to be executed
     * @return returns rows of query
     */
    public static final ArrayList<Object[]> getListOf(String query) {
        return getListOf(query, 0, 0);
    }
    
    /**
     * Gets list of rows based on query.
     * @param query to be executed
     * @param start starts at specified column (starts with 0)
     * @return returns rows of query
     */
    public static final ArrayList<Object[]> getListOf(String query, int start) {
        return getListOf(query, start, 0);
    }
    
    /**
     * Gets list of rows based on query.
     * @param query to be executed
     * @param start starts at specified column (starts with 0)
     * @param length number of columns to be returned
     * @return returns rows of query
     */
    public static final ArrayList<Object[]> getListOf(String query, int start, int length) {
        ArrayList<Object[]> list = null;
        Connection con = getConnection();
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            
            int count = rs.getMetaData().getColumnCount() - start;
            length = (length <= 0) ? count : length;
            
            if (length <= count) {
                list = new ArrayList<>();
                while (rs.next()) {
                    Object[] row = new Object[count];
                    for (int i = 1 + start; i <= count + start && length > 0; i++, length--)
                        row[i-1 - start] = rs.getObject(i);
                    list.add(row);
                }
            }
            con.close();
        } catch (SQLException e) {}
        return list;
    }
    
    /**
     * Writes rows into specified table.
     * @param table the table to be written in
     * @param query the query in which results are to be written in the table
     */
    public static final void writeRowsInto(JTable table, String query) {
        writeRowsInto(table, query, 0, 0);
    }
    
    /**
     * Writes rows into specified table.
     * @param table the table to be written in
     * @param query the query in which results are to be written in the table
     * @param start starts at specified column (starts with 0)
     */
    public static final void writeRowsInto(JTable table, String query, int start) {
        writeRowsInto(table, query, start, 0);
    }
    
    /**
     * Writes rows into specified table.
     * @param table the table to be written in
     * @param query the query in which results are to be written in the table
     * @param start starts at specified column (starts with 0)
     * @param length number of columns to be returned
     */
    public static final void writeRowsInto(JTable table, String query, int start, int length) {
        ArrayList<Object[]> list = getListOf(query, start, length);
        DefaultTableModel model = (DefaultTableModel)table.getModel();
        model.setRowCount(0);
        try {
            for (int i = 0; i < list.size(); i++)
                model.addRow(list.get(i));
        } catch(Exception e) {}
    }
    
    /**
     * Executes an SQL command.
     * @param sql SQL command to be executed
     * @throws Exception if an error occurs
     */
    public static final void executeUpdate(String sql) throws Exception {
        try (Connection con = getConnection()) {
            Statement stmt = con.createStatement();
            stmt.executeUpdate(sql);
        }
    }
    
    /**
     * Executes a procedure.
     * @param sql SQL command to be executed
     * @throws Exception if an error occurs
     */
    public static final void executeProcedure(String sql) throws Exception {
        try (Connection con = getConnection()) {
            CallableStatement cstmt = con.prepareCall(sql);
            cstmt.executeUpdate();
        }
    }
    
}
