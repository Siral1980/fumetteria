package it.apuliadigital.comicstore.controllers;

import it.apuliadigital.comicstore.models.Comic;
import it.apuliadigital.comicstore.models.Sell;
import it.apuliadigital.comicstore.services.ComicService;
import it.apuliadigital.comicstore.services.SellService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sell")
public class SellController {
    @Autowired
    public SellService sellService;
    public ComicService comicService;

    public SellController(ComicService comicService) {
        this.comicService = comicService;
    }

    @PostMapping("/sellComic")
    public ResponseEntity<Sell> sellComic(@RequestParam String title,
                                          @RequestParam int quantity){
        return ResponseEntity.ok(comicService.sellComic(title, quantity));
    }
}
