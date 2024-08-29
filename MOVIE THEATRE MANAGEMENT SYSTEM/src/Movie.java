import java.sql.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.*;

class DatabaseCon {
    static String URL = "jdbc:mysql://localhost:3306/movie_theatre_management_system";
    static String USER = "root";
    static String PASSWORD = "";

    static Connection getConnection() throws SQLException {
        Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
        //System.out.println((con!=null)?"success":"failure");
        return con;
    }
}

class signup {
    void signup(String username, String Password, int Age, String mobile_no) throws Exception {
        String q = "Insert into signup(username,Password,Age,mobile_no) values(?,?,?,?)";
        try (Connection con = DatabaseCon.getConnection()) {
            PreparedStatement pst = con.prepareStatement(q);
            pst.setString(1, username);
            pst.setString(2, Password);
            pst.setInt(3, Age);
            pst.setString(4, mobile_no);
            int r = pst.executeUpdate();
            System.out.println((r > 0) ? "yes" : "no");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void listUserDetails() throws SQLException {
        String q3 = "SELECT id,username,Age,mobile_no FROM signup";
        try (Connection con = DatabaseCon.getConnection()) {
            PreparedStatement pst = con.prepareStatement(q3);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                System.out.println("id:" + rs.getInt("id"));
                System.out.println("Username: " + rs.getString("username"));
                System.out.println("Age: " + rs.getInt("Age"));
                System.out.println("Mobile No: " + rs.getString("mobile_no"));
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

class details {
    void listUserDetails() throws SQLException {
        String q3 = "SELECT UserID,username,Age,mobile_no FROM signup";
        try (Connection con = DatabaseCon.getConnection()) {
            PreparedStatement pst = con.prepareStatement(q3);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                System.out.println("id:" + rs.getInt("UserID"));
                System.out.println("Username: " + rs.getString("username"));
                System.out.println("Age: " + rs.getInt("Age"));
                System.out.println("Mobile No: " + rs.getString("mobile_no"));
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

class LoginCheck {

    boolean isLogin(String mobile_no) throws SQLException {
        String q = "SELECT COUNT(*) FROM signup WHERE mobile_no=?";

        try (Connection con = DatabaseCon.getConnection()) {
            PreparedStatement pst = con.prepareStatement(q);
            pst.setString(1, mobile_no);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;

            }
        }
        return false;
    }

}

class movieAvailabale {
    boolean isAvaialable(int MovieID) throws SQLException {
        String q = "SELECT COUNT(*) FROM movies WHERE MovieID=?";

        try (Connection con = DatabaseCon.getConnection()) {
            PreparedStatement pst = con.prepareStatement(q);
            pst.setInt(1, MovieID);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;

            }
        }
        return false;
    }

    public boolean checkMovieNameMatchesMovieId(int MovieId, String bookingTable) {
        String query = "SELECT title FROM movies WHERE MovieID = ?";
        try (Connection con = DatabaseCon.getConnection()) {
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, MovieId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String dbMovieName = resultSet.getString("title");
                return bookingTable.equalsIgnoreCase(dbMovieName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}

class MovieManagement {
    void addMovie(String Title, String Genre, int Duration) throws SQLException {
        String q1 = "Insert into movies(Title,Genre,Duration) values(?,?,?)";
        try (Connection con = DatabaseCon.getConnection()) {
            PreparedStatement pst = con.prepareStatement(q1);
            pst.setString(1, Title);
            pst.setString(2, Genre);
            pst.setInt(3, Duration);
            pst.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void listMovies() throws SQLException {
        String q2 = "select * from movies";
        try (Connection con = DatabaseCon.getConnection()) {
            PreparedStatement pst = con.prepareStatement(q2);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                System.out.println("MOVIEID:" + rs.getInt("MovieID"));
                System.out.println("TITLE:" + rs.getString("Title"));
                System.out.println("GENRE:" + rs.getString("Genre"));
                System.out.println("DURATION:" + rs.getInt("Duration"));
                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void createBookingTable(String title) throws SQLException {
        String bookingTableSQL = "CREATE TABLE IF NOT EXISTS " + title.replaceAll("\\s+", "_") + "_bookings ("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "user_id INT NOT NULL, "
                + "seats INT NOT NULL, "
                + "booking_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
                + ")";
        try (Connection con = DatabaseCon.getConnection(); Statement stmt = con.createStatement()) {
            stmt.executeUpdate(bookingTableSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

class seatReservationViewCustomer {
    void removeMovie(int MovieID) throws Exception {
        String q12 = "DELETE FROM movies WHERE MovieId=?";
        try (Connection con = DatabaseCon.getConnection()) {
            PreparedStatement pst = con.prepareStatement(q12);
            pst.setInt(1, MovieID);
            int r = pst.executeUpdate();
            System.out.println((r > 0) ? "yes" : "no");

        } catch (Exception e) {
            System.out.println("enter valid movie id.");
        }
    }

    seatReservationViewCustomer() {
        String bookingTable = null;
        String createTableSQL = "CREATE TABLE IF NOT EXISTS " + bookingTable + " ("
                + "seatNumber INT PRIMARY KEY, "
                + "status CHAR(1) NOT NULL)";
        try (Connection conn = DatabaseCon.getConnection();
                Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

class pay {
    void payment(String mobile_no, String title, int amount_paid) throws SQLException {
        String query = "INSERT INTO payment(mobile_no,title, amount_paid) VALUES(?, ?,?)";
        try (Connection con = DatabaseCon.getConnection()) {
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, mobile_no);
            pst.setString(2, title);
            pst.setInt(3, amount_paid);
            pst.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}

class calc {

    private static final int TICKET_PRICE = 200;

    int total(int numOfSeats, int seatsToCancel) {
        int amount_paid = (numOfSeats - seatsToCancel) * TICKET_PRICE;
        return amount_paid;
    }
}

class TicketBookingSystem {
    char[][] seats;
    int rows = 5;
    int cols = 10;
    char AVAILABLE = 'A';
    char RESERVED = 'R';

    String bookingTable;
    final int TICKET_PRICE = 200;
    private final HashMap<Integer, String> seatBookingMap = new HashMap<>();
    seatReservationViewCustomer ss = new seatReservationViewCustomer();

    TicketBookingSystem(String bookingTable) {
        seats = new char[rows][cols];
        this.bookingTable = bookingTable;
        initializeSeats();

        String createTableSQL = "CREATE TABLE " + bookingTable + " (seatNumber INT PRIMARY KEY, status CHAR(1))";
        try (Connection conn = DatabaseCon.getConnection();
                Statement stmt = conn.createStatement()) {
            stmt.executeQuery(createTableSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void initializeSeats() {
        for (int i = 1; i <= rows * cols; i++) {
            String insertSQL = "INSERT IGNORE INTO " + bookingTable + " (seatNumber, status) VALUES (?, ?)";
            try (Connection conn = DatabaseCon.getConnection();
                    PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
                pstmt.setInt(1, i);
                pstmt.setString(2, String.valueOf(AVAILABLE));
                pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    void showAvailableSeats() {
        System.out.println("Seat Layout:");
        System.out.println("-------------------------------------");

        int seatNumber = 1;
        char rowLabel = 'A';
        for (int i = 0; i < rows; i++) {
            System.out.print(rowLabel + " | ");
            rowLabel++;
            for (int j = 0; j < cols; j++) {
                if (j == 5) {
                    System.out.print("   ");
                }
                seats[i][j] = getSeatStatus(seatNumber);
                if (seats[i][j] == AVAILABLE) {
                    System.out.printf("%2d ", seatNumber);
                } else {
                    System.out.print("\u001B[31m" + " X " + "\u001B[0m");
                }
                seatNumber++;
            }
            System.out.println();
            System.out.println();
        }
        System.out.println("-------------------------------------");
    }

    char getSeatStatus(int seatNumber) {
        String query = "SELECT status FROM " + bookingTable + " WHERE seatNumber = ?";
        try (Connection conn = DatabaseCon.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, seatNumber);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("status").charAt(0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return AVAILABLE;
    }

    void reserveSeat(int seatNumber) {
        int totalSeats = rows * cols;
        if (seatNumber < 1 || seatNumber > totalSeats) {
            System.out.println("Invalid seat number. Please enter a number between 1 and " + totalSeats);
            return;
        }

        if (getSeatStatus(seatNumber) == RESERVED) {
            System.out.println("Seat " + seatNumber + " is already reserved.");
        } else {
            updateSeatStatus(seatNumber, RESERVED);
            System.out.println("Seat " + seatNumber + " has been reserved.");
        }
    }

    void cancelReservation(int seatNumber) {
        int totalSeats = rows * cols;
        if (seatNumber < 1 || seatNumber > totalSeats) {
            System.out.println("Invalid seat number. Please enter a number between 1 and " + totalSeats);
            return;
        }

        if (getSeatStatus(seatNumber) == AVAILABLE) {
            System.out.println("Seat " + seatNumber + " is not reserved.");
        } else {
            updateSeatStatus(seatNumber, AVAILABLE);
            System.out.println("Reservation for seat " + seatNumber + " has been canceled.");
        }
    }

    void updateSeatStatus(int seatNumber, char status) {
        String updateSQL = "UPDATE " + bookingTable + " SET status = ? WHERE seatNumber = ?";
        try (Connection conn = DatabaseCon.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
            pstmt.setString(1, String.valueOf(status));
            pstmt.setInt(2, seatNumber);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    int bookSeats(int[] seatNumbers) {
        int reservedCount = 0;
        for (int seatNumber : seatNumbers) {
            if (getSeatStatus(seatNumber) == RESERVED) {
                System.out.println("Seat " + seatNumber + " is already reserved.");
            } else {
                updateSeatStatus(seatNumber, RESERVED);
                reservedCount++;
                System.out.println("Seat " + seatNumber + " has been reserved.");
            }
        }
        return reservedCount * TICKET_PRICE;
    }

    void showSeatBookings() {
        System.out.println("Seat bookings:");
        for (Map.Entry<Integer, String> entry : seatBookingMap.entrySet()) {
            System.out.println("Seat number: " + entry.getKey() + " booked by mobile number: " + entry.getValue());
        }
    }

}

class ratings {
    void rate(String title, int amount_paid) {
        String query1 = "SELECT title, sum(amount_paid) as total FROM payment where title=?"; // Corrected column name
        try (Connection con = DatabaseCon.getConnection()) {
            PreparedStatement pst = con.prepareStatement(query1);
            pst.setString(1, title);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {

                // System.out.println("total amount paid: " + rs.getInt("total"));

                int total = rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

class LL {
    class Node {
        int data;
        Node next;

        Node(int data) {
            this.data = data;
        }
    }

    Node head = null;

    void addLast(int data) {
        Node n = new Node(data);
        if (head == null) {
            head = n;
        } else {
            Node temp = head;
            while (temp.next != null) {
                temp = temp.next;
            }
            temp.next = n;
        }
    }

    void deleteValue(int value) {
        int flag = 0;
        if (head == null) {
            System.out.println("LL is empty");
        } else {
            Node temp = head;
            do {
                if (temp.data == value) {
                    flag = 1;
                    break;
                }
                temp = temp.next;
            } while (temp != head);
            if (flag == 0) {
                System.out.println("Value does not exists");
            } else {
                if (head.data == value) {
                    if (head.next == head) {
                        head = null;
                    } else {
                        Node temp1 = head;
                        while (temp1.next != head) {
                            temp1 = temp1.next;
                        }
                        head = head.next;
                        temp1.next = head;

                    }
                } else {
                    Node temp1 = head;
                    while (temp1.next.data != value) {
                        temp1 = temp1.next;
                    }
                    temp1.next = temp1.next.next;
                }
            }

        }

    }

    void display() {
        Node temp = head;
        System.out.print("[");
        while (temp != null) {
            System.out.print(temp.data + ",");
            temp = temp.next;
        }
        System.out.println("]");
    }

}

class main {
    static final int ROWS = 5;
    static final int COLUMNS = 10;
    static int numOfSeats = 0;
    static int seatsToCancel = 0;
    static int total = 0;

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        MovieManagement mm = new MovieManagement();
        seatReservationViewCustomer rs = new seatReservationViewCustomer();
        signup s = new signup();
        details d = new details();
        pay py = new pay();
        String bookingTable = null;
        LoginCheck loginCheck = new LoginCheck();
        calc ca = new calc();
        ratings r = new ratings();
        LL l1 = new LL();
        LL l2 = new LL();
        final int TICKET_PRICE = 200;
        int amount_paid = 0;

        int seatToCancel = 0;
        boolean b = true;
        while (b) {

            System.out.println("----------------------------------------------");
            System.out.println("| WELCOME TO MOVIE THEATRE MANAGEMENT SYSTEM |");
            System.out.println("|                                            |");
            System.out.println("|               1.USER signup                |");
            System.out.println("|               2.USER login                 |");
            System.out.println("|               3.ADMIN login                |");
            System.out.println("|               4.view movies                |");
            System.out.println("|             5.rating of movies             |");
            System.out.println("|              6.press to exit               |");
            System.out.println("----------------------------------------------");
            System.out.print("             enter your choice : ");
            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    System.out.println("----------USER SIGNUP----------");
                    while (true) {
                        System.out.print("Enter username: ");
                        String username = sc.next() ;

                        System.out.print("Enter password: ");
                        String Password = sc.next();

                        int Age = 0;
                        while (Age < 16) {
                            System.out.print("Please enter your age: ");
                            Age = sc.nextInt();
                            if (Age > 16) {
                                boolean b1 = true;
                                while (b1) {
                                    System.out.println("\nEnter  your mobile no.");
                                    System.out.print("+91 ");
                                    String mobile_no = sc.next();

                                    if (mobile_no.length() == 10) {
                                        int count = 0;
                                        for (int i = 0; i < 10; i++) {
                                            if (mobile_no.charAt(i) >= '0' && mobile_no.charAt(i) <= '9') {
                                                if (i == 9)
                                                    b1 = false;
                                            } else {
                                                count++;
                                                break;
                                            }
                                        }
                                        if (count > 0) {
                                            System.out.println("Please enter correct Mobile no.");
                                        }

                                        s.signup(username, Password, Age, mobile_no);
                                        System.out.println("userName:" + username + "\n" + "Age:" + Age + "\n"
                                                + "mobile_no" + mobile_no + "\n");

                                        System.out.println("SIGNUP COMPLETED");
                                    } else {
                                        System.out.println("Please enter correct mobile no.");
                                    }

                                }
                            }

                        }

                        break;
                    }
                    break;
                case 2:
                    System.out.println("----------USER LOGIN---------");
                    System.out.println("enter mobile no:");

                    String mobile_no = sc.next();
                    try {
                        int seatToReserve = 0;
                        boolean isLoggedIn = loginCheck.isLogin(mobile_no);
                        if (isLoggedIn) {
                            while (b) {
                                System.out.println("User is signed in.");
                                System.out.println("1.view movies");
                                System.out.println("2.booking and cancellation of tickets");
                                System.out.println("3.forgot password");
                                System.out.println("4.exit");
                                System.out.println("enter your choice:");
                                int c = sc.nextInt();
                                switch (c) {
                                    case 1:
                                        mm.listMovies();
                                        break;

                                    case 2:
                                        System.out.println("----------SEATS BOOKING---------");
                                        System.out.println("movies available:");
                                        mm.listMovies();
                                        System.out.println("enter movieid:");
                                        int MovieID = sc.nextInt();
                                        movieAvailabale ma = new movieAvailabale();
                                        boolean isAvaialable = ma.isAvaialable(MovieID);
                                        if (isAvaialable) {
                                            System.out.print("Enter the booking table name for the movie: ");
                                            bookingTable = sc.next();

                                            boolean isTrue = ma.checkMovieNameMatchesMovieId(MovieID, bookingTable);
                                            if (isTrue) {

                                                TicketBookingSystem tbs = new TicketBookingSystem(bookingTable);

                                                boolean exit = false;
                                                while (!exit) {
                                                    System.out.println("1. Show available seats");
                                                    System.out.println("2. Reserve a seat");
                                                    System.out.println("3. Cancel a reservation");
                                                    System.out.println("4. payment");
                                                    System.out.println("5. Exit");
                                                    System.out.print("Choose an option: ");

                                                    int choice2 = sc.nextInt();

                                                    switch (choice2) {
                                                        case 1:
                                                            tbs.showAvailableSeats();
                                                            break;
                                                        case 2:
                                                          
                                                            System.out.println("enter no. of u have to book:");
                                                            numOfSeats = sc.nextInt();
                                                            for (int i = 1; i <= numOfSeats; i++) {
                                                                System.out.print("Enter seat number to reserve: ");
                                                                seatToReserve = sc.nextInt();
                                                                l1.addLast(seatToReserve);
                                                                tbs.reserveSeat(seatToReserve);

                                                            }
                                                            // l1.display();
                                                            break;
                                                        case 3:
                                                            System.out.println("enter no.of seats to cancel:");
                                                            seatsToCancel = sc.nextInt();
                                                            for (int i = 1; i <= seatsToCancel; i++) {
                                                                System.out.print(
                                                                        "Enter seat number to cancel reservation: ");
                                                                seatToCancel = sc.nextInt();
                                                                l1.deleteValue(seatToCancel);
                                                                tbs.cancelReservation(seatToCancel);
                                                            }
                                                            // l1.display();

                                                            break;
                                                        case 4:
                                                            System.out.println("----------Payment----------");
                                                            System.out.println("enter mobile no:");
                                                            String mobile = sc.next();
                                                            if (mobile.equals(mobile_no)) {
                                                                amount_paid = ca.total(numOfSeats, seatsToCancel);
                                                                System.out.println("amount:" + amount_paid);
                                                                py.payment(mobile_no, bookingTable, amount_paid);
                                                                System.out.println("----------TICKET----------");
                                                                System.out.println("mobile no: " + mobile_no);
                                                                System.out.println("movie: " + bookingTable);
                                                                System.out.print("no. of seats: ");
                                                                l1.display();
                                                                // System.out.println("total seats: "+);
                                                                System.out.println("amount: " + amount_paid);
                                                                System.out.println("date & time: "
                                                                        + new Timestamp(System.currentTimeMillis()));
                                                                System.out.println(("---------------------------"));
                                                            } else {
                                                                System.out.println("entered mobile no. is incorrect.");
                                                            }
                                                            break;
                                                        case 5:
                                                            exit = true;
                                                            System.out.println("Exiting...");
                                                            break;
                                                        default:
                                                            System.out.println("Invalid choice. Please select again.");
                                                    }
                                                }
                                            } else {
                                                System.out
                                                        .println("movieid and moviename doesnt match.plz check again");
                                            }
                                        } else {
                                            System.out.println("movie name is incorrect");
                                        }
                                        break;
                                    case 3:
                                        HashMap<String, String> map1 = getData();
                                        System.out.println("enter your mobile no:");
                                        mobile_no = sc.next();
                                        if (map1.containsKey(mobile_no)) {
                                            System.out.println("your password: " + map1.get(mobile_no));
                                        }
                                        break;

                                    case 4:
                                        b = false;
                                        System.out.println("exit");
                                        break;
                                }
                            }
                        } else {
                            System.out.println("User is not signed in.");
                            System.out.println("you must have to sign in first.");
                        }

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;

                case 3:
                    System.out.println("----------ADMIN LOGIN----------");
                    System.out.println("enter admin password:");
                    String pw = sc.next();
                    if (pw.equals("MTMS")) {
                        while (b) {
                            System.out.println("1.add movie");
                            System.out.println("2.remove movie");
                            System.out.println("3.view signin customers details.");
                            System.out.println("4.view no. of booked seats by particular user by phone number.");
                            System.out.println("5.exit");
                            System.out.println("enter your choice:");
                            int ch = sc.nextInt();
                            switch (ch) {
                                case 1:
                                    System.out.println("enter title of movie:");
                                    String Title = sc.next();
                                    System.out.println();
                                    System.out.println("enter genre of movie:");
                                    String Genre = sc.next();
                                    System.out.println("enter duration of movie:");
                                    int Duration = sc.nextInt();
                                    mm.addMovie(Title, Genre, Duration);
                                    System.out.println(
                                            "Title:" + Title + "\n" + "Genre:" + Genre + "\n" + "Duration:" + Duration);
                                    System.out.println("MOVIE ADDED SUCCESFULLY.");
                                    break;

                                case 2:
                                    System.out.println("movie ID:");
                                    int MovieID = sc.nextInt();
                                    rs.removeMovie(MovieID);
                                    System.out.println(MovieID + " is successfully deleted.");
                                    break;

                                case 3:
                                    System.out.println("----------DETAILS----------");
                                    d.listUserDetails();
                                    break;

                                case 4:
                                    HashMap<String, Integer> map2 = getData1();

                                    for (HashMap.Entry<String, Integer> entry : map2.entrySet()) {
                                        System.out.println("mobile no :" + entry.getKey() + ",total no. of seats:"
                                                + (entry.getValue()) / TICKET_PRICE);
                                    }

                                    break;

                                case 5:
                                    b = false;
                                    System.out.println("exit");
                                    break;

                            }
                        }
                    } else {
                        System.out.println("you have entered incorrect password.");

                    }
                    break;
                case 4:
                    mm.listMovies();
                    break;
                case 5:
                    System.out.println("enter title:");
                    String title = sc.next();
                    r.rate(title, amount_paid);
                    if (total >= 9000) {
                        System.out.println("ratings: BLOCKBUSTUR");
                    } else if (total >= 6000 && total < 9000) {
                        System.out.println("ratings: HIT");
                    } else if (total >= 3000 && total < 6000) {
                        System.out.println("ratings: SEMI-HIT / AVERAGE");
                    } else {
                        System.out.println("ratings: FLOP");
                    }
                    break;
                case 6:
                    b = false;
                    System.out.println("---THANKYOU FOR VISITITNG!!---");
            }
        }

    }

    static HashMap<String, String> getData() throws SQLException {
        HashMap<String, String> map1 = new HashMap<>();
        String query = "select mobile_no,password from signup";
        try (Connection con = DatabaseCon.getConnection()) {
            PreparedStatement pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String mobile_no = rs.getString("mobile_no");
                String password = rs.getString("password");
                map1.put(mobile_no, password);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return map1;
    }

    static HashMap<String, Integer> getData1() throws SQLException {
        HashMap<String, Integer> map2 = new HashMap<>();
        String query = "select mobile_no,amount_paid from payment";
        try (Connection con = DatabaseCon.getConnection()) {
            PreparedStatement pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String mobile_no = rs.getString("mobile_no");
                int amount_paid = rs.getInt("amount_paid");
                map2.put(mobile_no, amount_paid);
            }

        }

        catch (Exception e) {
            e.printStackTrace();
        }
        return map2;

    }

}