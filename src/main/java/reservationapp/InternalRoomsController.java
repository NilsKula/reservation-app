package reservationapp;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import reservationapp.api.AddRoomRequest;
import reservationapp.api.Room;
import reservationapp.api.RoomSchedule;
import reservationapp.api.RoomStatistics;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/internal-api")
@RequiredArgsConstructor
public class InternalRoomsController {

    private final RoomService roomService;
    private final ReservationService reservationService;
    
    @GetMapping("/rooms")
    public List<Room> rooms() {
        return roomService.rooms();
    }

    @PutMapping("/rooms/manage")
    public void addOrUpdateRoom(@Valid @RequestBody AddRoomRequest addRoomRequest) {
        roomService.addOrUpdateRoom(addRoomRequest);
    }

    @GetMapping("/rooms/schedule")
    public List<RoomSchedule> roomSchedules() {
        return reservationService.roomSchedules();
    }

    @GetMapping("/rooms/availability")
    public RoomStatistics roomStatistics(@RequestParam("from") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate from,
                                         @RequestParam("to") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate to) {

        return reservationService.roomStatistics(from, to);

    }

    @DeleteMapping("/rooms/{room-number}")
    public void removeRoom(@PathVariable("room-number") Integer roomNumber) {
        roomService.removeRoom(roomNumber);
    }
}
