package pack.servlets;

import pack.utils.StaticKeyStorage;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
/**
 * Created by ASUS on 15.05.2016.
 */
@WebServlet(value= "/chat/getUsername")
public class UsernameServlet extends HttpServlet{
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BufferedReader br = req.getReader();
        String uid = br.readLine();
        uid = uid.substring(1, uid.length()-1);
        String username = StaticKeyStorage.getUserByUid(uid);
        resp.setHeader("login", username);
    }
}
