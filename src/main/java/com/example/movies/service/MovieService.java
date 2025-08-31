package com.example.movies.service;

import com.example.movies.entity.Movie;
import com.example.movies.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {
    private final MovieRepository repo;
    public MovieService(MovieRepository repo){ this.repo = repo; }

    public Movie save(Movie m){ return repo.save(m); }
    public List<Movie> findAll(){ return repo.findAll(); }
    public Movie update(Long id, Movie newM){
        return repo.findById(id).map(m -> {
            m.setTitle(newM.getTitle()); m.setYear(newM.getYear());
            m.setDirector(newM.getDirector()); m.setImdbId(newM.getImdbId());
            m.setPosterUrl(newM.getPosterUrl());
            return repo.save(m);
        }).orElseThrow(() -> new RuntimeException("Movie not found"));
    }
    public void delete(Long id){ repo.deleteById(id); }
}
