package view;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;

public class MainPage extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public void init() {
		
	}
	
	public MainPage() {
		setTitle("智能小区安全系统");
		setSize(300,300);
		setLocation(450,200);
		JPanel pLeft=new JPanel();
		pLeft.setLayout(new GridLayout(5,1)); 
		JSplitPane sp=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,pLeft,new JTextField(""));
		sp.setDividerSize(10);
		add(sp);
		JLabel lblSelection=new JLabel("功能选项>>>");
		JButton btnfunction1=new JButton("信号处理");
		JButton btnfunction2=new JButton("信号存储");
		JButton btnfunction3=new JButton("信号查询");
		pLeft.add(lblSelection);
		pLeft.add(btnfunction1);
		pLeft.add(btnfunction2);
		pLeft.add(btnfunction3);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//设置关闭动作
	}
	
}
