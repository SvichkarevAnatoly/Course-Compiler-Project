package main.ru.svichkarev.compiler.translator.table;

import main.ru.svichkarev.compiler.lexer.TokenType;

import java.util.Vector;

public class FunctionInfo {
    // TODO: добавить VOID в лексер
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

        public VariableInfo.VariableType convertToVariableType(){
            switch (this){
                case INT:
                    return VariableInfo.VariableType.INT;
                case DOUBLE:
                    return VariableInfo.VariableType.DOUBLE;
                default:
                    throw new RuntimeException( "TR: Недопустимый тип" );
            }
        }

        public String toString(){
            switch (this){
                case VOID:
                    return "V";
                case INT:
                    return "I";
                case DOUBLE:
                    return "D";
                default:
                    throw new RuntimeException( "TR: Недопустимый тип" );
            }
        }
    }

    public FunctionInfo(FunctionReturnType returnType, Vector<VariableInfo.VariableType> parameterTypes) {
        this.returnType = returnType;
        this.parameterTypes = parameterTypes;
    }

    private FunctionReturnType returnType;
    private Vector<VariableInfo.VariableType> parameterTypes;

    public FunctionReturnType getReturnType() {
        return returnType;
    }

    // возвращает число аргументов
    public int getAmountParameters() {
        return parameterTypes.size();
    }
}
