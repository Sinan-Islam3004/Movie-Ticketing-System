/**
 * Immutable value object describing a single movie.
 */
public final class Movie{

    private final String name;
    private final String genre;
    private final String duration;
    private final String description;

    public Movie(String name, String genre, String duration, String description){
        this.name=name;
        this.genre=genre;
        this.duration=duration;
        this.description=description;
    }

    public String getName(){
        return name;
    }

    public String getGenre(){
        return genre;
    }

    public String getDuration(){
        return duration;
    }

    public String getDescription(){
        return description;
    }
}
