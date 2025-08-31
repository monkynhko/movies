package com.example.movies.service;

import com.example.movies.entity.Movie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
public class OmdbService {
    @Value("${omdb.api.key}")
    private String apiKey;

    @Value("${omdb.api.url}")
    private String apiUrl;

    public Movie fetchMovieByTitle(String title) {
        try {
            RestTemplate rt = new RestTemplate();
            String url = UriComponentsBuilder.fromHttpUrl(apiUrl)
                  .queryParam("apikey", apiKey)
                  .queryParam("t", title)
                  .build()
                  .toUriString();

            Map<String,String> resp = rt.getForObject(url, Map.class);
            if (resp != null && "True".equalsIgnoreCase(resp.get("Response"))) {
                Movie m = new Movie();
                m.setTitle(resp.get("Title"));
                m.setYear(resp.get("Year"));
                m.setDirector(resp.get("Director"));
                m.setImdbId(resp.get("imdbID"));
                // posterUrl is not persisted to DB (transient) but we populate it on the Movie object
                m.setPosterUrl(resp.get("Poster"));
                return m;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String fetchPosterByImdbId(String imdbId) {
        try {
            RestTemplate rt = new RestTemplate();
            String url = UriComponentsBuilder.fromHttpUrl(apiUrl)
                    .queryParam("apikey", apiKey)
                    .queryParam("i", imdbId)
                    .build()
                    .toUriString();

            Map<String,String> resp = rt.getForObject(url, Map.class);
            if (resp != null && "True".equalsIgnoreCase(resp.get("Response"))) {
                return resp.get("Poster");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
