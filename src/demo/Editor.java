package demo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import demo.Token.TokenType;

public class Editor extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane = new JPanel();
	private JScrollPane jScrollPane = new JScrollPane();
	private LineNumberTextPane inputTextPane = new LineNumberTextPane();
	private JTextPane erroTextPane = new JTextPane();
	private StyledDocument document = (StyledDocument) inputTextPane.getDocument();
	private SimpleAttributeSet attributes = new SimpleAttributeSet();
	public JFileChooser filechooser = new JFileChooser(); // 文件选择器

	public Editor() {
		super("Mini-C Editor");

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		contentPane.add(jScrollPane, BorderLayout.CENTER);
		jScrollPane.setViewportView(inputTextPane);
		inputTextPane.setFont(new Font("仿宋", Font.PLAIN, 24));
		erroTextPane.setFont(new Font("仿宋", Font.PLAIN, 24));

		contentPane.add(erroTextPane, BorderLayout.SOUTH);

		initJMenuBar();

		setSize(1300, 1500);
		setVisible(true);
	}

	private void initJMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		JMenu menuFile = new JMenu("文件(F)");
		menuFile.setMnemonic('F');
		JMenu menuEdit = new JMenu("编辑(E)");
		menuEdit.setMnemonic('E');
		JMenu menuRun = new JMenu("运行(R)");
		menuRun.setMnemonic('R');
		JMenu menuAbout = new JMenu("帮助(H)");
		menuAbout.setMnemonic('H');

		JMenuItem menuItemCreate = new JMenuItem("新建(N)");
		menuItemCreate.setMnemonic('N');
		menuItemCreate.setAccelerator(KeyStroke.getKeyStroke("control N"));
		menuItemCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				inputTextPane.setText("");
			}
		});

		JMenuItem menuItemOpen = new JMenuItem("打开(O)");
		menuItemOpen.setMnemonic('O');
		menuItemOpen.setAccelerator(KeyStroke.getKeyStroke("control O"));
		menuItemOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (filechooser.showOpenDialog(Editor.this) == JFileChooser.APPROVE_OPTION) // 点击对话框打开选项
				{
					File f = filechooser.getSelectedFile(); // 得到选择的文件
					try {
						StringBuilder sb = new StringBuilder();
						String s = "";
						BufferedReader br = new BufferedReader(new FileReader(f));

						while ((s = br.readLine()) != null)
							sb.append(s).append("\r\n");
						s = sb.toString();
						br.close();

						inputTextPane.setText(s);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		});

		JMenuItem menuItemSave = new JMenuItem("保存(S)");
		menuItemSave.setMnemonic('S');
		menuItemSave.setAccelerator(KeyStroke.getKeyStroke("control S"));
		menuItemSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (filechooser.showSaveDialog(Editor.this) == JFileChooser.APPROVE_OPTION) {
					File f = filechooser.getSelectedFile();
					try {
						FileOutputStream out = new FileOutputStream(f);
						out.write(inputTextPane.getText().getBytes());
						out.close();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		});

		JMenuItem menuItemExit = new JMenuItem("退出(X)");
		menuItemExit.setMnemonic('X');
		menuItemExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		JMenuItem menuItemCut = new JMenuItem("剪切(T)");
		menuItemCut.setMnemonic('T');
		menuItemCut.setAccelerator(KeyStroke.getKeyStroke("control X"));
		menuItemCut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				inputTextPane.cut();
			}
		});

		JMenuItem menuItemCopy = new JMenuItem("复制(C)");
		menuItemCopy.setMnemonic('C');
		menuItemCopy.setAccelerator(KeyStroke.getKeyStroke("control C"));
		menuItemCopy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				inputTextPane.copy();
			}
		});

		JMenuItem menuItemPaste = new JMenuItem("粘贴(P)");
		menuItemPaste.setMnemonic('P');
		menuItemPaste.setAccelerator(KeyStroke.getKeyStroke("control V"));
		menuItemPaste.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				inputTextPane.paste();
			}
		});

		JMenuItem menuItemRun = new JMenuItem("运行(R)");
		menuItemRun.setMnemonic('R');
		menuItemRun.setAccelerator(KeyStroke.getKeyStroke("control R"));
		menuItemRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				actionRunPerformed(e);
			}
		});

		JMenuItem menuItemAbout = new JMenuItem("关于Mini-C(A)");
		menuItemAbout.setMnemonic('A');
		menuItemAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(Editor.this, "第15组哦\r\n实现了Mini-C的词法语法分析", "关于",
						JOptionPane.PLAIN_MESSAGE);
			}
		});

		menuFile.add(menuItemCreate);
		menuFile.add(menuItemOpen);
		menuFile.add(menuItemSave);
		menuFile.add(menuItemExit);
		menuEdit.add(menuItemCut);
		menuEdit.add(menuItemCopy);
		menuEdit.add(menuItemPaste);
		menuRun.add(menuItemRun);
		menuAbout.add(menuItemAbout);
		menuBar.add(menuFile);
		menuBar.add(menuEdit);
		menuBar.add(menuRun);
		menuBar.add(menuAbout);
		setJMenuBar(menuBar);
	}

	public void actionRunPerformed(ActionEvent e) {
		StringReader reader = null;
		try {
			String text = inputTextPane.getText();
			if (text == null)
				return;
			reader = new StringReader(text);
			System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream(new File("out.txt")))));
			System.setErr(new PrintStream(new BufferedOutputStream(new FileOutputStream(new File("err.txt")))));
		} catch (FileNotFoundException e1) {
			System.err.println(e1.getMessage());
			System.exit(0);
		}

		LALRParser lalrParser = new LALRParser(reader);
		Node root = lalrParser.getRoot();
		if (root != null)
			Node.print(root, null);
		System.out.flush();
		System.err.flush();

		ArrayList<Token> tokens = lalrParser.getTokens();
		String string = "";
		int line = 1;
		int pos = 1;
		int size = tokens.size();
		int index[] = new int[size];
		int tmp = 0;

		for (int i = 0; i < size; i++) {
			Token t = tokens.get(i);
			for (int j = 0; j < t.getLine() - line; j++) {
				string += "\r\n";
				pos = 1;
				tmp++;
			}
			for (int j = 0; j < t.getPosition() - pos; j++) {
				string += " ";
				tmp++;
			}
			line = t.getLine();
			pos = t.getPosition() + t.getValue().length();
			string += t.getValue();
			index[i] = tmp;
			tmp += t.getValue().length();
		}
		inputTextPane.setText(string);

		for (int i = 0; i < size; i++) {
			Token t = tokens.get(i);
			Color color = null;
			if (!t.isValid()) {
				color = Color.RED;
			} else {
				if (t.getType() == TokenType.KEYWORD)
					color = Color.BLUE;
				else if (t.getType() == TokenType.CHARACTER || t.getType() == TokenType.STRINGLITERAL)
					color = Color.ORANGE;
				else if (t.getType() == TokenType.COMMENT)
					color = Color.GREEN;
				else
					color = Color.BLACK;
			}
			StyleConstants.setForeground(attributes, color);
			document.setCharacterAttributes(index[i], t.getValue().length(), attributes, false);
		}

		try {
			StringBuilder sb = new StringBuilder();
			String s = "";
			BufferedReader br = new BufferedReader(new FileReader(new File("err.txt")));

			while ((s = br.readLine()) != null)
				sb.append(s).append("\r\n");
			s = sb.toString();
			br.close();

			erroTextPane.setText(sb.toString());
			StyledDocument document = (StyledDocument) erroTextPane.getDocument();
			StyleConstants.setForeground(attributes, Color.RED);
			document.setCharacterAttributes(0, s.length(), attributes, false);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		try {
			Runtime.getRuntime().exec("C:\\WINDOWS\\system32\\notepad.exe out.txt");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Editor frame = new Editor();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}

class LineNumberTextPane extends JTextPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LineNumberTextPane() {
		super();
	}

	public void paint(Graphics g) {
		super.paint(g);
		setMargin(new Insets(0, 55, 0, 0));
		g.setColor(new Color(180, 180, 180));// 背景颜色
		g.fillRect(0, 0, 45, getHeight());

		int rows = getStyledDocument().getDefaultRootElement().getElementCount();
		g.setColor(new Color(90, 90, 90));// 行号颜色
		for (int row = 1; row <= rows; row++) {
			g.drawString(String.valueOf(row), 3, row * 29);
		}
	}
}