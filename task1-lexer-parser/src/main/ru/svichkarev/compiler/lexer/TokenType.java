package main.ru.svichkarev.compiler.lexer;

public enum TokenType {
	// типы для арифметических выражений
	NUMBER,
	PLUS, MINUS,
	MULTIPLICATION, DIVISION,
	EXPONENTIATION,
	BRACKET_OPEN, BRACKET_CLOSE,
	
	// для всего остального
	EMPTY, // заглушка для обозначения пустоты в правилах вывода
	FUNCTION,
	NAME,
	BRACE_OPEN, BRACE_CLOSE, // фигурные скобки
	PARAMS_LIST, // список параметров
	PARAM,
	COMMA,
	BODY,
	SEMICOLON,
	COMMAND,
	ARG_LIST,
	
	TYPE,
	
	INT, DOUBLE,
	
	RETURN,
	
	// TODO: нужен ли
	CALL_FUNCTION,
	
	END,
}
