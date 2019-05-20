package reservationapp.api;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Data
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class ReservationRequest {

    private final LocalDate checkIn;
    private final LocalDate checkOut;
}
