package gma.controllers;

import java.io.*;

import javax.ejb.EJB;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.apache.commons.lang.StringEscapeUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gma.entities.Questionnaire;
import gma.entities.Statistics;
import gma.entities.User;
import gma.objects.Paths;
import gma.services.QuestionService;
import gma.services.ProductService;
import gma.services.QuestionnaireService;

@WebServlet("/Admin/InsertQuestionnaire")

public class InsertQuestionnaire extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	@EJB(name = "gma.services/QuestionnaireService")
	private QuestionnaireService qService;
	@EJB(name = "gma.services/ProductService")
	private ProductService pService;

	public InsertQuestionnaire() {
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
	
	private boolean isToday(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date now = new Date(System.currentTimeMillis());
		return sdf.format(now).equals(sdf.format(date));
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("InsertQuestionnaire");
		// Retrieve the type of user request (i.e. submitting or cancellation)
		if ("Submit".equals(action)) {
		    SubmitQuestionnaire(request, response);
		} else if ("Cancel".equals(action)) {
		    CancelQuestionnaire(request,response);
		}

	}

	protected void SubmitQuestionnaire(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Date date;
		Date now = new Date(System.currentTimeMillis());
		User user;
		String img;
		String pictureName;
		// Get data entered by the user
		// Get and Check Image Data
		try {
			//***verificare che sia giusto***
			img = StringEscapeUtils.escapeJava(request.getParameter("picture"));
			pictureName = request.getParameter("pictureName");
			// Check data
			if (img == null || img.isEmpty() || pictureName == null || pictureName.isEmpty()) {
				throw new Exception("Missing or empty credential value for the Image Data ");
			}
			pService.createProduct(pictureName, img);
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Ops! Some data was lost");
			return;
		}
		// Get and Check Time Data
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			date = (Date) sdf.parse(request.getParameter("date"));

			// Check data
			if (date == null) {
				throw new Exception("Missing or empty data");
			}
			if (now.after(date) || isToday(date)) {
				throw new Exception("No valid date");
			}

		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Ops! Some data was lost");
			return;
		}
		//CODICE CHE NON HO SCRITTO

		HttpSession session = request.getSession();
		user = (User) session.getAttribute("user");
		try {
			// Call the service to submit the statistics
			// sService.submitStatistics(age, expertise, questionnaire, sex, user);
			response.sendRedirect(getServletContext().getContextPath() + Paths.THANKS_PAGE.getPath());
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					"Ops! Something went wrong during the submission phase");
			return;
		}
	}

	protected void CancelQuestionnaire(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int questionnaire;
		User user;

		// Get information about the questionnaire
		try {
			// questionnaire = Integer.parseInt(request.getParameter("questionnaire"));
			questionnaire = 2;

		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Ops! Some questionnaire data was lost");
			return;
		}

		HttpSession session = request.getSession();
		user = (User) session.getAttribute("user");
		try {
			// Call the service to submit the statistics
			// sService.cancelStatistics(questionnaire, user);
			response.sendRedirect(getServletContext().getContextPath() + Paths.USER_HOME_PAGE.getPath());
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					"Ops! Something went wrong during the cancellation phase");
			return;
		}
	}
}
