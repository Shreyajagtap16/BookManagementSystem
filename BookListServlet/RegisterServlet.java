package com.idiot.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private static final String query = "INSERT INTO BOOKDATA (BOOKNAME, BOOKEDITION, BOOKPRICE) VALUES (?, ?, ?)";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // get PrintWriter
        PrintWriter pw = resp.getWriter();
        // set content type
        resp.setContentType("text/html");
        
        // GET THE book info
        String bookName = req.getParameter("bookName");
        String bookEdition = req.getParameter("bookedition");
        
        float bookPrice;
        String bookPriceParameter = req.getParameter("bookprice");

        if (bookPriceParameter != null && !bookPriceParameter.isEmpty()) {
            try {
                bookPrice = Float.parseFloat(bookPriceParameter);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                pw.println("<h2>Invalid book price format</h2>");
                return;
            }
        } else {
            pw.println("<h2>Book price is required</h2>");
            return;
        }

        // LOAD JDBC DRIVER
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException cnf) {
            cnf.printStackTrace();
            pw.println("<h1>Error loading JDBC driver</h1>");
            return;
        }

        // GENERATE THE CONNECTION
        try (Connection con = DriverManager.getConnection("jdbc:mysql:///book", "root", "shreya@16");
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, bookName);
            ps.setString(2, bookEdition);
            ps.setFloat(3, bookPrice);
            int count = ps.executeUpdate();
            if (count == 1) {
                pw.println("<h2>Record is registered successfully</h2>");
            } else {
                pw.println("<h2>Record is not registered successfully</h2>");
            }
        } catch (SQLException se) {
            se.printStackTrace();
            pw.println("<h1>" + se.getMessage() + "</h1>");
        } catch (Exception e) {
            e.printStackTrace();
            pw.println("<h1>" + e.getMessage() + "</h1>");
        }
        pw.println("<a href='home.html'>Home</a>");
        pw.println("<br>");
        pw.println("<a href='bookList'>Book List</a>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
