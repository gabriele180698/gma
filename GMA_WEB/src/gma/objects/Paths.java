package gma.objects;

public enum Paths {
	INDEX_PAGE("/index.html"), USER_HOME_PAGE("/WEB-INF/home.html"), ADMIN_HOME_PAGE("/WEB-INF/admin/adminPanel.html"), QUESTIONNAIRE_PAGE("/WEB-INF/questionnaire.html"),
	LEADERBOARD_PAGE("/WEB-INF/leaderboard.html"), THANKS_PAGE("/WEB-INF/thanks.html"), ADMIN_HOME("/Admin/Home"), USER_HOME("/App/Home");
	
	private final String path;
	
	Paths(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}

}
