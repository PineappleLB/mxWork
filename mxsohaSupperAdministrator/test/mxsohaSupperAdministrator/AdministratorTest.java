package mxsohaSupperAdministrator;

import java.util.List;

import org.junit.Test;

import model.Promoter;
import service.UserService;
import service.impl.UserServiceImpl;

public class AdministratorTest {
	UserService service = new UserServiceImpl();
	@Test
	public void test1() {
		int result = service.clearInfoTable();
		System.out.println(result);
	}
	@Test
	public void testUpdatedminPass() {
		int result = service.updateAdminPass("123456", "654321");
		System.out.println(result);
	}
	@Test
	public void testAddPromoter() {
		String result = service.addTopPromoter(new Promoter("abcde", "123456"));
		System.out.println(result);
	}
	
	@Test
	public void testSelectPromoters() {
		List<Promoter> promoters = service.selectAllTopPromoters();
		System.out.println(promoters);
	}
	@Test
	public void testDeletePromoterByName() {
		int result = service.deletePromoterByName("abcde");
		System.out.println(result);
	}
	@Test
	public void testdeleteAllUser() {
		int result = service.deleteAllUser();
		System.out.println(result);
	}
	@Test
	public void testDeleteAllPromoter() {
		int result = service.deleteAllPromoter();
		System.out.println(result);
	}
}
