package view;

import java.awt.FlowLayout;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;

public class TestFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	JDesktopPane desk = new JDesktopPane();
	
	JInternalFrame inFrame1 = new JInternalFrame("test1", true, true, true);
	
	JInternalFrame inFrame2 = new JInternalFrame("test2", true, true, true);
	
	public TestFrame() {
		super("TestFrame");
		setSize(400, 400);
		setLocation(500, 300);
		setLayout(new FlowLayout());
		inFrame1.setSize(300, 400);
		inFrame2.setSize(300, 400);
		inFrame1.setVisible(true);
		inFrame2.setVisible(true);
		add(desk);
		desk.add(inFrame2);
		desk.add(inFrame1);
		setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public static void main(String[] args) {
		new TestFrame();
	}

}
