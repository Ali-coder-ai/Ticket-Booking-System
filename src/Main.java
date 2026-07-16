import java.util.ArrayList;
import java.util.Scanner;
class Ticket {
    private int ticketId;
    private String category;   //  Movie, Flight etc
    private String name;
    private String info;
    private String date;
    private String time;
    private double price;
    private int totalUnits;
    private int availableUnits;

    public Ticket(int ticketId, String category, String name, String info, String date, String time,
                  double price, int totalUnits) {
        this.ticketId = ticketId;
        this.category = category;
        this.name = name;
        this.info = info;
        this.date = date;
        this.time = time;
        this.price = price;
        this.totalUnits = totalUnits;
        this.availableUnits = totalUnits;
    }

    public int getTicketId() { return ticketId; }
    public String getCategory() { return category; }
    public String getName() { return name; }
    public String getInfo() { return info; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public double getPrice() { return price; }
    public int getTotalUnits() { return totalUnits; }
    public int getAvailableUnits() { return availableUnits; }

    public boolean reserve(int count) {
        if (count > 0 && count <= availableUnits) {
            availableUnits -= count;
            return true;
        }
        return false;
    }

    public void release(int count) {
        availableUnits = Math.min(totalUnits, availableUnits + count);
    }

    @Override
    public String toString() {
        return String.format(
                "ID:%-3d | [%-8s] %-26s | %-16s | Date:%-11s | Time:%-9s | Rs.%-8.2f | Available:%d/%d",
                ticketId, category, name, info, date, time, price, availableUnits, totalUnits);
    }
}


class Booking {
    private int bookingId;
    private String customerName;
    private Ticket ticket;
    private int quantity;
    private double totalAmount;

    public Booking(int bookingId, String customerName, Ticket ticket, int quantity) {
        this.bookingId = bookingId;
        this.customerName = customerName;
        this.ticket = ticket;
        this.quantity = quantity;
        this.totalAmount = ticket.getPrice() * quantity;
    }

    public int getBookingId() { return bookingId; }
    public String getCustomerName() { return customerName; }
    public Ticket getTicket() { return ticket; }
    public int getQuantity() { return quantity; }
    public double getTotalAmount() { return totalAmount; }

    @Override
    public String toString() {
        return String.format("Booking ID:%-4d | Customer:%-15s | [%-8s] %-25s | Qty:%-3d | Total:Rs.%.2f",
                bookingId, customerName, ticket.getCategory(), ticket.getName(), quantity, totalAmount);
    }
}


class InputHelper {
    public static int readInt(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine();
            try {
                return Integer.parseInt(line.trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid number, please try again.");
            }
        }
    }

    public static int readPositiveInt(Scanner sc, String prompt) {
        while (true) {
            int value = readInt(sc, prompt);
            if (value > 0) return value;
            System.out.println("Please enter a value greater than 0.");
        }
    }

    public static double readPositiveDouble(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine();
            try {
                double value = Double.parseDouble(line.trim());
                if (value > 0) return value;
                System.out.println("Please enter a value greater than 0.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid number, please try again.");
            }
        }
    }

    public static String readNonEmptyLine(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine().trim();
            if (!line.isEmpty()) return line;
            System.out.println("This field cannot be empty.");
        }
    }
}


class TicketBookingSystem {
    private ArrayList<Ticket> ticketList = new ArrayList<>();
    private ArrayList<Booking> bookingList = new ArrayList<>();
    private int ticketIdCounter = 1;
    private int bookingIdCounter = 1;

    public void addTicket(String category, String name, String info, String date, String time,
                          double price, int totalUnits) {
        ticketList.add(new Ticket(ticketIdCounter++, category, name, info, date, time, price, totalUnits));
    }

    public void displayTickets() {
        if (ticketList.isEmpty()) {
            System.out.println("Nothing available at the moment.");
            return;
        }
        System.out.println("\nAVAILABLE TICKETS");
        for (Ticket t : ticketList) {
            System.out.println(t);
        }
        System.out.println();
    }


    public ArrayList<Ticket> findByCategory(String rawInput) {
        String normalized = rawInput.toLowerCase()
                .replace("tickets", "")
                .replace("ticket", "")
                .trim();
        ArrayList<Ticket> results = new ArrayList<>();
        if (normalized.isEmpty()) {
            return results;
        }
        for (Ticket t : ticketList) {
            if (t.getCategory().toLowerCase().contains(normalized)) {
                results.add(t);
            }
        }
        return results;
    }

    public Ticket findTicketById(int id) {
        for (Ticket t : ticketList) {
            if (t.getTicketId() == id) return t;
        }
        return null;
    }

