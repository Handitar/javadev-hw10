package org.example;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.ZoneId;

@WebFilter("/time")
public class TimezoneValidateFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String timezone = httpRequest.getParameter("timezone");
        System.out.println("DEBUG: TimezoneValidateFilter invoked.");
        System.out.println("DEBUG: Raw timezone parameter = " + timezone);

        try {
            if (timezone != null && !timezone.isEmpty()) {

                // заміна пробілу на `+` для коректного формату
                if (timezone.contains(" ")) {
                    timezone = timezone.replace(" ", "+");
                    System.out.println("DEBUG: Timezone corrected for space-to-plus = " + timezone);
                }

                // UTC+X на GMT+X
                if (timezone.startsWith("UTC+")) {
                    timezone = timezone.replace("UTC", "GMT");
                } else if (timezone.startsWith("UTC-")) {
                    timezone = timezone.replace("UTC", "GMT");
                }

                System.out.println("DEBUG: Final timezone parameter = " + timezone);

                ZoneId.of(timezone);
            }

            chain.doFilter(request, response);
        } catch (Exception e) {
            System.out.println("DEBUG: Invalid timezone - " + e.getMessage());
            httpResponse.setContentType("text/html");
            httpResponse.setCharacterEncoding("UTF-8");
            httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            try (PrintWriter writer = httpResponse.getWriter()) {
                writer.println("<html>");
                writer.println("<body>");
                writer.println("<h1>Invalid timezone</h1>");
                writer.println("</body>");
                writer.println("</html>");
            }
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}
}