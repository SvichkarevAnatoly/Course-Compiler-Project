package main.ru.svichkarev.compiler.parser;

import main.ru.svichkarev.compiler.lexer.Lexer;
import main.ru.svichkarev.compiler.lexer.Token;
import main.ru.svichkarev.compiler.lexer.TokenType;

public class Parser {
	private Lexer lexer;

	public Parser(Lexer lexer) {
		this.lexer = lexer;
	}

	// главная функция разбора всего исходника и постройки дерева
	public Node parseProgram(){
		// TODO: у программы нет ничего в содержимом
		//это просто список функций
		Node result = new Node( TokenType.PROGRAM );

		//правило вывода:
		//functionList -> function functionList | E вырождается
		
		result.setLeft( parseFunction() );
		
		// цикл - пока получается вытаскивать функцию,
		//если вернёт пусто, то останавливаемся
        // TODO: переписать на do - while
		Node curFunction = parseFunction();
		while( ! curFunction.match( TokenType.EMPTY ) ){
			result.setRight( curFunction );
			curFunction = parseFunction();
		}
		
		return result;
	}
	
	public Node parseFunction(){
		// проверка на отсутствие функций
		Token<?> emptyToken = lexer.peekToken();
		// если конец исходника, значит функций дальше нет,
		//возвращаем пустоту
		if( emptyToken.match( TokenType.END ) ){
			return new Node( TokenType.EMPTY );
		}
		
		// TODO: оптимизация
		// считываем несколько токенов и сверяем типы, должно быть точное совпадение
		Node typeNode = parseType();
		Token<?> nameToken = lexer.getToken();
		if( ! nameToken.match( TokenType.NAME ) ){
			// TODO: кинуть ошибку
            throw new RuntimeException("проверить");
		}
		
		// аргументы
		Token<?> openBracketToken = lexer.getToken();
		if( ! openBracketToken.match( TokenType.BRACKET_OPEN ) ){
			// TODO: кинуть ошибку
            throw new RuntimeException("проверить");
		}
		Node parlistNode = parseParlist();
		Token<?> closeBracketToken = lexer.getToken();
		if( ! closeBracketToken.match( TokenType.BRACKET_CLOSE ) ){
			// TODO: кинуть ошибку
            throw new RuntimeException("проверить");
		}
		
		// тело
		Token<?> openBraceToken = lexer.getToken();
		if( ! openBraceToken.match( TokenType.BRACE_OPEN ) ){
			// TODO: кинуть ошибку
            throw new RuntimeException("проверить");
		}
		Node bodyNode = parseBody();
		Token<?> closeBraceToken = lexer.getToken();
		if( ! closeBraceToken.match( TokenType.BRACE_CLOSE ) ){
			// TODO: кинуть ошибку
            throw new RuntimeException("проверить");
		}
		
		// если дошли до сюда, то смогли распарсить
		Node result = new Node( new Token<String>( TokenType.FUNCTION, (String)nameToken.getTokenValue() ) );
		// возвращаемый тип будет первым потомком
		result.setLeft( typeNode );
		// аргументы - второй потомок
		result.setRight( parlistNode );
		// тело - третий потомок
		result.setRight( bodyNode );
		
		return result;
	}
	
	public Node parseParlist(){
		//правило вывода:
		//params -> type name , params | type name вырождается
		
		// проверка на отсутствие параметров
		Token<?> closeBracketToken = lexer.peekToken();
		// если вернуло закрывающуюся скобку, то вернём пустоту
		if( closeBracketToken.match( TokenType.BRACKET_CLOSE ) ){
			Node emptyParList = new Node( TokenType.PARAMS_LIST );
			emptyParList.setLeft( new Node( TokenType.EMPTY ) );
			return emptyParList;
		}
		
		// иначе начинаем парсить аргументы
		// считываем несколько токенов и сверяем типы, должно быть точное совпадение
		// если будет запятая, то дальше идёт по идее ещё аргумент
		Node result = new Node( TokenType.PARAMS_LIST ); //TODO: так можно?
		Token<?> commaToken;
		do{
			Node typeNode = parseType(); //TODO почему никак не используется
			Token<?> nameToken = lexer.getToken();
			if( ! nameToken.match( TokenType.NAME ) ){
				// TODO: кинуть ошибку
                throw new RuntimeException("проверить");
			}
			// выделили один параметр, добавляем в список
			Node newParam = new Node( new Token<String>( TokenType.PARAM, (String) nameToken.getTokenValue() ) );
            newParam.setLeft( typeNode );
			result.setRight( newParam );
			
			commaToken = lexer.peekToken();
		} while( commaToken.match( TokenType.COMMA ) && commaToken == lexer.getToken() );
		
		// проверка корректно ли завершилось
		if( ! (commaToken.match( TokenType.COMMA ) || commaToken.match( TokenType.BRACKET_CLOSE )) ){
			// TODO: кинуть ошибку
            throw new RuntimeException("проверить");
		}
		
		return result;
	}
	
