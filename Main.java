import java.util.Scanner;


public class Main {

    public static void main(String[] args){
        if(args.length != 2){
            System.out.println("Usage: java Main <number_of_users> <seats_per_movie>");
            System.out.println("Example: java Main 5 50");
            return;
        }

        int numberOfUsers;
        int seatsPerMovie;

        try{
            numberOfUsers = Integer.parseInt(args[0]);
            seatsPerMovie = Integer.parseInt(args[1]);
        }
        catch (NumberFormatException e){
            System.out.println("Both arguments must be integers.");
            return;
        }

        if(numberOfUsers <= 0){
            System.out.println("Number of users must be greater than 0.");
            return;
        }

        if (seatsPerMovie <= 0||seatsPerMovie % SeatMap.SEATS_PER_ROW != 0) {
            System.out.println("Seats per movie must be a positive multiple of " + SeatMap.SEATS_PER_ROW + ".");
            return;
        }



        if(seatsPerMovie / SeatMap.SEATS_PER_ROW > 26){
            System.out.println("Maximum supported seats per movie is 260.");
            return;
        }

        SeatMap seatMap=new SeatMap(MovieCatalog.size(), seatsPerMovie);
        BookingTurnSemaphore turnSemaphore=new BookingTurnSemaphore();
        Scanner scanner=new Scanner(System.in);
        ConsoleBookingView view = new ConsoleBookingView(scanner, seatMap);

        System.out.println("Welcome to SPOTLIGHT THEATER (console edition).");
        System.out.println(numberOfUsers+" user(s) will book seats, one at a time.");

        Thread[] threads=new Thread[numberOfUsers];

        for (int i = 0; i < numberOfUsers; i++) {
            int userId = i + 1;
            UserWorker worker = new UserWorker(userId, seatMap, turnSemaphore, view);
            threads[i] = new Thread(worker,"UserThread-"+userId);
            threads[i].start();
        }

        for (Thread thread:threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        scanner.close();

        System.out.println();
        System.out.println("All users have completed their bookings. Goodbye!");
    }
}
