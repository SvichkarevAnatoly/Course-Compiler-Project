package main.ru.svichkarev.compiler.translator.table;

public class FunctionInfo {
    // для возвращаемого типа
    private enum FunctionReturnType{
        INT, DOUBLE, VOID
    }

    // для типа параметра
    private enum FunctionParameterType{
        INT, DOUBLE
    }

    private FunctionReturnType returnType;
    // TODO: или лучше вектор?
    private FunctionParameterType [] parameterTypes;

    // TODO: удобные методы
}
