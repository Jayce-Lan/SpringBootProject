package com.example.mybatis;

import lombok.Data;

import java.util.Date;

@Data
public class City {
    private Integer cityId;
    private String city;
    private Integer countryId;
    private Date lastUpdate;
}
