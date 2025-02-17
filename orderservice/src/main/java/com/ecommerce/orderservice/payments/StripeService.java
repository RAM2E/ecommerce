package com.ecommerce.orderservice.payments;

import com.ecommerce.orderservice.model.Order;
import com.ecommerce.orderservice.model.OrderItem;
import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StripeService {

    
    private final double deliveryCharge = 12.0;

    public StripeService(@Value("${stripe.secret.key}") String apiKey) {
        Stripe.apiKey = apiKey;  // Correct Stripe configuration
    }

	public String createPaymentSession(Order order, String origin) {
        List<SessionCreateParams.LineItem> lineItems = new ArrayList<>();

        for (OrderItem item : order.getItems()) {
            lineItems.add(SessionCreateParams.LineItem.builder()
                    .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                            .setCurrency("usd")
                            .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                    .setName(item.getName())
                                    .build())
                            .setUnitAmount((long) (item.getPrice() * 100))
                            .build())
                    .setQuantity((long) item.getQuantity())
                    .build());
        }

        // Add delivery charge
        lineItems.add(SessionCreateParams.LineItem.builder()
                .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                        .setCurrency("usd")
                        .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                .setName("Delivery Charge")
                                .build())
                        .setUnitAmount((long) (deliveryCharge * 100))
                        .build())
                .setQuantity(1L)
                .build());

        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(origin + "/verify?success=true&orderId=" + order.getId())
                .setCancelUrl(origin + "/verify?success=false&orderId=" + order.getId())
                .addAllLineItem(lineItems)
                .build();

        try {
            Session session = Session.create(params);
            return session.getUrl();
        } catch (Exception e) {
            throw new RuntimeException("Error creating Stripe payment session", e);
        }
    }
}
