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

@WebServlet("/editTask")
public class EditTaskServlet extends HttpServlet {
	    private static final long serialVersionUID = 1L;
	    private static final String taskDataQuery = "SELECT id, employee_id, task_description, start_date_time, end_date_time FROM tasks WHERE employee_id = ?";

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

	        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/tms", "root", "1234")) {
	            int employee_id = Integer.parseInt(request.getParameter("employee_id"));
	            try (PreparedStatement taskDataPS = con.prepareStatement(taskDataQuery)) {
	                taskDataPS.setInt(1,employee_id);
	                ResultSet taskDataRS = taskDataPS.executeQuery();

	                request.getRequestDispatcher("homeStart.html").include(request, response);

	                out.println("<html>");
	                out.println("<head>");
	                out.println("<title>Edit Task</title>");
	                // Add your styles and other head content here
	                out.println("</head>");
	                out.println("<body>");

	                out.println("<div class='container'>");
	                out.println("<h2>Edit Task</h2>");

	                out.println("<form action='updateTask' method='post'>");
	                out.println("<table>");

	                if (taskDataRS.next()) {
	                    out.println("<tr>");
	                    out.println("<td><label for='taskId'>Task ID:</label></td>");
	                    out.println("<td><input type='text' id='taskId' name='taskId' value='" + taskDataRS.getInt("id") + "' readonly></td>");
	                    out.println("</tr>");

	                    out.println("<tr>");
	                    out.println("<td><label for='employeeId'>Employee ID:</label></td>");
	                    out.println("<td><input type='text' id='employeeId' name='employeeId' value='" + taskDataRS.getInt("employee_id") + "' readonly></td>");
	                    out.println("</tr>");

	                    out.println("<tr>");
	                    out.println("<td><label for='taskDescription'>Task Description:</label></td>");
	                    out.println("<td><textarea id='taskDescription' name='taskDescription' rows='4'>" + taskDataRS.getString("task_description") + "</textarea></td>");
	                    out.println("</tr>");

	                    out.println("<tr>");
	                    out.println("<td><label for='startDateTime'>Start Date Time:</label></td>");
	                    out.println("<td><input type='datetime-local' id='startDateTime' name='startDateTime' value='" + taskDataRS.getTimestamp("start_date_time").toLocalDateTime() + "' required></td>");
	                    out.println("</tr>");

	                    out.println("<tr>");
	                    out.println("<td><label for='endDateTime'>End Date Time:</label></td>");
	                    out.println("<td><input type='datetime-local' id='endDateTime' name='endDateTime' value='" + taskDataRS.getTimestamp("end_date_time").toLocalDateTime() + "' required></td>");
	                    out.println("</tr>");

	                    out.println("</table>");

	                    out.println("<button type='submit'>Update Task</button>");
	                } else {
	                    out.println("<p>Task not found!</p>");
	                }

	                out.println("</form>");
	                out.println("</div>");

	                out.println("</body>");
	                out.println("</html>");
	            }
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
