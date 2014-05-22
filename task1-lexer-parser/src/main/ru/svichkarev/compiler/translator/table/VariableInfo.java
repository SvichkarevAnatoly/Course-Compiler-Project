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

    // смещение относительно начала области переменных
    //(для double смещение на 2)
    private int localsIndex;

    public VariableInfo( TokenType type ) {
        initialization = false;
        this.type = VariableType.convertFromTokenType( type );
        localsIndex = -1; // TODO: так ли надо
    }

    // получить тип переменной
    public VariableType getType() {
        return type;
    }

    // получить номер переменной
    public int getLocalsIndex(){
        return localsIndex;
    }

    // установить индекс(не в конструкторе, т.к. заранее не известен номер)
    public void setLocalsIndex(int localsIndex) {
        this.localsIndex = localsIndex;
    }

    // установить флаг инициализации
    public void setInitialization(){
        initialization = true;
    }
}
