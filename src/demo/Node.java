package demo;

import java.util.ArrayList;

public class Node {
	private Token token = null;
	private ArrayList<Node> chirldren = new ArrayList<>();
	private String typeString;

	public Node(Token token) {
		this.token = token;
		switch (token.getType()) {
		case INTEGER:
		case FLOAT:
		case DOUBLE:
		case CHARACTER:
			typeString = "constant";
			break;
		case IDENTIFIER:
			typeString = "identifier";
			break;
		case STRINGLITERAL:
			typeString = "string-literal";
			break;
		case KEYWORD:
		case PUNCTUATOR:
			typeString = token.getValue();
			break;
		default:
			typeString = "";
		}
	}

	public Node(String typeString) {
		this.typeString = typeString;
	}

	public void addChildren(Node node) {
		chirldren.add(node);
	}

	public Token getToken() {
		return token;
	}

	public ArrayList<Node> getChirldren() {
		return chirldren;
	}

	public String getTypeString() {
		return typeString;
	}

	public String getValue() {
		return getToken() == null ? getTypeString() : getToken().getValue();
	}

	public static void print(Node node, int lever) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < lever; i++)
			sb.append("-");
		sb.append(node.getValue());
		System.out.println(sb.toString());
		lever++;
		for (Node child : node.getChirldren()) {
			print(child, lever);
		}
	}

	private static String PREFIX_BRANCH = "├─";// 树枝
	private static String PREFIX_TRUNK = "│ ";// 树干
	private static String PREFIX_LEAF = "└─";// 叶子
	private static String PREFIX_EMP = "  ";// 空

	public static void print(Node node, String prefix) {
		if (prefix == null) {
			prefix = "";
			System.out.println(node.getValue());
		}
		prefix = prefix.replace(PREFIX_BRANCH, PREFIX_TRUNK);
		prefix = prefix.replace(PREFIX_LEAF, PREFIX_EMP);

		ArrayList<Node> children = node.getChirldren();
		for (int i = 0; i < children.size(); i++) {
			Node child = children.get(i);
			if (i == children.size() - 1) {// 最后一个是叶子
				System.out.println(prefix + PREFIX_LEAF + child.getValue());
				print(child, prefix + PREFIX_LEAF);
			} else {// 树枝
				System.out.println(prefix + PREFIX_BRANCH + child.getValue());
				print(child, prefix + PREFIX_TRUNK);
			}
		}
	}

	public String toString() {
		String result = "";
		for (Node node : chirldren)
			result += node.toString() + ",";
		return typeString + "[" + result.substring(0, result.length() - 1) + "]";
	}
}