    public ArrayList<Ticket> search(String keyword) {
        ArrayList<Ticket> results = new ArrayList<>();
        String k = keyword.toLowerCase();
        for (Ticket t : ticketList) {
            if (t.getName().toLowerCase().contains(k)
                    || t.getCategory().toLowerCase().contains(k)
                    || t.getInfo().toLowerCase().contains(k)) {
                results.add(t);
            }
        }
        return results;
    }

    public Booking bookTicket(int ticketId, String customerName, int quantity) {
        Ticket ticket = findTicketById(ticketId);
        if (ticket == null) {
            System.out.println("No ticket found with that ID!");
            return null;
        }
        if (!ticket.reserve(quantity)) {
            System.out.println("Booking failed! Only " + ticket.getAvailableUnits() + " unit(s) available.");
            return null;
        }
        Booking booking = new Booking(bookingIdCounter++, customerName, ticket, quantity);
        bookingList.add(booking);
        return booking;
    }

    public boolean cancelBooking(int bookingId) {
        for (int i = 0; i < bookingList.size(); i++) {
            Booking b = bookingList.get(i);
            if (b.getBookingId() == bookingId) {
                b.getTicket().release(b.getQuantity());
                bookingList.remove(i);
                return true;
            }
        }
        return false;
    }

    public void displayBookings() {
        if (bookingList.isEmpty()) {
            System.out.println("No bookings have been made yet.");
            return;
        }
        System.out.println("\nALL BOOKINGS");
        double total = 0;
        for (Booking b : bookingList) {
            System.out.println(b);
            total += b.getTotalAmount();
        }
        System.out.printf("%nTotal Revenue: Rs.%.2f", total);
    }


