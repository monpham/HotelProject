package com.nhat.demo.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Setter
@Getter
public class Convenience {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int convenienceId;
    private String convenienceName;
    @ManyToMany(mappedBy = "conveniences")
    private List<RoomType> roomTypes = new ArrayList<>();
}
