package reservationapp.api;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class RoomStatistics {

    private final Integer roomsFree;
    private final Integer roomsBusy;
}
