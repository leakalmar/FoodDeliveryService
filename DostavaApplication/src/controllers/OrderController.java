package controllers;

import static spark.Spark.post;
import static spark.Spark.get;
import static spark.Spark.put;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import beans.Customer;
import beans.Deliverer;
import beans.Order;
import beans.ShoppingCart;
import dto.OrderRequestDTO;
import dto.RequestForDeliveryDTO;
import services.OrderService;
import spark.Session;

public class OrderController {
    private static Gson gs = new GsonBuilder().setDateFormat("dd MMM yyyy").create();
    private static Gson gsTime = new GsonBuilder().setDateFormat("HH:mm'h'").create();

	public OrderController(OrderService orderService) {

        post("/order/create", (req, res) -> {
			res.type("application/json");

			try {
                Session session = req.session();
                ShoppingCart cart = session.attribute("cart");
                Customer customer = session.attribute("user");
                Order order = orderService.createOrder(cart, gs.fromJson(req.body(), String.class));
				customer = orderService.addOrder(customer,order);
				customer = orderService.calculatePoints(customer,cart);
                session.attribute("cart", new ShoppingCart(customer.getUsername()));
                session.attribute("user", customer);
				session.attribute("address","");
				return gs.toJson(order);
			} catch (Exception e) {
				e.printStackTrace();
				return "";
			}
		});

		put("/order/cancel/:orderID", (req, res) -> {
			res.type("application/json");

			try {
                Session session = req.session();
                Customer customer = session.attribute("user");
				orderService.cancelOrder(customer, req.params("orderID"));
				customer = orderService.cancelledOrderPoints(customer, req.params("orderID"));
                session.attribute("user", customer);
				return gs.toJson(customer);
			} catch (Exception e) {
				e.printStackTrace();
				return "";
			}
		});

		get("/order/getWaitingDeliveryOrders",(req, res)->{
			res.type("application/json");

			try {
                Session session = req.session();
                Deliverer deliverer = session.attribute("user");
				List<Order> orders = orderService.getWaitingDeliveryOrders(deliverer);
				return gsTime.toJson(orders);
			} catch (Exception e) {
				e.printStackTrace();
				return "";
			}
		});

		put("/order/setInTransport/:orderID/:username",(req, res)->{
			res.type("application/json");

			try {
                Session session = req.session();
                Deliverer deliverer = session.attribute("user");
				Order order = orderService.setOrderInTransport(deliverer, req.params("orderID"), req.params("username"));
				return gs.toJson(order);
			} catch (Exception e) {
				e.printStackTrace();
				return "";
			}
		});

		get("/order/getDelivererOrders",(req, res)->{
			res.type("application/json");

			try {
                Session session = req.session();
                Deliverer deliverer = session.attribute("user");
				return gs.toJson(deliverer.getOrdersToDeliver());
			} catch (Exception e) {
				e.printStackTrace();
				return "";
			}
		});

		put("/order/confirmDelivery",(req, res)->{
			res.type("application/json");

			try {
                Session session = req.session();
                Deliverer deliverer = session.attribute("user");
				deliverer = orderService.confirmDelivery(deliverer, gs.fromJson(req.body(), Order.class));
				session.attribute("user", deliverer);
				return gs.toJson(deliverer);
			} catch (Exception e) {
				e.printStackTrace();
				return "";
			}
		});

		put("/order/requestOrder",(req, res)->{
			res.type("application/json");

			try {
                Session session = req.session();
                Deliverer deliverer = session.attribute("user");
				orderService.requestOrder(deliverer, gs.fromJson(req.body(), OrderRequestDTO.class));
				return gs.toJson(orderService.getWaitingDeliveryOrders(deliverer));
			} catch (Exception e) {
				e.printStackTrace();
				return "";
			}
		});
		
		get("/order/getPreviousOrdersByRestaurant/:id",(req, res)->{
			res.type("application/json");

			try {
				List<Order> orders = orderService.getAllPreviousOrdersByRestaurant(Integer.parseInt(req.params("id")));
				return gs.toJson(orders);
			} catch (Exception e) {
				e.printStackTrace();
				return "";
			}
		});
		
		get("/order/getCurrentOrdersByRestaurant/:id",(req, res)->{
			res.type("application/json");

			try {
				List<Order> orders = orderService.getAllCurrentOrdersByRestaurant(Integer.parseInt(req.params("id")));
				return gsTime.toJson(orders);
			} catch (Exception e) {
				e.printStackTrace();
				return "";
			}
		});
		
		put("/order/process/:orderID/:username", (req, res) -> {
			res.type("application/json");

			try {
				orderService.processOrder(req.params("orderID"), req.params("username"));
				return gs.toJson("ok");
			} catch (Exception e) {
				e.printStackTrace();
				return "";
			}
		});
		
		put("/order/prepare/:orderID/:username", (req, res) -> {
			res.type("application/json");

			try {
				orderService.prepareOrder(req.params("orderID"), req.params("username"));
				return gs.toJson("ok");
			} catch (Exception e) {
				e.printStackTrace();
				return "";
			}
		});
		
		put("/order/approveDeliveryRequest", (req, res) -> {
			res.type("application/json");

			try {
				orderService.approveDeliveryRequest(gs.fromJson(req.body(), RequestForDeliveryDTO.class));
				return gs.toJson("ok");
			} catch (Exception e) {
				e.printStackTrace();
				return "";
			}
		});
		
		put("/order/rejectDeliveryRequest", (req, res) -> {
			res.type("application/json");

			try {
				orderService.rejectDeliveryRequest(gs.fromJson(req.body(), RequestForDeliveryDTO.class));
				return gs.toJson("ok");
			} catch (Exception e) {
				e.printStackTrace();
				return "";
			}
		});
		
		put("/order/rate/:orderID/:username", (req, res) -> {
			res.type("application/json");

			try {
				Customer customer = orderService.rateOrder(req.params("orderID"), req.params("username"));
				Session session = req.session();
	            session.attribute("user", customer);
				
				return gs.toJson("ok");
			} catch (Exception e) {
				e.printStackTrace();
				return "";
			}
		});
    }
}
