package main.ru.svichkarev.compiler.translator;

public class VariableTableInfo {
    // для типа переменной
    public enum VariableType{
        INT, DOUBLE
    }

    private VariableType type;
    private boolean initialization = false;
    // номер переменной на стеке
    private int stackNumber;
}
