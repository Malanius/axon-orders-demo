package cz.malanius.axondemo.order;

import cz.malanius.axondemo.order.commands.ConfirmOrderCommand;
import cz.malanius.axondemo.order.commands.PlaceOrderCommand;
import cz.malanius.axondemo.order.commands.ShipOrderCommand;
import cz.malanius.axondemo.order.events.OrderConfirmedEvent;
import cz.malanius.axondemo.order.events.OrderPlacedEvent;
import cz.malanius.axondemo.order.events.OrderShippedEvent;
import cz.malanius.axondemo.order.exceptions.UnconfirmedOrderException;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

class OrderAggregateTest {

    private FixtureConfiguration<OrderAggregate> fixture;

    @BeforeEach
    void setUp() {
        fixture = new AggregateTestFixture<>(OrderAggregate.class);
    }

    @Test
    void giveNoPriorActivity_whenPlaceOrderCommand_thenShouldPublishOrderPlacedEvent() {
        String orderId = UUID.randomUUID().toString();
        String product = "Starship";
        fixture.givenNoPriorActivity()
                .when(PlaceOrderCommand.builder().orderId(orderId).product(product).build())
                .expectEvents(OrderPlacedEvent.builder().orderId(orderId).product(product).build());
    }

    @Test
    void givenOrderPlacedEvent_whenConfirmOrderCommand_thenShouldPublishOrderConfirmedEvent() {
        String orderId = UUID.randomUUID().toString();
        String product = "Starship";
        fixture.given(OrderPlacedEvent.builder().orderId(orderId).product(product).build())
                .when(ConfirmOrderCommand.builder().orderId(orderId).build())
                .expectEvents(OrderConfirmedEvent.builder().orderId(orderId).build());
    }

    @Test
    void givenOrderPlacedEvent_whenShipOrderCommand_thenShouldThrowUnconfirmedOrderException() {
        String orderId = UUID.randomUUID().toString();
        String product = "Starship";
        fixture.given(OrderPlacedEvent.builder().orderId(orderId).product(product).build())
                .when(ShipOrderCommand.builder().orderId(orderId).build())
                .expectException(UnconfirmedOrderException.class);
    }

    @Test
    void givenOrderPlacedEventAndOrderConfirmedEvent_whenShipOrderCommand_thenShouldPublishOrderShippedEvent() {
        String orderId = UUID.randomUUID().toString();
        String product = "Starship";
        fixture.given(OrderPlacedEvent.builder().orderId(orderId).product(product).build(),
                OrderConfirmedEvent.builder().orderId(orderId).build())
                .when(ShipOrderCommand.builder().orderId(orderId).build())
                .expectEvents(OrderShippedEvent.builder().orderId(orderId).build());
    }

}
