<%@page import="java.util.*" %>
<%@page import="com.myapp.entity.*"%>
<nav class="navbar navbar-expand-lg navbar-light bg-light">
  <div class="container">
  	<a class="navbar-brand" href="index.jsp">Cart App</a>
	  <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
	    <span class="navbar-toggler-icon"></span>
	  </button>
	
	  <div class="collapse navbar-collapse" id="navbarSupportedContent">
	    <ul class="navbar-nav ml-auto">
	      <li class="nav-item active">
	        <a class="nav-link" href="index.jsp">Home</a>
	      </li>
	      <li class="nav-item">
	        <a class="nav-link" href="cart.jsp">
	        	Cart
		        <span class="badge badge-danger px-1">
		        	<%= cart_list == null ? 0 : cart_list.size() %>
		        </span>
	        </a>
	      </li>
	      <%if(auth != null){%>
	    	  <li class="nav-item">
		        <a class="nav-link" href="orders.jsp">Orders</a>
		      </li>
		      <li class="nav-item">
		        <a class="nav-link" href="log-out">Logout</a>
		      </li>
	      <% }else{ %>
	    	  <li class="nav-item">
		        <a class="nav-link" href="login.jsp">Login</a>
		      </li>
	      <%}
	      %>

	    </ul>
	    <form class="form-inline my-2 my-lg-0">
	      <input class="form-control mr-sm-2" type="search" placeholder="Search" aria-label="Search">
	      <button class="btn btn-outline-success my-2 my-sm-0" type="submit">Search</button>
	    </form>
	  </div>
  </div>
</nav>