	public Node parseBody(){
		Node result = new Node( TokenType.BODY );
		// проверка на пустое тело
		Token<?> closeBraceToken = lexer.peekToken();
		// если вернуло закрывающуюся фигурную скобку, то вернём пустоту
		if( closeBraceToken.match( TokenType.BRACE_CLOSE ) ){
			result.setLeft( new Node( TokenType.EMPTY ) );
			return result;
		}
		
		// начинаем парсить команды пока не вернёт пусто
		Node command = parseCommand();
		result.setLeft( command );

		do{
            // исключение из правил: если if и while, то точку с запятой не нужно ставить
            if( !( command.getFirstChildren().getTokenType() == TokenType.IF ||
                    command.getFirstChildren().getTokenType() == TokenType.WHILE) ) {
                Token<?> semicolonToken = lexer.getToken();
                if (!semicolonToken.match(TokenType.SEMICOLON)) {
                    throw new RuntimeException("P: после команды должна следовать \";\"");
                }
            }
			command = parseCommand();
			result.setRight( command );
		} while( ! command.getValue().match( TokenType.EMPTY ) );
		
		return result;
	}
	
	public Node parseCommand(){
		Node result = new Node( TokenType.COMMAND );

		Token<?> whatEver = lexer.peekToken();
		switch( whatEver.getTokenType() ){
		case BRACE_CLOSE: // это уже не команда, вернуть пустоту
			result = new Node( TokenType.EMPTY );
			break;
		// TODO: поидее нужно просто TYPE
		case INT:
		case DOUBLE:
			lexer.getToken();
			Token<?> nameToken = lexer.getToken();
			if( nameToken.match( TokenType.NAME ) ){
				Node typeNode = new Node( TokenType.TYPE );
				typeNode.setLeft( new Node( whatEver ) );
				result.setLeft( typeNode );
				result.setRight( new Node( nameToken ) );
			}else{
				// TODO: кинуть ошибку
                throw new RuntimeException("проверить");
			}
			
			break;
		case NAME:
			lexer.getToken();
			Token<?> assignToken = lexer.getToken();
			if( assignToken.match( TokenType.ASSIGNMENT ) ){
				result.setLeft( new Node(whatEver) );
                // TODO: нет необходимости добавлять
				result.setRight( new Node( assignToken ) );
				result.setRight( parseExpr() );
			}else{
				// TODO: кинуть ошибку
                throw new RuntimeException("проверить");
			}
			
			break;
		case RETURN:
			lexer.getToken();
			result.setLeft( new Node(whatEver) );
			result.setRight( parseExpr() );
			break;
		case PRINT:
			lexer.getToken();
			Token<?> openBracketToken = lexer.getToken();
			if( ! openBracketToken.match( TokenType.BRACKET_OPEN ) ){
				// TODO: кинуть ошибку
                throw new RuntimeException("проверить");
			}
            // TODO: стоит сделать expr
			Token<?> nameVariable = lexer.getToken();
			if( ! nameVariable.match( TokenType.NAME ) ){
				// TODO: кинуть ошибку
                throw new RuntimeException("проверить");
			}
			Token<?> closeBracketToken = lexer.getToken();
			if( ! closeBracketToken.match( TokenType.BRACKET_CLOSE ) ){
				// TODO: кинуть ошибку
                throw new RuntimeException("проверить");
			}
			result.setLeft( new Node( whatEver ) );
			result.setRight( new Node( nameVariable ) );
			
			break;
        case IF:
            lexer.getToken();
            Token<?> openBracketIfToken = lexer.getToken();
            if( ! openBracketIfToken.match( TokenType.BRACKET_OPEN ) ){
                throw new RuntimeException("P: ожидалась открывающаяся скобка после if");
            }

            result.setLeft( new Node(whatEver) );
            result.setRight( parseCondition() );

            Token<?> closeBracketIfToken = lexer.getToken();
            if( ! closeBracketIfToken.match( TokenType.BRACKET_CLOSE ) ){
                throw new RuntimeException("P: ожидалась закрывающаяся скобка после условия");
            }

            // TODO: попытаться отказаться от обязательных скобок
            Token<?> openBracketThenToken = lexer.getToken();
            if( ! openBracketThenToken.match( TokenType.BRACE_OPEN ) ){
                throw new RuntimeException("P: ожидалась открывающаяся фигурная скобка ветви if");
            }
            // ветвь then
            result.setRight( parseBody() );
            Token<?> closeBracketThenToken = lexer.getToken();
            if( ! closeBracketThenToken.match( TokenType.BRACE_CLOSE ) ){
                throw new RuntimeException("P: ожидалась закрывающаяся фигурная скобка ветви if");
            }

            // дальше может быть часть else, а может и не быть
            Token<?> elseToken = lexer.peekToken();
            if( elseToken.match( TokenType.ELSE ) ){
                // есть else ветвь
                lexer.getToken();
                Token<?> openBraceElseToken = lexer.getToken();
                if( ! openBraceElseToken.match( TokenType.BRACE_OPEN ) ){
                    throw new RuntimeException("P: ожидалась открывающаяся фигурная скобка после else");
                }
                // добавляем else часть
                result.setRight( parseBody() );
                Token<?> closeBraceElseToken = lexer.getToken();
                if( ! closeBraceElseToken.match( TokenType.BRACE_CLOSE ) ){
                    throw new RuntimeException("P: ожидалась закрывающаяся фигурная скобка ветви else");
                }
            }

            break;
        case WHILE:
            lexer.getToken();
            Token<?> openBracketWhileToken = lexer.getToken();
            if( ! openBracketWhileToken.match( TokenType.BRACKET_OPEN ) ){
                throw new RuntimeException("P: ожидалась открывающаяся скобка после while");
            }

            result.setLeft( new Node(whatEver) );
            result.setRight( parseCondition() );

            Token<?> closeBracketWhileToken = lexer.getToken();
            if( ! closeBracketWhileToken.match( TokenType.BRACKET_CLOSE ) ){
                throw new RuntimeException("P: ожидалась закрывающаяся скобка после условия");
            }

            Token<?> openBraceWhileToken = lexer.getToken();
            if( ! openBraceWhileToken.match( TokenType.BRACE_OPEN ) ){
                throw new RuntimeException("P: ожидалась открывающаяся фигурная скобка тела цикла");
            }

            result.setRight( parseBody() );

            Token<?> closeBraceWhileToken = lexer.getToken();
            if( ! closeBraceWhileToken.match( TokenType.BRACE_CLOSE ) ){
                throw new RuntimeException("P: ожидалась закрывающаяся фигурная скобка тела цикла");
            }
            break;
		default:
            throw new RuntimeException("P: неизвестная команда");
		}
		
		return result;
	}

