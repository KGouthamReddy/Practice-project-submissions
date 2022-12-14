package com.samples.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/searchServlet")
public class SearchServlet extends HttpServlet {
	
	Connection connection; 
	
	@Override
	public void init(ServletConfig config) throws ServletException {

		try {
			System.out.println("AddServlet init");
			ServletContext context = config.getServletContext();
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(context.getInitParameter("dburl"),
					context.getInitParameter("dbuser"), context.getInitParameter("dbpassword"));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		String bloc = request.getParameter("bloc");
		String dloc = request.getParameter("dloc");

		try (PreparedStatement statement = connection.prepareStatement("select * from flights where boardinglocation = ? and destinationlocation = ?")) {

			statement.setString(1, bloc);
			statement.setString(2, dloc);
			ResultSet rows = statement.executeQuery();
			PrintWriter out = response.getWriter();
			out.println("<table>");
			out.println("<tr>");
			out.print("<h2>Details of the Flights</h2>");
			out.println("<th>Fligh No. &nbsp; &nbsp;</th>");
			out.println("<th>Airline_Name&nbsp; &nbsp;</th>");
			out.println("<th>Sorce &nbsp; &nbsp;</th>");
			out.println("<th>Destination&nbsp; &nbsp;</th>");
			out.println("<th>Price</th>");
			out.println("</tr>");
			while (rows.next()) {
				out.println("<tr>");
				out.println("<td>" + rows.getString(1) + "</td>");
				out.println("<td>" + rows.getString(2) + "</td>");
				out.println("<td>" + rows.getString(3) + "</td>");
				out.println("<td>" + rows.getString(4) + "</td>");
				out.println("<td>" + rows.getString(5) + "</td>");
				out.println("</tr>");
				out.println("<br>");
			}
			out.println("</table>");
			PrintWriter pw = response.getWriter();

			
			pw.write("<p><a href=\"confirmflights.jsp\">Book Flight</a></p>");
			pw.write("<p><a href=\"homepage.html\">Home Page</a></p>");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void destroy() {
		try {
			System.out.println("DeleteServlet.destroy() method. DB connection closed");
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}