package com.myapp.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.text.SimpleDateFormat;

import com.myapp.connection.DBCon;
import com.myapp.dao.OrderDao;
import com.myapp.entity.Cart;
import com.myapp.entity.Order;
import com.myapp.entity.User;

/**
 * Servlet implementation class OrderNowServlets
 */
//@WebServlet("/order-now")
public class OrderNowServlets extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		try(PrintWriter out = response.getWriter()){
			
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
			
			User auth = (User)request.getSession().getAttribute("auth");
			if(auth != null) {
				String productId = request.getParameter("id");
				int productQuantity = Integer.parseInt(request.getParameter("quantity"));
				
				if(productQuantity<=0) {
					productQuantity = 1;
				}
				
				Order oderModelOrder = new Order();
				oderModelOrder.setId(Integer.parseInt(productId));
				oderModelOrder.setUid(auth.getId());
				oderModelOrder.setQuantity(productQuantity);
				oderModelOrder.setDate(formatter.format(date));
				
				OrderDao orderDao = new OrderDao(DBCon.getConnection());
				boolean result = orderDao.insertOrder(oderModelOrder);
				
				if(result) {
					ArrayList<Cart> cart_list = (ArrayList<Cart>)request.getSession().getAttribute("cart-list");
					if(cart_list != null) {
						for(Cart cart : cart_list) {
							if(cart.getId() == Integer.parseInt(productId)) {
								cart_list.remove(cart_list.indexOf(cart));
								break;
							}
						}
					}
					response.sendRedirect("orders.jsp");
				}else {
					out.print("order failed");
				}
			}else{
				response.sendRedirect("login.jsp");
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

}