    private Node parseCondition() {
        Node result = new Node( TokenType.CONDITION );

        result.setLeft( parseExpr() );
        Token<?> signToken = lexer.getToken();
        if( ! signToken.match( TokenType.SIGN ) ){
            throw new RuntimeException("P: ожидался знак условия");
        }
        result.setRight( new Node(signToken) );
        result.setRight( parseExpr() );

        return result;
    }

    public Node parseArgList(){
		//правило вывода:
		//args -> expr , args | expr вырождается
		
		// проверка на отсутствие параметров
		Token<?> closeBracketToken = lexer.peekToken();
		// если вернуло закрывающуюся скобку, то вернём пустоту
		if( closeBracketToken.match( TokenType.BRACKET_CLOSE ) ){
			return new Node( TokenType.EMPTY );
		}
		
		// иначе начинаем парсить
		Node result = new Node( TokenType.ARG_LIST );
		
		Token<?> commaToken;
		do{
			result.setRight( parseExpr() );
			commaToken = lexer.peekToken();
		} while( commaToken.match( TokenType.COMMA ) && commaToken == lexer.getToken() );
		
		if( ! commaToken.match( TokenType.BRACKET_CLOSE ) ){
			// TODO: кинуть ошибку
            throw new RuntimeException("проверить");
		}
		
		return result;
	}

