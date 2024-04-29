package com.example.v1project.service;

import com.example.v1project.dto.Events;

import java.util.List;

public interface EventService {
    List<Events> getAllEvents();
    Events createEvent(Events event);
    // Other service methods if needed
}
