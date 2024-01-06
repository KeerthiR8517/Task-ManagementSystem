package Employee;

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

@WebServlet("/viewTaskAssigned")
public class ViewTaskAssignedServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String selectQuery = "SELECT id, task_description, start_date_time, end_date_time, status FROM tasks WHERE employee_id = ?";
    private static final String updateStatusQuery = "UPDATE tasks SET status = ? WHERE id = ?";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/html");

        // Retrieve the employee ID from the request parameter
        String employeeId = request.getParameter("employee_id");

        out.println("<html>");
        out.println("<head>");
        out.println("<title>View Tasks</title>");
        out.println("<header><a href=\"index.html\">Home</a>"
                + "<a href=\"userhome.html\">Back</a></header>");
        out.println(" <style>header{ position: fixed;   top: 0; left: 0;  width: 100%; padding: 10px 120px 5px;  background:#466;  justify-content: space-between;  align-items: center;}</style>");
        out.println(" <style>a{  color:bisque; margin-right: 35px;   text-decoration: none; font-size: 20px;}</style>");
        out.println("<style>body\r\n"
      		+ "          {\r\n"
      		+ "            background-image: url('image/book3.jpeg');\r\n"
      		+ "            background-repeat: no-repeat;\r\n"
      		+ "            background-size: 3000px 1000px;\r\n"
      		+ "           </style> }");
     
        out.println("</head>");
        out.println("<body><br><br><br><br>");

        out.println("<h1>View Tasks Assigned to Employee</h1>");
        out.println("<form action='viewTaskAssigned' method='get'>");
        out.println("Enter Employee ID: <input type='text' name='employee_id'>");
        out.println("<input type='submit' value='View Tasks' style='background-color: #469; color: white; padding: 10px 20px; border: none; border-radius: 5px; cursor: pointer;'>");
        out.println("</form>");

        if (employeeId != null && !employeeId.isEmpty()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException cnf) {
                cnf.printStackTrace();
            }

            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/tms", "root", "1234");
                 PreparedStatement selectStatement = con.prepareStatement(selectQuery)) {

                selectStatement.setString(1, employeeId);

                ResultSet resultSet = selectStatement.executeQuery();
                

                out.println("<table align='center' border='1'>");
                out.println("<th colspan='6'><h2>Tasks Assigned to Employee ID: " + employeeId + "</h2></th>");
                out.println("<tr>");
                out.println("<th>Task ID</th>");
                out.println("<th>Task Description</th>");
                out.println("<th>Start Date Time</th>");
                out.println("<th>End Date Time</th>");
                out.println("<th>Status</th>");

                out.println("</tr>");

                while (resultSet.next()) {
                    out.println("<tr>");
                    out.println("<td>" + resultSet.getInt("id") + "</td>");
                    out.println("<td>" + resultSet.getString("task_description") + "</td>");
                    out.println("<td>" + resultSet.getTimestamp("start_date_time") + "</td>");
                    out.println("<td>" + resultSet.getTimestamp("end_date_time") + "</td>");

                    // Display dropdown menu for status
                    out.println("<td>");
                    out.println("<form action='updateTaskStatus' method='post'>");
                    out.println("<input type='hidden' name='taskId' value='" + resultSet.getInt("id") + "'>");
                    out.println("<select name='status'>");
                    out.println("<option value='' disabled selected>Status </option><option value='Pending' " + (resultSet.getString("status").equals("Pending") ? "selected" : "") + ">Pending</option>");
                    out.println("<option value='Completed' " + (resultSet.getString("status").equals("Completed") ? "selected" : "") + ">Completed</option>");
                    out.println("<option value='process' " + (resultSet.getString("status").equals("proccess") ? "Process" : "") + ">Process</option>");
                    out.println("<br></select><br><br>");
                    out.println("<br><input type='submit' value='Update' style='background-color: #469; color: white; padding: 10px 20px; border: none; border-radius: 5px; cursor: pointer;'>");

                    out.println("</form>");
                    out.println("</td>");

                    out.println("</tr>");
                }
                out.println("</table>");

                // Display update success message if it exists in the request attribute
                String updateMessage = (String) request.getAttribute("updateMessage");
                if (updateMessage != null) {
                    out.println("<p>" + updateMessage + "</p>");
                }

            } catch (SQLException se) {
                se.printStackTrace();
                out.println("<h3>Error: " + se.getMessage() + "</h3>");
            } catch (Exception e) {
                e.printStackTrace();
                out.println("<h3>Error: " + e.getMessage() + "</h3>");
            }
        }

        out.println("</body>");
        out.println("</html>");
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        doGet(req, res);
    }
}
