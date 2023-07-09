package com.example.triptip.controller;

import com.example.triptip.model.SortOrder;
import com.example.triptip.model.Tag;
import com.example.triptip.model.destination.Destination;
import com.example.triptip.model.user.User;
import com.example.triptip.service.DestinationService;
import com.example.triptip.service.ReviewService;
import com.example.triptip.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class DefaultController {

    private final UserService userService;
    private final DestinationService destinationService;
    private final ReviewService reviewService;

    private String currentUser; //NEVER DO THAT. THIS IS BAD AND IS ONLY FOR THE SAKE OF PRESENTATION.
    private String searched;
    private SortOrder order = SortOrder.CHEAP;

    public DefaultController(UserService userService, DestinationService destinationService, ReviewService reviewService) {
        this.userService = userService;
        this.destinationService = destinationService;
        this.reviewService = reviewService;
    }

    @GetMapping("/")
    public String index(Model model,
                        @RequestParam(name = "favourites", defaultValue = "false") boolean favourites){
        model.addAttribute("destinations",
                destinationService.readAllDestinationsContainingWordAndSortedWith(searched, order));
        model.addAttribute("user", userService.readUser(currentUser));
        model.addAttribute("showingFavourites", favourites);
        model.addAttribute("searched", searched);
        model.addAttribute("sortOrder", order.getId());
        return "index";
    }

    @GetMapping("/add/destination")
    public String addDestination(Model model){
        model.addAttribute("destination", new Destination());
        return "addDest";
    }

    @GetMapping("/destination")
    public String destination(Model model, @RequestParam("name") String name){
        Destination destination = destinationService.readDestination(name);
        if(destination == null) return "404";
        model.addAttribute("destination", destination);
        model.addAttribute("user", userService.readUser(currentUser));
        return "destination";
    }

    @GetMapping("/edit/destination")
    public String editDestination(@RequestParam(name = "name") String name, Model model) {
        model.addAttribute("destination", destinationService.readDestination(name));
        return "editDest";
    }

    @GetMapping("/user/login")
    public String loginUser(Model model){
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping("/user/logged")
    public RedirectView readUser(@ModelAttribute User user) {
        User readUser = userService.readUser(user.getUsername());
        if(readUser != null) currentUser = readUser.getUsername();
        return new RedirectView("/");
    }

    @GetMapping("/user/register")
    public String registerUser(Model model){
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/user/registered")
    public RedirectView createUser(@ModelAttribute User user) {
        User readUser = userService.createUser(user.getUsername(), user.getPasswordHash());
        if(readUser != null) currentUser = readUser.getUsername();
        return new RedirectView("/");
    }


    @GetMapping("/user/logout")
    public RedirectView logoutUser(){
        currentUser = null;
        return new RedirectView("/");
    }

    @PostMapping("/favDestination/{name}")
    public RedirectView favouriteDestination(@PathVariable String name) {
        if(userService.readUser(currentUser).hasFavourite(name)) userService.deleteUserFavourite(currentUser, name);
        else userService.addUserFavourite(currentUser, name);
        return new RedirectView("/destination?name=" + name);
    }

    @GetMapping("/settings")
    public String settings(Model model){
        User readUser = null;
        if(currentUser != null) readUser = userService.readUser(currentUser);
        model.addAttribute("user", readUser);
        model.addAttribute("tags", Tag.values());
        return "settings";
    }
	
    @GetMapping("/preferences")
    public String preferences(Model model){
        User readUser = null;
        if(currentUser != null) readUser = userService.readUser(currentUser);
        model.addAttribute("user", readUser);
        model.addAttribute("tags", Tag.values());
        return "preferences";
    }

    @GetMapping("/profile")
    public String profile(Model model){
        User readUser = null;
        if(currentUser != null) readUser = userService.readUser(currentUser);
        model.addAttribute("user", readUser);
        return "profile";
    }
	
    @GetMapping("/filters")
    public String filters(Model model){
        User readUser = null;
        if(currentUser != null) readUser = userService.readUser(currentUser);
        return "filters";
    }


    @GetMapping("/search")
    public ModelAndView SearchElement(@RequestParam("argument") String inputFieldValue, ModelMap model) {
        searched = inputFieldValue;
        return new ModelAndView("forward:/",model);
    }


    @PostMapping("/pref/{tag}/{value}")
    public RedirectView addPreference(@PathVariable String tag, @PathVariable int value) {
        userService.addUserPreference(currentUser, Tag.valueOf(tag), value);
        return new RedirectView("/preferences");
    }

    @GetMapping("/sort")
    public ModelAndView sortIndex(@RequestParam("sortOption") String sortingOption,
                                  ModelMap model) {
        order = SortOrder.getById(Integer.parseInt(sortingOption));
        return new ModelAndView("forward:/",model);
    }
}