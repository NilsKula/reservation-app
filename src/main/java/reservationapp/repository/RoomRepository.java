package reservationapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<RoomRecord, Integer> {

    RoomRecord findByRoomNumber(Integer roomNumber);

}
