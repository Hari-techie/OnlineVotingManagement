package com.kgisl.votingManagement;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uname = req.getParameter("email");
        String password = req.getParameter("password");
        System.out.println(uname + "" + password);
        RequestDispatcher rd = req.getRequestDispatcher("polling.html");
        rd.forward(req, resp);
    }
}