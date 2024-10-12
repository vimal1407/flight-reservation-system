import java.sql.*;

public class Flight {
    private Connection conn;

    public Flight(Connection conn) {
        this.conn = conn;
    }

    public void addFlight(String flightNumber, int totalSeats) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(
            "INSERT INTO Flights (flight_number, total_seats, available_seats) VALUES (?, ?, ?)")) {
            stmt.setString(1, flightNumber);
            stmt.setInt(2, totalSeats);
            stmt.setInt(3, totalSeats);
            stmt.executeUpdate();
        }
    }
    public void updateSeats(int flightId, int seatChange) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(
            "UPDATE Flights SET available_seats = available_seats + ? WHERE flight_id = ?")) {
            stmt.setInt(1, seatChange);
            stmt.setInt(2, flightId);
            stmt.executeUpdate();
        }
    }
    public ResultSet getFlightInfo(String flightNumber) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(
            "SELECT flight_id, total_seats, available_seats FROM Flights WHERE flight_number = ?");
        stmt.setString(1, flightNumber);
        return stmt.executeQuery();
    }
}