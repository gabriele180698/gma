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

import gma.entities.Product;
import gma.entities.Questionnaire;
import gma.entities.User;
import gma.exceptions.QuestionnaireException;
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
	@EJB(name = "gma.services/QuestionService")
	private QuestionService queService;

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

	/*
	 * private boolean isToday(Date date) { SimpleDateFormat sdf = new
	 * SimpleDateFormat("yyyy-MM-dd"); Date now = new
	 * Date(System.currentTimeMillis()); return
	 * sdf.format(now).equals(sdf.format(date)); }
	 */
	public static byte[] readImage(InputStream imageInputStream) throws IOException {

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[4096];// image can be maximum of 4MB
		int bytesRead = -1;

		try {
			while ((bytesRead = imageInputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, bytesRead);
			}

			byte[] imageBytes = outputStream.toByteArray();
			return imageBytes;
		} catch (IOException e) {
			throw e;
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("InsertQuestionnaire");
		// Retrieve the type of user request (i.e. submitting or cancellation)
		if ("Submit".equals(action)) {
			SubmitQuestionnaire(request, response);
		}
	}

	protected void SubmitQuestionnaire(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Date date;
		// Date now = new Date(System.currentTimeMillis());
		String pictureName;
		Integer counterQuestions;
		Product product = new Product();
		Questionnaire questionnaire = new Questionnaire();
		// Get data entered by the user
		// Get and Check Image Data
		try {
			Part imgFile = request.getPart("picture");
			InputStream imgContent = imgFile.getInputStream();
			byte[] imgByteArray = readImage(imgContent);
			pictureName = request.getParameter("pictureName");
			// Check data
			if (imgByteArray.length == 0 || pictureName.isEmpty()) {
				throw new Exception("Invalid photo parameters");
			}
			// Creation Product
			product = pService.createProduct(pictureName, imgByteArray);
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Ops! Some data was lost");
			return;
		}
		// Get and Check Time Data
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			date = (Date) sdf.parse(request.getParameter("date"));
			// Check data
			if (date == null) {
				throw new Exception("Missing or empty data");
			}
			// Check if there is an other questionnaire for the same day
			boolean exist = qService.questionnaireExist(date);
			if (exist) {
				throw new Exception("Existing questionnaire for the day: " + date);
			}

		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ops! Some data was lost");
			return;
		}
		// Creation Questionnaire
		try {
			questionnaire = qService.createQuestionnaire(date, product);
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Ops! Some data was lost");
			return;
		}
		// Get Questions and Creation
		try {
			counterQuestions = Integer.parseInt(request.getParameter("counter"));
			Integer i;
			for (i = 0; i < counterQuestions; i++) {
				queService.createQuestion(StringEscapeUtils.escapeJava(request.getParameter("q" + i)), questionnaire);
			}

			response.sendRedirect(
					getServletContext().getContextPath() + Paths.ADMIN_CREATE_QUESTIONNAIRE_PAGE.getPath());

		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Ops! Some data was lost");
			return;
		}
	}

}
