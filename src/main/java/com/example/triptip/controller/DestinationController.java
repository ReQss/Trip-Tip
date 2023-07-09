package com.example.triptip.controller;

import com.example.triptip.model.destination.Destination;
import com.example.triptip.service.DestinationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class DestinationController {

    private final DestinationService service;

    public DestinationController(DestinationService service) {
        this.service = service;
    }

    @PostMapping("/destinations/addDestination")
    public RedirectView addDestination(@ModelAttribute Destination destination) {
        service.createDestination(destination.getName(), destination.getDescription(), destination.getPrice());
        return new RedirectView("/edit/destination?name=" + destination.getName());
    }

    @PostMapping("/destinations/editDestination")
    public RedirectView formEditDestination(@RequestParam String name,
                                            @RequestParam String description,
                                            @RequestParam Integer price,
                                            @RequestParam String pictureUrl,
                                            @RequestParam String deletePicture) {
        if (!description.isEmpty()) service.updateDescription(name, description);
        if (price != 0) service.updatePrice(name, price);
        if (!pictureUrl.isEmpty()) service.addPicture(name, pictureUrl);
        if (!deletePicture.isEmpty()) service.deletePicture(name, deletePicture);
        return new RedirectView("/edit/destination?name=" + name);
    }
}
