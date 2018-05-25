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
	
	private JButton loginButton = new JButton("����");
	
	private JButton exitButton = new JButton("�˳�");
	
	private JButton registButton = new JButton("ע��");
	
	private JTextField nameText = new JTextField();//��¼�����
	
	private JPasswordField passwordText = new JPasswordField();//���������
	
	private MainPage mainPage;//��Ҫ����ҳ��
	
	private RegistePage registPage;

	public void init() {
		mainPage = VillageManageApplication.mainPage;//��Ҫ����ҳ��
		registPage = VillageManageApplication.registePage;
	}
	
	public LoginPage() {
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
		loginButton.setBounds(65,100,80,20);
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
			String name = nameText.getText();
			String pass = new String(passwordText.getPassword());
			int result = JDBCUtil.userLogin(name, pass);
			if(result > 0) {
				this.setVisible(false);
				mainPage.setVisible(true);
				nameText.setText("");
				passwordText.setText("");
			} else {
				JOptionPane.showMessageDialog(this, "�û������������ ��¼ʧ�ܣ�");
			}
		} else if (e.getSource() == exitButton){
			System.exit(0);
		} else if (e.getSource() == registButton){
			setVisible(false);
			registPage.setVisible(true);
		}
		
	}

}
	



	     