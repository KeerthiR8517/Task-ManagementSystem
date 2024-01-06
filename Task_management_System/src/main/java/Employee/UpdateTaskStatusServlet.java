package Employee;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/updateTaskStatus")
public class UpdateTaskStatusServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String updateStatusQuery = "UPDATE tasks SET status = ? WHERE id = ?";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Retrieve task ID, new status, and employee ID from the request parameters
        int taskId = Integer.parseInt(request.getParameter("taskId"));
        String newStatus = request.getParameter("status");
        String employeeId = request.getParameter("employeeId");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException cnf) {
            cnf.printStackTrace();
        }

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/tms", "root", "1234");
             PreparedStatement updateStatement = con.prepareStatement(updateStatusQuery)) {

            // Set parameters for the update statement
            updateStatement.setString(1, newStatus);
            updateStatement.setInt(2, taskId);

            // Execute the update statement
            int rowsUpdated = updateStatement.executeUpdate();

            if (rowsUpdated > 0) {
                // Set update success message in the request attribute
                request.getRequestDispatcher("viewTaskAssigned").include(request, response);
                String updateMessage = "Task status updated successfully!";
                request.setAttribute("updateMessage", updateMessage);
            } else {
                // Set update failure message in the request attribute
                String updateMessage = "Failed to update task status!";
                request.setAttribute("updateMessage", updateMessage);
            }

        } catch (SQLException se) {
            se.printStackTrace();
            // Set update failure message in the request attribute
            String updateMessage = "Error: " + se.getMessage();
            request.setAttribute("updateMessage", updateMessage);
        } catch (Exception e) {
            e.printStackTrace();
            // Set update failure message in the request attribute
            String updateMessage = "Error: " + e.getMessage();
            request.setAttribute("updateMessage", updateMessage);
        }

        // Forward back to ViewTaskAssignedServlet with the updated employee ID
        request.setAttribute("employee_id", employeeId);
        request.getRequestDispatcher("viewTaskAssigned").forward(request, response);
    }
}
