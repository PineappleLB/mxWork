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

public class RegistePage extends JFrame implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JButton loginButton = new JButton("返回登录");
	
	private JButton exitButton = new JButton("退出");
	
	private JButton registButton = new JButton("注册");
	
	private JTextField nameText = new JTextField();//登录输入框
	
	private JPasswordField passwordText = new JPasswordField();//密码输入框
	
	private LoginPage loginPage;
	
	public void init() {
		loginPage = VillageManageApplication.loginPage;
	}
	
	public RegistePage() {
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
		loginButton.setBounds(45,100,100,20);
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
			this.setVisible(false);
			loginPage.setVisible(true);
		} else if(e.getSource() == registButton) {
			String name = nameText.getText();
			String pass = new String(passwordText.getPassword());
			int result = JDBCUtil.adminRegist(name, pass);
			if(result > 0) {
				int i = JOptionPane.showConfirmDialog(this, "注册成功，是否跳转到登录界面？");
				if(i == JOptionPane.OK_OPTION) {
					this.setVisible(false);
					loginPage.setVisible(true);
				} 
				nameText.setText("");
				passwordText.setText("");
			}
		}
		
	}
		
}
