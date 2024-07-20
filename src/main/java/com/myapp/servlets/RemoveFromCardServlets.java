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
 * Servlet implementation class RemoveFromCardServlets
 */
//@WebServlet("/remove-from-card")
public class RemoveFromCardServlets extends HttpServlet {
	private static final long serialVersionUID = 1L;


	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		try(PrintWriter out = response.getWriter()){
			String id = request.getParameter("id");
//			out.print("Deleted ID: " + id);
			if(id != null) {
				ArrayList<Cart> cart_list = (ArrayList<Cart>)request.getSession().getAttribute("cart-list");
				if(cart_list != null) {
					for(Cart cart : cart_list) {
						if(cart.getId() == Integer.parseInt(id)) {
							cart_list.remove(cart_list.indexOf(cart));
							break;
						}
					}
					response.sendRedirect("cart.jsp");
				}
				
			} else {
				response.sendRedirect("cart.jsp");
			}
			
		}
	}

}
