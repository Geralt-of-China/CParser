package demo;

public class Token {
	public enum TokenType {
		KEYWORD, INTEGER, FLOAT, DOUBLE, CHARACTER, IDENTIFIER, PUNCTUATOR, STRINGLITERAL, COMMENT, ERR;
	};

	private int line;
	private TokenType type;
	private String value;
	private int position;
	private boolean valid = true;

	protected Token(int line, String value, TokenType type, int position) {
		this.line = line;
		this.value = value;
		this.type = type;
		this.position = position;
		if (type == TokenType.ERR)
			valid = false;
	}

	public int getLine() {
		return line;
	}

	public int getPosition() {
		return position;
	}

	public TokenType getType() {
		return type;
	}

	public String getValue() {
		return value;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public String toString() {
		return line + "	" + type + "	" + value + "	" + position;
	}
}
