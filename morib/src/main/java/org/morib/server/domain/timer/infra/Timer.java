package org.morib.server.domain.timer.infra;

import jakarta.persistence.*;

@Entity
public class Timer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "timer_id")
    private Long id;

}
