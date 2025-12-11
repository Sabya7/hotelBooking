package com.sabya.hms.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GuestRequest {
    private String name;
    private String address;
    private String email;
}
