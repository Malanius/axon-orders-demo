package cz.malanius.axondemo.order.commands;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@Builder
public class PlaceOrderCommand {

    @TargetAggregateIdentifier
    private final String orderId;
    private final String product;

}
