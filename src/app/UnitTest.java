package app;
import org.junit.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class UnitTest {
    private DbManager manager;

    @Before
    public void init() {

        try{
            manager = new DbManager("testBase");
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void readTest() {
        Customer cust1 = new Customer("ID", "Name 1", "Surname1");
        Customer cust2 = new Customer("ID 2", "Name 2", "Surname 3");
        Assert.assertTrue(manager.addCustomer(cust1));
        Assert.assertTrue(manager.addCustomer(cust2));

        ArrayList<Customer> results =  null;
        try {
            results = manager.getCustomers(null, null, null);
        } catch(Exception e){
            e.printStackTrace();
            Assert.fail();
        }
        Assert.assertNotNull(results);
        Assert.assertEquals(2, results.size());
        Assert.assertEquals(cust1, results.get(0));
        Assert.assertEquals(cust2.getId(), results.get(1).getId());

        Room room1 = new Room(1, RoomType.Single);
        Room room2 = new Room(2, RoomType.Triple);
        Assert.assertTrue(manager.addRoom(room1));
        Assert.assertTrue(manager.addRoom(room2));

        ArrayList<Room> roomResults = null;
        try {
            roomResults = manager.getRooms(-1, null);
        } catch (Exception e){e.printStackTrace(); Assert.fail();}
        Assert.assertNotNull(roomResults);
        Assert.assertEquals(room1, roomResults.get(0));
        Assert.assertEquals(room2, roomResults.get(1));

        try {
            roomResults = manager.getRooms(1, null);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
        Assert.assertEquals(1, roomResults.size());
        Assert.assertEquals(room1, roomResults.get(0));

        try {
            roomResults = manager.getRooms(2, RoomType.Triple);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
        Assert.assertEquals(1, roomResults.size());
        Assert.assertEquals(room2, roomResults.get(0));
        LocalDate start1 = LocalDate.of(2018,3,2);
        LocalDate end1 = LocalDate.of(2018, 3, 7);
        LocalDate start2 = LocalDate.of(2018, 3, 5);
        LocalDate end2 = LocalDate.of(2018, 3, 12);

        Reservation res1 = new Reservation();
        res1.setCustomer(cust1);
        res1.setRoom(room1);
        res1.setId(0);
        res1.setStart(start1);
        res1.setEnd(end1);
        res1.setState(State.Confirmed);

        Reservation res2 = new Reservation();
        res2.setCustomer(cust2);
        res2.setRoom(room2);
        res2.setId(2);
        res2.setStart(start2);
        res2.setEnd(end2);
        res2.setState(State.Payed);

        Assert.assertTrue(manager.addReservation(res1));
        Assert.assertTrue(manager.addReservation(res2));
        ArrayList<Reservation> resList = null;
        try {
            resList = manager.getReservations(-1, null, null, null, -1, null);
        } catch (Exception e){
            e.printStackTrace();
            Assert.fail();
        }

        Assert.assertNotNull(resList);
        Assert.assertEquals(2, resList.size());
        Assert.assertEquals(res1, resList.get(0));
        Assert.assertEquals(res2, resList.get(1));

    }
}
