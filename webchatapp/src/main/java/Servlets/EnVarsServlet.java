package Servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(value= "/vars")
public class EnVarsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String[] vars = { "JAVA_HOME", "M2_HOME", "CATALINA_HOME", "PATH", "USERNAME"};
        for (String var : vars) {
            resp.getOutputStream().println(String.format("%s=%s", var, System.getenv(var)));
        }
    }
}