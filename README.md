## Ticket Manager
Writer: Koo Bonchan
start date: 241031, end date: 241107
language: java

### Requirements
* Project Flow - 2 State machine
* Main Menu
    * Reserve a movie
    * Check Reservation
    * Cancel Reservation
    * to AdminMenu, requires password
* Admin Menu
    * Register a movie
    * Check movie list
    * Delete movie
    * CRUD reservation list
    * to Main menu

### Changes I've made from original
* Added connection pool, not single connection establishment for each CRUD
* extracted each page from main.
* extracted DB service
* project structuring
* Changed page nav from single memory to stack.

### Challenges and TODOs
* Apply dependency injection pattern to services
* Better command IO
* extract more features from main menu page.
