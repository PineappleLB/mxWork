package view;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Login extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Login() {
		setName("智能小区安全系统");
		setLayout(null);
		JLabel lblLogin1=new JLabel("用户名:");
		JTextField txtLogin=new JTextField();
		JLabel lblPassword=new JLabel("密 码:");
		JPasswordField txtPassword=new JPasswordField();
		JButton btnCon=new JButton("连接");
		JButton btnExit=new JButton("退出");
		JButton btnPro=new JButton("注册");
		setSize(400,200);
		setLocation(300,200);
		lblLogin1.setBounds(45,15,50,20);
		txtLogin.setBounds(100,15,210,20);
		lblPassword.setBounds(45,45,50,20);
		txtPassword.setBounds(100,45,210,20);
		btnCon.setBounds(65,100,80,20);
		btnExit.setBounds(152,100,80,20);
		btnPro.setBounds(240,100,80,20);
		add(lblLogin1);
		add(txtLogin);
		add(lblPassword);
		add(txtPassword);
		add(btnCon);
		add(btnExit);
		add(btnPro);
		setVisible(true);	
		
	}
	
	public static void main(String args[]){
		JFrame frame=new JFrame("智能小区安全系统");
		frame.setLayout(null);
		JLabel lblLogin1=new JLabel("用户名:");
		JTextField txtLogin=new JTextField();
		JLabel lblPassword=new JLabel("密 码:");
		JPasswordField txtPassword=new JPasswordField();
		JButton btnCon=new JButton("连接");
		JButton btnExit=new JButton("退出");
		JButton btnPro=new JButton("注册");
		frame.setSize(400,200);
		frame.setLocation(300,200);
		lblLogin1.setBounds(45,15,50,20);
		txtLogin.setBounds(100,15,210,20);
		lblPassword.setBounds(45,45,50,20);
		txtPassword.setBounds(100,45,210,20);
		btnCon.setBounds(65,100,80,20);
		btnExit.setBounds(152,100,80,20);
		btnPro.setBounds(240,100,80,20);
		frame.add(lblLogin1);
		frame.add(txtLogin);
		frame.add(lblPassword);
		frame.add(txtPassword);
		frame.add(btnCon);
		frame.add(btnExit);
		frame.add(btnPro);
		frame.setVisible(true);	
		}
	

	}
	



	     