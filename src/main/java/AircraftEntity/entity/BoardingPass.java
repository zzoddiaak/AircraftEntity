package AircraftEntity.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "boarding_passes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(BoardingPassId.class)
public class BoardingPass {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_no", referencedColumnName = "ticket_no")
    private Ticket ticket;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flight_id", referencedColumnName = "flight_id")
    private Flight flight;

    @Column(name = "boarding_no", nullable = false)
    private Integer boardingNo;

    @Column(name = "seat_no", length = 4, nullable = false)
    private String seatNo;
}
