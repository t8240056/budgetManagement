package auebprogramming;

import java.util.*;

public class BudgetAnalyzer {
    
    public static class TaktikosProupologismos {
        public String kodikosForea;
        public String onomaForea;
        public double synolo;
        
        public TaktikosProupologismos(String kodikos, String onoma, double synolo) {
            this.kodikosForea = kodikos;
            this.onomaForea = onoma;
            this.synolo = synolo;
        }
    }
    
    public static class DimosiesEpendiseis {
        public String kodikosForea;
        public String onomaForea;
        public double synolo;
        
        public DimosiesEpendiseis(String kodikos, String onoma, double synolo) {
            this.kodikosForea = kodikos;
            this.onomaForea = onoma;
            this.synolo = synolo;
        }
    }
    
    private TaktikosProupologismos[] taktikosProupologismosTable;
    private DimosiesEpendiseis[] dimosiesEpendiseisTable;
    
    public BudgetAnalyzer() {
        this.taktikosProupologismosTable = new TaktikosProupologismos[] {
            new TaktikosProupologismos("1001", "PROEDRIA TIS DEMOKRATIAS", 4638000.0),
            new TaktikosProupologismos("1003", "BOULI TON ELLINON", 169950000.0),
            new TaktikosProupologismos("1004", "PROEDRIA TIS KYBERNISIS", 37689000.0),
            new TaktikosProupologismos("1007", "YPOURGEIO ESOTERIKON", 3449276000.0),
            new TaktikosProupologismos("1009", "YPOURGEIO EXOTERIKON", 390237000.0),
            new TaktikosProupologismos("1011", "YPOURGEIO ETHNIKIS AMYNAS", 6061000000.0),
            new TaktikosProupologismos("1015", "YPOURGEIO YGEIAS", 6608424000.0),
            new TaktikosProupologismos("1017", "YPOURGEIO DIKAOSYNIS", 577803000.0),
            new TaktikosProupologismos("1020", "YPOURGEIO PAIDEIAS, THRISKEVMATON KAI ATHLITISMOU", 5594000000.0),
            new TaktikosProupologismos("1022", "YPOURGEIO POLITISMOU", 257419000.0),
            new TaktikosProupologismos("1024", "YPOURGEIO ETHNIKIS OIKONOMIAS KAI OIKONOMIKON", 1243381464000.0),
            new TaktikosProupologismos("1029", "YPOURGEIO AGROTIKIS ANAPTYXIS KAI TROFIMON", 222403000.0),
            new TaktikosProupologismos("1031", "YPOURGEIO PERIVALLONTOS KAI ENERGEIAS", 319227000.0),
            new TaktikosProupologismos("1032", "YPOURGEIO ERGASIAS KAI KOINONIKIS ASFALISIS", 18215084000.0),
            new TaktikosProupologismos("1034", "YPOURGEIO KOINONIKIS SYNOXIS KAI OIKOGENEIAS", 3786553000.0),
            new TaktikosProupologismos("1036", "YPOURGEIO ANAPTYXIS", 123045000.0),
            new TaktikosProupologismos("1039", "YPOURGEIO YPODOMON KAI METAFORON", 881810000.0),
            new TaktikosProupologismos("1041", "YPOURGEIO NAVTILIAS KAI NISIOTIKIS POLITIKIS", 336864000.0),
            new TaktikosProupologismos("1045", "YPOURGEIO TOURISMOU", 39293000.0),
            new TaktikosProupologismos("1053", "YPOURGEIO PSIFIAKIS DIAKYBERNISIS", 151928000.0),
            new TaktikosProupologismos("1055", "YPOURGEIO METANASTEYSIS KAI ASYLOU", 141871000.0),
            new TaktikosProupologismos("1057", "YPOURGEIO PROSTASIAS TOU POLITI", 2217820000.0),
            new TaktikosProupologismos("1060", "YPOURGEIO KLIMATIKIS KRISIS KAI POLITIKIS PROSTASIAS", 760116000.0),
            new TaktikosProupologismos("1901", "APOKENTROMENI DIOIKISI ATTIKIS", 13091000.0),
            new TaktikosProupologismos("1902", "APOKENTROMENI DIOIKISI THESSALIAS - STEREAS ELLADAS", 10579000.0),
            new TaktikosProupologismos("1903", "APOKENTROMENI DIOIKISI IPEIROU - DYTIKIS MAKEDONIAS", 9943000.0),
            new TaktikosProupologismos("1904", "APOKENTROMENI DIOIKISI PELOPONNISOU - DYTIKIS ELLADAS KAI IONIOU", 14918000.0),
            new TaktikosProupologismos("1905", "APOKENTROMENI DIOIKISI AIGAIOU", 6188000.0),
            new TaktikosProupologismos("1906", "APOKENTROMENI DIOIKISI KRITIS", 6497000.0),
            new TaktikosProupologismos("1907", "APOKENTROMENI DIOIKISI MAKEDONIAS - THRAKIS", 18376000.0)
        };
        
        this.dimosiesEpendiseisTable = new DimosiesEpendiseis[] {
            new DimosiesEpendiseis("1001", "PROEDRIA TIS DEMOKRATIAS", 0.0),
            new DimosiesEpendiseis("1003", "BOULI TON ELLINON", 2000000.0),
            new DimosiesEpendiseis("1004", "PROEDRIA TIS KYBERNISIS", 4000000.0),
            new DimosiesEpendiseis("1007", "YPOURGEIO ESOTERIKON", 381000000.0),
            new DimosiesEpendiseis("1009", "YPOURGEIO EXOTERIKON", 30000000.0),
            new DimosiesEpendiseis("1011", "YPOURGEIO ETHNIKIS AMYNAS", 69000000.0),
            new DimosiesEpendiseis("1015", "YPOURGEIO YGEIAS", 569000000.0),
            new DimosiesEpendiseis("1017", "YPOURGEIO DIKAOSYNIS", 73000000.0),
            new DimosiesEpendiseis("1020", "YPOURGEIO PAIDEIAS, THRISKEVMATON KAI ATHLITISMOU", 1012000000.0),
            new DimosiesEpendiseis("1022", "YPOURGEIO POLITISMOU", 318000000.0),
            new DimosiesEpendiseis("1024", "YPOURGEIO ETHNIKIS OIKONOMIAS KAI OIKONOMIKON", 0.0),
            new DimosiesEpendiseis("1029", "YPOURGEIO AGROTIKIS ANAPTYXIS KAI TROFIMON", 1059000000.0),
            new DimosiesEpendiseis("1031", "YPOURGEIO PERIVALLONTOS KAI ENERGEIAS", 2022000000.0),
            new DimosiesEpendiseis("1032", "YPOURGEIO ERGASIAS KAI KOINONIKIS ASFALISIS", 463000000.0),
            new DimosiesEpendiseis("1034", "YPOURGEIO KOINONIKIS SYNOXIS KAI OIKOGENEIAS", 203000000.0),
            new DimosiesEpendiseis("1036", "YPOURGEIO ANAPTYXIS", 695000000.0),
            new DimosiesEpendiseis("1039", "YPOURGEIO YPODOMON KAI METAFORON", 1813000000.0),
            new DimosiesEpendiseis("1041", "YPOURGEIO NAVTILIAS KAI NISIOTIKIS POLITIKIS", 315000000.0),
            new DimosiesEpendiseis("1045", "YPOURGEIO TOURISMOU", 150000000.0),
            new DimosiesEpendiseis("1053", "YPOURGEIO PSIFIAKIS DIAKYBERNISIS", 922000000.0),
            new DimosiesEpendiseis("1055", "YPOURGEIO METANASTEYSIS KAI ASYLOU", 334000000.0),
            new DimosiesEpendiseis("1057", "YPOURGEIO PROSTASIAS TOU POLITI", 68000000.0),
            new DimosiesEpendiseis("1060", "YPOURGEIO KLIMATIKIS KRISIS KAI POLITIKIS PROSTASIAS", 461000000.0),
            new DimosiesEpendiseis("1901", "APOKENTROMENI DIOIKISI ATTIKIS", 0.0),
            new DimosiesEpendiseis("1902", "APOKENTROMENI DIOIKISI THESSALIAS - STEREAS ELLADAS", 0.0),
            new DimosiesEpendiseis("1903", "APOKENTROMENI DIOIKISI IPEIROU - DYTIKIS MAKEDONIAS", 0.0),
            new DimosiesEpendiseis("1904", "APOKENTROMENI DIOIKISI PELOPONNISOU - DYTIKIS ELLADAS KAI IONIOU", 0.0),
            new DimosiesEpendiseis("1905", "APOKENTROMENI DIOIKISI AIGAIOU", 0.0),
            new DimosiesEpendiseis("1906", "APOKENTROMENI DIOIKISI KRITIS", 0.0),
            new DimosiesEpendiseis("1907", "APOKENTROMENI DIOIKISI MAKEDONIAS - THRAKIS", 0.0)
        };
    }
    
