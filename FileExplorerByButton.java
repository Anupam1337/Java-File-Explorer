import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.swing.*;
public class FileExplorerByButton extends JFrame {
	/**
	 * Coded By ANUPAM MISHRA ( https://github.com/Anupam1337 )
	 */
	private static final long serialVersionUID = 1L;
	JButton BackB,AddFolderB,DeleteFolderB;
	JLabel GoToL;
	JTextField jtf;
	JTextField GoToTF;
	JCheckBox VisibleCB;
	JScrollPane ViewPanel;
	File path;
	public FileExplorerByButton() {
		ViewPanel=new JScrollPane();
		ViewPanel.setBorder(null);
		ViewPanel.setBounds(100, 80, 1500, 1500);
		jtf=new JTextField();
		BackB=new JButton("Back");
		GoToL=new JLabel("Go To");
		GoToTF=new JTextField();
		VisibleCB=new JCheckBox("Show Hidden File/Folder");
		AddFolderB=new JButton("Add File/Folder");
		DeleteFolderB=new JButton("Delete File/Folder");
		BackB.setBounds(50, 25, 100, 30);
		GoToL.setBounds(200,25,100,30);
		GoToTF.setBounds(250, 25, 200, 30);
		VisibleCB.setBounds(450, 25, 160, 30);
		AddFolderB.setBounds(650, 25, 120, 30);
		DeleteFolderB.setBounds(800, 25, 130, 30);
		add(BackB);
		add(GoToL);
		add(GoToTF);
		add(VisibleCB);
		add(AddFolderB);
		add(DeleteFolderB);
		add(ViewPanel);
		BackB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(path==null ||path.getParent()==null)
					LoadDir();
				else
					openFolder(path.getParent());
			}
		});
		GoToTF.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(GoToTF.getText().equalsIgnoreCase("Root"))
					LoadDir();
				else if(new File(GoToTF.getText()).exists()) {
					openFolder(GoToTF.getText());
				}
				else {
                    JOptionPane.showMessageDialog(rootPane, "Invalid Directory", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		AddFolderB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String newDir=JOptionPane.showInputDialog("Enter new Folder Name : ");
                if(new File(path.getAbsolutePath()+"\\"+newDir).exists())
                    JOptionPane.showMessageDialog(null, "File/Folder already exist");
                else {
                    File newF=new File(path.getAbsolutePath()+"\\"+newDir);
                    if(newDir.contains(".")) {
                        try {
							newF.createNewFile();
						} catch (IOException e1) {
							
						}
                        JOptionPane.showMessageDialog(null, "File Created Successfully");
                    }
                    else {
                        newF.mkdir();
                        JOptionPane.showMessageDialog(null, "Folder Created Successfully");
                    }
                    openFolder(path.getAbsolutePath());
                }
			}
		});
		DeleteFolderB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String newDir=JOptionPane.showInputDialog("Enter File/Folder Name : ");
                if(!new File(path.getAbsolutePath()+"\\"+newDir).exists())
                    JOptionPane.showMessageDialog(null, "File/Folder doesn't exist");
                else {
                    File newF=new File(path.getAbsolutePath()+"\\"+newDir);
                    newF.delete();
                    openFolder(path.getAbsolutePath());
                }
			}
		});
		ImageIcon icon = new ImageIcon("F:\\thor.jpg");
        Image img = icon.getImage();
		this.setIconImage(img);
		LoadDir();
		//openFolder("F:\\");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(1000,1000);
		this.setLayout(null);
		this.setVisible(true);
	}
	public void LoadDir() {
		ViewPanel.removeAll();
		GoToTF.setText("Root");
        AddFolderB.setEnabled(false);
        DeleteFolderB.setEnabled(false);
		this.setTitle("My Explorer");
		File[] root=File.listRoots();
		int y=0,x=0;
		for(File f1:root) {
			double UsedSpace=0,FreeSpace=0;
			if(f1.getTotalSpace()>0) {
				UsedSpace=(double)(f1.getTotalSpace()-f1.getFreeSpace())/f1.getTotalSpace()*100;
				FreeSpace=(100-UsedSpace);
			}
			String UsedSpaceS=UsedSpace+"";
			String FreeSpaceS=FreeSpace+"";
			if(UsedSpaceS.substring(UsedSpaceS.indexOf(".")+1, UsedSpaceS.length()).length()>3) {
				UsedSpaceS=UsedSpaceS.substring(0, UsedSpaceS.indexOf(".")+3)+"%";
				FreeSpaceS=FreeSpaceS.substring(0, FreeSpaceS.indexOf(".")+3)+"%";
			}
			JLabel UsedL=new JLabel("Used Space : "+UsedSpaceS);
			JLabel FreeL=new JLabel("Free Space : "+FreeSpaceS);
			UsedL.setBounds(x+160, y, 150, 30);
			FreeL.setBounds(x+300, y, 150, 30);
			ViewPanel.add(UsedL);
			ViewPanel.add(FreeL);
			JButton b=new JButton(f1.getAbsolutePath());
			b.setBounds(x, y, 150, 30);
			if(x==0) {
				x=0;
				y+=50;
			}
			else
				x+=150;
			ViewPanel.add(b);
			b.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					openFolder(f1.getAbsolutePath());
				}
			});
			if(f1.getTotalSpace()==0) b.setEnabled(false);
			System.out.println(f1);
		}
		ViewPanel.repaint();
	}
	public void openFolder(String path) {
		this.path=new File(path);
		ViewPanel.removeAll();
        AddFolderB.setEnabled(true);
        DeleteFolderB.setEnabled(true);
		GoToTF.setText("Root\\"+path);
		this.setTitle(path);
		File file=new File(path);
		File[] f=file.listFiles();
		int y=0,x=60;
		for(File f1:f) {
			JButton b=new JButton(f1.getName());
			b.setBounds(x, y, 150, 50);
			if(x>600) {
				x=60;
				y+=55;
			}
			else
				x+=160;
			ViewPanel.add(b);
			b.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(f1.isFile()) {
						try {
							Desktop.getDesktop().open(f1);
						} catch(Exception ex) {
							
						}
					}
					else {
						openFolder(f1.getAbsolutePath());
					}
				}
			});
			ViewPanel.repaint();
			System.out.println(f1);
		}
	}
	public static void main(String[] args) {
		new FileExplorerByButton();
	}
}
