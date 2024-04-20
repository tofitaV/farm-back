package com.example.happyfarmer.Models;

import com.example.happyfarmer.Utils.ItemEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@Table(name = "wheel_prizes")
@AllArgsConstructor
@NoArgsConstructor
public class WheelPrize {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Column(name="prize_name")
    private String prizeName;
    @Column(name = "prize_count")
    private int prizeCount;
    @Column(name = "prize_type")
    @Enumerated(EnumType.ORDINAL)
    private ItemEnum prizeType;
}
