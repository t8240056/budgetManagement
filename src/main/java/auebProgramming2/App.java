package auebProgramming2;

import java.io.File;

public final class App {
    public static void main(final String[] args) throws Exception {
        Links link = new Links();
        link.linkGovBudget();

        File file = new File("https://minfin.gov.gr/wp-content/uploads/2024/11/kratikos-proypologismos-2025_ΟΕ.pdf");
        link.extractLines(file);

        ConnectToSql table = new ConnectToSql();
        table.getTableElements();
    }
}
