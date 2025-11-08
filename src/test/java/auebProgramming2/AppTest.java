package auebProgramming2;

/**
 * Unit test for simple App.
 */
public class AppTest {

    /**
     * Rigorous Test :-)
     */

    public static void main(String[] args) {
        try {
            Links links = new Links();
            links.linkGovBudget();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
