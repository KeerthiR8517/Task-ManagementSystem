
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/signup")
public class Signup extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve form parameters
        String employeeId = request.getParameter("employee_id");
        String employeeName = request.getParameter("employee_name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // JDBC variables
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            // Load JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish a connection
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tms", "root", "1234");

            // Create SQL query
            String sql = "INSERT INTO employees (employee_id, employee_name, email, password) VALUES (?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(sql);

            // Set parameters
            preparedStatement.setString(1, employeeId);
            preparedStatement.setString(2, employeeName);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, password);

            // Execute query
            preparedStatement.executeUpdate();

            // Close resources
            preparedStatement.close();
            connection.close();

            // Send a response
            response.setContentType("text/html");
            response.setContentType("text/html;charset=UTF-8");
            String popupScript = "<script>alert('Registration successful');"
                                + "window.location.href='Ulogin.html';</script>";
            response.getWriter().write(popupScript);
           

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
