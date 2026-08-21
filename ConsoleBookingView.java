import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConsoleBookingView{

    private final Scanner scanner;
    private final SeatMap seatMap;

    public ConsoleBookingView(Scanner scanner,SeatMap seatMap) {
        this.scanner=scanner;
        this.seatMap=seatMap;
    }

    public void printUserBanner(int userId)
    {
        System.out.println();
        System.out.println("================================================");
        System.out.println("                SPOTLIGHT THEATER               ");
        System.out.println("================================================");
        System.out.println("       It is now User " + userId + "'s turn     ");
    }

    public void printMovieMenu() {
        System.out.println();
        System.out.println("Select a movie:");

        List<Movie> movies = MovieCatalog.getAll();
        for (int i=0;i<movies.size();i++) {
            Movie movie=movies.get(i);
            int available=seatMap.countAvailable(i);
            System.out.printf("  %d. %-26s %-28s %-8s (%d seats available)%n",
                    i + 1, movie.getName(), movie.getGenre(), movie.getDuration(), available);
        }
    }

    public int promptMovieChoice() {
        while(true){
            System.out.print("Enter movie number: ");
            String line = scanner.nextLine().trim();

            try{
                int choice = Integer.parseInt(line);
                if (choice >= 1 && choice <= MovieCatalog.size()) {
                    return choice - 1;
                }
            }
            
            catch (NumberFormatException ignored){
            
            }

            System.out.println("Invalid choice. Enter a number between 1 and " + MovieCatalog.size() + ".");
        }
    }

    public void printSoldOut(int movie){
        System.out.println(MovieCatalog.get(movie).getName() + " is SOLD OUT. Please choose another movie.");
    }

    public void printMovieDetails(int movie,int available){
        Movie m = MovieCatalog.get(movie);
        System.out.println();
        System.out.println(m.getName());
        System.out.println("Genre: "+m.getGenre());
        System.out.println("Duration: "+m.getDuration());
        System.out.println("Available seats: "+available);
        System.out.println(m.getDescription());
    }

    public int promptSeatCount(int available){
    
    
        while(true){
            System.out.print("How many seats do you want (1-" + available + ")? ");
            String line = scanner.nextLine().trim();

            try
            {
                int count = Integer.parseInt(line);
                if(count >= 1 && count <= available){
                    return count;
                }
            } 
            catch (NumberFormatException ignored){
             
            }

            System.out.println("Please enter a number between 1 and " + available + ".");
        }
    }

    public void printSeatGrid(int movie, List<Integer> selected){
        int seatsPerMovie = seatMap.getSeatsPerMovie();
        int rows = seatsPerMovie / SeatMap.SEATS_PER_ROW;

        System.out.println();
        System.out.println("                      S C R E E N");
        System.out.println("      " + "-".repeat(SeatMap.SEATS_PER_ROW * 5));

        for (int row = 0; row < rows; row++) {
            StringBuilder line = new StringBuilder();
            line.append((char) ('A' + row)).append("   ");

            for(int col = 0; col < SeatMap.SEATS_PER_ROW; col++){
                int seat = row * SeatMap.SEATS_PER_ROW + col;
                String cell;

                if(seatMap.isBooked(movie, seat)){
                    cell = "[XX]";
                } 
                else if(selected.contains(seat)){
                    cell = "[**]";
                } 
                else{
                    cell = String.format("[%2d]", col + 1);
                }

                line.append(cell).append(' ');
            }

            System.out.println(line);
        }

        System.out.println("Legend: [XX] booked   [**] your selection   [ N] available (column number)");
    }

    public int[] promptSeatSelection(int movie,int requested) {
        List<Integer> selected = new ArrayList<>();

        while(selected.size() < requested){
            printSeatGrid(movie, selected);
            System.out.println("Selected so far: " + formatSeats(selected)
                    + " (" + selected.size() + "/" + requested + ")");
            System.out.print("Enter a seat (e.g. A1): ");

            String line = scanner.nextLine().trim();
            int seat = SeatMap.parseSeatLabel(line, seatMap.getSeatsPerMovie());

            if(seat < 0){
                System.out.println("Invalid seat format. Use a row letter followed by a column number, e.g. B7.");
                continue;
            }
            if(seatMap.isBooked(movie, seat)){
                System.out.println("That seat is already booked. Pick another.");
                continue;
            }
            if(selected.contains(seat)){
                System.out.println("You've already selected that seat.");
                continue;
            }

            selected.add(seat);
        }

        int[] result = new int[selected.size()];
        for(int i = 0; i < result.length; i++){
            result[i] = selected.get(i);
        }
        return result;
    }

    public boolean promptConfirmation(int movie, int[] seats) {
        System.out.println();
        System.out.println("Booking summary:");
        System.out.println("  Movie: " + MovieCatalog.get(movie).getName());
        System.out.println("  Seats: " + formatSeats(seats));
        System.out.print("Confirm booking? (Y/N): ");

        while(true) {
            String line = scanner.nextLine().trim().toLowerCase();
            if(line.equals("y") || line.equals("yes")){
                return true;
            }
            if(line.equals("n") || line.equals("no")){
                return false;
            }
            System.out.print("Please enter Y or N: ");
        }
    }

    public void printBookingConfirmed(int userId, int movie, int[] seats){
        System.out.println();
        System.out.println("BOOKING CONFIRMED!");
        System.out.println("  User: " + userId);
        System.out.println("  Movie: " + MovieCatalog.get(movie).getName());
        System.out.println("  Seats: " + formatSeats(seats));
        System.out.println("Enjoy your movie!");
    }

    public void printSeatsNoLongerAvailable(){
        System.out.println("Sorry, one or more of your seats were just taken. Booking cancelled.");
    }

    public void printBookingCancelled(int userId){
        System.out.println("User " + userId + " cancelled the booking.");
    }

    private String formatSeats(int[] seats) {
        List<Integer> list = new ArrayList<>();
        for (int seat : seats) {
            list.add(seat);
        }
        return formatSeats(list);
    }

    private String formatSeats(List<Integer> seats) {
        if (seats.isEmpty()){
            return "None";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < seats.size(); i++){
            sb.append(SeatMap.seatLabel(seats.get(i)));
            if (i < seats.size() - 1){
                sb.append(", ");
            }
        }
        return sb.toString();
    }
}
