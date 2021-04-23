package gma.objects;

public enum Paths {
	INDEX_PAGE("/index.html"), USER_HOME_PAGE("/WEB-INF/home.html"), ADMIN_HOME_PAGE("/WEB-INF/admin/adminPanel.html"),
	QUESTIONNAIRE_PAGE("/WEB-INF/questionnaire.html"), LEADERBOARD_PAGE("/WEB-INF/leaderboard.html"),
	THANKS_PAGE("/WEB-INF/thanks.html"), ADMIN_HOME("/Admin/Home"), USER_HOME("/App/Home"),
	ADMIN_CREATE_QUESTIONNAIRE_PAGE("/WEB-INF/admin/createQuestionnaire.html"),
	ADMIN_DELETE_QUESTIONNAIRE("/Admin/DeleteQuestionnaire"),
	ADMIN_DELETE_QUESTIONNAIRE_PAGE("/WEB-INF/admin/deleteQuestionnaire.html"),
	ADMIN_INSPECTION_PAGE("/WEB-INF/admin/inspection.html"),
	ADMIN_INSPECT_QUESTIONNAIRE_PAGE("/WEB-INF/admin/inspectQuestionnaire.html"),
	ADMIN_INSPECT_USER_ANSWERS_PAGE("/WEB-INF/admin/inspectUserAnswers.html");

	private final String path;

	Paths(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}

}
