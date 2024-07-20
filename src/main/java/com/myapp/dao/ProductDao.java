package com.myapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

import com.myapp.entity.Cart;
import com.myapp.entity.Product;

public class ProductDao {
	private Connection con;
	private PreparedStatement pst;
	private ResultSet rs;
	
	private String query;

	public ProductDao(Connection con) {
		this.con = con;
	}
	
	public List<Product> getAllProducts(){
		List<Product> products = new ArrayList<Product>();
		
		try {
			query = "select * from products";
			
			pst = this.con.prepareStatement(query);
			rs = pst.executeQuery();
			
			while(rs.next()) {
				Product row = new Product();
				row.setId(rs.getInt("id"));
				row.setName(rs.getString("name"));
				row.setCategory(rs.getString("category"));
				row.setPrice(rs.getDouble("price"));
				row.setImage(rs.getString("image"));
				
				products.add(row);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return products;
	}
	
	public List<Cart> getCartProducts(ArrayList<Cart> cartList){
		List<Cart> productsCarts = new ArrayList<Cart>();
		
		try {
			if(cartList.size()>0) {
				for(Cart itemCart : cartList) {
					query = "select * from products where id=?";
					pst = this.con.prepareStatement(query);
					pst.setInt(1, itemCart.getId());
					rs = pst.executeQuery();
					
					while(rs.next()) {
						Cart row = new Cart();
						row.setId(rs.getInt("id"));
						row.setName(rs.getString("name"));
						row.setCategory(rs.getString("category"));
						row.setPrice(rs.getDouble("price")*itemCart.getQuantity());
						row.setQuantity(itemCart.getQuantity());
						productsCarts.add(row);
					}
				}
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
		
		return productsCarts;
	}
	
	public double getTotalCartPrice(ArrayList<Cart> cartList) {
		double total = 0;
		
		try {
			if(cartList.size()>0) {
				for (Cart item : cartList) {
					query = "select price from products where id=?";
					
					pst = this.con.prepareStatement(query);
					pst.setInt(1, item.getId());
					rs = pst.executeQuery();
					
					while (rs.next()) {
						total += rs.getDouble("price")*item.getQuantity();
						
					}
				}
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return total;
	}
	
	public Product getSingleProduct(int id) {
		Product row = null;
		
		try {
			query = "select * from products where id=?";
			
			pst = this.con.prepareStatement(query);
			pst.setInt(1, id);
			rs = pst.executeQuery();
			
			while (rs.next()) {
				row = new Product();
				row.setId(rs.getInt("id"));
				row.setName(rs.getString("name"));
				row.setCategory(rs.getString("category"));
				row.setPrice(rs.getDouble("price"));
                row.setImage(rs.getString("image"));
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		
		return row;
	}
}
