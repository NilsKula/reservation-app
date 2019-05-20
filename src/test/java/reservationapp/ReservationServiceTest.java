package reservationapp;

import org.junit.Test;
import org.mockito.Mockito;
import reservationapp.api.ReservationRequest;
import reservationapp.api.RoomStatistics;
import reservationapp.repository.ReservationRepository;
import reservationapp.repository.RoomRecord;
import reservationapp.repository.RoomRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

public class ReservationServiceTest {

    private ReservationRepository reservationRepository = mock(ReservationRepository.class);
    private RoomRepository roomRepository = mock(RoomRepository.class);
    private ReservationService reservationService = new ReservationService(reservationRepository, roomRepository);

    @Test
    public void should_not_be_able_to_make_reservation_on_none_existing_room() {
        //given
        Mockito.when(roomRepository.findByRoomNumber(any())).thenReturn(null);
        ReservationRequest reservationRequest = new ReservationRequest(
                LocalDate.now(),
                LocalDate.now().plusDays(2)
        );
        //then
        assertThrows(InvalidRequestException.class, () -> reservationService.reserveRoom(420, reservationRequest));

    }

    @Test
    public void should_not_be_able_to_reserve_if_check_out_is_before_checkIn() {
        //given
        RoomRecord roomRecord = new RoomRecord();
        Mockito.when(roomRepository.findByRoomNumber(any())).thenReturn(roomRecord);
        ReservationRequest reservationRequest = new ReservationRequest(
                LocalDate.now().plusDays(2),
                LocalDate.now()
        );

        //then
        assertThrows(InvalidRequestException.class, () -> reservationService.reserveRoom(420, reservationRequest));
    }

    @Test
    public void should_not_be_able_to_reserve_if_reservation_already_exists() {
        //given
        ReservationRequest reservationRequest = new ReservationRequest(
                LocalDate.now(),
                LocalDate.now().plusDays(2)
        );
        RoomRecord roomRecord = roomRecord1();

        Mockito.when(roomRepository.findByRoomNumber(any())).thenReturn(roomRecord);
        Mockito.when(reservationService.reservedRoomsForPeriod(any(),
                any())).thenReturn(Collections.singletonList(roomRecord));
        //then
        assertThrows(InvalidRequestException.class, () -> reservationService.reserveRoom(420, reservationRequest));
    }

    @Test
    public void should_return_rooms_not_available_for_period() {
        //given
        List<RoomRecord> roomRecord = new ArrayList<>();
        roomRecord.add(roomRecord1());
        roomRecord.add(roomRecord2());

        Mockito.when(reservationService.reservedRoomsForPeriod(any(),
                any())).thenReturn(roomRecord);
        //when
        List<RoomRecord> result = reservationService.reservedRoomsForPeriod(LocalDate.now(), LocalDate.now().plusDays(2));
        //then
        assertEquals(2, result.size());
    }

    @Test
    public void should_return_available_rooms() {
        //given
        List<RoomRecord> roomRecord = new ArrayList<>();
        RoomRecord room1 = roomRecord1();
        RoomRecord room2 = roomRecord2();
        roomRecord.add(room1);
        roomRecord.add(room2);

        Mockito.when(roomRepository.findAll()).thenReturn(roomRecord);
        Mockito.when(reservationService.reservedRoomsForPeriod(any(),
                any())).thenReturn(Collections.singletonList(room1));

        //when
        List<RoomRecord> result = reservationService.availableRooms(LocalDate.now(), LocalDate.now().plusDays(2));
        //then
        assertEquals(1, result.size());
        assertEquals(result.get(0).getRoomNumber(), room2.getRoomNumber());
    }

    @Test
    public void should_return_statistics_for_available_rooms_for_period() {
        //given
        RoomStatistics roomStatistics = new RoomStatistics(
                1,
                1
        );
        List<RoomRecord> roomRecord = new ArrayList<>();
        RoomRecord room1 = roomRecord1();
        RoomRecord room2 = roomRecord2();
        roomRecord.add(room1);
        roomRecord.add(room2);

        Mockito.when(roomRepository.findAll()).thenReturn(roomRecord);
        Mockito.when(reservationService.reservedRoomsForPeriod(any(),
                any())).thenReturn(Collections.singletonList(room1));
        //when
        RoomStatistics statistics = reservationService.roomStatistics(LocalDate.now(), LocalDate.now().plusDays(2));
        //then
        assertEquals(roomStatistics.getRoomsBusy(), statistics.getRoomsBusy());
        assertEquals(roomStatistics.getRoomsFree(), statistics.getRoomsFree());
    }

    @Test
    public void should_not_be_able_to_search_statistics_if_checkout_before_checkin() {
        //then
        assertThrows(InvalidRequestException.class, () -> reservationService.roomStatistics(LocalDate.now().plusDays(2), LocalDate.now()));

    }

    private RoomRecord roomRecord1() {
        return new RoomRecord(423,
                new BigDecimal("100.0"),
                5);
    }

    private RoomRecord roomRecord2() {
        return new RoomRecord(425,
                new BigDecimal("100.0"),
                5);
    }
}