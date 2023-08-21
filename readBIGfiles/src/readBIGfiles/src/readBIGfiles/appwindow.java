package readBIGfiles;

/*   
Copyright 2023 Pantelis Rodis

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this software except in compliance with the License.
You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileSystemView;

public class appwindow extends Thread{
	
	File chosenfile=new File("/");
	boolean read=false, bof=false, eof=false;
	Thread t;
	int step=200000;

	private JTextArea textArea;
	private JFrame frame;
	private JButton button;
	private JButton btnNewButton;
	private JButton btnNewButton_0;
	private JButton btnNewButton_1;
	private JButton btnNewButton_2;
	private JTextField textField_1;
	private JTextField textField_2;
	private JLabel lblNewLabel;
	private JLabel lblNewLabel2;
	
	int lastl=0;

	public static void main(String[] args) {
		 String LF;
		 
		 LF=UIManager.getSystemLookAndFeelClassName();
		
		 try {
	            
		 UIManager.setLookAndFeel(LF);
		 } 
		    catch (UnsupportedLookAndFeelException e) {

		    }
		    catch (ClassNotFoundException e) {

		    }
		    catch (InstantiationException e) {

		    }
		    catch (IllegalAccessException e) {

		    }
		 
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					appwindow window = new appwindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


	//initialize app
	public appwindow() { 
		initialize();
	}

	private void initialize() {
		//make GUI
		frame = new JFrame();
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/assets/icon.png")));
		frame.setBounds(100, 100, 1000, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Read BIG Files");
		
		textArea = new JTextArea();
		textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
		textArea.setBackground(Color.WHITE);
		
		JScrollPane jsp=new JScrollPane(textArea,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		frame.getContentPane().add(jsp);
		
		Panel panel = new Panel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEADING);
		frame.getContentPane().add(panel, BorderLayout.NORTH);
		
		btnNewButton_0 = new JButton("");
		btnNewButton_0.setToolTipText("create new file");
		btnNewButton_0.setIcon(new ImageIcon(appwindow.class.getResource("/assets/new.png")));
		btnNewButton_0.setPreferredSize(new Dimension(30, 30));
		panel.add(btnNewButton_0);
		btnNewButton_0.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newfile();
			}
		});
		
		button = new JButton("");
		button.setForeground(new Color(255, 255, 255));
		button.setToolTipText("choose file");
		button.setPreferredSize(new Dimension(40, 30));
		button.setFont(new Font("SansSerif", Font.PLAIN, 12));
		button.setBackground(Color.WHITE);
		button.setIcon(new ImageIcon(appwindow.class.getResource("/assets/folder.jpg")));
		panel.add(button);
		button.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){  
		           choosefile(); 
			}
		});
		
		btnNewButton_1 = new JButton("");
		btnNewButton_1.setToolTipText("save current view");
		btnNewButton_1.setIcon(new ImageIcon(appwindow.class.getResource("/assets/save.png")));
		btnNewButton_1.setPreferredSize(new Dimension(30, 30));
		panel.add(btnNewButton_1);
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				savetextArea();
			}
		});
		
		textField_1 = new JTextField();
		textField_1.setEditable(false);
		
		textField_1.setBackground(Color.getHSBColor(
				Color.RGBtoHSB(240,244,255,null)[0],
				Color.RGBtoHSB(240,244,255,null)[1],
				Color.RGBtoHSB(240,244,255,null)[2]));
		textField_1.setForeground(Color.BLUE);
		textField_1.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		textField_1.setColumns(30);
		textField_1.setVisible(true);
		panel.add(textField_1);
		
		lblNewLabel = new JLabel("   ");
		lblNewLabel.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		lblNewLabel.setEnabled(false);
		panel.add(lblNewLabel);
		
		textField_2 = new JTextField();
		textField_2.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		textField_2.setColumns(8);
		textField_2.setText(Integer.toString(step));
		panel.add(textField_2);
		textField_2.getDocument().addDocumentListener(new DocumentListener() {
			  public void changedUpdate(DocumentEvent e) {
				 chk();
			  }
			  
			  public void removeUpdate(DocumentEvent e) {
				 chk();
			  }
			  
			  public void insertUpdate(DocumentEvent e) {
				chk();
			  }
		});
	
		
		lblNewLabel2 = new JLabel("   ");
		lblNewLabel2.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		lblNewLabel2.setEnabled(false);
		panel.add(lblNewLabel2);		
		
		btnNewButton = new JButton("");
		btnNewButton.setSelectedIcon(null);
		btnNewButton.setToolTipText("read next lines");
		btnNewButton.setPreferredSize(new Dimension(30, 30));
		btnNewButton.setEnabled(false);
		btnNewButton.setFont(new Font("SansSerif", Font.PLAIN, 12));
		btnNewButton.setBackground(Color.WHITE);
		btnNewButton.setIcon(new ImageIcon(appwindow.class.getResource("/assets/play.png")));
		panel.add(btnNewButton);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				readfile();
			}
		});
		
		btnNewButton_2 = new JButton("");
		btnNewButton_2.setToolTipText("about");
		btnNewButton_2.setBackground(new Color(184, 217, 241));
		btnNewButton_2.setFont(new Font("SansSerif", Font.PLAIN, 11));
		btnNewButton_2.setIcon(new ImageIcon(appwindow.class.getResource("/assets/q.png")));
		btnNewButton_2.setPreferredSize(new Dimension(28, 28));
		panel.add(btnNewButton_2);
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				about();
			}
		});
		
		about();
	}

	//check input, it defines the number ogf lines read in each step 
	//and must be a number between 0 and 1000000
	private void chk() {
		int tst;
		
		try {
		    tst = Integer.parseInt(textField_2.getText());
		
		    if(tst>0 && tst<=1000000) {
				step=tst;
				lastl=0;
				btnNewButton.setEnabled(true);
			}else {
				 textField_1.setText("You must set a number between 0 and 1000000");
				 btnNewButton.setEnabled(false);
			}
		 
		} catch (NumberFormatException e) {
			textField_1.setText("You must set a number between 0 and 1000000");
			btnNewButton.setEnabled(false);
		}
	}
	
	//when press the play button read file or pause reading
	private void readfile() {
		if(!read) {
			readthefile();
		}else {
			stopreading();
			btnNewButton.setIcon(new ImageIcon(appwindow.class.getResource("/assets/play.png")));
			lblNewLabel.setText("Read next");
			textField_2.setEnabled(true);
			lblNewLabel2.setText("lines");
		}
	}
	
	//read the file
	private void readthefile() {
		textArea.setEditable(true);
		textArea.setForeground(Color.BLACK);
		textArea.setText("");
		btnNewButton_1.setEnabled(false);
		read=true;
		lblNewLabel.setText("   ");
		textField_2.setEnabled(false);
		lblNewLabel2.setText("   ");
		btnNewButton.setIcon(new ImageIcon(appwindow.class.getResource("/assets/stop.png")));
		
		lastl++;
		(new readthread()).start(lastl);
		
	}
	
	//start a new text file
	private void newfile() {
		textArea.setText("");
		textArea.setEditable(true);
		textArea.setForeground(Color.BLACK);
		btnNewButton.setEnabled(false);
		frame.setTitle("Read BIG Files");
		textField_1.setText("Save changes before exit");
		lblNewLabel.setEnabled(false);
		textField_2.setEnabled(false);
		lblNewLabel2.setEnabled(false);
		btnNewButton_1.setEnabled(true);
	}
	
	//thread that read file in steps
	class readthread implements Runnable {
		   private Thread t;
		   private String threadName="Read";
		   int s;
		   int lastline;
		   int cnt;

		   public void run() {
			   cnt=step*(lastline-1);
			   int cnt2=0;
			   
			   textArea.setEditable(false);
			   textField_1.setText("Reading "+chosenfile.getName()+". Please wait...");
			   String mes="";

			   try{
			    	Scanner scanner = new Scanner(chosenfile);

			    	StringBuilder sb = new StringBuilder();
			    	
			    	int cl=0; //number of current line
			    	
			    	while(scanner.hasNext() && read ){
			    		String s=scanner.nextLine();

			    		if(cl>=cnt && cl<step*(lastline)) {
			    			sb.append(s+"\n");
			    			cnt++;
			    			cnt2++;
			    		}

			    		cl++;
			    		
			    		if(cl>step*(lastline)) {
			    			break;
			    		}
			    	}
			    	
			    	if(!scanner.hasNext()) {
			    		eof=true;
			    		lastl=0;
			    		mes=". End Of File reached.";
			    		lblNewLabel.setText("");
			    		textField_2.setEnabled(false);
			    		lblNewLabel2.setText("");
			    	}else {
			    		eof=false;
			    		mes="";
			    		btnNewButton.setEnabled(true);
			    		lblNewLabel.setText("Read next");
			    		textField_2.setEnabled(true);
			    		lblNewLabel2.setText("lines");
			    	}
			    	
			    	if(step*(lastline-1)+1==1) {
			    		bof=true;
			    	}else{
			    		bof=false;
			    	}
			    	
			    	scanner.close();
			    	
			    	textArea.setEditable(true);
					textArea.setText("");
					textArea.setText(sb.toString());			
			    	read=false;  
				}
				catch (IOException e) {
				       e.printStackTrace();
				   }
			   textField_1.setText("lines: "+(step*(lastline-1)+1)+"-"+((step*(lastline-1)+cnt2))+mes);
			   btnNewButton.setIcon(new ImageIcon(appwindow.class.getResource("/assets/play.png")));
			   btnNewButton_1.setEnabled(true);
			   
		   }
		   
		   public void start (int lastline) {
			   this.lastline=lastline;
		         t = new Thread (this, threadName);
		         t.start ();
		   }
	}
	
	//pause reading
	private void stopreading() {
		read=false;
	}
	
	//save current view
	private void savetextArea() {
		File file2save;
		JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		
		if(chosenfile.isFile()) {
			fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getParentDirectory(chosenfile));
		}
		
		fileChooser.setDialogTitle("Save as");
		textField_1.setText("Choose file to save.");
		 
		int userSelection = fileChooser.showSaveDialog(frame);
		
		int n=0;
		if (userSelection == JFileChooser.APPROVE_OPTION && fileChooser.getSelectedFile().isFile()) {
				n = JOptionPane.showConfirmDialog( frame,
			    "replace file \""+fileChooser.getSelectedFile().getName()+"\"?","File exists",
			    JOptionPane.YES_NO_OPTION);
		}
		
		
		if (userSelection == JFileChooser.APPROVE_OPTION && n==0) {
			file2save = fileChooser.getSelectedFile();
		    textField_1.setText("Saving to "+chosenfile.getName());

		    FileWriter fw = null;
		    BufferedWriter bw = null;
		    PrintWriter pw = null;

		    try {
		    	try {
		    		fw = new FileWriter(file2save,false);
		    	} catch (IOException e) {
		    		e.printStackTrace();
		    	}
		    	bw = new BufferedWriter(fw);
		    	pw = new PrintWriter(bw);
		    	pw.println(textArea.getText());
		    	pw.flush();
		    }finally {
		    	try {
		    		pw.close();
		    		bw.close();
		    		fw.close();
		    	} catch (IOException io) { 
	        		}
		    }
		    textField_1.setText("File saved.");
		}else {
			textField_1.setText("File not saved.");
		}
	
	}
	
	//about the app
	private void about() {
		textArea.setEditable(false);
		textArea.setForeground(Color.BLUE);
		textField_1.setText("");
		lblNewLabel.setEnabled(false);
		textField_2.setEnabled(false);
		lblNewLabel2.setEnabled(false);
		btnNewButton_1.setEnabled(false);
		frame.setTitle("Read BIG Files");
		textArea.setText(
				"\n \t Read BIG Files"+
				"\n \t is an application for viewing and editing really big files."+
				"\n\n \t Choose a file, set the number of lines to be displayed on every screen"+
				"\n \t and read its content. You may edit and save current view in a new text file."+
				"\n \t You may also create and edit new file from scratch."+
				"\n\n \t This application is available under the Apache License."+
				"\n \t https://github.com/rodispantelis/tools/readBIGfiles"+
				"\n\n \t Icons retrieved from wikimedia (https://commons.wikimedia.org)"+
				"\n\n \t 2023 Pantelis Rodis"
				);
	}
	
	//file chooser
	private void choosefile() {
		JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

			if(chosenfile.isFile()) {
		jfc = new JFileChooser(FileSystemView.getFileSystemView().getParentDirectory(chosenfile));
			}
		int returnValue = jfc.showOpenDialog(null);

		if (returnValue == JFileChooser.APPROVE_OPTION) {
			textArea.setEditable(false);
			textArea.setForeground(Color.BLUE);
			textArea.setText("");
			chosenfile = jfc.getSelectedFile();
			btnNewButton.setEnabled(true);
			textField_1.setEditable(false);
			lblNewLabel.setEnabled(true);
			textField_2.setEnabled(true);
			lblNewLabel2.setEnabled(true);
			btnNewButton_1.setEnabled(false);
			frame.setTitle("Read BIG Files: "+chosenfile.getPath());
			lastl=0;
			readfile();
		}
	}
}
