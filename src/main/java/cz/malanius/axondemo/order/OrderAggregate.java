package cz.malanius.axondemo.order;

import cz.malanius.axondemo.order.commands.ConfirmOrderCommand;
import cz.malanius.axondemo.order.commands.PlaceOrderCommand;
import cz.malanius.axondemo.order.commands.ShipOrderCommand;
import cz.malanius.axondemo.order.events.OrderConfirmedEvent;
import cz.malanius.axondemo.order.events.OrderPlacedEvent;
import cz.malanius.axondemo.order.events.OrderShippedEvent;
import cz.malanius.axondemo.order.exceptions.UnconfirmedOrderException;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Aggregate
@NoArgsConstructor // Default constructor required by Axon framework
public class OrderAggregate {

    @AggregateIdentifier
    private String orderId;
    private boolean orderConfirmed;

    @CommandHandler
    public OrderAggregate(PlaceOrderCommand command) {
        apply(OrderPlacedEvent.builder()
                .orderId(command.getOrderId())
                .product(command.getProduct())
                .build()
        );
    }

    @EventSourcingHandler
    public void on(OrderPlacedEvent event) {
        orderId = event.getOrderId();
        orderConfirmed = false;
    }

    @CommandHandler
    public void handle(ConfirmOrderCommand command) {
        apply(OrderConfirmedEvent.builder().orderId(orderId).build());
    }

    @EventSourcingHandler
    public void on(OrderConfirmedEvent event) {
        orderConfirmed = true;
    }

    @CommandHandler
    public void handle(ShipOrderCommand command) {
        if (!orderConfirmed) {
            throw new UnconfirmedOrderException();
        }
        apply(OrderShippedEvent.builder().orderId(orderId).build());
    }

}
