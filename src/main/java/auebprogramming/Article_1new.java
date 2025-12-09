package auebprogramming;
public class Article_1new {
    public static void printArticle1New(int year){
        ExpenseManager exp_mgr = new ExpenseManager("expense_categories_" + year + ".csv");
        RevenueManager.showRevenues();
        exp_mgr.showExpenses();
    }

}
