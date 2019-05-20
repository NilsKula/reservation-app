package reservationapp.api;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class RoomSchedule {

    private final Integer roomNumber;
    private final List<Reservation> reservations;

}
