package com.zhao.demo;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.zhao.demo.utils.IOUtil;
import com.zhao.demo.utils.StringUtil;

public class Main extends JPanel implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static JTextArea contextJ;	//中间大块部分显示内容
	private static JTextField selectField;	//选择text
	private static JTextField targetField;	//目标text
	private JFileChooser fileChooser = new JFileChooser();

	public static void main(String[] args) {
		JFrame frame = new JFrame("复制文件");
		frame.setSize(400, 500);
		frame.setLocationRelativeTo(null);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				super.windowClosed(e);
				System.exit(0);
			}
		});
		
		showContext(frame);
		
		frame.setVisible(true);
	}

	private static void showContext(JFrame frame) {
		/******************上层，目录选择和目标目录*******************/
		JLabel selectLabel = new JLabel("源目录");
		selectField = new JTextField();
		JButton selectBt = new JButton("源目录");
		JLabel targetLabel = new JLabel("目标目录");
		targetField = new JTextField();
		JButton targetBt = new JButton("目标目录");
		
		JPanel selectAndTargetPan = new JPanel();
		selectAndTargetPan.setLayout(new GridLayout(2, 3));
		selectAndTargetPan.add(selectLabel);
		selectAndTargetPan.add(selectField);
		selectAndTargetPan.add(selectBt);
		selectAndTargetPan.add(targetLabel);
		selectAndTargetPan.add(targetField);
		selectAndTargetPan.add(targetBt);
		
		/******************中层，内容打印部分*******************/
		contextJ = new JTextArea();
		JScrollPane scroll = new JScrollPane(contextJ);
		
		/******************下层，复制按钮*******************/
		JButton copyBt = new JButton("复制");
		
		//添加按钮监听
		Main m = new Main();
		selectBt.addActionListener(m);
		targetBt.addActionListener(m);
		copyBt.addActionListener(m);
		
		frame.setLayout(new BorderLayout());
		frame.add(selectAndTargetPan, BorderLayout.NORTH);
		frame.add(scroll, BorderLayout.CENTER);
		frame.add(copyBt, BorderLayout.SOUTH);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			switch (e.getActionCommand()) {
			case "源目录":
				File file = openChooseGetFile();
				if(file != null) {
					showChooseFile(file);
				}
				break;
			case "目标目录":
				saveChooseGetFile();
				break;
			case "复制":
				//耗时操作，建议在工作线程中进行
				new Thread(new Runnable() {
					@Override
					public void run() {
						doCopy();
					}
				}).start();
				break;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	private File openChooseGetFile() throws FileNotFoundException {
		fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		fileChooser.showOpenDialog(this);
		File file = fileChooser.getSelectedFile();
		if(file != null) {
			selectField.setText(file.getAbsolutePath());
			return file;
		}
		return null;
	}
	
	private void saveChooseGetFile() throws FileNotFoundException {
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.showSaveDialog(null);
		File file = fileChooser.getSelectedFile();
		if(file != null)
			targetField.setText(file.getAbsolutePath());
	}

	private void showChooseFile(File file) throws FileNotFoundException {
		if(file.isFile()) {
			contextJ.append(file.getAbsolutePath() + "\n");
			return;
		}
		File[] files = file.listFiles();
		for(File f : files) {
			if(f.isDirectory()) {
				showChooseFile(f);
			}
			if(f.isFile()) {
				contextJ.append(f.getAbsolutePath() + "\n");
			}
		}
	}

	private void doCopy() {
		String fromPath = selectField.getText();
		String toPath = targetField.getText();
		if(StringUtil.isNull(fromPath)) {
			JOptionPane.showMessageDialog(this, "请选择源目录");
			return;
		}
		if(StringUtil.isNull(toPath)){
			JOptionPane.showMessageDialog(this, "请选择目标目录");
			return;
		}
		try {
			doCopy(new File(fromPath), new File(toPath));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void doCopy(File from, File to) throws IOException{
		File newFile = new File(to.getAbsolutePath() + "/" + from.getName());
		if(from.isFile()) {
			if(newFile.exists()){	//如果目标目录已有此文件，则先删除，否则会追加写入
				newFile.delete();
				System.out.println("原有文件被删除了");
			}
			contextJ.append("正在创建文件" + newFile.getAbsolutePath() + "...\n");
			IOUtil.ByteWriteToCopy(from, newFile);
		}
		if(from.isDirectory()){
			newFile.mkdir();
			File[] files = from.listFiles();
			for(File f : files) {
				if(f.isDirectory()) {
					doCopy(f, newFile);
				}
				if(f.isFile()) {
					File newFilef = new File(newFile.getAbsolutePath() + "/" + f.getName());
					if(newFilef.exists()) {
						newFilef.delete();
						System.out.println("原有文件被删除了");
					}
					contextJ.append("正在复制文件" + f + "...\n");
					IOUtil.ByteWriteToCopy(f, newFilef);
				}
			}
		}
	}
	
}
