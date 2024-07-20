<%@page import="java.text.DecimalFormat"%>
<%@page import="com.myapp.connection.DBCon"%>
<%@page import="com.myapp.dao.ProductDao"%>
<%@page import="java.util.*"%>
<%@page import="com.myapp.entity.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%
    DecimalFormat dcf = new DecimalFormat("#.##");
    request.setAttribute("dcf", dcf);
    User auth = (User) request.getSession().getAttribute("auth");
    if(auth != null){
    	response.sendRedirect("index.jsp");
    }
    
    ArrayList<Cart> cart_list = (ArrayList<Cart>) session.getAttribute("cart-list");
    List<Cart> cartProducts = null;
    double total = 0.0;
    
    if(cart_list != null){
    	ProductDao pdao = new ProductDao(DBCon.getConnection());
    	cartProducts = pdao.getCartProducts(cart_list);
    	total = pdao.getTotalCartPrice(cart_list);
    	request.setAttribute("cart-list", cart_list);
    	request.setAttribute("total", total);
    	
    }
    %>
<!DOCTYPE html>
<html>
<head>
<%@include file="includes/head.jsp" %>
<title>Cart Page</title>
<style>
.table tbody td {
	vertical-align: middle;
}

.btn-incre, .btn-decre {
	box-shadow: none;
	font-size: 25px;
}
</style>
</head>
<body>
<%@include file="includes/navbar.jsp" %>

<div class="container">
<div class="d-flex py-3">
<h3>Total Price: $ ${ (total>0)?dcf.format(total):0 }</h3>
<a class="mx-3 btn btn-primary" href="cart-check-out">Check Outs</a></div>
<table class="table table-loght">
<thead>
	<tr>
		<th scope="col">Name</th>
		<th scope="col">Category</th>
		<th scope="col">Price</th>
		<th scope="col">Buy Now</th>
		<th scope="col">Cancel</th>
	</tr>
</thead>
<tbody>
	<%
	if(cart_list != null){
		for(Cart c : cartProducts){%>
			<tr>
			<td><%= c.getName() %></td>
			<td><%= c.getCategory() %></td>
			<td><%= dcf.format(c.getPrice()) %></td>
			<td>
				<form action="order-now" method="post" class="form-inline">
					<input type="hidden" name="id" value="<%= c.getId() %>" class="form-input">
					<div class="form-group d-flex justify-content-between w-50">
						<a class="btn btn-sm btn decre" href="quantity-inc-des?action=dec&id=<%= c.getId() %>"><i class="fas fa-minus-square"></i></a>
						<input type="text" name="quantity" class="form-control w-50" value="<%= c.getQuantity() %>" readonly>
						<a class="btn btn-sm btn incre" href="quantity-inc-des?action=inc&id=<%= c.getId() %>"><i class="fas fa-plus-square"></i></a>
						
					</div>
					<button type="submit" class="btn btn-sm btn-primary">Buy</button>
				</form>
			</td>
			<td>
				<a class="btn btn-sm btn-danger" href="remove-from-card?id=<%= c.getId() %>">Remove</a>
			</td>
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