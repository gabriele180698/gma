
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

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import gma.entities.Answer;
import gma.entities.Questionnaire;
import gma.entities.User;
import gma.objects.Paths;
import gma.services.QuestionService;
import gma.services.QuestionnaireService;
import gma.services.UserService;

@WebServlet("/Admin/InspectUserAnswers")
public class GoToInspectUserAnswers extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	@EJB(name = "gma.services/QuestionnaireService.java")
	private QuestionnaireService qService;
	@EJB(name = "gma.services/UserService.java")
	private UserService uService;
	@EJB(name = "gma.services/QuestionService.java")
	private QuestionService quService;

	public GoToInspectUserAnswers() {
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
		Integer idUser = null;
		Questionnaire q = null;
		User u = null;
		List<Answer> answers = null;
		List<User> cancellers = null;
		try {
			// Get id of the selected questionnaire and selected user
			idQuestionnaire = Integer.parseInt(request.getParameter("idQuestionnaire"));
			idUser = Integer.parseInt(request.getParameter("idUser"));
			q = qService.getQuestionnaireById(idQuestionnaire);
			u = uService.getUserById(idUser);
			answers = quService.getAnswers(q, u);
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					"Ops! Something went wrong during the access to the questionnaire data");
			return;
		}

		// Set parameters and redirect to the next inspection page
		final WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());
		ctx.setVariable("questionnaires", q);
		ctx.setVariable("users", u);
		ctx.setVariable("answers", answers);
		templateEngine.process(Paths.ADMIN_INSPECT_QUESTIONNAIRE_PAGE.getPath(), ctx, response.getWriter());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public void destroy() {
	}

}
