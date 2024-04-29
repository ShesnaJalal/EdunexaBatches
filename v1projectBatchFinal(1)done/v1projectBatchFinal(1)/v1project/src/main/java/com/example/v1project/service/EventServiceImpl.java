package com.example.v1project.service;

import com.example.v1project.dto.Events;
import com.example.v1project.dao.EventDao;
import com.example.v1project.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private EventDao eventRepository;

    @Override
    public List<Events> getAllEvents() {
        return eventRepository.findAll();
    }

    @Override
    public Events createEvent(Events event) {
        return eventRepository.save(event);
    }

    // Other service methods implementations
}
