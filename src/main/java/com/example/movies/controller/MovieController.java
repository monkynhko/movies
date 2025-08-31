package com.example.movies.controller;

import com.example.movies.entity.Movie;
import com.example.movies.service.MovieService;
import com.example.movies.service.OmdbService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieController {
    private final MovieService movieService;
    private final OmdbService omdbService;

    public MovieController(MovieService movieService, OmdbService omdbService) {
        this.movieService = movieService;
        this.omdbService = omdbService;
    }

    @PostMapping
    public Movie create(@RequestBody Movie movie){ return movieService.save(movie); }

    @PostMapping("/fetch/{title}")
    public Movie fetchAndSave(@PathVariable String title){
        Movie m = omdbService.fetchMovieByTitle(title);
        return (m != null) ? movieService.save(m) : null;
    }

    @GetMapping
    public List<Movie> getAll(){
        List<Movie> movies = movieService.findAll();
        // populate transient posterUrl for UI display; do not persist
        for (Movie m : movies) {
            if (m.getImdbId() != null && !m.getImdbId().isEmpty()) {
                String poster = omdbService.fetchPosterByImdbId(m.getImdbId());
                m.setPosterUrl(poster);
            }
        }
        return movies;
    }

    @PutMapping("/{id}")
    public Movie update(@PathVariable Long id, @RequestBody Movie movie){ return movieService.update(id, movie); }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){ movieService.delete(id); }
}
