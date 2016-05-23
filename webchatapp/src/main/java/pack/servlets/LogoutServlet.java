package pack.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * Created by ASUS on 15.05.2016.
 */
@WebServlet(value= "/chat/logout")
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) /*throws ServletException, IOException*/ {
       try {
           Cookie[] cookies = ((HttpServletRequest) req).getCookies();
           for (Cookie cookie : cookies) {
               if (cookie.getName().equals(Authentication.COOKIE_USER_ID)) {
                   cookie.setValue("");
               }
           }
           getServletContext().getRequestDispatcher("/pages/login.jsp").forward(req, resp);
           //resp.sendRedirect("/pages/login.jsp");
       } catch (ServletException se)
       {
           se.printStackTrace();
           System.out.println(se.toString());
       }
        catch (IOException ex){
            System.out.println(ex.toString());
            ex.printStackTrace();
        }
    }
}
