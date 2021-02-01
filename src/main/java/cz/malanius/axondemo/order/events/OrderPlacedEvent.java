package cz.malanius.axondemo.order.events;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderPlacedEvent {

    private final String orderId;
    private final String product;

}
