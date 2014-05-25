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

        // возможность приведения типов
        public boolean isCast(VariableType actualVariableType) {
            switch ( this ){
                case DOUBLE:
                    return true;
                case INT:
                    switch (actualVariableType){
                        case INT:
                            return true;
                        case DOUBLE:
                            return false;
                    }
                default:
                    throw new RuntimeException( "Недопустимый тип" );
            }
        }

        public String castStr(VariableType actualType) {
            switch ( this ){
                case DOUBLE:
                    switch ( actualType ){
                        case INT:
                            return "   i2d\n";
                        case DOUBLE:
                            return "";
                    }
                case INT:
                    return "";
                default:
                    throw new RuntimeException( "Недопустимый тип" );
            }
        }

        public String toString(){
            switch (this){
                case INT:
                    return "I";
                case DOUBLE:
                    return "D";
                default:
                    throw new RuntimeException( "TR: Недопустимый тип" );
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

    // конструктор копирования
    public VariableInfo( VariableInfo parentVarInf ){
        this.initialization = parentVarInf.initialization;
        this.type = parentVarInf.type;
        this.localsIndex = parentVarInf.localsIndex;
    }

    // проверка инициализации
    public boolean isInitialized() {
        return initialization;
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
