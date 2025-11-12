package auebprogramming;

/**entry point of the application */
public class App {
    public static void main(final String[] args) throws Exception {
        ReadPDF read = new ReadPDF();
        read.getInFile();

        Links link = new Links();
        link.linkGovBudget();
    }

}
