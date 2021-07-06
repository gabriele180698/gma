package gma.controllers;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import gma.entities.Questionnaire;
import gma.entities.User;
import gma.exceptions.QuestionnaireException;
import gma.objects.Paths;
import gma.services.ProductService;
import gma.services.QuestionnaireService;

@WebServlet("/Admin/RemoveQuestionnaire")

public class RemoveQuestionnaire extends HttpServlet {
	private static final long serialVersionUID = 1L;
	@EJB(name = "gma.services/QuestionnaireService")
	private QuestionnaireService qService;

	public RemoveQuestionnaire() {
		super();
	}

	private boolean isValidDate(Date date) {
		Date today = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
				
		System.out.println("prova prova 1 " + date.before(today));
		System.out.println("prova prova 1 " + !date.equals(today));
		
	 
		
        return (date.before(today) && (!sdf.format(date).equals(sdf.format(today))));
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// get and check parameters
		Integer idQuestionnaire = null;
		try {
			idQuestionnaire = Integer.parseInt(request.getParameter("idQuestionnaire"));
		} catch (NumberFormatException | NullPointerException e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Something went wrong during the retrieving of the questionnaire's id!");
			return;
		}

		Questionnaire questionnaire = null;
		try {
			questionnaire = qService.getQuestionnaireById(idQuestionnaire);
		} catch (QuestionnaireException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Something went wrong during the retrieving of the questionnaire!");
			return;
		}

		// deletion should be possible only for a date preceding the current date
		if (!isValidDate(questionnaire.getDate())) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "This questionnare can not be deleted because its date precedes the current one!");
			return;
		}

		// delete the questionnaire
		try {
			qService.removeQuestionnaire(questionnaire);
		} catch (QuestionnaireException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Something went wrong during the deletion phase!");
			return;
		}

		// redirect to Admin Delete Questionnaire
		response.sendRedirect(getServletContext().getContextPath() + Paths.ADMIN_DELETE_QUESTIONNAIRE.getPath());

	}
}
