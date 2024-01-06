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

@WebServlet("/displayTasks")
public class DisplayTasksServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String taskDataQuery = "SELECT id, employee_id, task_description, start_date_time, end_date_time FROM tasks";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/html");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException cnf) {
            cnf.printStackTrace();
        }

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/tms", "root", "1234");
             PreparedStatement taskDataPS = con.prepareStatement(taskDataQuery)) {
            ResultSet taskDataRS = taskDataPS.executeQuery();

            out.println("<table align='center'>");
            out.println("<th><h1>All Tasks</h1></th>");
            out.println("</table>");
            out.println("<table border='1' align='center'>");
            out.println("<tr>");
            out.println("<th>Task ID</th>");
            out.println("<th>Employee ID</th>");
            out.println("<th>Task Description</th>");
            out.println("<th>Start Date Time</th>");
            out.println("<th>End Date Time</th>");
            out.println("<th>Edit</th>");
            out.println("<th>Delete</th>");
            out.println("</tr>");

            while (taskDataRS.next()) {
                out.println("<tr>");
                out.println("<td>" + taskDataRS.getInt("id") + "</td>");
                out.println("<td>" + taskDataRS.getInt("employee_id") + "</td>");
                out.println("<td>" + taskDataRS.getString("task_description") + "</td>");
                out.println("<td>" + taskDataRS.getTimestamp("start_date_time") + "</td>");
                out.println("<td>" + taskDataRS.getTimestamp("end_date_time") + "</td>");
                out.println("<td><a href='editTask?employee_id=" + taskDataRS.getInt("employee_id") + "'style='color:#469;'>Edit</a></td>");
                out.println("<td><a href='deleteTask?employee_id=" + taskDataRS.getInt("employee_id") + "' style='color:#469;'>Delete</a></td>");
                out.println("</tr>");
            }
           out.println("</table>");

        } catch (SQLException se) {
            se.printStackTrace();
            out.println("<h1>" + se.getMessage() + "</h2>");
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<h1>" + e.getMessage() + "</h2>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        doGet(req, res);
    }
}
