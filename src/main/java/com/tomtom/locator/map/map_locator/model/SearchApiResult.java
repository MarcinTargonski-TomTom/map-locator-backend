package com.tomtom.locator.map.map_locator.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchApiResult {
    private String id;
    private double score;
    private double dist;
    private SearchApiPoi poi;
    private SearchApiPosition position;

}
