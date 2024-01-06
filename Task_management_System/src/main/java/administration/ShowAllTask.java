package administration;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/ShowAllTaskForm")
public class ShowAllTask extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out=response.getWriter();
		
		out.print("<!DOCTYPE html>");
		out.print("<html>");
		out.println("<head>");
		out.println("<title>Add  Form</title>");
		out.println("<style>");
	    out.println("body {");
	    out.println("background-image: url('book2.jpg')"); 
	    out.println("background-size: cover;");
	    out.println("}");
	    out.println("</style>");
		out.println("</head>");
		out.println("<body>");
		
		request.getRequestDispatcher("homeStart.html").include(request, response);
		out.println("<div class='container'>");
		request.getRequestDispatcher("displayTasks").include(request, response);

		out.close();
	}
}