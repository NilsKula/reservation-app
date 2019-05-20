package reservationapp;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reservationapp.api.Reservation;
import reservationapp.api.ReservationRequest;
import reservationapp.api.RoomSchedule;
import reservationapp.api.RoomStatistics;
import reservationapp.repository.ReservationRecord;
import reservationapp.repository.ReservationRepository;
import reservationapp.repository.RoomRecord;
import reservationapp.repository.RoomRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;

    List<RoomSchedule> roomSchedules() {

        return roomRepository.findAll()
                .stream()
                .map(this::roomSchedule)
                .collect(Collectors.toList());

    }

    void reserveRoom(Integer roomNumber, ReservationRequest reservationRequest) {

        RoomRecord roomRecord = roomRepository.findByRoomNumber(roomNumber);
        if (roomRecord == null
                || reservationRequest.getCheckOut().isBefore(reservationRequest.getCheckIn())
                || reservedRoomsForPeriod(reservationRequest.getCheckIn(),
                reservationRequest.getCheckOut()).contains(roomRecord)) {
            throw new InvalidRequestException();
        }

        reservationRepository.save(new ReservationRecord(
                roomRecord,
                reservationRequest.getCheckIn(),
                reservationRequest.getCheckOut())
        );
    }

    List<RoomRecord> availableRooms(LocalDate checkIn, LocalDate checkOut) {
        return roomRepository.findAll()
                .stream()
                .filter(roomRecord -> !(reservedRoomsForPeriod(checkIn, checkOut).contains(roomRecord)))
                .collect(Collectors.toList());
    }

    RoomStatistics roomStatistics(LocalDate dateFrom, LocalDate dateTo) {
        if (dateTo.isBefore(dateFrom)) {
            throw new InvalidRequestException();
        }
        List<RoomRecord> allRooms = roomRepository.findAll();
        List<RoomRecord> reservedRooms = reservedRoomsForPeriod(dateFrom, dateTo);

        return new RoomStatistics(
                allRooms.size() - reservedRooms.size(),
                reservedRooms.size()
        );
    }

    private RoomSchedule roomSchedule(RoomRecord roomRecord) {
        List<Reservation> reservations = reservationRepository.findAllByRoomRecord(roomRecord);

        return new RoomSchedule(
                roomRecord.getRoomNumber(),
                reservations
        );
    }

    public List<RoomRecord> reservedRoomsForPeriod(LocalDate from, LocalDate to) {
        return reservationRepository.checkAvailabilityForPeriod(from, to);

    }
}
