package reservationapp;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reservationapp.api.AddRoomRequest;
import reservationapp.api.Room;
import reservationapp.repository.RoomRecord;
import reservationapp.repository.RoomRepository;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;

    List<Room> rooms() {
        return roomRepository.findAll()
                .stream()
                .map(this::room)
                .collect(Collectors.toList());
    }

    void addOrUpdateRoom(AddRoomRequest addRoomRequest) {
        RoomRecord roomRecord = new RoomRecord();
        roomRecord.setRoomNumber(addRoomRequest.getRoomNumber());
        roomRecord.setPrice(addRoomRequest.getPrice());
        roomRecord.setRoomSize(addRoomRequest.getRoomSize());

        roomRepository.save(roomRecord);
    }

    void removeRoom(Integer roomNumber) {
        roomRepository.deleteById(roomNumber);

    }

    private Room room(RoomRecord roomRecord) {

        return new Room(
                roomRecord.getRoomNumber(),
                roomRecord.getPrice(),
                roomRecord.getRoomSize());
    }
}
