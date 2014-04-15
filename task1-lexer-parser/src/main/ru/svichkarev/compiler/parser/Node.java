package main.ru.svichkarev.compiler.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.ArrayBlockingQueue;

import main.ru.svichkarev.compiler.lexer.Token;
import main.ru.svichkarev.compiler.lexer.TokenType;

public class Node {
	private Token<?> value;
	private List< Node > listChild;
	
	public Token<?> getValue() {
		return value;
	}
	
	public Node( Token<?> newValue ) {
		value = newValue;
		listChild = new ArrayList< Node >();
	}
	
	// для более простого создания нетерминалов
	public Node( TokenType typeValue ) {
		value = new Token<>( typeValue );
		listChild = new ArrayList< Node >();
	}
	
	public void setLeft( Node leftChild ){
		// указываем индекс, чтобы явно перед всеми другими добавил
		listChild.add(0, leftChild);
	}
	
	public void setRight( Node rightChild ){
		// добавляем в конец списка, т.е. всегда справа
		listChild.add( rightChild );
	}

	// метод сверки типов
	public boolean match( TokenType type ){
		return value.match( type );
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		// здесь начинается сравнение деревьев
		
		// сравниваем списки
		Node other = (Node) obj;
		if (listChild == null) {
			if (other.listChild != null)
				return false;
		} else{
			if (!listChild.equals(other.listChild)){ //TODO
				return false;
			}
		}
		
		// сравниваем значения в узлах
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value)) // TODO
			return false;
		
		return true;
	}
}
