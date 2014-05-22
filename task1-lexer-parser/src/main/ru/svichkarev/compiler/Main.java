package main.ru.svichkarev.compiler;

import main.ru.svichkarev.compiler.buffer.Buffer;
import main.ru.svichkarev.compiler.lexer.Lexer;
import main.ru.svichkarev.compiler.parser.Node;
import main.ru.svichkarev.compiler.parser.Parser;
import main.ru.svichkarev.compiler.translator.Translator;

import java.io.*;

public final class Main {
	public static void main( String[] args ){
		// при подаче файла, выделяем имя и действуем как обычный компилятор

        String inputFileName = null;
        String outputFileName = "out.j";
        switch ( args.length ){
            case 2: // указан файл исходного кода и выходной файл
                outputFileName = args[1];
            case 1: // указан только файл исходного кода
                inputFileName = args[0];

                Reader fileReader = null;
                try {
                    fileReader = new FileReader( inputFileName );
                } catch (IOException e) {
                    System.out.println( "Не смогли открыть файл: " + inputFileName );
                    return;
                }

                Buffer buffer = new Buffer( fileReader );
                Lexer lexer = new Lexer( buffer );
                Parser parser = new Parser( lexer );

                Node programTree = parser.parseProgram();

                Writer fileWriter = null;
                try {
                    fileWriter = new FileWriter( outputFileName );
                } catch (IOException e) {
                    System.out.println("Не смогли открыть файл: " + outputFileName);
                    return;
                }

                // отрезать расширение
                outputFileName = outputFileName.substring(0, outputFileName.lastIndexOf("."));

                Translator translator = new Translator( programTree, fileWriter, outputFileName );
                translator.translateProgram();

                try {
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
            default:
                System.out.println( "Не указан входной файл" );
        }

		System.out.println("Завершился успешно.");
	}
}