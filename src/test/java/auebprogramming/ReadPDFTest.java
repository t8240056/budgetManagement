package auebprogramming;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class ReadPDFTest {

    private ReadPDF readPDF;

    @BeforeEach
    public void setUp() {
        readPDF = new ReadPDF();
    }

    @Test
    public void testReadPDFCreation() {
        assertNotNull(readPDF);
    }
}
