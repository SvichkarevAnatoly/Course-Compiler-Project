package main.ru.svichkarev.compiler.translator.table;

import main.ru.svichkarev.compiler.lexer.TokenType;

public class VariableInfo{
    // для типа переменной
    public enum VariableType{
        INT, DOUBLE;

        public static VariableType convertFromTokenType(TokenType tokenType){
            switch (tokenType){
                case INT:
                    return INT;
                case DOUBLE:
                    return DOUBLE;
                default:
                    throw new RuntimeException( "Недопустимый тип" );
            }
        }
    }

    private VariableType type;

    private boolean initialization = false;

    // номер переменной на стеке
    private int localsIndex;

    public VariableInfo( VariableType type, int index ) {
        initialization = false;
        this.type = type;
        localsIndex = index;
    }

    // получить номер переменной
    public int getLocalsIndex(){
        return localsIndex;
    }

    // установить флаг инициализации
    public void setInitialization(){
        initialization = true;
    }
}
