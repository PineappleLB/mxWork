/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.LayoutStyle;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DateFormatter;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

import jdbc.JDBCUtil;
import model.WarningConfig;

/**
 *
 * @author Administrator
 */
public class MainPage extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

 // Variables declaration - do not modify          
    private JLabel jLabel1 = new JLabel("业主id：");
    private JLabel jLabel10 = new JLabel();
    private JLabel jLabel11 = new JLabel();
    private JLabel jLabel12 = new JLabel();
    private JLabel jLabel13 = new JLabel();
    private JLabel jLabel14 = new JLabel();
    private JLabel jLabel2 = new JLabel("警报项：");
    private JLabel jLabel3 = new JLabel("值：");
    private JLabel jLabel4 = new JLabel("单元号：");
    private JLabel jLabel6 = new JLabel();
    private JLabel jLabel7 = new JLabel();
    private JLabel jLabel8 = new JLabel();
    private JLabel jLabel9 = new JLabel();
    private JTable jTable = new JTable();
    
    private JButton aCancelButton = new JButton();//存储页面取消按钮
    private JButton aOKButton = new JButton(); //存储页面确认按钮
    private JButton sSelectButton = new JButton(); //查询页面查询按钮
    private JButton uCancelButton = new JButton("取消修改"); //信号处理页面取消按钮
    private JButton uOKButton = new JButton("确认修改"); //信号处理页面确认按钮
    
    private JFormattedTextField aAlertTypeTextField = new JFormattedTextField(); //存储页面时间格式化字段
    private JFormattedTextField aValueTextFiled = new JFormattedTextField(); // 存储页面值输入字段
    private JFormattedTextField uValueTextFiled = new JFormattedTextField(); // 信号处理页面值输入字段   
    private JScrollPane jScrollPane = new JScrollPane();
    
    //查询页面
    private JComboBox<String> sAlertTypeSelect = new JComboBox<>(); //警报类型选项框
    private JComboBox<String> sRoomSelect = new JComboBox<>();		//房间号选项框
    private JComboBox<String> sUnitSelect = new JComboBox<>();		//单元号选项框
    private JComboBox<String> sUserSelect = new JComboBox<>();		//业主id选项框
    //信号处理页面
    private JComboBox<String> uAlertTypeSelect = new JComboBox<>(); //警报类型选项框
    private JComboBox<String> uUnitSelect = new JComboBox<>();		//单元号选项框
    private JComboBox<String> uUserIdSelect = new JComboBox<>();	//业主id选项框
    //信号存储页面
    private JComboBox<String> aUnitSelect = new JComboBox<>();		//单元号选项框
    private JComboBox<String> aUserSelect = new JComboBox<>();		//业主id选项框
    private JComboBox<String> aAlertTypeSelect = new JComboBox<>();//警报类型选项框
    
    private JInternalFrame updateInfoInFrame = new JInternalFrame();	//信号处理页面
    private JInternalFrame selectInfoInFrame = new JInternalFrame();	//查询信号页面
    private JInternalFrame addInfoInFrame = new JInternalFrame();		//存储信号页面
    
    private JMenuBar topMenu = new JMenuBar();					//顶部导航栏
    private JMenuItem selectInfoMenuItem = new JMenuItem();			//查询警报选项
    private JMenuItem updateInfoMenuItem = new JMenuItem();			//信号处理选项
    private JMenuItem addInfoMenuItem = new JMenuItem();				//存储信号选项
    
    //------------------------------需要使用的数据属性------------------------------------------------
    private String[] units;		//单元下拉选项
    private String[] alertTypes;//警报下拉选项
    
    
    
    //------------------------------end------------------------------------------------
