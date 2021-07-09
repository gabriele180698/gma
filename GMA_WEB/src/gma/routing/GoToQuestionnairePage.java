package gma.routing;

import java.io.IOException;
import java.util.ArrayList;
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

import gma.entities.Question;
import gma.entities.Questionnaire;
import gma.entities.User;
import gma.objects.Paths;
import gma.services.QuestionService;
import gma.services.QuestionnaireService;

@WebServlet("/App/Questionnaire")
public class GoToQuestionnairePage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	@EJB(name = "gma.services/QuestionnaireService")
	private QuestionnaireService qService;
	@EJB(name = "gma.services/QuestionService.java")
	private QuestionService quService;
	
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
		Date date;
		Questionnaire questionnaire;
		List<Question> questions;
		List<Integer> questionsId = new ArrayList<Integer>();
		HttpSession session = request.getSession();
		
		User user = (User) session.getAttribute("user");

		// Get the current date
	    date = new Date(System.currentTimeMillis());
		
		final WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());
		
		// Access to the list of questions related to the questionnaire
	    try {
	    	questionnaire = qService.getQuestionnaireByDate(date);
	    	if(questionnaire!=null) {
	    		questions =  qService.getQuestionsByQuestionnaire(questionnaire);
	    		if(questions!=null) {
	    			questionsId = quService.getQuestionsId(questions);
	    		}
		    	request.getSession().setAttribute("questionnaire", questionnaire);
		    	request.getSession().setAttribute("questionsId", questionsId);
		    	ctx.setVariable("questions", questions);
	    	}
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ops! Something went wrong during the access to the questions");
			return;
		}
	    
	    // render the Admin Inspection page		
		templateEngine.process(Paths.QUESTIONNAIRE_PAGE.getPath(), ctx, response.getWriter());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public void destroy() {
	}

}