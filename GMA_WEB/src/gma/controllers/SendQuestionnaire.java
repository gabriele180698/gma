package gma.controllers;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;


import gma.entities.User;
import gma.objects.Paths;
import gma.services.StatisticsService;


@WebServlet("/App/SendQuestionnaire")
public class SendQuestionnaire extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	@EJB(name = "gma.services/StatisticsService")
	private StatisticsService sService;

	public SendQuestionnaire() {
		super();
	}

	public void init() throws ServletException {
		ServletContext servletContext = getServletContext();
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		this.templateEngine = new TemplateEngine();
		this.templateEngine.setTemplateResolver(templateResolver);
		templateResolver.setSuffix(".html");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("SendQuestionnaire");
		
		// Retrieve the type of user request (i.e. submitting or cancellation)
		if ("Submit".equals(action)) {
		    SubmitQuestionnaire(request, response);
		} else if ("Cancel".equals(action)) {
		    CancelQuestionnaire(request,response);
		}

	}
	
	protected void SubmitQuestionnaire(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int age; 
		int expertise; 
		int questionnaire;
		int sex;
		User user;
		
		// Get data entered by the user
		try {
			expertise = Integer.parseInt(request.getParameter("expertise"));
			sex = Integer.parseInt(request.getParameter("sex"));
			//questionnaire = Integer.parseInt(request.getParameter("questionnaire"));
			questionnaire = 2;

		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Ops! Some data was lost");
			return;
		}
		
		//If the user has not inserted the information about her/his age, set the default value
		if(request.getParameter("age") == null || request.getParameter("age").isEmpty()) {
			age = 0; //default value
		} else {
			try {
				age = Integer.parseInt(request.getParameter("age"));
				}catch (Exception e) {
					e.printStackTrace();
					response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Ops! Some data was lost");
					return;
				}
		}
		
		HttpSession session = request.getSession();
		user = (User) session.getAttribute("user");
		try {
			//Call the service to submit the statistics
			sService.submitStatistics(age, expertise, questionnaire, sex, user);
			response.sendRedirect(getServletContext().getContextPath() + Paths.THANKS_PAGE.getPath());
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
					"Ops! Something went wrong during the submission phase");
			return;
		}
	}

	protected void CancelQuestionnaire(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int questionnaire;
		User user;
		
		// Get information about the questionnaire
		try {
			//questionnaire = Integer.parseInt(request.getParameter("questionnaire"));
			questionnaire = 2;

		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Ops! Some questionnaire data was lost");
			return;
		}
		
		HttpSession session = request.getSession();
		user = (User) session.getAttribute("user");
		try {
			//Call the service to submit the statistics
			sService.cancelStatistics(questionnaire, user);
			response.sendRedirect(getServletContext().getContextPath() + Paths.USER_HOME_PAGE.getPath());
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
					"Ops! Something went wrong during the cancellation phase");
			return;
		}
	}

	
	public void destroy() {
	}
}
