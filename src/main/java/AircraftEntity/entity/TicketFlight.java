package AircraftEntity.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "ticket_flights")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(TicketFlightId.class)
public class TicketFlight {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_no", referencedColumnName = "ticket_no")
    private Ticket ticket;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flight_id", referencedColumnName = "flight_id")
    private Flight flight;

    @Column(name = "fare_conditions", length = 10, nullable = false)
    private String fareConditions;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

}
