package com.nhat.demo.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int roomId;
    private int roomNumber;
    private int floor;

    @ManyToOne
    @JoinColumn(name = "room_type_id")
    private RoomType roomType;
    @OneToMany(mappedBy = "room")
    private List<BookingDetail> bookingDetail = new ArrayList<>();
}
