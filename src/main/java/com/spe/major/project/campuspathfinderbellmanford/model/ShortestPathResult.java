package com.spe.major.project.campuspathfinderbellmanford.model;

import java.util.List;

public class ShortestPathResult {

    private List<String> path;


    public ShortestPathResult() {
    }

    public ShortestPathResult(List<String> path) {
        this.path = path;
    }

    public List<String> getPath() {
        return path;
    }

    public void setPath(List<String> path) {
        this.path = path;
    }
}
