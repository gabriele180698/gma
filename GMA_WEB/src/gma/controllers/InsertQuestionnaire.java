
package gma.controllers;

import java.io.*;

import javax.ejb.EJB;
import javax.persistence.Lob;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
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
import java.util.Stack;

import gma.entities.Product;
import gma.entities.Questionnaire;
import gma.entities.User;
import gma.exceptions.QuestionnaireException;
import gma.objects.Paths;
import gma.services.QuestionService;
import gma.services.ProductService;
import gma.services.QuestionnaireService;

@WebServlet("/Admin/InsertQuestionnaire")
@MultipartConfig
public class InsertQuestionnaire extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	@EJB(name = "gma.services/QuestionnaireService.java")
	private QuestionnaireService qService;
	@EJB(name = "gma.services/ProductService.java")
	private ProductService pService;
	@EJB(name = "gma.services/QuestionService.java")
	private QuestionService queService;

	public InsertQuestionnaire() {
		super();
	}
	//return true if the date is today or later
	private boolean isValidDate(Date date) {
		Date today = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return (!date.before(today));
	}

	public void init() throws ServletException {
		ServletContext servletContext = getServletContext();
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		this.templateEngine = new TemplateEngine();
		this.templateEngine.setTemplateResolver(templateResolver);
		templateResolver.setSuffix(".html");
	}

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
		Date date = null;
		// Date now = new Date(System.currentTimeMillis());
		String pictureName = null;
		Integer counterQuestions = null;
		Product product = new Product();
		List<String> questions = new Stack<String>();
		InputStream imgContent = null;
		
		try {
			// get and check Image Data
			Part imgFile = (Part) request.getPart("picture");
			imgContent = imgFile.getInputStream();
			byte[] imgByteArray = readImage(imgContent);
			pictureName = (String) request.getParameter("pictureName");
			if (imgByteArray.length == 0 || pictureName.isEmpty()) {
				throw new Exception("Invalid photo parameters");
			}
			
			// get Date
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			date = (Date) sdf.parse(request.getParameter("date"));
			// check data
			if (date == null && isValidDate(date)) {
				throw new Exception("Missing or empty data");
			}
			// check if there is another questionnaire for the same day
			boolean exist = qService.questionnaireExist(date);
			if (exist) {
				throw new Exception("Existing questionnaire for the day: " + date);
			}
			// get Questions
			counterQuestions = (Integer) Integer.parseInt(request.getParameter("counter"));
			Integer i;
			System.out.println(counterQuestions);
			if (counterQuestions == 0) {
				throw new Exception("No question submitted");
			}
			for (i = 0; i < counterQuestions; i++) {
				questions.add(i, StringEscapeUtils.escapeJava(request.getParameter("q" + i)));
				System.out.println(request.getParameter("q" + i));
			}
			// creation of the product, Questionnaire and Questions
			product = pService.createProductQuestionnaireAndQuestions(pictureName, imgByteArray, date, questions);
			
			// creation Questionnaire and Questions
			//qService.createQuestionnaireAndQuestions(date, product, questions);
			
			// redirect to Admin Home page
			response.sendRedirect(getServletContext().getContextPath() + Paths.ADMIN_INSPECTION.getPath());
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}
	}

}