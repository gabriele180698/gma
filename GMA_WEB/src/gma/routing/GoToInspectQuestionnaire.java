package gma.routing;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import gma.entities.Product;
import gma.entities.Questionnaire;
import gma.entities.User;
import gma.objects.Paths;
import gma.services.QuestionnaireService;

@WebServlet("/Admin/InspectQuestionnaire")
public class GoToInspectQuestionnaire extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	@EJB(name = "gma.services/QuestionnaireService.java")
	private QuestionnaireService qService;

	public GoToInspectQuestionnaire() {
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
		Integer idQuestionnaire = null;
		Questionnaire q = null;
		List<User> submitters = null;
		List<User> cancellers = null;
		// get id of the selected questionnaire
		idQuestionnaire = Integer.parseInt(request.getParameter("idQuestionnaire"));
		try {
			q = qService.getQuestionnaireById(idQuestionnaire);
			// get users that have submitted and cancelled the questionnaire
			submitters = qService.getUsersSubmitedQuestionnaire(q);
			cancellers = qService.getUsersCancelledQuestionnaire(q);
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					"Something went wrong during the access to the questionnaire data");
			return;
		}
		final WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());
		// set the context variable to pass data to the page
		ctx.setVariable("questionnaire", q);
		ctx.setVariable("submitters", submitters);
		ctx.setVariable("cancellers", cancellers);
		// render Admin Inspect Questionnaire Page
		templateEngine.process(Paths.ADMIN_INSPECT_QUESTIONNAIRE_PAGE.getPath(), ctx, response.getWriter());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public void destroy() {
	}

}
