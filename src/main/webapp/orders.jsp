<%@page import="com.myapp.connection.DBCon"%>
<%@page import="com.myapp.dao.OrderDao"%>
<%@page import="com.myapp.entity.*"%>
<%@page import="java.util.*" %>
<%@page import="java.text.DecimalFormat"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%
    DecimalFormat dcf = new DecimalFormat("#.##");
    request.setAttribute("dcf", dcf);
    User auth = (User) request.getSession().getAttribute("auth");
    List<Order> orders = null;
    
    if(auth != null){
    	request.setAttribute("auth", auth);
    	orders = new OrderDao(DBCon.getConnection()).userOrders(auth.getId());
    }else{
    	response.sendRedirect("login.jsp");
    }
    
    ArrayList<Cart> cart_list = (ArrayList<Cart>) session.getAttribute("cart-list");
    if(cart_list != null){
    	request.setAttribute("cart-list", cart_list);
    }
    %>
<!DOCTYPE html>
<html>
<head>
<%@include file="includes/head.jsp" %>
<title>Orders Page</title>
</head>
<body>
<%@include file="includes/navbar.jsp" %>

<div class="container">
	<div class="card-header my-3">All Orders</div>
	<table class="table table-light">
		<thead>
			<tr>
				<th scope="col">Date</th>
				<th scope="col">Name</th>
				<th scope="col">Category</th>
				<th scope="col">Quantity</th>
				<th scope="col">Price</th>
				<th scope="col">Cancel</th>
			</tr>
			
		</thead>
		<tbody>
			<%
			if(orders != null){
				for(Order o : orders){%>
					<tr>
						<td><%= o.getDate() %></td>
						<td><%= o.getName() %></td>
						<td><%= o.getCategory() %></td>
						<td><%= o.getQuantity() %></td>
						<td><%= dcf.format(o.getPrice()) %></td>
						<td><a class="btn btn-sm btn-danger" href="cancel-order?id=<%= o.getOrderId() %>">Cancel</a></td>
					</tr>
				<%}
			}
			%>
		</tbody>
	</table>
</div>

<%@include file="includes/footer.jsp" %>
</body>
</html>