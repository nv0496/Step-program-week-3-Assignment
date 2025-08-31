/**
 * Assignment3_YourName.java
 * Hotel Reservation System
 * Demonstrates OOP with multiple interacting classes and static reporting
 */

import java.util.*;

/**
 * Room class - represents a hotel room
 */
class Room {
    private String roomNumber;
    private String roomType;
    private double pricePerNight;
    private boolean isAvailable;
    private int maxOccupancy;

    public Room(String roomNumber, String roomType, double pricePerNight, int maxOccupancy) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.pricePerNight = pricePerNight;
        this.isAvailable = true; // default when created
        this.maxOccupancy = maxOccupancy;
    }

    // Getters and setters
    public String getRoomNumber() { return roomNumber; }
    public String getRoomType() { return roomType; }
    public double getPricePerNight() { return pricePerNight; }
    public boolean isAvailable() { return isAvailable; }
    public int getMaxOccupancy() { return maxOccupancy; }

    public void setAvailable(boolean available) { this.isAvailable = available; }

    public void displayRoomInfo() {
        System.out.println("Room " + roomNumber + " | Type: " + roomType +
                " | Price/Night: $" + pricePerNight +
                " | Max Occupancy: " + maxOccupancy +
                " | Available: " + isAvailable);
    }
}

/**
 * Guest class - represents a hotel guest
 */
class Guest {
    private String guestId;
    private String guestName;
    private String phoneNumber;
    private String email;
    private String[] bookingHistory;
    private int bookingCount;

    public Guest(String guestId, String guestName, String phoneNumber, String email) {
        this.guestId = guestId;
        this.guestName = guestName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.bookingHistory = new String[20]; // limit for demo
        this.bookingCount = 0;
    }

    public String getGuestId() { return guestId; }
    public String getGuestName() { return guestName; }

    public void addBookingToHistory(String bookingId) {
        if (bookingCount < bookingHistory.length) {
            bookingHistory[bookingCount++] = bookingId;
        }
    }

    public void displayGuestInfo() {
        System.out.println("Guest ID: " + guestId + " | Name: " + guestName +
                " | Phone: " + phoneNumber + " | Email: " + email);
        System.out.print("Booking History: ");
        for (int i = 0; i < bookingCount; i++) {
            System.out.print(bookingHistory[i] + " ");
        }
        System.out.println();
    }
}

/**
 * Booking class - represents a hotel booking
 */
class Booking {
    private String bookingId;
    private Guest guest;
    private Room room;
    private String checkInDate;
    private String checkOutDate;
    private double totalAmount;

    // static reporting
    private static int totalBookings = 0;
    private static double hotelRevenue = 0;
    private static String hotelName = "Grand Java Hotel";

    public Booking(String bookingId, Guest guest, Room room,
                   String checkInDate, String checkOutDate, double totalAmount) {
        this.bookingId = bookingId;
        this.guest = guest;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.totalAmount = totalAmount;

        totalBookings++;
        hotelRevenue += totalAmount;
    }

    public String getBookingId() { return bookingId; }
    public Guest getGuest() { return guest; }
    public Room getRoom() { return room; }
    public double getTotalAmount() { return totalAmount; }

    public static int getTotalBookings() { return totalBookings; }
    public static double getHotelRevenue() { return hotelRevenue; }
    public static String getHotelName() { return hotelName; }

    public void displayBookingInfo() {
        System.out.println("Booking ID: " + bookingId + " | Guest: " + guest.getGuestName() +
                " | Room: " + room.getRoomNumber() + " (" + room.getRoomType() + ")" +
                " | Check-in: " + checkInDate + " | Check-out: " + checkOutDate +
                " | Amount: $" + totalAmount);
    }

    // Static reporting methods
    public static void displayHotelStats() {
        System.out.println("\n--- Hotel Report (" + hotelName + ") ---");
        System.out.println("Total Bookings: " + totalBookings);
        System.out.println("Total Revenue: $" + hotelRevenue);
    }
}

/**
 * Main class - Hotel Reservation System
 */
public class Assignment3_YourName {
    private static int bookingCounter = 1;

    public static void main(String[] args) {
        // Create sample rooms
        Room[] rooms = {
                new Room("101", "Single", 100, 1),
                new Room("102", "Double", 150, 2),
                new Room("201", "Suite", 250, 4)
        };

        // Create sample guests
        Guest guest1 = new Guest("G001", "Alice", "1234567890", "alice@mail.com");
        Guest guest2 = new Guest("G002", "Bob", "9876543210", "bob@mail.com");

        // Make reservations
        Booking booking1 = makeReservation(guest1, rooms[0], "2025-09-01", "2025-09-05");
        Booking booking2 = makeReservation(guest2, rooms[1], "2025-09-03", "2025-09-06");

        // Display info
        System.out.println("\n--- Room Info ---");
        for (Room r : rooms) r.displayRoomInfo();

        System.out.println("\n--- Guest Info ---");
        guest1.displayGuestInfo();
        guest2.displayGuestInfo();

        System.out.println("\n--- Booking Info ---");
        booking1.displayBookingInfo();
        booking2.displayBookingInfo();

        // Hotel stats
        Booking.displayHotelStats();
    }

    // Reservation method
    public static Booking makeReservation(Guest guest, Room room, String checkIn, String checkOut) {
        if (!room.isAvailable()) {
            System.out.println("Room " + room.getRoomNumber() + " is not available!");
            return null;
        }

        // For simplicity, assume 1 day = 1 night
        int nights = Math.max(1, (int) (Math.random() * 5 + 1)); // random nights for demo
        double totalAmount = room.getPricePerNight() * nights;

        String bookingId = "B" + String.format("%03d", bookingCounter++);
        Booking booking = new Booking(bookingId, guest, room, checkIn, checkOut, totalAmount);

        // Update room availability & guest history
        room.setAvailable(false);
        guest.addBookingToHistory(bookingId);

        System.out.println("Reservation successful for Guest " + guest.getGuestName() +
                " | Room: " + room.getRoomNumber());
        return booking;
    }
}
