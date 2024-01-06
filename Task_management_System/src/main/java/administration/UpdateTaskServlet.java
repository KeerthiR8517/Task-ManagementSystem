package administration;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/updateTask")
public class UpdateTaskServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String updateTaskQuery = "UPDATE tasks SET task_description = ?, start_date_time = ?, end_date_time = ? WHERE id = ?";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/html");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        
        catch (ClassNotFoundException cnf) {
            cnf.printStackTrace();
        }

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/tms", "root", "1234")) {
            // Retrieve form parameters
            int taskId = Integer.parseInt(request.getParameter("taskId"));
            String taskDescription = request.getParameter("taskDescription");
            LocalDateTime startDateTime = LocalDateTime.parse(request.getParameter("startDateTime"));
            LocalDateTime endDateTime = LocalDateTime.parse(request.getParameter("endDateTime"));

            // Prepare and execute SQL query to update task details
            try (PreparedStatement updateTaskPS = con.prepareStatement(updateTaskQuery)) {
                updateTaskPS.setString(1, taskDescription);
                updateTaskPS.setTimestamp(2, Timestamp.valueOf(startDateTime));
                updateTaskPS.setTimestamp(3, Timestamp.valueOf(endDateTime));
                updateTaskPS.setInt(4, taskId);

                // Execute the update query
                int rowsAffected = updateTaskPS.executeUpdate();

                out.println("<html>");
                out.println("<head>");
                out.println("<title>Update Task Result</title>");
                // Add your styles and other head content here
                out.println("</head>");
                out.println("<body>");

                out.println("<div class='container'>");
               

                if (rowsAffected > 0) {
                	request.getRequestDispatcher("homeStart.html").include(request, response);
                    out.println("<p>Task updated successfully!</p>");
                } else {
                	request.getRequestDispatcher("homeStart.html").include(request, response);
                    out.println("<p>Failed to update task. Please try again.</p>");
                }

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
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        // Redirect to the main task display page after updating
        res.sendRedirect(req.getContextPath() + "/displayTasks");
    }
}
