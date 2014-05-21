package main.ru.svichkarev.compiler.translator.table;

import main.ru.svichkarev.compiler.lexer.TokenType;

public class FunctionInfo {
    // для возвращаемого типа
    public enum FunctionReturnType{
        INT, DOUBLE, VOID;

        public static FunctionReturnType convertFromTokenType(TokenType tokenType){
            switch (tokenType){
                case INT:
                    return INT;
                case DOUBLE:
                    return DOUBLE;
                case VOID:
                    return VOID;
                default:
                    throw new RuntimeException( "TR: Недопустимый тип" );
            }
        }
    }

    private FunctionReturnType returnType;
    // TODO: или лучше вектор?
    private VariableInfo.VariableType [] parameterTypes;

    public FunctionReturnType getReturnType() {
        return returnType;
    }
}
