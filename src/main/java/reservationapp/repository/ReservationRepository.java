package reservationapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import reservationapp.api.Reservation;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<ReservationRecord, Integer> {

    List<Reservation> findAllByRoomRecord(RoomRecord roomRecord);

    @Query("SELECT DISTINCT room.roomRecord FROM ReservationRecord room  "
            + " WHERE :checkIn BETWEEN room.checkIn AND room.checkOut "
            + "OR :checkOut BETWEEN room.checkIn AND room.checkOut "
            + "OR room.checkIn BETWEEN :checkIn AND :checkOut")
    List<RoomRecord> checkAvailabilityForPeriod(@Param("checkIn") LocalDate checkIn,
                                                @Param("checkOut") LocalDate checkOut);
}

