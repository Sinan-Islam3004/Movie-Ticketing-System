import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;


public class SeatMap {


    public static final int SEATS_PER_ROW=10;

    private final boolean[][] seats;
    private final int seatsPerMovie;
    private final ReentrantLock lock=new ReentrantLock();

    public SeatMap(int numberOfMovies, int seatsPerMovie){
        this.seatsPerMovie = seatsPerMovie;
        this.seats =new boolean[numberOfMovies][seatsPerMovie];
        randomlyBookInitialSeats();
    }


    private void randomlyBookInitialSeats(){
        Random random=new Random();

        lock.lock();
        try{
            for(int movie = 0; movie < seats.length; movie++) {
                for(int seat = 0; seat < seatsPerMovie; seat++) {
                    seats[movie][seat] = random.nextInt(100) < 20;
                }
            }
        } 
       
       
       finally{
            lock.unlock();
        }
    }

    public int getSeatsPerMovie() {
        return seatsPerMovie;
    }
    
    

    public boolean isBooked(int movie, int seat){
        lock.lock();
        try{
            return seats[movie][seat];
        }
        
        
        finally{
            lock.unlock();
        }
    }

    public int countAvailable(int movie){
        lock.lock();
        try{
            int count = 0;
            for(int seat = 0; seat < seatsPerMovie; seat++) {
                if(!seats[movie][seat]) {
                    count++;
                }
            }
            return count;
            
            
        } 
        finally {
            lock.unlock();
        }
    }

   
   
   
    public boolean tryBookSeats(int movie, int[] seatIndices, int count) {
        lock.lock();
        try {
            for (int i = 0; i < count; i++) {
                if (seats[movie][seatIndices[i]]) {
                    return false;
                }
            }
            for (int i = 0; i < count; i++) {
                seats[movie][seatIndices[i]] = true;
            }
            return true;
        } finally {
            lock.unlock();
        }
    }




   
    public static String seatLabel(int seat) {
        int row=seat / SEATS_PER_ROW;
        int col=seat % SEATS_PER_ROW;
        return "" + (char) ('A' + row) + (col + 1);
    }

    public static int parseSeatLabel(String text,int seatsPerMovie){
        if (text==null || text.length()<2) {
            return -1;
        }

        char rowChar = Character.toUpperCase(text.charAt(0));
        if(rowChar<'A' || rowChar>'Z'){
            return -1;
        }

        String columnPart=text.substring(1);
        int column;
        try{
            column = Integer.parseInt(columnPart);
        } 
        catch(NumberFormatException e){
            return -1;
        }

        if(column < 1 || column > SEATS_PER_ROW){
            return -1;
        }
        
        

        int row = rowChar - 'A';
        int seat = row*SEATS_PER_ROW+(column - 1);

        if (seat < 0 || seat >= seatsPerMovie){
            return -1;
        }

        return seat;
    }
}
