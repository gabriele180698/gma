package gma.routing;

import java.io.IOException;
import java.util.List;

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

import gma.entities.Questionnaire;
import gma.entities.User;
import gma.exceptions.QuestionnaireException;
import gma.objects.Paths;
import gma.services.QuestionService;
import gma.services.QuestionnaireService;

@WebServlet("/Admin/Inspection")
public class GoToInspectionPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	private QuestionnaireService qService;

	public GoToInspectionPage() {
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
		List<Questionnaire> questionnaires = null;
		try {
			questionnaires = qService.getAllQuestionnaire();
			request.getSession().setAttribute("questionnaires", questionnaires);
		} catch (QuestionnaireException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					"Ops! Something went wrong during the access to the questionnaire");
			return;
		}

		// Redirect to the Home page and add missions to the parameters
		final WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());
		templateEngine.process(Paths.ADMIN_INSPECTION_PAGE.getPath(), ctx, response.getWriter());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public void destroy() {
	}

}
