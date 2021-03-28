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
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import gma.entities.Questionnaire;
import gma.entities.User;
import gma.objects.Paths;
import gma.services.StatisticsService;
import gma.services.AnswerService;
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
		
		// Retrieve the type of user request (i.e. submitting or cancellation)
		if ("Submit".equals(action)) {
		    SubmitQuestionnaire(request, response);
		} else if ("Cancel".equals(action)) {
		    CancelQuestionnaire(request,response);
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
		User user;
		List<Integer> questionsIdList;
		String answer;
		Map<Integer, String> map = new HashMap<Integer, String>();
		
		HttpSession session = request.getSession();
		
		//Get the user
		user = (User) session.getAttribute("user");
		
		//Get the list of questions Id
		questionsIdList = (List<Integer>) session.getAttribute("questionsId");	
		
		//Get the previously saved questionnaire
		questionnaire = (Questionnaire) session.getAttribute("questionnaire");	
		
		// Get the current date and compare the saved questionnaire with the questionnaire of the day 
		// (not mandatory, it is a coherence check)
	    date = new Date(System.currentTimeMillis());
	    try {
	    	if(questionnaire.getId()!=qService.getQuestionnaireByDate(date).getId()) {
	    		response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
						"Ops! There appears that the questionnaire is not the right one");
	    		return;
	    	}
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
					"Ops! Something went wrong during the check of the questionnaire");
			return;
		}
		
		
	    //Get the mandatory answers
		try {
			for (int i = 0; i < questionsIdList.size(); i++) {
				idQuestion = questionsIdList.get(i);
				//Take the answer correspondent to the specific question Id
				answer = StringEscapeUtils.escapeJava(request.getParameter(idQuestion.toString()));
				//Put all pairs (idQuestion,Answer) in an map table
				map.put(idQuestion, answer);
			}
			aService.submitAnswers(map, user); //Submit the answers
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Ops! Some data was lost");
			return;
		}
		
		// Get personal data entered by the user
		try {
			expertise = Integer.parseInt(request.getParameter("expertise"));
			sex = Integer.parseInt(request.getParameter("sex"));
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Ops! Some data was lost");
			return;
		}
		
		//If the user has not inserted the information about her/his age, set the default value
		if(request.getParameter("age") == null || request.getParameter("age").isEmpty()) {
			age = 0; //default value
		} else {
			try {
				age = Integer.parseInt(request.getParameter("age"));
				}catch (Exception e) {
					e.printStackTrace();
					response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Ops! Some data was lost");
					return;
				}
		}
		
		//Call the service to submit the statistics
		try {
			sService.submitStatistics(age, expertise, questionnaire, sex, user);
			response.sendRedirect(getServletContext().getContextPath() + Paths.THANKS_PAGE.getPath());
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
					"Ops! Something went wrong during the submission of the personal data phase");
			return;
		}
	}

	protected void CancelQuestionnaire(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Date date;
		Questionnaire questionnaire;
		User user;
		
		HttpSession session = request.getSession();
		
		//Get the previously saved questionnaire
		questionnaire = (Questionnaire) session.getAttribute("questionnaire");	
		
		// Get the current date and compare the saved questionnaire with the questionnaire of the day 
		// (not mandatory, it is a coherence check)
	    date = new Date(System.currentTimeMillis());
	    try {
	    	if(questionnaire!=qService.getQuestionnaireByDate(date)) {
	    		response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
						"Ops! There appears that the questionnaire is not the right one");
	    	}
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
					"Ops! Something went wrong during the check of the questionnaire");
			return;
		}
		
	    //Get the user
		user = (User) session.getAttribute("user");
		try {
			//Call the service to submit the statistics
			sService.cancelStatistics(questionnaire, user);
			response.sendRedirect(getServletContext().getContextPath() + Paths.USER_HOME_PAGE.getPath());
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
					"Ops! Something went wrong during the cancellation phase");
			return;
		}
	}

	
	public void destroy() {
	}
}
