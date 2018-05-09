package mxsohaSupperAdministrator;

import org.junit.Test;

import service.UserService;
import service.impl.UserServiceImpl;

public class AdministratorTest {
	UserService service = new UserServiceImpl();
	@Test
	public void test1() {
		int result = service.clearInfoTable();
		System.out.println(result);
	}
	
}
