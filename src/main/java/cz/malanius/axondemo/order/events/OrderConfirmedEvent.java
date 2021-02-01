package cz.malanius.axondemo.order.events;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderConfirmedEvent {

    private final String orderId;

}
