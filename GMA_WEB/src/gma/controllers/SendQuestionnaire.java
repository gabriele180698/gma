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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import gma.entities.Blacklist;
import gma.entities.Questionnaire;
import gma.entities.Statistics;
import gma.entities.User;
import gma.exceptions.AnswerException;
import gma.objects.Paths;
import gma.services.StatisticsService;
import gma.services.UserService;
import gma.services.AnswerService;
import gma.services.BlacklistService;
import gma.services.QuestionService;
import gma.services.QuestionnaireService;

@WebServlet("/App/SendQuestionnaire")
public class SendQuestionnaire extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	@EJB(name = "gma.services/StatisticsService")
	private StatisticsService sService;
	@EJB(name = "gma.services/AnswerService")
	private AnswerService aService;
	@EJB(name = "gma.services/QuestionService")
	private QuestionnaireService qService;
	@EJB(name = "gma.services/BlacklistService")
	private BlacklistService bService;
	@EJB(name = "gma.services/UserService")
	private UserService uService;

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

		// retrieve the type of user request (i.e. submission or cancellation)
		if ("Submit".equals(action)) {
			SubmitQuestionnaire(request, response);
		} else if ("Cancel".equals(action)) {
			CancelQuestionnaire(request, response);
		}

	}

	protected void SubmitQuestionnaire(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int age;
		int expertise;
		int questionnaireId;
		int sex;
		Date date;
		Integer idQuestion;
		Questionnaire questionnaire;
		Statistics statistics;
		User user;
		List<Integer> questionsIdList;
		Blacklist offensive;
		String answer;
		String answersConcatenation;
		Map<Integer, String> map = new HashMap<Integer, String>();
		StringBuilder sb = new StringBuilder();

		HttpSession session = request.getSession();

		user = (User) session.getAttribute("user");

		// get the list of questions Id
		questionsIdList = (List<Integer>) session.getAttribute("questionsId");

		// get the previously saved questionnaire
		questionnaire = (Questionnaire) session.getAttribute("questionnaire");

		// get the current date and compare the saved questionnaire with the
		// questionnaire of the day
		// (not mandatory, it is a coherence check)
		date = new Date(System.currentTimeMillis());
		try {
			if (questionnaire.getId() != qService.getQuestionnaireByDate(date).getId()) {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
						"Ops! There appears that the questionnaire is not the right one!");
				return;
			}
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					"Something went wrong during the check of the questionnaire!");
			return;
		}

		// check if the user has already submitted a questionnaire for the given product
		try {
			statistics = sService.existingStatistics(user.getId(), questionnaire.getId());
			if (statistics != null) {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
						"Ops! There appears that you have already submitted a questionnaire for this product!");
				return;
			}
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					"Something went wrong during the check of the old statistics!");
			return;
		}

		// get the mandatory answers
		try {
			for (int i = 0; i < questionsIdList.size(); i++) {
				idQuestion = questionsIdList.get(i);
				// take the answer correspondent to the specific question Id
				answer = StringEscapeUtils.escapeJava(request.getParameter(idQuestion.toString()));
				// put all pairs (idQuestion,Answer) in an map table
				map.put(idQuestion, answer);
				sb.append(answer);
				sb.append(" ");
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Something went wrong during the retrieving of the mandatory answers!");
			return;
		}
		answersConcatenation = sb.toString();

		// get personal data entered by the user
		try {
			expertise = Integer.parseInt(request.getParameter("expertise"));
			sex = Integer.parseInt(request.getParameter("sex"));
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Something went wrong during the retrieving of the user personal data!");
			return;
		}

		// if the user has not inserted the information about her/his age, set the
		// default value
		if (request.getParameter("age") == null || request.getParameter("age").isEmpty()) {
			age = 0; // default value
		} else {
			try {
				age = Integer.parseInt(request.getParameter("age"));
			} catch (Exception e) {
				e.printStackTrace();
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Something went wrong during the retrieving of the age!");
				return;
			}
		}

		// Call the service to check the presence of offensive words
		try {
			offensive = bService.searchOffensiveWord(answersConcatenation); // Check the answers
		} catch (Exception e) {
			try {
				aService.deleteAnswers(questionsIdList, user.getId());
			} catch (AnswerException e1) {
				e1.printStackTrace();
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
						"Something went wrong during the submission!");
				return;
			}
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					"Something went wrong during the checking of the presence of offensive words!");
			return;
		}
		if (offensive != null) {
			try {
				// Ban the user
				uService.banUser(user);
			} catch (Exception e) {
				e.printStackTrace();
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
						"Something went wrong during the ban operation!");
				return;
			}
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					"): You have inserted a bad word. Now you are banned! :(");
			return;
		}
		
		// call the service to submit the statistics and the answers
		try {
			sService.submitStatistics(age, expertise, questionnaire, sex, user, map); // submit the statistics and the answers
			//aService.submitAnswers(map, user); // submit the answers
			final WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());
			templateEngine.process(Paths.THANKS_PAGE.getPath(), ctx, response.getWriter());

		} catch (Exception e) {
			try {
				aService.deleteAnswers(questionsIdList, user.getId());
			} catch (AnswerException e1) {
				e1.printStackTrace();
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
						"Something went wrong during the submission!");
				return;
			}
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					"Something went wrong during the submission of the personal data phase!");
			return;
		}
	}

	protected void CancelQuestionnaire(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Date date;
		Questionnaire questionnaire;
		User user;

		HttpSession session = request.getSession();

		// get the previously saved questionnaire
		questionnaire = (Questionnaire) session.getAttribute("questionnaire");

		// get the current date and compare the saved questionnaire with the
		// questionnaire of the day
		// (not mandatory, it is a coherence check)
		date = new Date(System.currentTimeMillis());
		try {
			if (questionnaire.getId() != qService.getQuestionnaireByDate(date).getId()) {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
						"Ops! There appears that the questionnaire is not the right one");
				return;
			}
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					"Something went wrong during the check of the questionnaire");
			return;
		}

		user = (User) session.getAttribute("user");
		try {
			// call the service to submit the statistics
			sService.cancelStatistics(questionnaire, user);
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					"Something went wrong during the cancellation phase");
			return;
		}
		
		
		// redirect to User Home Page
		response.sendRedirect(getServletContext().getContextPath() + Paths.USER_HOME.getPath());
	}
}
