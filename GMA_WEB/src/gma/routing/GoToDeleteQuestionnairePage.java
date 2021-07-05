package gma.routing;

import java.io.IOException;
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

@WebServlet("/Admin/DeleteQuestionnaire")
public class GoToDeleteQuestionnairePage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	@EJB(name = "gma.services/ProductService")
	private QuestionnaireService qService;

	public GoToDeleteQuestionnairePage() {
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
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		final WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());

		List<Questionnaire> questionnaires = null;
		try {
			// get all questionnaires
			questionnaires = qService.getAllQuestionnaire();
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Something went wrong during the retrieving of all the questionnaires!");
			return;
		}

		// set the context variable to pass data to the page
		ctx.setVariable("questionnaires", questionnaires);
		// redirect to the Delete Questionnaire page
		templateEngine.process(Paths.ADMIN_DELETE_QUESTIONNAIRE_PAGE.getPath(), ctx, response.getWriter());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public void destroy() {
	}

}