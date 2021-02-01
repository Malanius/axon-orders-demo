package cz.malanius.axondemo.order;

import cz.malanius.axondemo.order.commands.PlaceOrderCommand;
import cz.malanius.axondemo.order.events.OrderPlacedEvent;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
@NoArgsConstructor(access = AccessLevel.PROTECTED) // Default constructor required by Axon framework
public class OrderAggregate {

    @AggregateIdentifier
    private String orderId;
    private boolean orderConfirmed;

    @CommandHandler
    public OrderAggregate(PlaceOrderCommand command) {
        AggregateLifecycle.apply(OrderPlacedEvent.builder()
                .orderId(command.getOrderId())
                .product(command.getProduct())
                .build());
    }

    @EventSourcingHandler
    public void on(OrderPlacedEvent event) {
        this.orderId = event.getOrderId();
        orderConfirmed = false;
    }

}
