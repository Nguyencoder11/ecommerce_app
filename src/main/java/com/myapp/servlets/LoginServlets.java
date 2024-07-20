package com.myapp.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import com.myapp.connection.DBCon;
import com.myapp.dao.UserDao;
import com.myapp.entity.User;

/**
 * Servlet implementation class LoginServlets
 */
//@WebServlet("/user-login")
public class LoginServlets extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlets() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendRedirect("login.jsp");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		try(PrintWriter out = response.getWriter()) {
			//out.print("this is login servlet");
			
			String email = request.getParameter("login-email");
			String password = request.getParameter("login-password");
			
			try {
				UserDao uDao = new UserDao(DBCon.getConnection());
				User user = uDao.userLogin(email, password);
				
				if(user != null) {
					//out.print("Login successfully");
					request.getSession().setAttribute("auth", user);
					response.sendRedirect("index.jsp");
				}else {
					out.print("Login failed");
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
			//out.print(email + " " + password);
		}
	}

}
