package auebprogramming;

public class TestRevenue{
    public static void main(String [] args) {
        RevenueDataManager revenueDataManager = new RevenueDataManager();
        String[][] array1;
        array1 = (revenueDataManager.get3DigitCodes("11"));
        for (int i = 0; i< array1.length; i++){
            for (int j =0; j<array1[i].length; j++){
                System.out.println(array1[i][j]);
            }
        }
    }
}