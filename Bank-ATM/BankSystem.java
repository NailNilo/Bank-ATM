import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import java.text.*;
import java.io.*;
import java.awt.PrintJob.*;
import javax.swing.plaf.metal.*;

public class BankSystem extends JFrame implements ActionListener, ItemListener {

	// Main Place on Form where All Child Forms will Shown.
	ImageIcon icon = new ImageIcon(this.getClass().getResource("bank.jpg"));
	Image img = icon.getImage();
	private JDesktopPane desktop = new JDesktopPane() {
		public void paintComponent(Graphics g) {
			g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
		}
	};

	// For Program's MenuBar.
	private JMenuBar bar;

	// All the Main Menu of the Program.
	private JMenu mnuFile, mnuEdit, mnuView, mnuWin;

	private JMenuItem addNew, end; // File Menu Options.
	private JMenuItem deposit, withdraw, delRec, search, searchName; // Edit Menu Options.
	private JMenuItem oneByOne, allCustomer; // View Menu Options.
	private JMenuItem style; // Option Menu Option.
	private JMenuItem close, closeAll; // Window Menu Options.

	private JPanel statusBar = new JPanel();
	private JLabel welcome;

	// Making the LookAndFeel Menu.
	private String strings[] = { "1. Metal", "2. Motif", "3. Windows" };
	private ButtonGroup group = new ButtonGroup();
	private JRadioButtonMenuItem radio[] = new JRadioButtonMenuItem[strings.length];

	// Getting the Current System Date.
	private java.util.Date currDate = new java.util.Date();
	private SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
	private String d = sdf.format(currDate);

	// Variable use in Reading the BankSystem Records File & Store it in an Array.
	private int rows = 0;
	private int total = 0;

	// String Type Array use to Load Records From File.
	private String records[][] = new String[500][6];

	// Variable for Reading the BankSystem Records File.
	private FileInputStream fis;
	private DataInputStream dis;

