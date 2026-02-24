package AircraftEntity.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "aircrafts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Aircraft {

    @Id
    @Column(name = "aircraft_code", length = 3)
    private String aircraftCode;

    @Column(name = "model", nullable = false)
    private String model;

    @Column(name = "range", nullable = false)
    private Integer range;

    @OneToMany(mappedBy = "aircraft", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Seat> seats;

    @OneToMany(mappedBy = "aircraft", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Flight> flights;
}
