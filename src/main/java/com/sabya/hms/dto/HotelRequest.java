package com.sabya.hms.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HotelRequest {
    private String name;
    private String address;
}
