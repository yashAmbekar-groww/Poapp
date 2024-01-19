package com.poapp.Poapp.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stockId;

    @Column(unique = true)
    private String stockName;

    private Float openPrice = 0.0f;

    private Float closePrice = 0.0f;

    private Float highPrice = 0.0f;

    private Float lowPrice = 0.0f;

    private Float currPrice = 0.0f;

}

