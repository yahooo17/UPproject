package pack.servlets;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

import pack.models.Account;
import pack.models.LoginStorage;
import pack.utils.StaticKeyStorage;

/**
 * Created by ASUS on 15.05.2016.
 */

@WebServlet(value = "/login", initParams = {
        @WebInitParam(name = "cookie-live-time", value = "300")
})
public class Authentication extends HttpServlet{
    public static final String COOKIE_USER_ID = "uid";

    private int cookieLifeTime = -1;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        cookieLifeTime = Integer.parseInt(config.getInitParameter("cookie-live-time"));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("login");
        String password = req.getParameter("pass");
        try {
            password = encryptPassword(password);
            LoginStorage loginStorage = new LoginStorage();
            loginStorage.loadAccounts();
            //System.out.println(username+" "+password);
            if (loginStorage.findAccount(username,password)) {
                String userId = StaticKeyStorage.getByUsername(username);
                Cookie userIdCookie = new Cookie(COOKIE_USER_ID, userId);
                userIdCookie.setMaxAge(cookieLifeTime);
                resp.addCookie(userIdCookie);
                getServletContext().getRequestDispatcher("/chatServlet").forward(req, resp);
            } else {
                req.setAttribute("errorMsg", "Incorrect login or password");
                getServletContext().getRequestDispatcher("/AuthServlet").forward(req, resp);
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
