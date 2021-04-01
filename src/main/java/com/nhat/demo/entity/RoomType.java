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
public class RoomType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int roomTypeId;
    private String typeName;
    private int adultCapacity;
    private int childrenCapacity;
    @Column(columnDefinition = "TEXT")
    private String description;
    private double unitPrice;

    @OneToMany(mappedBy = "roomType")
    private List<RoomTypeImage> roomTypeImages = new ArrayList<>();
    @OneToMany(mappedBy = "roomType")
    private List<Room> rooms = new ArrayList<>();
    @ManyToMany
    @JoinTable(name = "roomType_conveniences",
            joinColumns = @JoinColumn(name = "room_type_id"),
            inverseJoinColumns = @JoinColumn(name = "convenience_id"))
    private List<Convenience> conveniences = new ArrayList<>();
}
