package auebprogramming;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ReadTxtoutputsCompleteCSV {

    /**
     * Εξάγει ΟΛΕΣ τις δομημένες εγγραφές από το αρχείο input file
     * σε μορφή CSV, με βελτιωμένη ευελιξία για πίνακες Υπουργείων (5 στήλες),
     * που μπορεί να έχουν ή να μην έχουν κωδικό.
     *
     * @param inputFileName Το όνομα του αρχείου εισόδου (π.χ. output2021.txt)
     * @param outputFileName Το όνομα του αρχείου εξόδου (π.χ. output.csv)
     */
    public void processTxtFileToCsv(final String inputFileName, final String outputFileName) {

        // 1. Patterns για την αναγνώριση
        
        // Pattern για να αναγνωρίσει οποιαδήποτε γραμμή ξεκινά με αριθμό (Κωδικό)
        final String RECORD_START_REGEX = "^\\s*(\\d[\\d\\.-]*)\\s+.*"; 

        // Pattern για να αναγνωρίσει την ομάδα των 3 ποσών στο τέλος (Σηματοδοτεί το τέλος 5-στηλης εγγραφής)
        final Pattern FIVE_COL_END_PATTERN = Pattern.compile("([\\d\\.,]+|0)\\s+([\\d\\.,]+|0)\\s+([\\d\\.,]+)$"); 

        // Pattern για 5 στήλες (Υπουργεία/Φορείς) - Ευέλικτο: Group 1: [Κωδικός] [Ονομασία] / Group 2,3,4: Ποσά
        final Pattern ENTITY_FIVE_COL_PATTERN = Pattern.compile("^(.+?)\\s+([\\d\\.,]+|0)\\s+([\\d\\.,]+|0)\\s+([\\d\\.,]+)$");

        // Pattern για 3 στήλες (Έσοδα/Αναλυτικά Έξοδα)
        final Pattern THREE_COL_PATTERN = Pattern.compile("^([\\d\\.-]+)\\s+(.+?)\\s+([\\d\\.,]+)$");
        
        // Pattern για να διαχωρίσει τον Κωδικό από την Ονομασία, αν υπάρχει
        final Pattern CODE_PREFIX_PATTERN = Pattern.compile("^([\\d\\.-]+)\\s+(.+)$");

        try {
            String rawText = new String(Files.readAllBytes(Paths.get(inputFileName)));

            // 2. Καθαρισμός και Ενοποίηση Γραμμών
            // (Προσαρμόστε τα παρακάτω αν οι κεφαλίδες του 2021 είναι διαφορετικές)
            String cleanedText = rawText
                .replaceAll("(?s)Οικονομικό έτος:.*?(?=\\n\\d|\\n\\s*\\n|$)", "")
                .replaceAll("(?s)Κωδικός\\s*ταξινόμησης\\s*Ο\\s*ν\\s*ο\\s*μ\\s*α\\s*σ\\s*ί\\s*α\\s*2025", "")
                .replaceAll("(?s)ΚΡΑΤΙΚΟΣ ΠΡΟΫΠΟΛΟΓΙΣΜΟΣ", "")
                .replaceAll("(?s)ΕΣΟΔΑ\\s*ΣΥΝΟΠΤΙΚΟΣ ΠΙΝΑΚΑΣ", "")
                .replaceAll("(?s)ΠΙΣΤΩΣΕΙΣ ΚΑΤΑ ΕΙΔΙΚΟ ΦΟΡΕΑ ΚΑΙ ΜΕΙΖΟΝΑ ΚΑΤΗΓΟΡΙΑ ΔΑΠΑΝΗΣ.*?(?=\\n\\d|\\n\\s*\\n|$)", "")
                .replaceAll("(?s)ΣΥΝΟΛΟ.*", ""); 

            String[] lines = cleanedText.split("\\n");
            StringBuilder mergedRecords = new StringBuilder();
            String currentRecord = "";
            boolean inTableArea = false; 

            for (String line : lines) {
                String trimmedLine = line.trim();
                if (trimmedLine.isEmpty()) continue;
                
                // Έλεγχοι για το πότε μια γραμμή σηματοδοτεί την έναρξη μιας νέας εγγραφής
                boolean startsWithCode = trimmedLine.matches(RECORD_START_REGEX);
                // ΝΕΟΣ ΕΛΕΓΧΟΣ: Η γραμμή τελειώνει με 3 ποσά (χαρακτηριστικό των Υπουργείων)
                boolean endsWithFiveColumns = FIVE_COL_END_PATTERN.matcher(trimmedLine).find(); 
                
                // Έναρξη περιοχής πίνακα (για παράλειψη άσχετων αρχικών κειμένων)
                if (startsWithCode || trimmedLine.startsWith("Υπουργεία") || trimmedLine.startsWith("Αποκεντρωμένες Διοικήσεις") || trimmedLine.startsWith("Κάλυψη") || trimmedLine.startsWith("Τακτικός")) {
                    inTableArea = true;
                }
                
                if (inTableArea) {
                    // Έναρξη ΝΕΑΣ εγγραφής: Είτε ξεκινά με κωδικό ΕΙΤΕ τελειώνει με τρία ποσά (για Υπουργεία)
                    if (startsWithCode || endsWithFiveColumns || trimmedLine.startsWith("Υπουργεία") || trimmedLine.startsWith("Αποκεντρωμένες Διοικήσεις") || trimmedLine.startsWith("Κάλυψη") || trimmedLine.startsWith("Τακτικός")) {
                        
                        if (!currentRecord.isEmpty()) {
                            // Ολοκληρωμένη η προηγούμενη εγγραφή, προσθήκη στο merged list
                            mergedRecords.append(currentRecord.replaceAll("\\s{2,}", " ")).append("\n");
                        }
                        // Έναρξη νέας εγγραφής
                        currentRecord = trimmedLine;
                    } 
                    // Συνέχεια περιγραφής (π.χ. πολυγραμμικό πεδίο)
                    else if (!currentRecord.isEmpty()) {
                        currentRecord += " " + trimmedLine;
                    }
                }
            }
            // Προσθήκη της τελευταίας εγγραφής
            if (!currentRecord.isEmpty()) {
                mergedRecords.append(currentRecord.replaceAll("\\s{2,}", " ")).append("\n");
            }


            // 3. Εφαρμογή CSV Formatting
            
            StringBuilder csvContent = new StringBuilder();
            csvContent.append("Κωδικός,Ονομασία,Ποσό 1,Ποσό 2,Ποσό 3").append("\n"); 
            
            String[] finalLines = mergedRecords.toString().split("\\n");

            int recordsFound = 0;
            for (String line : finalLines) {
                
                Matcher entityFiveColMatcher = ENTITY_FIVE_COL_PATTERN.matcher(line.trim());
                Matcher threeColMatcher = THREE_COL_PATTERN.matcher(line.trim());
                
                // Χειρισμός 5-στηλων (Υπουργεία/Φορείς)
                if (entityFiveColMatcher.find()) {
                    String fullPrefix = entityFiveColMatcher.group(1).trim(); 
                    String amount1 = entityFiveColMatcher.group(2).trim();
                    String amount2 = entityFiveColMatcher.group(3).trim();
                    String amount3 = entityFiveColMatcher.group(4).trim();

                    // Προσπάθεια διαχωρισμού Κωδικού & Ονομασίας
                    Matcher codePrefixMatcher = CODE_PREFIX_PATTERN.matcher(fullPrefix);
                    String code = "";
                    String description = "";

                    if (codePrefixMatcher.find()) {
                        // Case A: Βρέθηκε Κωδικός (π.χ. 1001 Προεδρία)
                        code = codePrefixMatcher.group(1).trim(); 
                        description = codePrefixMatcher.group(2).trim().replace("\"", "").replace(",", " "); 
                    } else {
                        // Case B: Δεν βρέθηκε Κωδικός (π.χ. Υπουργείο Εσωτερικών)
                        code = ""; 
                        description = fullPrefix.replace("\"", "").replace(",", " "); 
                    }
                    
                    if (!description.startsWith("Τακτικός Προϋπολογισμός")) {
                        csvContent
                            .append(code).append(",")
                            .append("\"").append(description).append("\"").append(",")
                            .append(amount1).append(",")
                            .append(amount2).append(",")
                            .append(amount3).append("\n");
                        recordsFound++;
                    }

                } 
                // Χειρισμός 3-στηλων (Έσοδα/Αναλυτικά Έξοδα)
                else if (threeColMatcher.find()) {
                    String code = threeColMatcher.group(1).trim(); 
                    String description = threeColMatcher.group(2).trim().replace("\"", "").replace(",", " "); 
                    String amount1 = threeColMatcher.group(3).trim();
                    
                    if (!code.equals("2") && !code.equals("1")) { 
                        csvContent
                            .append(code).append(",")
                            .append("\"").append(description).append("\"").append(",")
                            .append(amount1).append(",")
                            .append("").append(",") 
                            .append("").append("\n"); 
                        recordsFound++;
                    }
                    
                } 
                // Χειρισμός ειδικών γραμμών 3-στηλων χωρίς αριθμό στην αρχή
                else if (line.startsWith("Κάλυψη") || line.contains("ΑΠΟΤΕΛΕΣΜΑ")) {
                    Pattern singleAmountPattern = Pattern.compile("(.+?)\\s+([\\d\\.,-]+)$");
                    Matcher singleAmountMatcher = singleAmountPattern.matcher(line.trim());
                    if (singleAmountMatcher.find()) {
                        csvContent
                            .append("").append(",")
                            .append("\"").append(singleAmountMatcher.group(1).trim().replace("\"", "").replace(",", " ")).append("\",")
                            .append(singleAmountMatcher.group(2).trim()).append(",,")
                            .append("\n");
                        recordsFound++;
                    } else if (line.contains("ΑΠΟΤΕΛΕΣΜΑ")) {
                        csvContent
                            .append("").append(",")
                            .append("\"").append(line.trim().replace("\"", "").replace(",", " ")).append("\",")
                            .append("").append(",,")
                            .append("\n");
                        recordsFound++;
                    }
                }
            }

            // 4. Γράφουμε το αναλυμένο περιεχόμενο στο αρχείο CSV.
            try (FileWriter writer = new FileWriter(outputFileName)) {
                writer.write(csvContent.toString());
            }
            
            System.out.println("Επιτυχής εξαγωγή " + recordsFound + " εγγραφών με βελτιωμένη ευελιξία σε " + outputFileName);
            
        } catch (IOException e) {
            System.err.println("Σφάλμα I/O κατά τη λειτουργία: " + e.getMessage());
        }
    }
}