    private void loadSampleData() {

        addTicket("Movie", "Avengers: Endgame", "Action", "2026-07-15", "10:00 AM", 500.0, 50);
        addTicket("Movie", "Joker", "Drama", "2026-07-17", "02:00 PM", 400.0, 35);
        addTicket("Movie", "Interstellar", "Sci-Fi", "2026-07-16", "07:00 PM", 470.0, 50);
        addTicket("Movie", "The Dark Knight", "Action", "2026-07-16", "04:00 PM", 480.0, 45);
        addTicket("Movie", "Spider-Man: No Way Home", "Action", "2026-07-17", "10:30 AM", 500.0, 60);

        addTicket("Flight", "PK-301 (Economy)", "Lahore -> Karachi", "2026-07-15", "08:00 AM", 15000.0, 150);
        addTicket("Flight", "ER-450 (Business)", "Lahore -> Dubai", "2026-07-16", "02:15 AM", 145000.0, 25);
        addTicket("Flight", "PA-205 (Economy)", "Karachi -> Islamabad", "2026-07-16", "12:30 PM", 16500.0, 140);
        addTicket("Flight", "SG-112 (Economy)", "Islamabad -> Peshawar", "2026-07-17", "05:45 PM", 9500.0, 100);
        addTicket("Flight", "PK-786 (Economy)", "Lahore -> Jeddah", "2026-07-18", "03:00 AM", 85000.0, 180);

        addTicket("Train", "Tezgam Express (AC Business)", "Karachi -> Lahore", "2026-07-15", "11:00 PM", 4500.0, 40);
        addTicket("Train", "Green Line Express (AC Standard)", "Karachi -> Rawalpindi", "2026-07-15", "06:00 AM", 3800.0, 50);
        addTicket("Train", "Khyber Mail (Economy)", "Lahore -> Peshawar", "2026-07-16", "09:30 PM", 1200.0, 70);
        addTicket("Train", "Karakoram Express (AC Standard)", "Lahore -> Karachi", "2026-07-16", "07:00 AM", 3900.0, 55);
        addTicket("Train", "Jaffar Express (Economy)", "Quetta -> Peshawar", "2026-07-17", "10:00 AM", 1500.0, 45);

        addTicket("Bus", "Daewoo Executive", "Lahore -> Islamabad", "2026-07-16", "09:00 AM", 1800.0, 45);
        addTicket("Bus", "Faisal Movers Business Class", "Lahore -> Multan", "2026-07-17", "11:00 AM", 1600.0, 40);
        addTicket("Bus", "Skyways Standard", "Karachi -> Hyderabad", "2026-07-17", "03:30 PM", 900.0, 50);
        addTicket("Bus", "Niazi Express", "Lahore -> Faisalabad", "2026-07-18", "08:00 AM", 700.0, 55);
        addTicket("Bus", "Bilal Travels VIP", "Karachi -> Quetta", "2026-07-18", "06:00 PM", 2200.0, 35);

        addTicket("Concert", "Coke Studio Live", "Open Air Grounds", "2026-07-20", "07:00 PM", 3500.0, 200);
        addTicket("Concert", "Atif Aslam Live in Concert", "Expo Center Lahore", "2026-07-22", "08:00 PM", 4500.0, 150);
        addTicket("Concert", "Ali Zafar Unplugged", "Jinnah Stadium Islamabad", "2026-07-25", "07:30 PM", 3000.0, 180);
        addTicket("Concert", "Rahat Fateh Ali Khan Live", "Liberty Grounds Lahore", "2026-07-27", "08:30 PM", 5000.0, 160);
        addTicket("Concert", "Junoon Reunion Concert", "National Stadium Karachi", "2026-07-29", "07:00 PM", 4000.0, 220);

        addTicket("Sports", "Pakistan vs India T20", "Gaddafi Stadium", "2026-07-18", "07:30 PM", 5000.0, 300);
        addTicket("Sports", "PSL Final", "National Stadium Karachi", "2026-07-21", "08:00 PM", 6000.0, 250);
        addTicket("Sports", "Lahore Marathon", "Race starts at Liberty Chowk", "2026-07-19", "06:00 AM", 1000.0, 500);
        addTicket("Sports", "Quetta Gladiators vs Lahore Qalandars", "Bugti Stadium Quetta", "2026-07-23", "08:00 PM", 4500.0, 280);
        addTicket("Sports", "National Hockey Championship Final", "Hockey Stadium Lahore", "2026-07-24", "05:00 PM", 800.0, 150);

        addTicket("Hotel", "Pearl Continental Deluxe Room", "Lahore, 1 Night", "2026-07-19", "02:00 PM", 22000.0, 15);
        addTicket("Hotel", "Marriott Executive Suite", "Islamabad, 1 Night", "2026-07-19", "02:00 PM", 28000.0, 10);
        addTicket("Hotel", "Movenpick Standard Room", "Karachi, 1 Night", "2026-07-19", "02:00 PM", 18000.0, 20);
        addTicket("Hotel", "Serena Hotel Deluxe Room", "Quetta, 1 Night", "2026-07-20", "02:00 PM", 20000.0, 12);
        addTicket("Hotel", "Avari Towers Suite", "Karachi, 1 Night", "2026-07-20", "02:00 PM", 26000.0, 8);

        addTicket("Cruise", "Arabian Sea Sunset Cruise", "Karachi Harbour, 3 Hours", "2026-07-20", "05:30 PM", 3500.0, 60);
        addTicket("Cruise", "Dubai Marina Dinner Cruise", "Dubai Marina, 2 Hours", "2026-07-21", "08:00 PM", 12000.0, 40);
        addTicket("Cruise", "Mediterranean Explorer", "7-Night Voyage", "2026-08-01", "10:00 AM", 250000.0, 100);
        addTicket("Cruise", "Nile River Cruise", "Cairo -> Luxor, 4 Nights", "2026-08-05", "09:00 AM", 180000.0, 80);
        addTicket("Cruise", "Caribbean Getaway", "5-Night Voyage", "2026-08-10", "11:00 AM", 220000.0, 90);

        addTicket("Theatre", "Alif Laila Stage Drama", "Alhamra Arts Council", "2026-07-23", "06:30 PM", 1500.0, 120);
        addTicket("Theatre", "Bulleh Shah - The Musical", "Al-Hamra Hall Lahore", "2026-07-24", "07:00 PM", 2000.0, 100);
        addTicket("Theatre", "Comedy Nights Live", "Nafees Auditorium Karachi", "2026-07-26", "08:00 PM", 1200.0, 150);
        addTicket("Theatre", "Heer Ranjha Stage Play", "Arts Council Karachi", "2026-07-27", "06:00 PM", 1400.0, 110);
        addTicket("Theatre", "Stand-up Comedy Fest", "PC Hotel Ballroom Lahore", "2026-07-28", "08:30 PM", 2500.0, 90);

        addTicket("Event", "Lahore Tech Expo 2026", "Tech Exhibition", "2026-07-22", "10:00 AM", 1000.0, 300);
        addTicket("Event", "Karachi Food Festival", "Food & Culture Fest", "2026-07-24", "12:00 PM", 800.0, 400);
        addTicket("Event", "Islamabad Book Fair", "Literary Event", "2026-07-25", "09:00 AM", 300.0, 250);
        addTicket("Event", "Startup Summit Pakistan", "Business Conference", "2026-07-26", "09:30 AM", 2500.0, 150);
        addTicket("Event", "Lahore Auto Show", "Automobile Exhibition", "2026-07-27", "10:00 AM", 1200.0, 350);

        addTicket("Match", "Pakistan vs Australia ODI", "Gaddafi Stadium Lahore", "2026-07-30", "02:00 PM", 4500.0, 300);
        addTicket("Match", "Lahore Qalandars vs Karachi Kings", "Gaddafi Stadium Lahore", "2026-08-01", "08:00 PM", 5000.0, 280);
        addTicket("Match", "Pakistan vs England Test - Day 1", "National Stadium Karachi", "2026-08-03", "10:00 AM", 2000.0, 350);
        addTicket("Match", "Islamabad United vs Peshawar Zalmi", "Rawalpindi Cricket Stadium", "2026-08-05", "07:30 PM", 4800.0, 260);
        addTicket("Match", "Pakistan vs New Zealand T20", "Multan Cricket Stadium", "2026-08-07", "06:00 PM", 4200.0, 300);
    }

