package gma.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import gma.entities.User;
import gma.objects.Paths;

@WebFilter("/App/*")
public class UserLoginFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (session.isNew() || user == null || user.getType() == 2) {
        	// No logged-in user found, so redirect to login page
            response.sendRedirect(request.getContextPath() + Paths.INDEX_PAGE.getPath()); 
        } else {
        	// Logged-in user found, so just continue request.
            chain.doFilter(req, res); 
        }
        
    }

}