package administration;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/viewAllTasks")
public class ViewEmployeeDetailsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String selectAllTasksQuery =
            "SELECT id,employee_id, task_description, start_date_time, end_date_time, status " +
            "FROM tasks";

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/html");
        request.getRequestDispatcher("homeStart.html").include(request, response);
        out.println("<html>");
        out.println("<head>");
        out.println("<title>View All Tasks</title>");
        out.println("</head>");
        out.println("<body>");

        out.println("<h1>All Task Status</h1>");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException cnf) {
            cnf.printStackTrace();
        }

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/tms", "root", "1234");
             PreparedStatement selectAllTasksStatement = con.prepareStatement(selectAllTasksQuery)) {

            // Execute the query to get all tasks
            ResultSet resultSet = selectAllTasksStatement.executeQuery();

            // Display all tasks
            out.println("<table align='center' border='1'>");
            out.println("<tr>");
            out.println("<th>Task ID</th>");
            out.println("<th>Employee ID</th>");
            out.println("<th>Task Description</th>");
            out.println("<th>Start Date Time</th>");
            out.println("<th>End Date Time</th>");
            out.println("<th>Status</th>");
            out.println("</tr>");

            while (resultSet.next()) {
                out.println("<tr>");
                out.println("<td>" + resultSet.getInt("id") + "</td>");
                out.println("<td>" + resultSet.getInt("employee_id") + "</td>");
                out.println("<td>" + resultSet.getString("task_description") + "</td>");
                out.println("<td>" + resultSet.getTimestamp("start_date_time") + "</td>");
                out.println("<td>" + resultSet.getTimestamp("end_date_time") + "</td>");
                out.println("<td>" + resultSet.getString("status") + "</td>");
                out.println("</tr>");
            }

            out.println("</table>");

        } catch (SQLException se) {
            se.printStackTrace();
            out.println("<h3>Error: " + se.getMessage() + "</h3>");
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<h3>Error: " + e.getMessage() + "</h3>");
        }

        out.println("</body>");
        out.println("</html>");
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        doGet(req, res);
    }
}
