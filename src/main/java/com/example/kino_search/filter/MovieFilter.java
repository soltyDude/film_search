package com.example.kino_search.filter;

import java.util.List;
import java.util.Map;

public interface MovieFilter {
    List<Map<String, String>> filter(List<Map<String, String>> movies);
}
