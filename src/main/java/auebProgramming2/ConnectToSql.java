package auebProgramming2;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ConnectToSql {
    public void getTableElements(){
        Elements tables = doc.select("table");
        for (Element table : tables) {
            Elements rows = table.select("tr");
            for (Element row : rows) {
                Elements cols = row.select("td");
                for (Element col : cols) {
                    System.out.print(col.text() + "\t");
                }
                System.out.println();
            }
        }
    }
}
