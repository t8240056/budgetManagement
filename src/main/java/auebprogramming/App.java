<<<<<<< HEAD
package auebProgramming2;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        System.out.println("Hello World!");
    }
=======
package auebprogramming;

/**entry point of the application */
public class App {
    public static void main(final String[] args) throws Exception {
        ReadPDF read = new ReadPDF();
        read.getInFile();

        Links link = new Links();
        link.linkGovBudget();
    }

>>>>>>> sqlToJava
}
