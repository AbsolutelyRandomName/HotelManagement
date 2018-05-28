package app;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DbManager {
    private Connection conn;


    public DbManager() throws Exception {
        Statement stmt;
        try {
            Class.forName("org.hsqldb.jdbcDriver");
        } catch (ClassNotFoundException e){
            throw new ClassNotFoundException();
        }
        conn = DriverManager.getConnection("jdbc:hsqldb:file:hotel_data");
        stmt = conn.createStatement();
        String sql = "CREATE TABLE RESERVATIONS IF NOT EXISTS (" +
                "id INT NOT NULL," +
                "start DATE NOT NULL," +
                "end DATE NOT NULL," +
                "customerId VARCHAR(100) NOT NULL," +
                "roomNo INT," +
                "state INT NOT NULL)";
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

    public DbManager(String fileName) throws Exception {
        Statement stmt;
        try {
            Class.forName("org.hsqldb.jdbcDriver");
        } catch (ClassNotFoundException e){
            throw new ClassNotFoundException();
        }
        String connName = "jdbc:hsqldb:mem:" + fileName;
        conn = DriverManager.getConnection(connName);
        stmt = conn.createStatement();
        String sql = "CREATE TABLE IF NOT EXISTS RESERVATIONS(" +
                "id INT NOT NULL," +
                "start DATE NOT NULL," +
                "end DATE NOT NULL," +
                "customerId VARCHAR(100) NOT NULL," +
                "roomNo INT," +
                "state INT NOT NULL)";
        stmt.execute(sql);
        sql = "CREATE TABLE IF NOT EXISTS CUSTOMERS (" +
                "id VARCHAR (100) NOT NULL," +
                "name VARCHAR(50) NOT NULL," +
                "surname VARCHAR(50) NOT NULL)";
        stmt.execute(sql);
        sql = "CREATE TABLE IF NOT EXISTS ROOMS(" +
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

    public boolean addCustomer(Customer customer) {
        PreparedStatement stmt = null;
        String sql = "INSERT INTO CUSTOMERS VALUES (?, ?, ?)";
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, customer.getId());
            stmt.setString(2, customer.getName());
            stmt.setString(3, customer.getSurname());
            stmt.execute();
        } catch (Exception e){
            e.printStackTrace();
            return false;
        } finally {
            if (stmt != null) try { stmt.close(); } catch(SQLException e) { }
        }
        return true;
    }

    public boolean addRoom(Room room) {
        PreparedStatement stmt = null;
        String sql = "INSERT INTO ROOMS VALUES(?, ?)";
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, room.getNumber());
            stmt.setInt(2, room.getType().ordinal());
            stmt.execute();
        } catch(SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if(stmt != null) try { stmt.close(); } catch(SQLException e) {e.printStackTrace();}
        }
        return true;
    }

    public boolean addReservation(Reservation reservation) {
        PreparedStatement stmt = null;
        String sql = "INSERT INTO RESERVATIONS VALUES(?, ?, ?, ?, ?, ?)";
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, reservation.getId());
            stmt.setDate(2, java.sql.Date.valueOf(reservation.getStart()));
            stmt.setDate(3, java.sql.Date.valueOf(reservation.getEnd()));
            stmt.setString(4, reservation.getCustomer().getId());
            stmt.setInt(5, reservation.getRoom().getNumber());
            stmt.setInt(6, reservation.getState().ordinal());
            stmt.execute();
        } catch(SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if(stmt != null) try { stmt.close(); } catch(SQLException e) {e.printStackTrace();}
        }
        return true;
    }
    public ArrayList<Customer> getCustomers(String id, String name, String surname) throws SQLException {
        if(id == null || id.equals("")) id = "%";
        if(name == null || name.equals("")) name = "%";
        if(surname == null || surname.equals("")) surname = "%";
        ArrayList<Customer> results = new ArrayList<>();
        PreparedStatement stmt = null;
        String sql = "SELECT * FROM CUSTOMERS WHERE " +
                "id LIKE ? AND name LIKE ? AND surname LIKE ?";
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, id);
            stmt.setString(2, name);
            stmt.setString(3, surname);
            ResultSet rs = stmt.executeQuery();
            while(rs.next())
                results.add(new Customer(rs.getString(1),
                        rs.getString(2), rs.getString(3)));
        } catch(Exception e) {
            throw e;
        } finally {
            if(stmt != null) try { stmt.close(); } catch (Exception e) { }
        }
        return results;
    }

    public ArrayList<Room> getRooms(int number, RoomType type) throws SQLException {
        ArrayList<Room> results = new ArrayList<>();
        PreparedStatement stmt = null;
        String sql = "SELECT * FROM ROOMS WHERE number LIKE ? AND type LIKE ?";
        try {
            stmt = conn.prepareStatement(sql);
            if(number >= 0) stmt.setInt(1, number);
            else stmt.setString(1, "%");

            if(type == null) stmt.setString(2, "%");
            else stmt.setInt(2, type.ordinal());
            ResultSet rs = stmt.executeQuery();
            while(rs.next())
                results.add(new Room(rs.getInt(1), RoomType.values()[rs.getInt(2)]));
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        finally {
            if(stmt != null) try {
                stmt.close();
            } catch (Exception e) {e.printStackTrace();}
        }
        return results;
    }

    public ArrayList<Reservation> getReservations(int id, Date start, Date end,
                                                  String personId, int roomNo, State state) throws SQLException {
        ArrayList<Reservation> results = new ArrayList<>();
        PreparedStatement stmt = null;
        String sql = "SELECT * FROM ((RESERVATIONS INNER JOIN CUSTOMERS ON customerId = CUSTOMERS.id)" +
                "INNER JOIN ROOMS ON roomNo = ROOMS.number) " +
                "WHERE (id LIKE ? AND start LIKE ? AND " +
                "end LIKE ? AND customerId LIKE ? AND roomNo LIKE ? AND state LIKE ?)";
        try {
            stmt = conn.prepareStatement(sql);
            if(id >= 0) stmt.setInt(1, id);
            else stmt.setString(1, "%");

            if(start != null) stmt.setDate(2, start);
            else stmt.setString(2, "%");

            if(end != null) stmt.setDate(3, end);
            else stmt.setString(3, "%");

            if(personId == null || personId.equals("")) stmt.setString(4, "%");
            else stmt.setString(4, personId);

            if(roomNo >= 0) stmt.setInt(5, roomNo);
            else stmt.setString(5, "%");

            if(state != null) stmt.setInt(6, state.ordinal());
            else stmt.setString(6, "%");

            ResultSet rs = stmt.executeQuery();
            int count = 0;
            while(rs.next()){
                Customer customer = new Customer(rs.getString(7),
                        rs.getString(8), rs.getString(9));
                Room room = new Room(rs.getInt(10), RoomType.values()[rs.getInt(11)]);

                Reservation reservation = new Reservation(rs.getInt(1), rs.getDate(2).toLocalDate(),
                        rs.getDate(3).toLocalDate(), room, customer, State.values()[rs.getInt(6)]);
                results.add(reservation);
            }
        } catch (Exception e){ e.printStackTrace(); throw e;}
        finally {
            if(stmt != null) try { stmt.close(); } catch (Exception e){ e.printStackTrace(); }
        }
        return results;
    }

}
