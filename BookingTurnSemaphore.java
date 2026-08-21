import java.util.concurrent.Semaphore;


public class BookingTurnSemaphore {

    private final Semaphore semaphore = new Semaphore(1,true);

    public void acquireTurn() throws InterruptedException{
        semaphore.acquire();
    }

    public void releaseTurn(){
        semaphore.release();
    }
}
