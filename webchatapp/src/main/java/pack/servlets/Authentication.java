package pack.servlets;

/**
 * Created by ASUS on 01.05.2016.
 */
import pack.models.LoginStorage;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;


@WebServlet(value= "/chat")
public class Authentication extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");
        String password = req.getParameter("pass");
        try {
            String pass_hash = encryptPassword(password);
            LoginStorage loginStorage = new LoginStorage();
            loginStorage.loadAccounts();
            if (loginStorage.findAccount(login, pass_hash)){
                RequestDispatcher rd=getServletContext().getRequestDispatcher("/pages/homepage.html");
                rd.forward(req,resp);
            }
            else {
                RequestDispatcher rd=getServletContext().getRequestDispatcher("/pages/login.jsp");
                req.setAttribute("errorMsg", "Incorrect login or password");
                rd.forward(req,resp);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch(UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private static String encryptPassword(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String sha1;
        MessageDigest crypt = MessageDigest.getInstance("SHA-1");
        crypt.reset();
        crypt.update(password.getBytes("UTF-8"));
        Formatter formatter = new Formatter();
        for (byte b : crypt.digest()) {
            formatter.format("%02x", b);
        }
        sha1 = formatter.toString();
        formatter.close();
        return sha1;
    }
}
