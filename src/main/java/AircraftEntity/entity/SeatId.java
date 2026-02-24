package AircraftEntity.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class SeatId implements Serializable {
    private String aircraftCode;
    private String seatNo;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SeatId that = (SeatId) o;
        return Objects.equals(aircraftCode, that.aircraftCode) &&
                Objects.equals(seatNo, that.seatNo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(aircraftCode, seatNo);
    }
}
