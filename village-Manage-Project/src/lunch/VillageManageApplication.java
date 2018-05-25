package lunch;

import view.LoginPage;
import view.MainPage;
import view.RegistePage;

public class VillageManageApplication {
	
	public static LoginPage loginPage = new LoginPage();

	public static MainPage mainPage = new MainPage();
	
	public static RegistePage registePage = new RegistePage();
	
	public static void main(String[] args) {
		loginPage.init();
		mainPage.init();
		registePage.init();
		loginPage.setVisible(true);
	}
	
}
