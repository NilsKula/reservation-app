package reservationapp;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import reservationapp.api.ReservationRequest;
import reservationapp.repository.RoomRecord;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/public-api")
@RequiredArgsConstructor
public class PublicRoomsController {
    
    private final ReservationService reservationService;

    @PostMapping("/rooms/{room-number}/reserve")
    public void reserveRoom(@PathVariable("room-number") Integer roomNumber,
                            @RequestBody ReservationRequest reservationRequest) {
        reservationService.reserveRoom(roomNumber, reservationRequest);
    }

    @GetMapping("/rooms/search")
    public List<RoomRecord> availableRooms(@RequestParam("checkIn") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate checkIn,
                                           @RequestParam("checkOut") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate checkOut) {

        return reservationService.availableRooms(checkIn, checkOut);

    }
}
