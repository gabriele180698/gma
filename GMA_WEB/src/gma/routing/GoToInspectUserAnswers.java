
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
import gma.services.AnswerService;
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
	@EJB(name = "gma.services/AnswerService.java")
	private AnswerService aService;

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
		// get the id of the selected questionnaire and of the selected user
		idQuestionnaire = Integer.parseInt(request.getParameter("idQuestionnaire"));
		idUser = Integer.parseInt(request.getParameter("idUser"));
		try {
			q = qService.getQuestionnaireById(idQuestionnaire);
			u = uService.getUserById(idUser);
			answers = aService.getAnswers(q, u);
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					"Something went wrong during the retrieving of the answers!");
			return;
		}

		final WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());
		ctx.setVariable("questionnaire", q);
		ctx.setVariable("user", u);
		ctx.setVariable("answers", answers);
		// render to the Admin Inspect User Answer page
		templateEngine.process(Paths.ADMIN_INSPECT_USER_ANSWERS_PAGE.getPath(), ctx, response.getWriter());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public void destroy() {
	}

}
