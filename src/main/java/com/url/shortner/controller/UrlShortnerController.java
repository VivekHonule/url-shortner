package com.url.shortner.controller;

import com.url.shortner.model.URL;
import com.url.shortner.service.UrlShortnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/url")
public class UrlShortnerController {

    private final UrlShortnerService shortnerService;

    @Autowired
    public UrlShortnerController(UrlShortnerService shortnerService) {
        this.shortnerService = shortnerService;
    }

    @RequestMapping(value = "/shorten", method = RequestMethod.POST)
    public URL shortenURL(@RequestBody URL url) {
        return shortnerService.shorten(url);
    }
}
