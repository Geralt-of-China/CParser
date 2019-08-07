package demo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;

import demo.Token.TokenType;

public class LALRParser {
	private ArrayList<Token> tokens = new ArrayList<>();
	private int index = 0;
	private Node current;
	private Node root;
	private String[][] table;
	private ArrayList<String> header = new ArrayList<>();
	private ArrayList<String> productions = new ArrayList<>();
	private Stack<Integer> stackStatus = new Stack<>();
	private Stack<Node> stackNode = new Stack<>();
	private String action;

	public LALRParser(Reader r) {
		Init(r);
		StartParsing();
	}

	public Node getRoot() {
		return root;
	}

	public ArrayList<Token> getTokens() {
		return tokens;
	}

	private void Init(Reader r) {
		Lexer lexer = new Lexer(r);
		tokens = lexer.getTokens();
		current = getNextNode();

		String tmpTable[][] = null;
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File("LALRTabble")));
			tmpTable = (String[][]) ois.readObject();

			Object obj = null;
			while ((obj = ois.readObject()) != null) {
				productions.add((String) obj);
			}
			productions.remove(0);
			ois.close();

		} catch (IOException | ClassNotFoundException e) {
			System.err.println(e.getMessage());
			System.exit(0);
		}

		int width = tmpTable.length - 1;
		int height = tmpTable[0].length - 1;
		table = new String[width][height];
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++)
				table[i][j] = tmpTable[i + 1][j + 1];

		for (int i = 0; i < height; i++)
			header.add(tmpTable[0][i + 1]);
	}

	private void StartParsing() {
		stackStatus.push(0);
		action = table[stackStatus.peek()][getID(current.getTypeString())];

		while (true) {
			if (action.equals("")) {
				if (!dealWithExpection()) {
					System.err.println("Î´Öª´íÎó");
					break;
				}
			}

			if (action.equals("acc")) {
				root = stackNode.pop();
				break;
			}

			if (action.startsWith("s")) {
				stackNode.push(current);
				current = getNextNode();
				stackStatus.push(Integer.parseInt(action.substring(1)));

			} else if (action.startsWith("r")) {
				int index = Integer.parseInt(action.substring(1)) - 1;
				String production = productions.get(index);

				Node node = new Node(production.split(":")[0]);
				int num = Integer.parseInt(production.split(":")[1]);
				Node nodeChild[] = new Node[num];

				for (int i = num - 1; i >= 0; i--) {
					nodeChild[i] = stackNode.pop();
					stackStatus.pop();
				}

				for (int i = 0; i < num; i++)
					node.addChildren(nodeChild[i]);

				stackStatus.push(Integer.parseInt(table[stackStatus.peek()][getID(node.getTypeString())]));
				stackNode.push(node);
			}
			action = table[stackStatus.peek()][getID(current.getTypeString())];
		}
	}

	private boolean dealWithExpection() {
		int status = stackStatus.peek();
		Token token = tokens.get(index >= 1 ? index - 1 : index);
		token.setValid(false);
		System.err.println("syntax error near line " + token.getLine() + ", position " + token.getPosition() + " ["
				+ token.getValue() + "]");

		int index = header.indexOf("statement");
		Node node = null;
		while (table[status][index].equals("") && !stackNode.isEmpty() && !stackNode.isEmpty()) {
			node = stackNode.pop();
			status = stackStatus.pop();
		}
		stackStatus.push(status);
		stackNode.push(node);

		String string = table[status][index];
		if (string.equals(""))
			status = 0;
		else
			status = Integer.valueOf(string);

		stackStatus.push(status);
		stackNode.push(new Node("statement"));
		HashSet<String> follows = getFollows(status);
		while (!follows.contains(current.getTypeString())) {
			current = getNextNode();
			if (current.getTypeString().equals("#"))
				return false;
		}
		return true;
	}

	private HashSet<String> getFollows(int status) {
		HashSet<String> result = new HashSet<>();
		for (int i = 0; i < table[0].length; i++) {
			String s = table[status][i];
			if (!s.equals("") && !Character.isDigit(s.charAt(0)))
				result.add(header.get(i));
		}
		return result;
	}

	private Node getNextNode() {
		if (index == tokens.size())
			return new Node("#");
		if (tokens.get(index).getType() == TokenType.ERR || tokens.get(index).getType() == TokenType.COMMENT) {
			index++;
			return getNextNode();
		}
		return new Node(tokens.get(index++));
	}

	private int getID(String s) {
		return header.indexOf(s);
	}
}