    public void run(Scanner sc) {
        loadSampleData();
        System.out.println();
        System.out.println("WELCOME TO THE TICKET BOOKING SYSTEM");

        boolean running = true;
        while (running) {
            printMenu();
            int choice = InputHelper.readInt(sc, "Enter your choice: ");

            switch (choice) {
                case 1: { // View & Book Tickets (by category)
                    String category = InputHelper.readNonEmptyLine(sc,
                            "Enter category (e.g. Movie ticket, Flight ticket, Bus ticket, Event, Concert, Match, Cruise, Hotel, Sports Tickets, etc.): ");
                    ArrayList<Ticket> matches = findByCategory(category);
                    if (matches.isEmpty()) {
                        System.out.println("No tickets found for category: " + category);
                        break;
                    }
                    System.out.println("\n" + category.toUpperCase() + " - AVAILABLE OPTIONS");
                    for (Ticket t : matches) {
                        System.out.println(t);
                    }
                    System.out.println();

                    int ticketId = InputHelper.readInt(sc, "Enter Ticket ID to book (0 to go back): ");
                    if (ticketId == 0) break;

                    Ticket selected = findTicketById(ticketId);
                    if (selected == null || !matches.contains(selected)) {
                        System.out.println("That ID isn't in the " + category + " list above.");
                        break;
                    }
                    String name = InputHelper.readNonEmptyLine(sc, "Enter your name: ");
                    int qty = InputHelper.readPositiveInt(sc, "Enter number of tickets/seats: ");
                    Booking booking = bookTicket(ticketId, name, qty);
                    if (booking != null) {
                        System.out.println("\nBooking Successful!");
                        System.out.println(booking);
                    }
                    break;
                }

                case 2: { // Cancel a booking
                    displayBookings();
                    int cancelId = InputHelper.readInt(sc, "Enter Booking ID to cancel: ");
                    if (cancelBooking(cancelId)) {
                        System.out.println("Booking cancelled successfully. Capacity released.");
                    } else {
                        System.out.println("Booking ID not found.");
                    }
                    break;
                }

                case 3:
                    displayBookings();
                    break;

                case 4: { // Search
                    String keyword = InputHelper.readNonEmptyLine(sc, "Enter keyword (name/category/info): ");
                    ArrayList<Ticket> results = search(keyword);
                    if (results.isEmpty()) {
                        System.out.println("No matches found.");
                    } else {
                        System.out.println("\nSEARCH RESULTS");
                        for (Ticket t : results) System.out.println(t);
                    }
                    break;
                }

                case 5: { // Add a new ticket type — works for ANY category
                    String category = InputHelper.readNonEmptyLine(sc, "Enter category (Movie/Flight/Train/Bus/Event/Concert/Match/etc): ");
                    String name = InputHelper.readNonEmptyLine(sc, "Enter name (title/flight no./train name/etc): ");
                    String info = InputHelper.readNonEmptyLine(sc, "Enter extra info (genre/route/venue/etc): ");
                    String date = InputHelper.readNonEmptyLine(sc, "Enter date (YYYY-MM-DD): ");
                    String time = InputHelper.readNonEmptyLine(sc, "Enter time (e.g. 06:00 PM): ");
                    double price = InputHelper.readPositiveDouble(sc, "Enter ticket price: ");
                    int totalUnits = InputHelper.readPositiveInt(sc, "Enter total seats/tickets available: ");
                    addTicket(category, name, info, date, time, price, totalUnits);
                    System.out.println("Ticket added successfully!");
                    break;
                }

                case 6:
                    running = false;
                    System.out.println("Thank you for using the Ticket Booking System!");
                    break;

                default:
                    System.out.println("Invalid choice! Please select a valid menu option (1-6).");
            }
        }
    }

    private void printMenu() {
        System.out.println("\n MAIN MENU\n");
        System.out.println("1. View & Book Tickets (by category)");
        System.out.println("2. Cancel a Booking");
        System.out.println("3. View All Bookings");
        System.out.println("4. Search Tickets");
        System.out.println("5. Add New Ticket Type");
        System.out.println("6. Exit");
        System.out.println();
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        new TicketBookingSystem().run(sc);
        sc.close();
    }
}