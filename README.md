# Universal Ticket Booking System

A robust, console-based Java application designed to manage and book tickets across a wide variety of categories, including Movies, Flights, Trains, Concerts, Sports, and Hotels. 

This project demonstrates core Object-Oriented Programming (OOP) concepts, collection management, and safe user-input handling.

## Features

*   **Multi-Category Booking:** Search and book tickets across dynamic categories (e.g., Flight, Movie, Hotel, Cruise).
*   **Smart Search:** Filter tickets by category or search by keywords (name, route, genre, venue).
*   **Dynamic Inventory:** Automatically tracks total capacity and available units. Prevents overbooking.
*   **Booking Management:** View all active bookings, calculate total revenue, and cancel bookings (which automatically restores ticket availability).
*   **Admin Capabilities:** Add entirely new ticket types and events during runtime.
*   **Crash-Proof Input Handling:** Custom input validation ensures the program never crashes due to invalid user data (e.g., typing text when a number is expected).
*   **Pre-loaded Sample Data:** Comes with an extensive dataset of 50+ pre-configured tickets so you can test the system immediately.

## Code Structure

The application is built using four primary Java classes:
1.  `Ticket`: Represents an event/service. Manages details like price, date, time, and tracks available capacity.
2.  `Booking`: Links a customer to a specific `Ticket` and calculates total costs based on quantity.
3.  `InputHelper`: A utility class that sanitizes console inputs, handling exceptions and enforcing positive values.
4.  `TicketBookingSystem`: The main engine that initializes the lists, loads sample data, and drives the interactive console menu.


### Prerequisites
*   Java Development Kit (JDK) 8 or higher installed on your machine.
