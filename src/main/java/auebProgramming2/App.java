package auebProgramming2;

import java.io.File;

public final class App {
    public static void main(final String[] args) throws Exception {
        Links link = new Links();
        link.linkGovBudget();

        File file = new File("https://minfin.gov.gr/wp-content/uploads/2024/11/%CE%9A%CF%81%CE%B1%CF%84%CE%B9%CE%BA%CF%8C%CF%82-%CE%A0%CF%81%CE%BF%CF%8B%CF%80%CE%BF%CE%BB%CE%BF%CE%B3%CE%B9%CF%83%CE%BC%CF%8C%CF%82-2025_%CE%9F%CE%95.pdf");
        link.extractLines(file);

        ConnectToSql table = new ConnectToSql();
        table.getTableElements();
    }
}
