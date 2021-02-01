package cz.malanius.axondemo.order;

import cz.malanius.axondemo.order.events.OrderConfirmedEvent;
import cz.malanius.axondemo.order.events.OrderPlacedEvent;
import cz.malanius.axondemo.order.events.OrderShippedEvent;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class OrderedProductsService {

    private final Map<String, OrderedProduct> orderedProducts = new HashMap<>();

    @EventHandler
    public void on(OrderPlacedEvent event) {
        orderedProducts.put(event.getOrderId(), new OrderedProduct(event.getOrderId(), event.getProduct()));
    }

    @EventHandler
    public void on(OrderConfirmedEvent event) {
        orderedProducts.computeIfPresent(event.getOrderId(), (orderId, orderedProduct) -> {
            orderedProduct.setOrderConfirmed();
            return orderedProduct;
        });
    }

    @EventHandler
    public void on(OrderShippedEvent event) {
        orderedProducts.computeIfPresent(event.getOrderId(), (orderId, orderedProduct) -> {
            orderedProduct.setOrderShipped();
            return orderedProduct;
        });
    }
}
