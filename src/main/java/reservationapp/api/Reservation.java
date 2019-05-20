package reservationapp.api;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Data
@RequiredArgsConstructor
public class Reservation {

    private final LocalDate checkIn;
    private final LocalDate checkOut;

}
