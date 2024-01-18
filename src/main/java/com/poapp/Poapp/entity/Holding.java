package com.poapp.Poapp.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Holding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long holdingId;

    private Long userAccId;

    private Long stockId;

    private int quantity = 0;

    private Float buyPrice = (float)0.0;

    private Float gainLoss = 0f;


}
