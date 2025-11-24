package auebprogramming;

/**entry point of the application */
public class App {

    //we dont want everytime we run the main to download the files from the start
    static int getFilesOnce = 0;
    public static void main(final String[] args) {
        if (getFilesOnce == 0 ) {
            DownloadingOutputFiles.run();
            getFilesOnce +=1;            
        }
    }
}
