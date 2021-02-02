package cz.malanius.axondemo.order;

import cz.malanius.axondemo.order.commands.ConfirmOrderCommand;
import cz.malanius.axondemo.order.commands.PlaceOrderCommand;
import cz.malanius.axondemo.order.commands.ShipOrderCommand;
import cz.malanius.axondemo.order.query.FindAllOrderedProductsQuery;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class OrderEndpoint {

    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;

    @Autowired
    public OrderEndpoint(CommandGateway commandGateway, QueryGateway queryGateway) {
        this.commandGateway = commandGateway;
        this.queryGateway = queryGateway;
    }

    @PostMapping("/ship-order")
    public void shipOrder() {
        String orderId = UUID.randomUUID().toString();
        commandGateway.sendAndWait(PlaceOrderCommand.builder().orderId(orderId).product("Millennium Falcon").build());
        commandGateway.sendAndWait(ConfirmOrderCommand.builder().orderId(orderId).build());
        commandGateway.sendAndWait(ShipOrderCommand.builder().orderId(orderId).build());
    }

    @PostMapping("/ship-unconfirmed-order")
    public void shipUnconfirmedOrder() {
        String orderId = UUID.randomUUID().toString();
        commandGateway.sendAndWait(PlaceOrderCommand.builder().orderId(orderId).product("Tie Fighter").build());
        // This throws an exception, as an Order cannot be shipped if it has not been confirmed yet.
        commandGateway.sendAndWait(ShipOrderCommand.builder().orderId(orderId).build());
    }

    @GetMapping("/all-orders")
    public List<OrderedProduct> findAllOrderedProducts() {
        return queryGateway.query(new FindAllOrderedProductsQuery(), ResponseTypes.multipleInstancesOf(OrderedProduct.class))
                .join();
    }
}
