package reservationapp.repository;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "reservations")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class ReservationRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "room_number")
    private RoomRecord roomRecord;
    private LocalDate checkIn;
    private LocalDate checkOut;

    public ReservationRecord(RoomRecord roomRecord, LocalDate checkIn, LocalDate checkOut) {
        this.roomRecord = roomRecord;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }
}
