package AircraftEntity.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "seats")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(SeatId.class)
public class Seat {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aircraft_code", referencedColumnName = "aircraft_code")
    private Aircraft aircraft;

    @Id
    @Column(name = "seat_no", length = 4)
    private String seatNo;

    @Column(name = "fare_conditions", length = 10, nullable = false)
    private String fareConditions;
}
