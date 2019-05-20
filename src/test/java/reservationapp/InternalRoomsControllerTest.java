package reservationapp;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import reservationapp.api.AddRoomRequest;
import reservationapp.api.Room;
import reservationapp.api.RoomSchedule;
import reservationapp.api.RoomStatistics;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(InternalRoomsController.class)
public class InternalRoomsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    RoomService roomService;

    @MockBean
    ReservationService reservationService;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();

        JavaTimeModule javaTimeModule = new JavaTimeModule();

        javaTimeModule.addDeserializer(
                LocalDate.class,
                new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        );

        javaTimeModule.addSerializer(
                LocalDate.class,
                new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        );

        builder.modules(javaTimeModule);
        builder.featuresToDisable(WRITE_DATES_AS_TIMESTAMPS);

        MAPPER.registerModule(javaTimeModule);
    }

    @Test
    public void should_return_all_rooms() throws Exception {

        Room room1 = new Room(
                420,
                new BigDecimal("100.2"),
                5
        );

        Room room2 = new Room(
                420,
                new BigDecimal("100.2"),
                5
        );

        List<Room> rooms = new ArrayList<>();
        rooms.add(room1);
        rooms.add(room2);

        Mockito.lenient()
                .when(roomService.rooms())
                .thenReturn(rooms);

        String jsonResponse = mockMvc.perform(get("/internal-api/rooms"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<Room> result = MAPPER.readValue(jsonResponse, new TypeReference<List<Room>>() {
                }
        );

        assertEquals(rooms, result);

    }

    @Test
    public void should_return_200_when_adding_new_room() throws Exception {

        AddRoomRequest roomRequest = new AddRoomRequest(
                420,
                new BigDecimal("100.2"),
                5
        );

        String json = MAPPER.writeValueAsString(roomRequest);

        mockMvc.perform(
                put("/internal-api/rooms/manage")
                        .content(json)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    public void should_not_be_able_to_add_room_null_room_number() throws Exception {

        AddRoomRequest roomRequest = new AddRoomRequest(
                null,
                new BigDecimal("100.2"),
                5
        );

        String json = MAPPER.writeValueAsString(roomRequest);

        mockMvc.perform(
                put("/internal-api/rooms/manage")
                        .content(json)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_fetch_room_with_schedule() throws Exception {


        RoomSchedule roomSchedule = new RoomSchedule(
                420,
                new ArrayList<>()
        );

        Mockito.lenient()
                .when(reservationService.roomSchedules())
                .thenReturn(Collections.singletonList(roomSchedule));

        String response = mockMvc.perform(
                get("/internal-api/rooms/schedule"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<RoomSchedule> result = MAPPER.readValue(response, new TypeReference<List<RoomSchedule>>() {
                }
        );

        assertEquals(Collections.singletonList(roomSchedule), result);

    }

    @Test
    public void should_return_room_statistics() throws Exception {

        RoomStatistics statistics = new RoomStatistics(
                2,
                2
        );

        String json = MAPPER.writeValueAsString(statistics);

        Mockito.lenient()
                .when(reservationService.roomStatistics(any(), any()))
                .thenReturn(statistics);

        String response = mockMvc.perform(
                get("/internal-api/rooms/availability")
                        .param("from", "2019-01-10")
                        .param("to", "2019-01-18"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(json, response);
    }

    @Test
    public void should_be_able_to_delete_room() throws Exception {

        mockMvc.perform(
                delete("/internal-api/rooms/{room-number}", "420"))
                .andExpect(status().isOk());

    }
}
