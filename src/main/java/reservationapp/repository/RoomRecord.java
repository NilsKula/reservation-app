package reservationapp.repository;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "rooms")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "roomNumber")
public class RoomRecord {

    @Id
    private Integer roomNumber;
    private BigDecimal price;
    private int roomSize;

    public RoomRecord(Integer roomNumber, BigDecimal price, int roomSize) {
        this.roomNumber = roomNumber;
        this.price = price;
        this.roomSize = roomSize;
    }
}