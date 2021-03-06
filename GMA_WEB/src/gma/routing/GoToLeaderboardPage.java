package gma.routing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import gma.services.*;
import gma.entities.*;
import gma.exceptions.QuestionnaireException;
import gma.objects.Paths;
import gma.objects.ScoreComparator;

@WebServlet("/App/Leaderboard")
public class GoToLeaderboardPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	@EJB(name = "gma.services/StatisticsService")
	private StatisticsService sService;
	@EJB(name = "gma.services/QuestionnaireService.java")
	private QuestionnaireService qService;

	public GoToLeaderboardPage() {
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

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Questionnaire questionnaire;
		List<Statistics> statistics=new ArrayList<Statistics>();

		// Get the current date
		Date date = new Date(System.currentTimeMillis());

		try {
			questionnaire = qService.getQuestionnaireByDate(date);
			if(questionnaire!=null) {
				statistics = sService.getStatistics(questionnaire);
				Collections.sort(statistics, new ScoreComparator().reversed());
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR," Ops! Something went wrong during the access to the leaderboard");
			return;
		}

		
		final WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());
		ctx.setVariable("statistics", statistics);
		// Render the Leaderboard page
		templateEngine.process(Paths.LEADERBOARD_PAGE.getPath(), ctx, response.getWriter());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public void destroy() {
	}

}
