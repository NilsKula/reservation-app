package reservationapp.api;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class AddRoomRequest {

    @NotNull
    private final Integer roomNumber;
    @NotNull
    private final BigDecimal price;
    @NotNull
    private final Integer roomSize;

}
