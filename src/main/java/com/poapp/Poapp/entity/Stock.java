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

    private Float openPrice;

    private Float closePrice;

    private Float highPrice;

    private Float lowPrice;

    private Float currPrice;

}

