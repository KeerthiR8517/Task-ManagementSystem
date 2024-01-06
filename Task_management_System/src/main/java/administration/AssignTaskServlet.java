package administration;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/AssignTaskServlet")
public class AssignTaskServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String query = "INSERT INTO tasks (employee_id, task_description, start_date_time, end_date_time) VALUES (?, ?, ?, ?)";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter pw = response.getWriter();
        response.setContentType("text/html");

        String employeeId = request.getParameter("employee");
        String taskDescription = request.getParameter("task");
        String startDateTime = request.getParameter("startDateTime");
        String endDateTime = request.getParameter("endDateTime");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException cnf) {
            cnf.printStackTrace();
        }

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tms", "root", "1234");
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, employeeId);
            preparedStatement.setString(2, taskDescription);
            preparedStatement.setString(3, startDateTime);
            preparedStatement.setString(4, endDateTime);

            int count = preparedStatement.executeUpdate();

            if (count == 1) {
                request.getRequestDispatcher("homeStart.html").include(request, response);
                pw.println("<div class='container'>");
                pw.println("<h2>Task assigned successfully!</h2>");
            } else {
                request.getRequestDispatcher("homeStart.html").include(request, response);
                pw.println("<h2>Task assignment failed. Please try again.</h2>");
            }

        } catch (SQLException se) {
            se.printStackTrace();
            pw.println("<h1>" + se.getMessage() + "</h2>");
        } catch (Exception e) {
            e.printStackTrace();
            pw.println("<h1>" + e.getMessage() + "</h2>");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Handle GET requests if needed
    }
}
