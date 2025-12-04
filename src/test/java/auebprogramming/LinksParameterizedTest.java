package auebprogramming;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import auebprogramming.ImportPDFoutputTXT.Links;

class LinksParameterizedTest {

    @ParameterizedTest
    @CsvSource({
        "2025, https://minfin.gov.gr/wp-content/uploads/2024/11/%CE%9A%CF%81%CE%B1%CF%84%CE%B9%CE%BA%CF%8C%CF%82-%CE%A0%CF%81%CE%BF%CF%8B%CF%80%CE%BF%CE%BB%CE%BF%CE%B3%CE%B9%CF%83%CE%BC%CF%8C%CF%82-2025_%CE%9F%CE%95.pdf",
        "2024, https://minfin.gov.gr/wp-content/uploads/2023/11/%CE%9A%CE%A1%CE%91%CE%A4%CE%99%CE%9A%CE%9F%CE%A3-%CE%A0%CE%A1%CE%9F%CE%A5%CE%A0%CE%9F%CE%9B%CE%9F%CE%93%CE%99%CE%A3%CE%9C%CE%9F%CE%A3-2024.pdf",
        "2023, https://minfin.gov.gr/wp-content/uploads/2023/11/21-11-2022-%CE%9A%CE%A1%CE%91%CE%A4%CE%99%CE%9A%CE%9F%CE%A3-%CE%A0%CE%A1%CE%9F%CE%AB%CE%A0%CE%9F%CE%9B%CE%9F%CE%93%CE%99%CE%A3%CE%9C%CE%9F%CE%A3-2023.pdf",
        "2022, https://minfin.gov.gr/wp-content/uploads/2023/11/%CE%9A%CE%A1%CE%91%CE%A4%CE%99%CE%9A%CE%9F%CE%A3-%CE%A0%CE%A1%CE%9F%CE%A5%CE%A0%CE%9F%CE%9B%CE%9F%CE%93%CE%99%CE%A3%CE%9C%CE%9F%CE%A3_2022.pdf",
        "2021, https://minfin.gov.gr/wp-content/uploads/2023/11/%CE%9A%CE%A1%CE%91%CE%A4%CE%99%CE%9A%CE%9F%CE%A3-%CE%A0%CE%A1%CE%9F%CE%A5%CE%A0%CE%9F%CE%9B%CE%9F%CE%93%CE%99%CE%A3%CE%9C%CE%9F%CE%A3-2021.pdf",
        "2020, https://minfin.gov.gr/wp-content/uploads/2019/11/21-11-2019-%CE%9A%CE%A1%CE%91%CE%A4%CE%99%CE%9A%CE%9F%CE%A3-%CE%A0%CE%A1%CE%9F%CE%A5%CE%A0%CE%9F%CE%9B%CE%9F%CE%93%CE%99%CE%A3%CE%9C%CE%9F%CE%A3-2020.pdf"
    })
    void testLinksForBudget_ValidYears(int year, String expectedUrl) {
        // Arrange
        Links links = new Links();
        
        // Act
        String result = links.linksForBudget(year);
        
        // Assert
        assertEquals(expectedUrl, result);
    }

    @ParameterizedTest
    @CsvSource({
        "2019",
        "2030",
        "1999",
        "2027"
    })
    void testLinksForBudget_InvalidYears(int invalidYear) {
        // Arrange
        Links links = new Links();
        
        // Act
        String result = links.linksForBudget(invalidYear);
        
        // Assert
        assertEquals("", result);
    }
}
