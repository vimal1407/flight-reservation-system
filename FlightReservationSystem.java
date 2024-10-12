import java.sql.*;
import java.util.Scanner;

public class FlightReservationSystem {
    private static final String URL = "jdbc:mysql://localhost:3306/FlightReservationSystem";
    private static final String USER = "root";
    private static final String PASSWORD = "vimal14";

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void main(String[] args) {
        FlightReservationSystem system = new FlightReservationSystem();
        try (Connection conn = system.connect(); Scanner scanner = new Scanner(System.in)) {
            Flight flightService = new Flight(conn);
            Passenger passengerService = new Passenger(conn);

            while (true) {
                System.out.println("\n1. Add Flight\n2. Book Seat(s)\n3. Cancel Booking\n4. Exit");
                switch (scanner.nextInt()) {
                    case 1:
                        System.out.print("Enter flight number: ");
                        String flightNumber = scanner.next();
                        System.out.print("Enter total seats: ");
                        int totalSeats = scanner.nextInt();
                        flightService.addFlight(flightNumber, totalSeats);
                        break;
                    case 2:
                        System.out.print("Enter flight number: ");
                        flightNumber = scanner.next();
                        ResultSet rs = flightService.getFlightInfo(flightNumber);
                        if (rs.next()) {
                            int flightId = rs.getInt("flight_id");
                            int availableSeats = rs.getInt("available_seats");
                            totalSeats = rs.getInt("total_seats");

                            System.out.println("Available seats: " + availableSeats);
                            System.out.print("How many seats do you want to book? ");
                            int seatsToBook = scanner.nextInt();

                            if (seatsToBook <= availableSeats) {
                                for (int i = 0; i < seatsToBook; i++) {
                                    System.out.print("Enter passenger name: ");
                                    String passengerName = scanner.next();

                                    int seatNumber = totalSeats - availableSeats + i + 1;
                                    passengerService.bookSeat(flightId, passengerName, seatNumber);
                                }
                                flightService.updateSeats(flightId, -seatsToBook);
                                System.out.println(seatsToBook + " seat(s) booked successfully.");
                            } else {
                                System.out.println("Not enough seats available.");
                            }
                        } else {
                            System.out.println("Flight not found or no available seats.");
                        }
                        break;
                    case 3:
                        System.out.print("Enter flight number: ");
                        String cancelFlightNumber = scanner.next();
                        System.out.print("Enter passenger name: ");
                        String cancelPassengerName = scanner.next();
                        passengerService.cancelBooking(cancelFlightNumber, cancelPassengerName);
                        break;
                    case 4:
                        return;
                    default:
                        System.out.println("Invalid option.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}