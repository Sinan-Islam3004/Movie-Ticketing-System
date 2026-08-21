import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public final class MovieCatalog{

    private static final List<Movie> MOVIES = Collections.unmodifiableList(buildMovies());

    private MovieCatalog(){
    }

    private static List<Movie> buildMovies(){
        List<Movie> list=new ArrayList<>();

        list.add(new Movie(
                "Interstellar",
                "Science Fiction / Drama",
                "2h 49m",
                "A team of explorers travels through a wormhole in space in an attempt to ensure humanity's survival."));

        list.add(new Movie(
                "Inception",
                "Science Fiction / Thriller",
                "2h 28m",
                "A skilled thief enters people's dreams to steal secrets and is given a chance to erase his past."));

        list.add(new Movie(
                "The Dark Knight",
                "Action / Crime",
                "2h 32m",
                "Batman faces the Joker, a dangerous criminal who brings chaos to Gotham City."));

        list.add(new Movie(
                "Avengers: Endgame",
                "Action / Adventure",
                "3h 2m",
                "The Avengers make their final attempt to defeat Thanos and save the universe."));

        list.add(new Movie(
                "Spider-Man: No Way Home",
                "Action / Adventure",
                "2h 28m",
                "Spider-Man faces enemies from different dimensions after his identity is revealed."));

        return list;
    }

    public static List<Movie> getAll(){
        return MOVIES;
    }

    public static Movie get(int index){
        return MOVIES.get(index);
    }

    public static int size(){
        return MOVIES.size();
    }
}
