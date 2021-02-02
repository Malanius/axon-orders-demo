package cz.malanius.axondemo.order;

import cz.malanius.axondemo.order.events.OrderConfirmedEvent;
import cz.malanius.axondemo.order.events.OrderPlacedEvent;
import cz.malanius.axondemo.order.events.OrderShippedEvent;
import cz.malanius.axondemo.order.query.FindAllOrderedProductsQuery;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class OrderedProductsService {

    private final Map<String, OrderedProduct> orderedProducts = new HashMap<>();

    @EventHandler
    public void on(OrderPlacedEvent event) {
        log.info("Handling order placed event: {}", event);
        orderedProducts.put(event.getOrderId(), new OrderedProduct(event.getOrderId(), event.getProduct()));
    }

    @EventHandler
    public void on(OrderConfirmedEvent event) {
        log.info("Handling order confirmed event: {}", event);
        orderedProducts.computeIfPresent(event.getOrderId(), (orderId, orderedProduct) -> {
            orderedProduct.setOrderConfirmed();
            return orderedProduct;
        });
    }

    @EventHandler
    public void on(OrderShippedEvent event) {
        log.info("Handling order shipped event: {}", event);
        orderedProducts.computeIfPresent(event.getOrderId(), (orderId, orderedProduct) -> {
            orderedProduct.setOrderShipped();
            return orderedProduct;
        });
    }

    @QueryHandler
    public List<OrderedProduct> handle(FindAllOrderedProductsQuery query) {
        return new ArrayList<>(orderedProducts.values());
    }
}
