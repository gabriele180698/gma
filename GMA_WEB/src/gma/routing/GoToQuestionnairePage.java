package gma.routing;

import java.io.IOException;
import java.util.Arrays;
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
import gma.objects.Paths;

@WebServlet("/App/Questionnaire")
public class GoToQuestionnairePage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	/*@EJB(name = "it.polimi.db2.mission.services/MissionService")
	private MissionService mService;*/
	@EJB(name = "gma.services/QuestionnaireService")
	private QuestionnaireService qService;

	public GoToQuestionnairePage() {
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
		//Get the user
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		// Redirect to the Home page and add missions to the parameters
		final WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());
		
		// Access to the list of questions related to the Product Of The Day
	    try {
	    	List<Question> questions =  qService.getQuestionsByQuestionnaire(2);
	    	ctx.setVariable("questions", questions);
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
					"Ops! Something went wrong during the access to the questions");
			return;
		}
				
		templateEngine.process(Paths.QUESTIONNAIRE_PAGE.getPath(), ctx, response.getWriter());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public void destroy() {
	}

}