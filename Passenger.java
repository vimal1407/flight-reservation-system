import java.sql.*;

public class Passenger {
    private Connection conn;

    public Passenger(Connection conn) {
        this.conn = conn;
    }
    public void bookSeat(int flightId, String passengerName, int seatNumber) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(
            "INSERT INTO Passengers (flight_id, passenger_name, seat_number) VALUES (?, ?, ?)")) {
            stmt.setInt(1, flightId);
            stmt.setString(2, passengerName);
            stmt.setInt(3, seatNumber);
            stmt.executeUpdate();
        }
    }
    public void cancelBooking(String flightNumber, String passengerName) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(
            "DELETE FROM Passengers WHERE flight_id = (SELECT flight_id FROM Flights WHERE flight_number = ?) AND passenger_name = ?")) {
            stmt.setString(1, flightNumber);
            stmt.setString(2, passengerName);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Booking canceled successfully for " + passengerName + " on flight " + flightNumber + ".");
            } else {
                System.out.println("No booking found for " + passengerName + " on flight " + flightNumber + ".");
            }
        }
    }
}