//    private 
    // End of variables declaration               
    
    public void init() {
    	
    }
    
    /**
     * Creates new form NewJFrame
     */
    public MainPage() {
    	this.units = JDBCUtil.selectUnits();
		this.alertTypes = JDBCUtil.selectAlertItems();
        initComponents();
        updateInfoMenuItemActionPerformed(null);
    }
    
    private void initComponents() {
        setLocation(500, 300);
    	//信号处理菜单项点击事件
		updateInfoMenuItem.addActionListener((evt) -> {
			updateInfoMenuItemActionPerformed(evt);
		});
		 //信号存储菜单项点击事件
		addInfoMenuItem.addActionListener((evt) -> {
			addInfoMenuItemActionPerformed(evt);
		});
		 //信号查询菜单项点击事件
		selectInfoMenuItem.addActionListener((evt) -> {
			selectInfoMenuItemActionPerformed(evt);
		});
        
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        updateInfoInFrame.setTitle("信号处理");
        updateInfoInFrame.setToolTipText("");
        updateInfoInFrame.setVisible(true);

        uOKButton.addActionListener((evt) -> {
            uOKButtonActionPerformed(evt);
        });
        uCancelButton.addActionListener((evt) -> {
        	uCancelButtonActionPerformed(evt);
        });
        uUnitSelect.addActionListener((evt) -> {
        	uUnitSelectActionPerformed(evt);
        });
        //单元号下拉框改变时自动改变业主选项
        uUnitSelect.addItemListener((e) -> {
        	if(e.getStateChange() == ItemEvent.SELECTED) {
        		unitSelectItemChangePerformed(e, uUnitSelect, uUserIdSelect);
        	}
        });
        sUserSelect.addItemListener((e) -> {
        	userSelectItemChangePerformed(e, sUserSelect, sRoomSelect);
        });
        sRoomSelect.addItemListener((e) -> {
        	roomSelectItemChangePerformed(e, sRoomSelect, sUserSelect);
        });
        aUnitSelect.addItemListener((evt) -> {
        	if(evt.getStateChange() == ItemEvent.SELECTED) {
        		unitSelectItemChangePerformed(evt, aUnitSelect, aUserSelect);
        	}
        });
        sSelectButton.addActionListener((e) -> {
        	sSelectButtonActionPerformed(e);
        });
        
         GroupLayout updateInfoInFrameLayout = new GroupLayout(updateInfoInFrame.getContentPane());
        updateInfoInFrame.getContentPane().setLayout(updateInfoInFrameLayout);
        updateInfoInFrameLayout.setHorizontalGroup(
            updateInfoInFrameLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(updateInfoInFrameLayout.createSequentialGroup()
                .addGap(68, 68, 68)
                .addGroup(updateInfoInFrameLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(updateInfoInFrameLayout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addComponent(uUnitSelect, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addGroup(updateInfoInFrameLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(uUserIdSelect, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addGroup(updateInfoInFrameLayout.createSequentialGroup()
                        .addGroup(updateInfoInFrameLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2))
                        .addGap(18, 18, 18)
                        .addGroup(updateInfoInFrameLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(uAlertTypeSelect, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(uValueTextFiled, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)))
                    .addGroup(updateInfoInFrameLayout.createSequentialGroup()
                        .addComponent(uOKButton)
                        .addGap(45, 45, 45)
                        .addComponent(uCancelButton)))
                .addContainerGap(350, Short.MAX_VALUE))
        );
        updateInfoInFrameLayout.setVerticalGroup(
            updateInfoInFrameLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(updateInfoInFrameLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(updateInfoInFrameLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4)
                    .addComponent(uUnitSelect, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(updateInfoInFrameLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(uUserIdSelect, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(updateInfoInFrameLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(uAlertTypeSelect, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34)
                .addGroup(updateInfoInFrameLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(uValueTextFiled, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE))
                .addGap(65, 65, 65)
                .addGroup(updateInfoInFrameLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(uOKButton)
                    .addComponent(uCancelButton))
                .addContainerGap(151, Short.MAX_VALUE))
        );

        addInfoInFrame.setTitle("信号存储");
        addInfoInFrame.setToolTipText("");
        addInfoInFrame.setVisible(false);

        aUserSelect.setModel(new DefaultComboBoxModel<>(new String[] { "item" }));
        aUserSelect.addActionListener((evt) -> {
            aUserSelectActionPerformed(evt);
        });

        jLabel6.setText("值：");

        aOKButton.setText("添加");
        aOKButton.setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        aOKButton.addActionListener((evt) -> {
            aOKButtonActionPerformed(evt);
        });

        aCancelButton.setText("取消");
        aCancelButton.addActionListener((evt) -> {
            aCancelButtonActionPerformed(evt);
        });

        jLabel7.setText("业主id：");

        jLabel8.setText("单元号：");

        aUnitSelect.setModel(new DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel9.setText("警报类型：");

        aAlertTypeSelect.setModel(new DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel10.setText("警报时间：");

        aAlertTypeTextField.setFormatterFactory(new DefaultFormatterFactory(new DateFormatter(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"))));
        aValueTextFiled.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(NumberFormat.getNumberInstance())));
        uValueTextFiled.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(NumberFormat.getNumberInstance())));
        
        GroupLayout addInfoInFrameLayout = new GroupLayout(addInfoInFrame.getContentPane());
        addInfoInFrame.getContentPane().setLayout(addInfoInFrameLayout);
        addInfoInFrameLayout.setHorizontalGroup(
            addInfoInFrameLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(addInfoInFrameLayout.createSequentialGroup()
                .addGap(200, 200, 200)
                .addGroup(addInfoInFrameLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(addInfoInFrameLayout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addGroup(addInfoInFrameLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(addInfoInFrameLayout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addGap(18, 18, 18)
                                .addComponent(aUnitSelect, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                            .addGroup(addInfoInFrameLayout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addGap(18, 18, 18)
                                .addComponent(aUserSelect, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                            .addGroup(addInfoInFrameLayout.createSequentialGroup()
                                .addComponent(aOKButton)
                                .addGap(45, 45, 45)
                                .addComponent(aCancelButton))))
                    .addGroup(addInfoInFrameLayout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addGap(18, 18, 18)
                        .addComponent(aAlertTypeTextField, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE))
                    .addGroup(addInfoInFrameLayout.createSequentialGroup()
                        .addGroup(addInfoInFrameLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel9)
                            .addComponent(jLabel6))
                        .addGap(18, 18, 18)
                        .addGroup(addInfoInFrameLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(aAlertTypeSelect, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(aValueTextFiled, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(247, Short.MAX_VALUE))
        );
        addInfoInFrameLayout.setVerticalGroup(
            addInfoInFrameLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(addInfoInFrameLayout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addGroup(addInfoInFrameLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel8)
                    .addComponent(aUnitSelect, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(addInfoInFrameLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(aUserSelect, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(addInfoInFrameLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(aAlertTypeSelect, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(addInfoInFrameLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(aAlertTypeTextField, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19)
                .addGroup(addInfoInFrameLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(aValueTextFiled, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE))
                .addGap(42, 42, 42)
                .addGroup(addInfoInFrameLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(aOKButton)
                    .addComponent(aCancelButton))
                .addContainerGap(138, Short.MAX_VALUE))
        );

        selectInfoInFrame.setTitle("信号查询");
        selectInfoInFrame.setToolTipText("");
        selectInfoInFrame.setVisible(false);

        jLabel11.setText("单元号：");

        sUnitSelect.setModel(new DefaultComboBoxModel<>(new String[] {}));

        jLabel12.setText("业主id：");

        sUserSelect.setModel(new DefaultComboBoxModel<>(new String[] {}));

        jLabel13.setText("门牌号：");

        sRoomSelect.setModel(new DefaultComboBoxModel<>(new String[] {}));

        jLabel14.setText("警报类型：");

        sAlertTypeSelect.setModel(new DefaultComboBoxModel<>(new String[] {}));

        sSelectButton.setText("查询");

        jTable.setModel(new DefaultTableModel(
            new Object [][] {
                {"", "", "", "", ""},
                {"", "", "", "", ""},
                {"", "", "", "", ""},
                {"", "", "", "", ""}
            },
            new String [] {
                "警报类型", "时间", "范围", "警报值", "业主id"
            }
        ) {
           
			private static final long serialVersionUID = 1L;
			Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane.setViewportView(jTable);

        GroupLayout selectInfoInFrameLayout = new GroupLayout(selectInfoInFrame.getContentPane());
        selectInfoInFrame.getContentPane().setLayout(selectInfoInFrameLayout);
        selectInfoInFrameLayout.setHorizontalGroup(
            selectInfoInFrameLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(selectInfoInFrameLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel11)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sUnitSelect, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel12)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sUserSelect, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(jLabel13)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sRoomSelect, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(jLabel14)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sAlertTypeSelect, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(sSelectButton)
                .addContainerGap(80, Short.MAX_VALUE))
            .addComponent(jScrollPane, GroupLayout.Alignment.TRAILING)
        );
        selectInfoInFrameLayout.setVerticalGroup(
            selectInfoInFrameLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(selectInfoInFrameLayout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(selectInfoInFrameLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(sUnitSelect, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12)
                    .addComponent(sUserSelect, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13)
                    .addComponent(sRoomSelect, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14)
                    .addComponent(sAlertTypeSelect, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(sSelectButton))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane, GroupLayout.PREFERRED_SIZE, 334, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(100, Short.MAX_VALUE))
        );

        updateInfoMenuItem.setText("信号处理");
        updateInfoMenuItem.setToolTipText("");
        updateInfoMenuItem.addActionListener((evt) -> {
            updateInfoMenuItemActionPerformed(evt);
        });
        topMenu.add(updateInfoMenuItem);

        addInfoMenuItem.setText("信号存储");
        topMenu.add(addInfoMenuItem);

        selectInfoMenuItem.setText("信号查询");
        selectInfoMenuItem.setToolTipText("");
        topMenu.add(selectInfoMenuItem);

        setJMenuBar(topMenu);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(updateInfoInFrame, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(addInfoInFrame))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(selectInfoInFrame, GroupLayout.Alignment.TRAILING))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(updateInfoInFrame, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(addInfoInFrame, GroupLayout.Alignment.TRAILING))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(selectInfoInFrame, GroupLayout.Alignment.TRAILING))
        );
         
        //TODO
        setResizable(false);
        pack();
    }// </editor-fold>                        

    /**
     * 查询页面查询按钮点击事件
     * @param e
     */
    private void sSelectButtonActionPerformed(ActionEvent e) {
		String username = sUserSelect.getSelectedItem().toString();
		String alertType = sAlertTypeSelect.getSelectedItem().toString();
		Object[][] tableModel = JDBCUtil.selectAlertTableModels( username, alertType);
		
		//TODO
		jTable.setModel(new DefaultTableModel(
				tableModel,
	            new String [] {
	                "警报类型", "时间", "范围", "警报值", "业主id"
	            }
	        ) {
	           
				private static final long serialVersionUID = 1L;
				Class[] types = new Class [] {
	                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class
	            };
	            boolean[] canEdit = new boolean [] {
	                false, false, false, false, false
	            };

	            public Class getColumnClass(int columnIndex) {
	                return types [columnIndex];
	            }

	            public boolean isCellEditable(int rowIndex, int columnIndex) {
	                return canEdit [columnIndex];
	            }
	        });
	}

	/**
     * 取消按钮点击
     * @param evt
     */
    private void uCancelButtonActionPerformed(ActionEvent evt) {
    	uAlertTypeSelect.setSelectedIndex(0);
    	uUnitSelect.setSelectedIndex(0);
    	uValueTextFiled.setValue("0");
	}

	private void unitSelectItemChangePerformed(ItemEvent e, JComboBox<String> selectJcb, JComboBox<String> jcb) {
		String selectItem = selectJcb.getSelectedItem().toString();
		int unit = 0;
		if("任意".equals(selectItem)) {
			unit = -1;
		} else {
			unit = Integer.parseInt(selectItem.substring(0, 1));
		}
		String[] strs = JDBCUtil.selectUsernamesByUnit(unit);
		jcb.setModel(new DefaultComboBoxModel<>(strs));
	}

	/**
	 * 打开信号查询页面
	 * @param evt
	 */
	private void selectInfoMenuItemActionPerformed(ActionEvent evt) {
    	addInfoInFrame.setVisible(false);
		updateInfoInFrame.setVisible(false);
		selectInfoInFrame.setVisible(true);
		sUnitSelect.setModel(new DefaultComboBoxModel<>(new String[] {}));
		sUnitSelect.addItem("任意");
		for (String string : units) {
			sUnitSelect.addItem(string);
		}
		unitSelectItemChangePerformed(null, sUnitSelect, sUserSelect);
		unitSelectItemChangeRoomPerfored(null, sUnitSelect, sRoomSelect);
		userSelectItemChangePerformed(null, sUserSelect, sRoomSelect);
		roomSelectItemChangePerformed(null, sRoomSelect, sUserSelect);
		sAlertTypeSelect.setModel(new DefaultComboBoxModel<>(new String[] {}));
		sAlertTypeSelect.addItem("任意");
		for (String string : alertTypes) {
			sAlertTypeSelect.addItem(string);
		}
	}

	private void unitSelectItemChangeRoomPerfored(ItemEvent e, JComboBox<String> selectJcb,	JComboBox<String> jcb) {
		String selectItem = selectJcb.getSelectedItem().toString();
		int unit = 0;
		if("任意".equals(selectItem)) {
			unit = -1;
		} else {
			unit = Integer.parseInt(selectItem.substring(0, 1));
		}
		String[] strs = JDBCUtil.selectRoomsByUnit(unit);
		jcb.setModel(new DefaultComboBoxModel<>(strs));
		
	}

	private void roomSelectItemChangePerformed(ItemEvent e, JComboBox<String> selectJcb, JComboBox<String> jcb) {
		String room = selectJcb.getSelectedItem().toString();
		String name = JDBCUtil.selectUsernameByRoom(room);
		if(jcb.getSelectedItem().equals(name)) return;
		int count = jcb.getItemCount();
		if(name != null)
		for (int i = 0; i < count; i++) {
			if(name.equals(jcb.getItemAt(i))) {
				jcb.setSelectedIndex(i);
			}
		}
	}

	private void userSelectItemChangePerformed(ItemEvent e, JComboBox<String> selectJcb, JComboBox<String> jcb) {
		String username = selectJcb.getSelectedItem().toString();
		String room = JDBCUtil.selectRoomByUsername(username);
		if(jcb.getSelectedItem().equals(room)) return;
		int count = jcb.getItemCount();
		if(room != null)
		for (int i = 0; i < count; i++) {
			if(room.equals(jcb.getItemAt(i))) {
				jcb.setSelectedIndex(i);
			}
		}
	}

	/**
	 * 打开存储信号页面
	 * @param evt
	 */
	private void addInfoMenuItemActionPerformed(ActionEvent evt) {
    	selectInfoInFrame.setVisible(false);
		updateInfoInFrame.setVisible(false);
		addInfoInFrame.setVisible(true);
		aUnitSelect.setModel(new DefaultComboBoxModel<>(units));
		aAlertTypeSelect.setModel(new DefaultComboBoxModel<>(alertTypes));
		unitSelectItemChangePerformed(null, aUnitSelect, aUserSelect);
	}

	/**
	 * 打开信号处理页面
	 * @param evt
	 */
	private void updateInfoMenuItemActionPerformed(ActionEvent evt) {                                                   
    	selectInfoInFrame.setVisible(false);
		updateInfoInFrame.setVisible(true);
		addInfoInFrame.setVisible(false);
		uUnitSelect.setModel(new DefaultComboBoxModel<>(units));
		unitSelectItemChangePerformed(null, uUnitSelect, uUserIdSelect);
		uAlertTypeSelect.setModel(new DefaultComboBoxModel<>(alertTypes));
    }                                                  

    /**
     * 信号处理
     * @param evt
     */
    private void uOKButtonActionPerformed(ActionEvent evt) {                                          
    	int value = Integer.parseInt(uValueTextFiled.getValue().toString());
    	String alertItem = uAlertTypeSelect.getSelectedItem().toString();
    	String username = uUserIdSelect.getSelectedItem().toString();
    	WarningConfig config = JDBCUtil.selectWarningConfigByUsername(username, alertItem);
    	if(config != null) {
    		if(value > config.getSafemax()) {
    			JOptionPane.showMessageDialog(this, alertItem + "值大于了安全警报！", "警告", JOptionPane.WARNING_MESSAGE);
    			saveWarningMessage(username, alertItem, value);
    		}
    		else if(value < config.getSafemin()) {
    			JOptionPane.showMessageDialog(this, alertItem + "值小于了安全警报！", "警告", JOptionPane.WARNING_MESSAGE);
    			saveWarningMessage(username, alertItem, value);
    		}
    		else {
    			JOptionPane.showMessageDialog(this, "修改成功！");
    		}
    	}
    	
    }                                                                    

    /**
     * 有警报时保存改警报信息
     * @param username
     * @param alertItem
     * @param value
     */
    private void saveWarningMessage(String username, String alertItem, int value) {
		JDBCUtil.saveWarningMessage(username, alertItem, value);
	}

	private void aUserSelectActionPerformed(ActionEvent evt) {                                            
        // TODO add your handling code here:
    }                                           

    private void aOKButtonActionPerformed(ActionEvent evt) {     
        String username = aUserSelect.getSelectedItem().toString();
        String alertItem = aAlertTypeSelect.getSelectedItem().toString();
        String value = aValueTextFiled.getText();
        if("".equals(value)) {
        	JOptionPane.showMessageDialog(this, "值不能为空！");
        	return;
        }
        int intValue = Integer.parseInt(value);
        String time = aAlertTypeTextField.getText();
        if("".equals(time)) {
        	JOptionPane.showMessageDialog(this, "时间不能为空！");
        	return;
        }
    	int result = JDBCUtil.saveWarningMessage(username, alertItem, intValue, time);
    	if(result > 0) {
    		JOptionPane.showMessageDialog(this, "保存成功！");
    	}
    }                                         

    private void aCancelButtonActionPerformed(ActionEvent evt) {                                              
        aUnitSelect.setSelectedIndex(0);
//        aUserSelect.setSelectedIndex(0);
        aAlertTypeSelect.setSelectedIndex(0);
        aAlertTypeTextField.setText("");
        aValueTextFiled.setText("");
    }                                             

    private void uUnitSelectActionPerformed(ActionEvent evt) {                                            
        // TODO add your handling code here:
    }                                           

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
    	new MainPage().setVisible(true);
    }

}
