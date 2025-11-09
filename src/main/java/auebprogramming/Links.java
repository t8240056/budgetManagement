 package auebprogramming;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public final class Links {
    /** this is a method for getting the budget links */
    public String linkGovBudget() throws Exception {
        String url = "https://minfin.gov.gr/kratikos-proypologismos/";
        Document doc = Jsoup.connect(url).get();

        Elements links = doc.select("a[href]");
        for (Element link : links) {
            String href = link.attr("abs:href");
            if (href.contains("kratikos-proypologismos")) {
                System.out.println(href);
            }
        }
    }
}
