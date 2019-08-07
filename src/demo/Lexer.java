package demo;

import java.io.LineNumberReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import demo.Token.TokenType;

public class Lexer {
	private ArrayList<Token> queue = new ArrayList<Token>();
	private boolean hasMore;
	private LineNumberReader reader;
	private String line;
	private int lineNo;
	private int pos;
	private int endPos;

	public Lexer(Reader r) {
		hasMore = true;
		reader = new LineNumberReader(r);
		while (hasMore) {
			try {
				readLine();
			} catch (Exception e) {
				System.err.println("lexer error at " + e.getMessage());
			}
		}
	}

	public ArrayList<Token> getTokens() {
		return queue;
	}

	private void readLine() throws Exception {
		line = reader.readLine();
		if (line == null) {
			hasMore = false;
			return;
		}
		lineNo = reader.getLineNumber();
		pos = 0;
		endPos = line.length();

		while (pos < endPos && (lexKeyword() || lexIdentifier() || lexComment() || lexPunctuator() || lexConstant()
				|| lexString() || lexCharacter()))
			;
		if (pos != endPos) {
			String value = line.substring(pos).trim();
			int index = line.substring(pos).indexOf(value) + pos + 1;
			queue.add(new Token(lineNo, value, TokenType.ERR, index));
			throw new Exception("line " + lineNo + ", position " + index + ": unknown token [" + value + "]");
		}
	}

	private boolean lexComment() throws Exception {
		String regex = "//.*|/\\*.*\\*/";
		return lex(regex, TokenType.COMMENT);
	}

	private boolean lexKeyword() throws Exception {
		String regex = "auto|break|extern|return|void|case|float|short|char|for|while|const|goto|sizeof|bool|continue|if|static|default|inline|struct|int|switch|double|long|typedef|else|union|do";
		return lex(regex, TokenType.KEYWORD);
	}

	private boolean lexConstant() throws Exception {
		String regexInteger = "([1-9][0-9]*)" + "|((0(x|X))[0-9a-fA-F]+)" + "|(0[0-7]*)";
		String regexFloat = "([0-9]*\\.[0-9]+((e|E)(-|\\+)?[0-9]+)?(f|F))" + "|([0-9]+\\.((e|E)(-|\\+)?[0-9]+)?(f|F))"
				+ "|([0-9]+((e|E)(-|\\+)?[0-9]+)(f|F))"
				+ "|(((0x)|(0X))[0-9a-fA-F]*\\.[0-9a-fA-F]+(p|P)(-|\\+)?[0-9]+(f|F))"
				+ "|(((0x)|(0X))[0-9a-fA-F]+\\.(p|P)(-|\\+)?[0-9]+(f|F))"
				+ "|(((0x)|(0X))[0-9a-fA-F]+(p|P)(-|\\+)?[0-9]+(f|F))";
		String regexDouble = regexFloat.replaceAll("(\\(f\\|F\\))", "");
		regexInteger = "(" + regexInteger + ")(?![A-Z_a-z\\'\\''])";
		regexFloat = "(" + regexFloat + ")(?![A-Z_a-z\\'\\''])";
		regexDouble = "(" + regexDouble + ")(?![A-Z_a-z\\'\\''])";
		return lex(regexInteger, TokenType.INTEGER) || lex(regexFloat, TokenType.FLOAT)
				|| lex(regexDouble, TokenType.DOUBLE);
	}

	private boolean lexCharacter() throws Exception {
		String regex = "\'([^\'|\\\\]|\\\\(b|f|n|r|t|\'|\"|\\\\))\'";
		return lex(regex, TokenType.CHARACTER);
	}

	private boolean lexIdentifier() throws Exception {
		String regex = "[A-Z_a-z][A-Z_a-z0-9]*";
		return lex(regex, TokenType.IDENTIFIER);
	}

	private boolean lexPunctuator() throws Exception {
		String regex = "->|\\+\\+|--|<<|>>|<=|>=|==|!=|&&|\\|\\||\\.\\.\\.|\\*=|/=|%=|\\+=|-=|<<=|>>=|&=|\\^=|\\|=|\\[|\\]|\\(|\\)|\\{|\\}|\\.|&|\\*|\\+|-|~|!|/|%|<|>|\\^|\\||\\?|:|;|=|,|#";
		return lex(regex, TokenType.PUNCTUATOR);
	}

	private boolean lexString() throws Exception {
		String regex = "\"([^\"|\\\\]|\\\\(b|f|n|r|t|\'|\"|\\\\))*\"";
		return lex(regex, TokenType.STRINGLITERAL);
	}

	private boolean lex(String regex, TokenType type) throws Exception {
		Pattern pattern = Pattern.compile("\\s*(" + regex + ")");
		Matcher matcher = pattern.matcher(line);
		matcher.region(pos, endPos);
		if (matcher.lookingAt()) {
			String value = matcher.group(1);
			dealWithException(value, type);
			queue.add(new Token(lineNo, value, type, line.substring(pos).indexOf(value) + pos + 1));
			pos = matcher.end();
			return true;
		} else if (pos < endPos && line.substring(pos).trim().equals("")) {
			pos = endPos;
			return true;
		} else
			return false;
	}

	private void dealWithException(String value, TokenType type) throws Exception {
		switch (type) {
		case INTEGER:
			try {
				Integer.parseInt(value);
			} catch (Exception e) {
				int index = line.substring(pos).indexOf(value) + pos + 1;
				queue.add(new Token(lineNo, value, TokenType.ERR, index));
				throw new Exception(
						"line " + lineNo + ", position " + index + ": token [" + value + "] Integer overflow");
			}
			break;
		case FLOAT:
			try {
				Float.parseFloat(value);
			} catch (Exception e) {
				int index = line.substring(pos).indexOf(value) + pos + 1;
				queue.add(new Token(lineNo, value, TokenType.ERR, index));
				throw new Exception(
						"line " + lineNo + ", position " + index + ": token [" + value + "] Float overflow");
			}
			break;
		case DOUBLE:
			try {
				Double.parseDouble(value);
			} catch (Exception e) {
				int index = line.substring(pos).indexOf(value) + pos + 1;
				queue.add(new Token(lineNo, value, TokenType.ERR, index));
				throw new Exception(
						"line " + lineNo + ", position " + index + ": token [" + value + "] Double overflow");
			}
			break;
		default:
			break;
		}
	}
}