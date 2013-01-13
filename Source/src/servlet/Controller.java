package servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javabean.DBConnection;
import javabean.Lobby;
import javabean.Player;
import javabean.Table;
import javabean.UserInfo;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class Controller
 */
@WebServlet("/Controller")
public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;
	DBConnection dbConnection;
    /**
     * Default constructor. 
     */ 
    public Controller() throws ClassNotFoundException, SQLException{
        // TODO Auto-generated constructor stub
    	dbConnection=new DBConnection();
    }
    

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		RequestDispatcher dispatch = getServletContext().getRequestDispatcher("/Acceuil.jsp");
		dispatch.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		HttpSession session = request.getSession();
		ServletContext context = getServletConfig().getServletContext();
		//Index.jsp Buttons
		if(request.getParameter("button")!=null && request.getParameter("button").equals("Inscrivez-vous!"))
		{
			
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/Registration.jsp");
	        rd.forward(request, response);
	        return;
		}
		else if(request.getParameter("button")!=null && request.getParameter("button").equals("Se connecter"))
		{
			boolean success=false;
			try {
				success=dbConnection.checkPasswordCombination(request.getParameter("nickName"),request.getParameter("password"));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(success)
			{
				UserInfo user=new UserInfo();
				user.setNickName(request.getParameter("nickName"));
				session.setAttribute("user", user);
				RequestDispatcher rd = getServletContext().getRequestDispatcher("/UserProfile.jsp");
		        rd.forward(request, response);
		        return;
			}
			else
			{
				request.setAttribute("errors","La combinaison Pseudo/Mot de passe est incorrect");
				RequestDispatcher rd = getServletContext().getRequestDispatcher("/Index.jsp");
		        rd.forward(request, response);
		        return;
			}
		}
		//Registration.jsp Buttons
		else if(request.getParameter("button")!=null && request.getParameter("button").equals("Retour"))
		{
			
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/Acceuil.jsp");
	        rd.forward(request, response);
	        return;
		}
		else if(request.getParameter("button")!=null && request.getParameter("button").equals("Souscrire"))
		{
			try {
				ArrayList<String> errors=dbConnection.createNewUser(request.getParameter("firstName"), request.getParameter("lastName"), request.getParameter("nickName"), request.getParameter("password"));
				if(!errors.isEmpty())
				{
					request.setAttribute("errors",errors);
					RequestDispatcher rd = getServletContext().getRequestDispatcher("/Registration.jsp");
					rd.forward(request, response);
					return;
				}
					
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			request.setAttribute("sender", "registration");
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/Index.jsp");
	        rd.forward(request, response);
	        return;
		}
		//UserProfile.jsp Buttons
		else if(request.getParameter("button")!= null && request.getParameter("button").equals("Disconnect"))
		{
		
			request.getSession(false).invalidate();
			request.setAttribute("errors","Vous vous êtes bien déconnecté");
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/Index.jsp");
	        rd.forward(request, response);
	      
	        return;
		}
		else if(request.getParameter("button")!=null && request.getParameter("button").equals("Creer une nouvelle table"))
		{
			Lobby lobby;
			if(context.getAttribute("lobby")!=null)
				lobby=(Lobby)context.getAttribute("lobby");
			else
				lobby=new Lobby();
			UserInfo user=(UserInfo)session.getAttribute("user");
			int tableId=lobby.addTable();
			user.setCurrentTable(tableId);
			Player player=new Player(user);
			Table table=lobby.getTable(tableId); 
			table.addPlayer(player);
			session.setAttribute("player", player);
			session.setAttribute("user", user);
			context.setAttribute("lobby",lobby);
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/Table.jsp");
	        rd.forward(request, response);
	        return;
		}
		else if(request.getParameter("button")!=null && (request.getParameter("button").substring(0,5)).equals("Table"))
		{	
			Lobby lobby=(Lobby)context.getAttribute("lobby");
			UserInfo user=(UserInfo)session.getAttribute("user");
			int tableId=Integer.parseInt(request.getParameter("tableId"));
			Table table=lobby.getTable(tableId);
			user.setCurrentTable(tableId);
			Player player=new Player(user);
			table.addPlayer(player);
			session.setAttribute("player", player);
			session.setAttribute("user", user);
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/Table.jsp");
	        rd.forward(request, response);
	        return;
		}
		else if(request.getParameter("hiddenExitTable")!=null && request.getParameter("hiddenExitTable").equals("ExitTable"))
		{
			Lobby lobby=(Lobby)context.getAttribute("lobby");
			UserInfo user=(UserInfo)session.getAttribute("user");
			int tableId=user.getCurrentTable();
			request.setAttribute("exitTable", tableId);
			Table table=lobby.getTable(tableId);
			if(table.getNumberOfPlayer()==1)
				request.setAttribute("removeTable", "true");
			user.setCurrentTable(-1);
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/UserProfile.jsp");
	        rd.forward(request, response);
	        return;
		}
	}

}
