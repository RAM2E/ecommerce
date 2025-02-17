package com.ecommerce.orderservice.service.impl;

import com.ecommerce.orderservice.dto.*;
import com.ecommerce.orderservice.exception.OrderNotFoundException;
import com.ecommerce.orderservice.model.*;
import com.ecommerce.orderservice.repository.OrderRepository;
import com.ecommerce.orderservice.service.OrderService;
import com.ecommerce.orderservice.client.CartServiceClient;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
 
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

	private final OrderRepository orderRepository;
	private final CartServiceClient cartServiceClient;

	public OrderServiceImpl(OrderRepository orderRepository, CartServiceClient cartServiceClient) {
		this.orderRepository = orderRepository;
		this.cartServiceClient = cartServiceClient;
	}

	@Value("${stripe.secret.key}")
	private String stripeSecretKey;

	
	private String currency = "usd";

	private static final double DELIVERY_CHARGE = 12.00;

	@Override
	public OrderResponse placeOrder(Long userId, OrderRequest request) {
	    Order order = createOrderEntity(userId, request);
	    // Set payment method directly since it's not coming from the frontend
	    order.setPaymentMethod("COD");
	    order.setStatus("Order Placed");

	    Order savedOrder = orderRepository.save(order);

	    // If needed, clear the cart
	    cartServiceClient.clearCart(userId);

	    return mapToResponse(savedOrder);
	}


	@Override
	public String createStripePaymentSession(Long userId, OrderRequest request, String origin) {
	    Order order = createOrderEntity(userId, request);
	    order.setPaymentMethod("STRIPE");
	    order.setStatus("Payment Pending");
	    Order savedOrder = orderRepository.save(order);

	    return createStripeSession(savedOrder, origin);
	}


	@Override
	public void verifyStripePayment(Long orderId, boolean success) {
		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new OrderNotFoundException("Order not found"));

		if (success) {
			order.setPayment(true);
			order.setStatus("Payment Confirmed");
			order.setStatus("Order Placed");
			cartServiceClient.clearCart(order.getUserId());
		} else {
			order.setStatus("Payment Failed");
			orderRepository.delete(order);
		}
	}

	@Override
	public List<OrderResponse> getUserOrders(Long userId) {
		return orderRepository.findByUserId(userId).stream().map(this::mapToResponse).collect(Collectors.toList());
	}

	@Override
	public List<OrderResponse> getAllOrders() {
		return orderRepository.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());
	}

	@Override
	public void updateOrderStatus(Long orderId, String status) {
	    Order order = orderRepository.findById(orderId)
	            .orElseThrow(() -> new OrderNotFoundException("Order not found"));
	    order.setStatus(status);
	    orderRepository.save(order);
	}
	private Order createOrderEntity(Long userId, OrderRequest request) {
		Order order = new Order();
		order.setUserId(userId);
		order.setAmount(request.getAmount());
		order.setAddress(mapAddress(request.getAddress()));
		order.setItems(mapOrderItems(request.getItems(), order));
		order.setPayment(false);
		return order;
	}

	private Address mapAddress(AddressDto addressDto) {
		Address address = new Address();
		address.setStreet(addressDto.getStreet());
		address.setCity(addressDto.getCity());
		address.setState(addressDto.getState());
		address.setZipcode(addressDto.getZipcode());
		address.setCountry(addressDto.getCountry());
		return address;
	}

	private List<OrderItem> mapOrderItems(List<OrderItemDto> itemDtos, Order order) {
		return itemDtos.stream().map(dto -> {
			OrderItem item = new OrderItem();
			item.setProductId(dto.getProductId());
			item.setName(dto.getName());
			item.setPrice(dto.getPrice());
			item.setQuantity(dto.getQuantity());
			item.setSize(dto.getSize());
			item.setImage(dto.getImage());
			item.setOrder(order);
			return item;
		}).collect(Collectors.toList());
	}

	// Updated mapToResponse method
	private OrderResponse mapToResponse(Order order) {
		return new OrderResponse(order.getId(), order.getUserId(), mapToItemDtos(order.getItems()), order.getAmount(),
				mapToAddressDto(order.getAddress()), order.getStatus(), order.getPaymentMethod(), order.getPayment(),
				order.getCreatedAt());
	}

	private List<OrderItemDto> mapToItemDtos(List<OrderItem> items) {
		return items.stream().map(item -> new OrderItemDto(item.getProductId(), item.getName(), item.getPrice(),
				item.getQuantity(), item.getSize(),item.getImage())).collect(Collectors.toList());
	}

	// Updated mapToAddressDto method
	private AddressDto mapToAddressDto(Address address) {
		return new AddressDto(address.getStreet(), address.getCity(), address.getState(), address.getZipcode(),
				address.getCountry());
	}

	

	private String createStripeSession(Order order, String origin) {
	    Stripe.apiKey = stripeSecretKey;

	    try {
	        List<SessionCreateParams.LineItem> lineItems = new ArrayList<>();

	        // Add order items
	        for (OrderItem item : order.getItems()) {
	            SessionCreateParams.LineItem.PriceData priceData = SessionCreateParams.LineItem.PriceData.builder()
	                .setCurrency(currency)
	                .setUnitAmount((long)(item.getPrice() * 100))
	                .setProductData(
	                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
	                        .setName(item.getName())
	                        .build()
	                )
	                .build();

	            SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder()
	                .setPriceData(priceData)
	                .setQuantity((long)item.getQuantity())
	                .build();

	            lineItems.add(lineItem);
	        }

	        // Add delivery charge
	        SessionCreateParams.LineItem.PriceData deliveryPriceData = SessionCreateParams.LineItem.PriceData.builder()
	            .setCurrency(currency)
	            .setUnitAmount((long)(DELIVERY_CHARGE * 100))
	            .setProductData(
	                SessionCreateParams.LineItem.PriceData.ProductData.builder()
	                    .setName("Delivery Charge")
	                    .build()
	            )
	            .build();

	        lineItems.add(
	            SessionCreateParams.LineItem.builder()
	                .setPriceData(deliveryPriceData)
	                .setQuantity(1L)
	                .build()
	        );

	        // Create Stripe Session with required parameters
	        SessionCreateParams params = SessionCreateParams.builder()
	            .setMode(SessionCreateParams.Mode.PAYMENT)
	            .setSuccessUrl(origin + "/verify?success=true&orderId=" + order.getId())
	            .setCancelUrl(origin + "/verify?success=false&orderId=" + order.getId())
	            .addAllLineItem(lineItems)
	            .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD) // Fixed method
	            .setClientReferenceId(order.getId().toString())
	            .build();

	        Session session = Session.create(params);
	        return session.getUrl();
	    } catch (StripeException e) {
	        throw new RuntimeException("Error creating Stripe session: " + e.getMessage(), e);
	    }
	}


}