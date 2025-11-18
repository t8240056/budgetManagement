 package auebprogramming;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public final class Links {    
    /** this is a method for getting the budget links for each year */

    String specificYearHref;

    public String linkGovBudget(int year) throws Exception {

        String url = "https://minfin.gov.gr/kratikos-proypologismos/";
        Document doc = Jsoup.connect(url).get();
        Elements links = doc.select("a[href]");
        if (year != 2018){
            for (Element link : links) {
                String href = link.attr("abs:href");
                if (href.contains("https://minfin.gov.gr/kratikos-proypologismos-" + year)) {
                    String specificYearHref = href;
                    return href ;
                    
                }
            }

        }else {
            String href = "https://minfin.gov.gr/proypologismos-2018/";
            String specificYearHref = href;
            return href;
            
        }
        return "";

    } 

     public String getUrlPdf(int year) throws Exception {
        
        String url =   specificYearHref;
      
        Document doc = Jsoup.connect(url).get();
        Elements links = doc.select("a[href]");
        if (year != 2018){
            for (Element link : links) {
                String href = link.attr("abs:href");
                if (href.contains("ΚΡΑΤΙΚΟΣ-ΠΡΟΥΠΟΛΟΓΙΣΜΟΣ-" + year)) {
                    
                    return href;
                    
                }
            }

        }else {
            return "exception maybe ";
        }
        return "";
    }

}
