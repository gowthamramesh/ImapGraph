import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Frame;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

public class MainImapGraph extends JFrame
{
	private static MainImapGraph	mainFrame;
	private static File	selectedExcel;
	private JPanel	basePanel;
	private JPanel	mainPanel;
	
	public MainImapGraph()
	{
		this.setTitle("I2D Graph plotter");
		initComponents();
	}
	
	private void initComponents()
	{

		basePanel = new JPanel();
		basePanel.setLayout(new BorderLayout());
		mainPanel = MainPanel.getMainPanel();
		basePanel.add(mainPanel,BorderLayout.NORTH);
		basePanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		getContentPane().add(basePanel);
		setPreferredSize(new Dimension(600,600));

		pack();
		
	}


	public static void main(String args[])
	{
		showMainForm();
	}

	private static void showMainForm()
	{
		// Create mainFrame in EDT thread
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					mainFrame = new MainImapGraph();
					mainFrame.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}

		});
	}

	public static void setSelectedExcelFile(File selectedFile)
	{
		selectedExcel = selectedFile;
		
	}

}
