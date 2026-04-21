package com.valore.dto;

import lombok.Data;
import java.util.List;

@Data
public class CheckoutRequest {
    private List<Long> productIds;
    private String successUrl;
    private String cancelUrl;
}