	public BankSystem() {

		super("Bank App");

		UIManager.addPropertyChangeListener(new UISwitchListener((JComponent) getRootPane()));

		// Creating the MenuBar.
		bar = new JMenuBar();

		// Setting the Main Window of Program.
		setSize(700, 550);
		setJMenuBar(bar);

		// Closing Code of Main Window.
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				quitApp();
			}
		});

		// Creating the MenuBar Items.
		mnuFile = new JMenu("File");
		mnuFile.setMnemonic((int) 'F');
		mnuEdit = new JMenu("Edit");
		mnuEdit.setMnemonic((int) 'E');
		mnuView = new JMenu("View");
		mnuView.setMnemonic((int) 'V');
		mnuWin = new JMenu("Window");
		mnuWin.setMnemonic((int) 'W');

		// Creating the MenuItems of Program.
		// MenuItems for FileMenu.
		addNew = new JMenuItem("Create New Account");
		addNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, Event.CTRL_MASK));
		addNew.setMnemonic((int) 'N');
		addNew.addActionListener(this);
		end = new JMenuItem("Quit");
		end.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, Event.CTRL_MASK));
		end.setMnemonic((int) 'Q');
		end.addActionListener(this);

		// MenuItems for EditMenu.
		deposit = new JMenuItem("Deposit Money");
		deposit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, Event.CTRL_MASK));
		deposit.setMnemonic((int) 'T');
		deposit.addActionListener(this);
		withdraw = new JMenuItem("Withdraw Money");
		withdraw.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, Event.CTRL_MASK));
		withdraw.setMnemonic((int) 'W');
		withdraw.addActionListener(this);
		delRec = new JMenuItem("Delete Account");
		delRec.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, Event.CTRL_MASK));
		delRec.setMnemonic((int) 'D');
		delRec.addActionListener(this);
		search = new JMenuItem("Search Account By ID.");
		search.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK));
		search.setMnemonic((int) 'S');
		search.addActionListener(this);
		searchName = new JMenuItem("Search Account By Name");
		searchName.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, Event.CTRL_MASK));
		searchName.setMnemonic((int) 'M');
		searchName.addActionListener(this);

		// MenuItems for ViewMenu.
		oneByOne = new JMenuItem("View Accounts One By One");
		oneByOne.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, Event.CTRL_MASK));
		oneByOne.addActionListener(this);
		allCustomer = new JMenuItem("View All Accounts");
		allCustomer.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, Event.CTRL_MASK));
		allCustomer.addActionListener(this);

		// MenuItems for WindowMenu.
		close = new JMenuItem("Close Active Window");
		close.setMnemonic((int) 'C');
		close.addActionListener(this);
		closeAll = new JMenuItem("Close All Windows...");
		closeAll.setMnemonic((int) 'A');
		closeAll.addActionListener(this);

		// Adding MenuItems to Menu.

		// File Menu Items.
		mnuFile.add(addNew);
		mnuFile.addSeparator();
		mnuFile.add(end);

		// Edit Menu Items.
		mnuEdit.add(deposit);
		mnuEdit.add(withdraw);
		mnuEdit.addSeparator();
		mnuEdit.add(delRec);
		mnuEdit.addSeparator();
		mnuEdit.add(search);
		mnuEdit.add(searchName);

		// View Menu Items.
		mnuView.add(oneByOne);
		mnuView.addSeparator();
		mnuView.add(allCustomer);

		// Window Menu Items.
		mnuWin.add(close);
		mnuWin.addSeparator();
		mnuWin.add(closeAll);

		// Adding Menus to Bar.
		bar.add(mnuFile);
		bar.add(mnuEdit);
		bar.add(mnuView);
		bar.add(mnuWin);

		// Creating the StatusBar of Program.
		welcome = new JLabel("Welcome, Today is " + d + " ", JLabel.RIGHT);
		welcome.setForeground(Color.black);
		statusBar.setLayout(new BorderLayout());
		statusBar.add(welcome, BorderLayout.EAST);
		// System.out.println("Before Deposit");

		// For Making the Dragging Speed of Internal Frames Faster.
		desktop.putClientProperty("JDesktopPane.dragMode", "outline");

		// Setting the Contents of Programs.
		getContentPane().add(desktop, BorderLayout.CENTER);
		getContentPane().add(statusBar, BorderLayout.SOUTH);

		// Showing The Main Form of Application.
		setVisible(true);

	}

	static void Track() {
		System.out.println("Before Deposit");
	}
	// Function For Performing different Actions By Menus of Program.

	public void actionPerformed(ActionEvent ae) {

		Object obj = ae.getSource();

		if (obj == addNew) {

			boolean b = openChildWindow("Create New Account");
			if (b == false) {
				NewAccount newAcc = new NewAccount();
				desktop.add(newAcc);
				newAcc.show();
			}
		} else if (obj == end) {

			quitApp();

		} else if (obj == deposit) {

			boolean b = openChildWindow("Deposit Money");
			if (b == false) {
				DepositMoney depMon = new DepositMoney();
				desktop.add(depMon);
				depMon.show();
			}

		} else if (obj == withdraw) {

			boolean b = openChildWindow("Withdraw Money");
			if (b == false) {
				WithdrawMoney withMon = new WithdrawMoney();
				desktop.add(withMon);
				withMon.show();
			}

		} else if (obj == delRec) {

			boolean b = openChildWindow("Delete Account Holder");
			if (b == false) {
				DeleteCustomer delCus = new DeleteCustomer();
				desktop.add(delCus);
				delCus.show();
			}

		} else if (obj == search || obj == searchName) {

			boolean b = openChildWindow("Search Account ID");
			if (b == false) {
				FindAccount fndAcc = new FindAccount();
				desktop.add(fndAcc);
				fndAcc.show();
			}

		} else if (obj == searchName) {

			boolean b = openChildWindow("Search Account Name");
			if (b == false) {
				FindName fndNm = new FindName();
				desktop.add(fndNm);
				fndNm.show();
			}

		} else if (obj == oneByOne)

		{

			boolean b = openChildWindow("View Account Holders");
			if (b == false) {
				ViewOne vwOne = new ViewOne();
				desktop.add(vwOne);
				vwOne.show();
			}

		} else if (obj == allCustomer) {

			boolean b = openChildWindow("View All Account Holders");
			if (b == false) {
				ViewCustomer viewCus = new ViewCustomer();
				desktop.add(viewCus);
				viewCus.show();
			}

		} else if (obj == close) {

			try {
				desktop.getSelectedFrame().setClosed(true);
			} catch (Exception CloseExc) {
			}

		} else if (obj == closeAll) {

			JInternalFrame Frames[] = desktop.getAllFrames(); // Getting all Open Frames.
			for (int getFrameLoop = 0; getFrameLoop < Frames.length; getFrameLoop++) {
				try {
					Frames[getFrameLoop].setClosed(true); // Close the frame.
				} catch (Exception CloseExc) {
				} // if we can't close it then we have a problem.
			}

		}

	}

	// Function For Closing the Program.

	private void quitApp() {

		try {
			// Show a Confirmation Dialog.
			int reply = JOptionPane.showConfirmDialog(this,
					"Are you sure you want to quit?",
					"Bank App - Exit", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
			// Check the User Selection.
			if (reply == JOptionPane.YES_OPTION) {
				setVisible(false); // Hide the Frame.
				dispose(); // Free the System Resources.
				System.exit(0); // Close the Application.
			} else if (reply == JOptionPane.NO_OPTION) {
				setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			}
		}

		catch (Exception e) {
		}

	}

	// Loop Through All the Opened JInternalFrame to Perform the Task.

	private boolean openChildWindow(String title) {

		JInternalFrame[] childs = desktop.getAllFrames();
		for (int i = 0; i < childs.length; i++) {
			if (childs[i].getTitle().equalsIgnoreCase(title)) {
				childs[i].show();
				return true;
			}
		}
		return false;

	}

	// Function use to load all Records from File when Application Execute.

	boolean populateArray() {

		boolean b = false;
		try {
			fis = new FileInputStream("Bank.dat");
			dis = new DataInputStream(fis);
			// Loop to Populate the Array.
			while (true) {
				for (int i = 0; i < 6; i++) {
					records[rows][i] = dis.readUTF();
				}
				rows++;
			}
		} catch (Exception ex) {
			total = rows;
			if (total == 0) {
				JOptionPane.showMessageDialog(null, "No Accounts Found",
						"Bank App - EmptyFile", JOptionPane.PLAIN_MESSAGE);
				b = false;
			} else {
				b = true;
				try {
					dis.close();
					fis.close();
				} catch (Exception exp) {
				}
			}
		}
		return b;

	}

	public static void main(String args[]) {

		new BankSystem();
		Track();

	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub

	}

}