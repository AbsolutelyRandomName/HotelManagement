package app;

import javax.swing.text.html.HTMLDocument;
import java.io.IOError;
import java.sql.*;

public class DbManager {
    private Connection conn;


    public DbManager() throws Exception {
        Statement stmt;
        try {
            Class.forName("org.hsqldb.jdbcDriver");
        } catch (ClassNotFoundException e){
            throw new ClassNotFoundException();
        }
        conn = DriverManager.getConnection("hsqldb:file:hotel_data");
        stmt = conn.createStatement();
        String sql = "CREATE TABLE RESERVATIONS IF NOT EXISTS (" +
                "id INT NOT NULL," +
                "start DATE NOT NULL," +
                "end DATE NOT NULL," +
                "personId VARCHAR(100) NOT NULL," +
                "roomNo INT," +
                "status INT NOT NULL)";
        stmt.execute(sql);
        sql = "CREATE TABLE CUSTOMERS IF NOT EXISTS (" +
                "id VARCHAR (100) NOT NULL," +
                "name VARCHAR(50) NOT NULL," +
                "surname VARCHAR(50) NOT NULL)";
        stmt.execute(sql);
        sql = "CREATE TABLE ROOMS IF NOT EXISTS (" +
                "number INT NOT NULL," +
                "type INT NOT NULL)";
        stmt.execute(sql);
        stmt.close();
    }
    public void close() throws SQLException {
        try {
            conn.close();
        } catch (SQLException e){
            throw e;
        }
    }

    public boolean addCustomer(Customer customer){
        boolean result = false;
        PreparedStatement stmt = null;
        String sql = "INSERT INTO CUSTOMERS VALUES (?, ?, ?)";
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, customer.getId());
            stmt.setString(2, customer.getName());
            stmt.setString(3, customer.getSurname());
            result = stmt.execute();
        } catch (Exception e){
            e.printStackTrace();
            return false;
        } finally {
            if (stmt != null) try { stmt.close(); } catch(SQLException e) { }
        }
        return result;
    }

    
}
