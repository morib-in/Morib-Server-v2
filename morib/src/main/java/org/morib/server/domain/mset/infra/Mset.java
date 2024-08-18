package org.morib.server.domain.mset.infra;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Mset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mset_id")
    private Long id;
}
