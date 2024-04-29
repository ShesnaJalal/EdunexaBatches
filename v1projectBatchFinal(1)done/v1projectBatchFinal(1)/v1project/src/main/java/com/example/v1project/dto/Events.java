package com.example.v1project.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import jakarta.persistence.ManyToOne;

import jakarta.persistence.JoinColumn;

import jakarta.persistence.ForeignKey;
import lombok.NoArgsConstructor;


import java.sql.Date;
import java.sql.Time;


@Entity
@Table(name = "events")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Events {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_ID")
    private int eventId;

    @Column(name = "event_title", nullable = false, columnDefinition = "LONGTEXT")
    private String eventTitle;

    @ManyToOne
    @JoinColumn(name = "host", nullable = false, referencedColumnName = "user_id")
    private Users host;
//    @Column(name = "host", nullable = false)
//    private int host;

    @Column(name = "description", nullable = false,  columnDefinition = "LONGTEXT")
    private String description;

    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @Column(name = "end_date", nullable = false)
    private Date endDate;

    @Column(name = "contact", nullable = false)
    private int contact;

    @Column(name = "link", nullable = false, length = 150)
    private String link;

    @Column(name = "event_type", nullable = false, length = 45)
    private String eventType;

    @Column(name = "speaker", nullable = false, length = 45)
    private String speaker;

    @Column(name = "start_time", nullable = false)
    private Time startTime;

    @Column(name = "end_time", nullable = false)
    private Time endTime;

    @Column(name = "location", length = 45)
    private String location;

    @Column(name = "speaker_description", length = 45)
    private String speakerDescription;

    @Column(name = "event_mode", length = 45)
    private String eventMode;

}
