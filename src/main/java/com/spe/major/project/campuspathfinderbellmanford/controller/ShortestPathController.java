package com.spe.major.project.campuspathfinderbellmanford.controller;

import com.spe.major.project.campuspathfinderbellmanford.model.ShortestPathResult;
import com.spe.major.project.campuspathfinderbellmanford.service.ShortestPathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
public class ShortestPathController {

    private final ShortestPathService shortestPathService;

    @Autowired
    public ShortestPathController(ShortestPathService shortestPathService) {
        this.shortestPathService = shortestPathService;
    }

    @GetMapping("/")
    public String helloWorld() {
        return "Hello,World!";
    }

    @GetMapping("/shortest-path")
    public ShortestPathResult findShortestPath(
            @RequestParam("start") String start,
            @RequestParam("end") String end) {
        return shortestPathService.findShortestPath(start, end);
    }

}
