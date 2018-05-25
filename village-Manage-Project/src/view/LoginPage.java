package view;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import jdbc.JDBCUtil;
import lunch.VillageManageApplication;

public class LoginPage extends JFrame implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JButton loginButton = new JButton("连接");
	
	private JButton exitButton = new JButton("退出");
	
	private JButton registButton = new JButton("注册");
	
	private JTextField nameText = new JTextField();//登录输入框
	
	private JPasswordField passwordText = new JPasswordField();//密码输入框
	
	private MainPage mainPage;//主要功能页面
	
	private RegistePage registPage;

	public void init() {
		mainPage = VillageManageApplication.mainPage;//主要功能页面
		registPage = VillageManageApplication.registePage;
	}
	
	public LoginPage() {
		setTitle("智能小区安全系统");
		setLayout(null);
		JLabel lblLogin1=new JLabel("用户名:");
		
		JLabel lblPassword=new JLabel("密 码:");
		
		setSize(400,200);
		setLocation(300,200);
		lblLogin1.setBounds(45,15,50,20);
		nameText.setBounds(100,15,210,20);
		lblPassword.setBounds(45,45,50,20);
		passwordText.setBounds(100,45,210,20);
		loginButton.setBounds(65,100,80,20);
		exitButton.setBounds(152,100,80,20);
		registButton.setBounds(240,100,80,20);
		loginButton.addActionListener(this);//添加点击事件处理
		exitButton.addActionListener(this);
		registButton.addActionListener(this);
		add(lblLogin1);
		add(nameText);
		add(lblPassword);
		add(passwordText);
		add(loginButton);
		add(exitButton);
		add(registButton);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//设置关闭动作
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == loginButton) {
			String name = nameText.getText();
			String pass = new String(passwordText.getPassword());
			int result = JDBCUtil.userLogin(name, pass);
			if(result > 0) {
				this.setVisible(false);
				mainPage.setVisible(true);
				nameText.setText("");
				passwordText.setText("");
			} else {
				JOptionPane.showMessageDialog(this, "用户名或密码错误！ 登录失败！");
			}
		} else if (e.getSource() == exitButton){
			System.exit(0);
		} else if (e.getSource() == registButton){
			setVisible(false);
			registPage.setVisible(true);
		}
		
	}

}
	



	     