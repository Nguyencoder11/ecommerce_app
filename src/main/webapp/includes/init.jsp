<%@page import="java.util.*"%>
<%@page import="com.myapp.entity.*"%>

<%
User auth = (User) request.getSession().getAttribute("auth");
ArrayList<Cart> cart_list = (ArrayList<Cart>) session.getAttribute("cart-list");

request.setAttribute("auth", auth);
request.setAttribute("cart_list", cart_list);
%>