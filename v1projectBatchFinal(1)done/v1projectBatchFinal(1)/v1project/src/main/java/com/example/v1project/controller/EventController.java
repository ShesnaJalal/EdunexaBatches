package com.example.v1project.controller;

import com.example.v1project.dto.Events;
import com.example.v1project.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @GetMapping("/api/getevent")
    public List<Events> getAllEvents() {
        return eventService.getAllEvents();
    }

    @PostMapping("/api/addevent")
    public Events createEvent(@RequestBody Events event) {
        return eventService.createEvent(event);
    }

    // Other endpoints for updating, deleting events if needed
}

