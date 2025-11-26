package auebprogramming;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class GetPdf {
    /*
        This is a method that download the budget for eatch year as a pdf 
        It uses the static linksForBudget to get the correct url
    */
    public void fileDownloader(final int year) {
        String fileUrl = GetPdf.linksForBudget(year);
        String destinationFile = "src/main/resources/budget" + year + ".pdf";

        try {
            URL url = new URL(fileUrl);
            try (InputStream in = url.openStream()) {
                Files.copy(in, Paths.get(destinationFile), StandardCopyOption.REPLACE_EXISTING);
            }
            System.out.println("File downloaded successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static String urlBudget = "";

    /*
        this is a method that selects the correct link to work with
    */
    static String linksForBudget(final int year) throws IllegalArgumentException {
        try { switch (year) {
            case 2026:
                urlBudget = "https://minfin.gov.gr/wp-content/uploads/2025/11/%CE%9A%CF%81%CE%B1%CF%84%CE%B9%CE%BA%CF%8C%CF%82-%CE%A0%CF%81%CE%BF%CF%8B%CF%80%CE%BF%CE%BB%CE%BF%CE%B3%CE%B9%CF%83%CE%BC%CF%8C%CF%82-2026.pdf";
                return urlBudget;
            case 2025: 
                urlBudget = "https://minfin.gov.gr/wp-content/uploads/2024/11/%CE%9A%CF%81%CE%B1%CF%84%CE%B9%CE%BA%CF%8C%CF%82-%CE%A0%CF%81%CE%BF%CF%8B%CF%80%CE%BF%CE%BB%CE%BF%CE%B3%CE%B9%CF%83%CE%BC%CF%8C%CF%82-2025_%CE%9F%CE%95.pdf";
                return urlBudget;
            case 2024:
                urlBudget = "https://minfin.gov.gr/wp-content/uploads/2023/11/%CE%9A%CE%A1%CE%91%CE%A4%CE%99%CE%9A%CE%9F%CE%A3-%CE%A0%CE%A1%CE%9F%CE%A5%CE%A0%CE%9F%CE%9B%CE%9F%CE%93%CE%99%CE%A3%CE%9C%CE%9F%CE%A3-2024.pdf";
                return urlBudget;
            case 2023:
                urlBudget = "https://minfin.gov.gr/wp-content/uploads/2023/11/21-11-2022-%CE%9A%CE%A1%CE%91%CE%A4%CE%99%CE%9A%CE%9F%CE%A3-%CE%A0%CE%A1%CE%9F%CE%AB%CE%A0%CE%9F%CE%9B%CE%9F%CE%93%CE%99%CE%A3%CE%9C%CE%9F%CE%A3-2023.pdf";
                return urlBudget;
            case 2022:
                urlBudget = "https://minfin.gov.gr/wp-content/uploads/2023/11/%CE%9A%CE%A1%CE%91%CE%A4%CE%99%CE%9A%CE%9F%CE%A3-%CE%A0%CE%A1%CE%9F%CE%A5%CE%A0%CE%9F%CE%9B%CE%9F%CE%93%CE%99%CE%A3%CE%9C%CE%9F%CE%A3_2022.pdf";
                return urlBudget;
            case 2021:
                urlBudget = "https://minfin.gov.gr/wp-content/uploads/2023/11/%CE%9A%CE%A1%CE%91%CE%A4%CE%99%CE%9A%CE%9F%CE%A3-%CE%A0%CE%A1%CE%9F%CE%A5%CE%A0%CE%9F%CE%9B%CE%9F%CE%93%CE%99%CE%A3%CE%9C%CE%9F%CE%A3-2021.pdf";
                return urlBudget;
            case 2020:
                urlBudget = "https://minfin.gov.gr/wp-content/uploads/2019/11/21-11-2019-%CE%9A%CE%A1%CE%91%CE%A4%CE%99%CE%9A%CE%9F%CE%A3-%CE%A0%CE%A1%CE%9F%CE%A5%CE%A0%CE%9F%CE%9B%CE%9F%CE%93%CE%99%CE%A3%CE%9C%CE%9F%CE%A3-2020.pdf";
                return urlBudget;
            }
        } catch (IllegalArgumentException e) {
            return "wrong year, choose from 2020 - 2025";
        }
        return "";

    }
}