package org.example;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@WebServlet("/time")
public class TimeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String timezone = req.getParameter("timezone");
        System.out.println("DEBUG: TimeServlet invoked."); //на випадок проблеми - logs/catalina.out в томкаті
        System.out.println("DEBUG: Received timezone = " + timezone);

        if (timezone != null && timezone.contains(" ")) {
            timezone = timezone.replace(" ", "+");
            System.out.println("DEBUG: Corrected timezone in TimeServlet = " + timezone);
        }

        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");

        try {
            if (timezone == null || timezone.isEmpty()) {
                timezone = "UTC";
            } else if (timezone.startsWith("UTC+")) {
                timezone = timezone.replace("UTC", "GMT");
            } else if (timezone.startsWith("UTC-")) {
                timezone = timezone.replace("UTC", "GMT");
            }

            ZoneId zoneId = ZoneId.of(timezone);
            ZonedDateTime now = ZonedDateTime.now(zoneId);
            String formattedTime = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            try (PrintWriter writer = resp.getWriter()) {
                writer.println("<html>");
                writer.println("<body>");
                writer.println("<h1>Current Time</h1>");
                writer.println("<p>Time: " + formattedTime + " (" + zoneId + ")</p>");
                writer.println("</body>");
                writer.println("</html>");
            }
        } catch (Exception e) {
            System.out.println("DEBUG: Exception in TimeServlet: " + e.getMessage());
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("<html><body><h1>Invalid timezone</h1></body></html>");
        }
    }
}