    // TODO: разделить на 2 функции,
    //одна для аргументов, другая для возвращаемого значения
	public Node parseType(){
		Node result = null;
		
		Token<?> typeToken = lexer.getToken();
		if( typeToken.match( TokenType.INT ) ||
		    typeToken.match( TokenType.DOUBLE ) ||
            typeToken.match( TokenType.VOID )){
			
			Node specificType = new Node( typeToken );
			result = new Node( TokenType.TYPE );
			result.setLeft( specificType );
		}else{
			// TODO: кинуть ошибку
            throw new RuntimeException("проверить");
		}
		
		return result;
	}
	
	//---------------------------------------------------
	//парсинг арифметических выражений
	//---------------------------------------------------
	
	// TODO: сделать приватными, только как тогда тестить
	public Node parseExpr(){
		Node result = parseTerm();
		
		Token<?> curToken = lexer.peekToken();
		while( curToken.match( TokenType.PLUS ) ||
			   curToken.match( TokenType.MINUS ) ){
			
			// пропускаем знак
			lexer.getToken();
			
			// комплектуем дерево
			Node sign = new Node( curToken );
			sign.setLeft( result );
			sign.setRight( parseTerm() );
			
			result = sign;
			
			curToken = lexer.peekToken();
		}
		
		return result;
	}
	
	public Node parseTerm(){
		Node result = parseFactor();
		
		Token<?> curToken = lexer.peekToken();
		while( curToken.match( TokenType.MULTIPLICATION ) ||
			   curToken.match( TokenType.DIVISION ) ){
			
			lexer.getToken();
			
			// комплектуем дерево
			Node sign = new Node( curToken );
			sign.setLeft( result );
			sign.setRight( parseFactor() );
			
			result = sign;
			
			curToken = lexer.peekToken();
		}
		
		return result;
	}
	
	public Node parseFactor(){
		Node result = parsePower();
		
		Token<?> curToken = lexer.peekToken();
		if( curToken.match( TokenType.EXPONENTIATION ) ){
			lexer.getToken(); // пропускаем знак ^
			
			Node exp = new Node( curToken );
			exp.setLeft( result );
			exp.setRight( parseFactor() );
			
			result = exp;
		}
		
		return result;
	}
	
	public Node parsePower(){
		Token<?> curToken = lexer.peekToken();
		// если унарный минус
		if( curToken.match( TokenType.MINUS ) ){
			lexer.getToken(); // пропускаем -
			
			Node minus = new Node( curToken );
			minus.setLeft( parseAtom() ); // тут только левый ребёнок
			return minus;
		}
		
		return parseAtom();
	}
	
	// TODO: доделать реализацию
	public Node parseAtom(){
		Node result = null;
		
		Token<?> token = lexer.getToken();
		switch( token.getTokenType() ){
		case BRACKET_OPEN:
			result = parseExpr();
			token = lexer.getToken();
			if( token.match( TokenType.BRACKET_CLOSE ) ){
				return result;
			}else{
				// TODO: кинуть ошибку
                throw new RuntimeException("проверить");
			}
		case NUMBER:
			result = new Node( token );
			break;
		case NAME:
			Token<?> nextToken = lexer.peekToken();
			if( nextToken.match( TokenType.BRACKET_OPEN ) ){
				// значит это вызов функции
				lexer.getToken();
				result = new Node( TokenType.CALL_FUNCTION );
				result.setLeft( new Node( token ) );
				result.setRight( parseArgList() );
				
				nextToken = lexer.getToken();
				if( ! nextToken.match( TokenType.BRACKET_CLOSE ) ){
					// TODO: кинуть ошибку
                    throw new RuntimeException("проверить");
				}
			} else{
				result = new Node( token );
			} 
			
			break;
		default:
			// TODO: кинуть ошибку
            throw new RuntimeException("проверить");
		}
		
		return result;
	}
}
