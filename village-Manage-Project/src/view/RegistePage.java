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

	private JButton loginButton = new JButton("���ص�¼");
	
	private JButton exitButton = new JButton("�˳�");
	
	private JButton registButton = new JButton("ע��");
	
	private JTextField nameText = new JTextField();//��¼�����
	
	private JPasswordField passwordText = new JPasswordField();//���������
	
	private LoginPage loginPage;
	
	public void init() {
		loginPage = VillageManageApplication.loginPage;
	}
	
	public RegistePage() {
		setTitle("����С����ȫϵͳ");
		setLayout(null);
		JLabel lblLogin1=new JLabel("�û���:");
		
		JLabel lblPassword=new JLabel("�� ��:");
		
		setSize(400,200);
		setLocation(300,200);
		lblLogin1.setBounds(45,15,50,20);
		nameText.setBounds(100,15,210,20);
		lblPassword.setBounds(45,45,50,20);
		passwordText.setBounds(100,45,210,20);
		loginButton.setBounds(45,100,100,20);
		exitButton.setBounds(152,100,80,20);
		registButton.setBounds(240,100,80,20);
		loginButton.addActionListener(this);//��ӵ���¼�����
		exitButton.addActionListener(this);
		registButton.addActionListener(this);
		add(lblLogin1);
		add(nameText);
		add(lblPassword);
		add(passwordText);
		add(loginButton);
		add(exitButton);
		add(registButton);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//���ùرն���
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == loginButton) {
			this.setVisible(false);
			loginPage.setVisible(true);
		} else if(e.getSource() == registButton) {
			String name = nameText.getText();
			String pass = new String(passwordText.getPassword());
			int result = JDBCUtil.userRegist(name, pass);
			if(result > 0) {
				int i = JOptionPane.showConfirmDialog(this, "ע��ɹ����Ƿ���ת����¼���棿");
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
