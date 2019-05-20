package reservationapp.api;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Data
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class Room {

    private final Integer roomNumber;
    private final BigDecimal price;
    private final int roomSize;
}