    public boolean displaySynola(String kodikosForea) {
        Double taktikosSynolo = findTaktikosSynolo(kodikosForea);
        String onomaForea = findOnomaForea(kodikosForea);
        Double dimosiesSynolo = findDimosiesSynolo(kodikosForea);
        
        if (onomaForea == null) {
            System.out.println("Error: Foreas not found: " + kodikosForea);
            return false;
        }
        
        System.out.println("\n=== BUDGET DATA ===");
        System.out.println("FOREA: " + onomaForea);
        System.out.println("===================");
        
        if (taktikosSynolo != null) {
            System.out.println("Regular Budget: " + taktikosSynolo + " EUR");
        } else {
            System.out.println("Regular Budget: -");
        }
        
        if (dimosiesSynolo != null && dimosiesSynolo > 0) {
            System.out.println("Public Investments: " + dimosiesSynolo + " EUR");
        } else {
            System.out.println("Public Investments: -");
        }
        
        if (taktikosSynolo != null && dimosiesSynolo != null && dimosiesSynolo > 0) {
            double synolikoSynolo = taktikosSynolo + dimosiesSynolo;
            System.out.println("TOTAL: " + synolikoSynolo + " EUR");
        }
        
        System.out.println("===================");
        return true;
    }
    
    public Double findTaktikosSynolo(String kodikosForea) {
        for (TaktikosProupologismos taktikos : taktikosProupologismosTable) {
            if (taktikos.kodikosForea.equals(kodikosForea)) {
                return taktikos.synolo;
            }
        }
        return null;
    }
    
    public Double findDimosiesSynolo(String kodikosForea) {
        for (DimosiesEpendiseis dimosies : dimosiesEpendiseisTable) {
            if (dimosies.kodikosForea.equals(kodikosForea)) {
                return dimosies.synolo;
            }
        }
        return null;
    }
    
    public String findOnomaForea(String kodikosForea) {
        for (TaktikosProupologismos taktikos : taktikosProupologismosTable) {
            if (taktikos.kodikosForea.equals(kodikosForea)) {
                return taktikos.onomaForea;
            }
        }
        
        for (DimosiesEpendiseis dimosies : dimosiesEpendiseisTable) {
            if (dimosies.kodikosForea.equals(kodikosForea)) {
                return dimosies.onomaForea;
            }
        }
        
        return null;
    }
    
    public boolean isValidKodikos(String kodikosForea) {
        return findOnomaForea(kodikosForea) != null;
    }
}