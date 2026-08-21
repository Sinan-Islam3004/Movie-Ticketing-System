
public class UserWorker implements Runnable{

    private final int userId;
    private final SeatMap seatMap;
    private final BookingTurnSemaphore turnSemaphore;
    private final ConsoleBookingView view;

    public UserWorker(int userId,SeatMap seatMap,BookingTurnSemaphore turnSemaphore,ConsoleBookingView view){
        this.userId=userId;
        this.seatMap=seatMap;
        this.turnSemaphore=turnSemaphore;
        this.view=view;
    }

    @Override
    public void run(){
        try{
           
            turnSemaphore.acquireTurn();

            view.printUserBanner(userId);

            int movie=chooseAvailableMovie();
            int available=seatMap.countAvailable(movie);
            int requestedSeats=view.promptSeatCount(available);
            int[] seats = view.promptSeatSelection(movie,requestedSeats);
            boolean confirmed = view.promptConfirmation(movie,seats);

            if(confirmed){
               
                boolean success=seatMap.tryBookSeats(movie, seats, seats.length);

                if(success){
                    view.printBookingConfirmed(userId,movie,seats);
                } 
                else {
                    view.printSeatsNoLongerAvailable();
                }
            } 
            else{
                view.printBookingCancelled(userId);
            }

        }
        catch(InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        finally{
      
      
            turnSemaphore.releaseTurn();
        }
    }


    private int chooseAvailableMovie(){
        while(true){
            view.printMovieMenu();
            int choice=view.promptMovieChoice();
            int available=seatMap.countAvailable(choice);

            if(available<=0){
                view.printSoldOut(choice);
                continue;
            }

            view.printMovieDetails(choice,available);
            return choice;
        }
    }
}
