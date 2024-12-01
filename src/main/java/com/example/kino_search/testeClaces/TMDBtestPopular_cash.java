package com.example.kino_search.testeClaces;

import com.example.kino_search.util.TMDBApiUtil;

public class TMDBtestPopular_cash {
    public static void main(String[] args) {
        try {
            // Первый запрос (нет в кэше)
            String popularMovies = TMDBApiUtil.sendRequest("/movie/popular");
            System.out.println("Popular Movies: " + popularMovies);

            // Повторный запрос (будет использован кэш)
            String cachedMovies = TMDBApiUtil.sendRequest("/movie/popular");
            System.out.println("Cached Movies: " + cachedMovies);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}