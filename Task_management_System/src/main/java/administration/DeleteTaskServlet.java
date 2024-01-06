package administration;

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

@WebServlet("/deleteTask")
public class DeleteTaskServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String DELETE_TASK_QUERY = "DELETE FROM tasks WHERE employee_id = ?";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get the task ID from the request parameter
        int employee_id = Integer.parseInt(request.getParameter("employee_id"));

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException cnf) {
            cnf.printStackTrace();
        }

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/tms", "root", "1234");
             PreparedStatement deleteTaskPS = con.prepareStatement(DELETE_TASK_QUERY)) {
            // Set the task ID parameter for the DELETE query
            deleteTaskPS.setInt(1, employee_id);

            // Execute the DELETE query
            int rowsDeleted = deleteTaskPS.executeUpdate();

            if (rowsDeleted > 0) {
                // Deletion successful
            	 request.getRequestDispatcher("homeStart.html").include(request, response);
                response.sendRedirect("displayTasks");
            } else {
                // Task not found or deletion failed
            	 request.getRequestDispatcher("homeStart.html").include(request, response);
                response.getWriter().println("Task not found or deletion failed.");
            }

        } catch (SQLException se) {
            se.printStackTrace();
            // Handle the exception as needed
            response.getWriter().println("<h1>" + se.getMessage() + "</h2>");
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception as needed
            response.getWriter().println("<h1>" + e.getMessage() + "</h2>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        doGet(req, res);
    }
}
