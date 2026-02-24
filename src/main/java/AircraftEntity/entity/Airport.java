package AircraftEntity.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "airports")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Airport {

    @Id
    @Column(name = "airport_code", length = 3)
    private String airportCode;

    @Column(name = "airport_name", nullable = false)
    private String airportName;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "coordinates", nullable = false)
    private String coordinates;

    @Column(name = "timezone", nullable = false)
    private String timezone;

    @OneToMany(mappedBy = "departureAirport", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Flight> departingFlights;

    @OneToMany(mappedBy = "arrivalAirport", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Flight> arrivingFlights;
}
