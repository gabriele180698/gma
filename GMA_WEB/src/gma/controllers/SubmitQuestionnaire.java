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


//DA FINIRE - NON MODIFICATE SENZA AVVISARE PLEASE - DA DECIDERE ALCUNE COSE INSIEME

@WebServlet("/App/Submit")
public class SubmitQuestionnaire extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	@EJB(name = "gma.services/StatisticsService")
	private StatisticsService sService;

	public SubmitQuestionnaire() {
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
		String loginpath;
		String path;
		int score;
		int age; 
		int expertise; 
		int questionnaire;
		int sex;
		User user;
		int status;
		try {
			score = Integer.parseInt(request.getParameter("score"));
			age = Integer.parseInt(request.getParameter("age"));
			expertise = Integer.parseInt(request.getParameter("expertise"));
			sex = Integer.parseInt(request.getParameter("sex"));
			status = Integer.parseInt(request.getParameter("status"));
			questionnaire = Integer.parseInt(request.getParameter("questionnaire"));
			// definire come capire che siano effettivamente stati passati
		} catch (Exception e) {
			// for debugging only e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing credential value");
			return;
		}
		
		HttpSession session = request.getSession();
		user = (User) session.getAttribute("user");
		try {
			//Submit the statistics
			sService.submitStatistics(score, age, expertise, questionnaire, sex, user, status);
			response.sendRedirect(getServletContext().getContextPath() + Paths.THANKS_PAGE.getPath());
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to submit data");
			return;
		}

	}

	public void destroy() {
	}
}
