package legends;

import legends.grids.Grid;
import legends.grids.cells.*;

import java.util.ArrayList;
import java.util.List;

enum CellType {
    NEXUS,
    PLAIN,
    KOULOU,
    CAVE,
    BUSH,
    INACCESSIBLE
}

public class LOVMap extends Grid {
    protected static int size = 8;

    public LOVMap(int numRows, int numCols) {
        super(numRows, numCols);
        createMap();
    }

    private void createOutterCell(CellType[][] map, List<StringBuilder> printableMap, int row, int col) {
        switch (map[row/3][col]){
            case NEXUS:
                grids[row][col] = new NexusCell();
                printableMap.get(row).append(grids[row][col].getBorder());
                break;
            case PLAIN:
                grids[row][col] = new PlainCell();
                printableMap.get(row).append(grids[row][col].getBorder());
                break;
            case KOULOU:
                grids[row][col] = new KoulouCell();
                printableMap.get(row).append(grids[row][col].getBorder());
                break;
            case CAVE:
                grids[row][col] = new CaveCell();
                printableMap.get(row).append(grids[row][col].getBorder());
                break;
            case BUSH:
                grids[row][col] = new BushCell();
                printableMap.get(row).append(grids[row][col].getBorder());
                break;
            case INACCESSIBLE:
                grids[row][col] = new InaccessbileCell();
                printableMap.get(row).append(grids[row][col].getBorder());
                break;
        }
    }

    public CellType calculateCellType(){
        double index = Math.random();
        if (index <= 0.2){
            return CellType.CAVE;
        }
        else if (index <= 0.4){
            return CellType.BUSH;
        }
        else if (index <= 0.6){
            return CellType.KOULOU;
        }
        else{
            return CellType.PLAIN;
        }
    }
    public CellType[][] createTypes(){
        CellType [][]map = {
                {CellType.NEXUS, CellType.NEXUS, CellType.INACCESSIBLE, CellType.NEXUS, CellType.NEXUS, CellType.INACCESSIBLE, CellType.NEXUS, CellType.NEXUS},
                {calculateCellType(), calculateCellType(), CellType.INACCESSIBLE, calculateCellType(), calculateCellType(), CellType.INACCESSIBLE, CellType.INACCESSIBLE, CellType.INACCESSIBLE},
                {calculateCellType(), calculateCellType(), CellType.INACCESSIBLE, calculateCellType(), calculateCellType(), CellType.INACCESSIBLE, CellType.INACCESSIBLE, CellType.INACCESSIBLE},
                {calculateCellType(), calculateCellType(), CellType.INACCESSIBLE, calculateCellType(), calculateCellType(), CellType.INACCESSIBLE, CellType.INACCESSIBLE, CellType.INACCESSIBLE},
                {calculateCellType(), calculateCellType(), CellType.INACCESSIBLE, calculateCellType(), calculateCellType(), CellType.INACCESSIBLE, CellType.INACCESSIBLE, CellType.INACCESSIBLE},
                {calculateCellType(), calculateCellType(), CellType.INACCESSIBLE, calculateCellType(), calculateCellType(), CellType.INACCESSIBLE, CellType.INACCESSIBLE, CellType.INACCESSIBLE},
                {calculateCellType(), calculateCellType(), CellType.INACCESSIBLE, calculateCellType(), calculateCellType(), CellType.INACCESSIBLE, CellType.INACCESSIBLE, CellType.INACCESSIBLE},
                {CellType.NEXUS, CellType.NEXUS, CellType.INACCESSIBLE, CellType.NEXUS, CellType.NEXUS, CellType.INACCESSIBLE, CellType.NEXUS, CellType.NEXUS},
        };
        return map;
    }

    private static String getInnerCellStr(String component){
        return "| " + component + " |   ";
    }

    private static String getCellComponent(int row, int col){
        if (row == 7 && col == 1){
            return "H1   ";
        }else if (row == 1 && col == 3){
            return "H2   ";
        }else if (row == 3 && col == 1){
            return "   M1";
        }else if (row == 1 && col == 4){
            return "   M2";
        }else if (row == 3 && col == 6){
            return "H3 M3";
        }else{
            return "     ";
        }
    }

    private static void createInnerCell(CellType[][] map, List<StringBuilder> printableMap, int row, int col) {
        String component = getCellComponent(row/3, col);
        if (map[row/3][col] == CellType.INACCESSIBLE)
            component = "X X X";
        printableMap.get(row).append(getInnerCellStr(component));
    }

    public void createMap(){
        List<StringBuilder> printableMap = new ArrayList<StringBuilder>();
        CellType[][]map = createTypes();
        for (int row = 0; row < size * 3; row++) {
            printableMap.add(new StringBuilder());
            if ((row / 3) % 2 == 0){
                for (int col = 0; col < size; col++) {
                    if (row % 2 == 0){
                        createOutterCell(map, printableMap, row, col);
                    }else{
                        createInnerCell(map, printableMap, row, col);
                    }

                    if (col == size - 1)
                        printableMap.get(row).append("\n");
                }
            }else{
                for (int col = 0; col < size; col++) {
                    if (row % 2 == 1){
                        createOutterCell(map, printableMap, row, col);
                    }else{
                        createInnerCell(map, printableMap, row, col);
                    }

                    if (col == size - 1)
                        printableMap.get(row).append("\n");
                }
            }

            if (row % 3 == 2)
                printableMap.get(row).append("\n");
        }

        for (int i = 0; i < size * 3; i++) {
            System.out.print(printableMap.get(i));
        }
    }
}