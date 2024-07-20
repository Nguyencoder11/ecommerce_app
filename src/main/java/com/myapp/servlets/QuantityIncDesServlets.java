package com.myapp.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import com.myapp.entity.Cart;

/**
 * Servlet implementation class QuantityIncDesServlets
 */
//@WebServlet("/quantity-inc-des")
public class QuantityIncDesServlets extends HttpServlet {
	private static final long serialVersionUID = 1L;
    

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		response.setContentType("text/html;charset=UTF-8");
		try(PrintWriter out = response.getWriter()){
			String action = request.getParameter("action");
			int id = Integer.parseInt(request.getParameter("id"));
			
			ArrayList<Cart> cart_list = (ArrayList<Cart>) request.getSession().getAttribute("cart-list");
			
			if(action != null && id>=1) {
				if(action.equals("inc")) {
					for(Cart cart : cart_list) {
						if(cart.getId() == id) {
							int quantity = cart.getQuantity();
							quantity++;
							
							cart.setQuantity(quantity);
							response.sendRedirect("cart.jsp");
						}
					}
				}
				
				if(action.equals("dec")) {
					for(Cart cart : cart_list) {
						if(cart.getId() == id && cart.getQuantity()>1) {
							int quantity = cart.getQuantity();
							quantity--;
							
							cart.setQuantity(quantity);
							break;
						}
					}
					response.sendRedirect("cart.jsp");
				}
			}else{
				response.sendRedirect("cart.jsp");
			}
		}
	}

}
