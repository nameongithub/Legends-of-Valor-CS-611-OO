package legends;

import legends.characters.heroes.Hero;
import legends.characters.heroes.Warrior;
import legends.characters.monsters.Monster;
import legends.characters.monsters.Spirit;
import legends.games.LegendsOfValor;
import legends.grids.Grid;
import legends.grids.HeroNexus;
import legends.grids.cells.*;
import legends.characters.Character;
import legends.utilities.Colors;
import java.util.ArrayList;
import java.util.List;

/**
 * Grids of Legends of Valor
 * a.k.a. Map
 */
public class LOVGrid extends Grid {

    List<StringBuilder> printableMap;

    protected static int size = 8;
    private LegendsOfValor legendsOfValor;

    public static void main(String[] args) {
        LegendsOfValor legendsOfValor=new LegendsOfValor();
        LOVGrid lovGrid = new LOVGrid(8, 8, legendsOfValor);
    }


    public LOVGrid(int numRows, int numCols, LegendsOfValor legendsOfValor) {
        super(numRows, numCols);
        this.legendsOfValor=legendsOfValor;
        createMap();
    }

    private static String getOuterCellStr(String c){
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < 2; i++) {
            str.append(c).append(" - ");
        }
        str.append(c).append("   ");
        return str.toString();
    }

    private void createOutterCell(CellType[][] map, List<StringBuilder> printableMap, int row, int col) {
        int cellRow = row/3;
        switch (map[cellRow][col]){
            case NEXUS:
                grid[cellRow][col] = new NexusCell(cellRow, col);
                printableMap.get(row).append(getOuterCellStr(grid[cellRow][col].getIcon()));
                break;
            case PLAIN:
                grid[cellRow][col] = new PlainCell(cellRow, col);
                printableMap.get(row).append(getOuterCellStr(grid[cellRow][col].getIcon()));
                break;
            case KOULOU:
                grid[cellRow][col] = new KoulouCell(cellRow, col);
                printableMap.get(row).append(getOuterCellStr(grid[cellRow][col].getIcon()));
                break;
            case CAVE:
                grid[cellRow][col] = new CaveCell(cellRow, col);
                printableMap.get(row).append(getOuterCellStr(grid[cellRow][col].getIcon()));
                break;
            case BUSH:
                grid[cellRow][col] = new BushCell(cellRow, col);
                printableMap.get(row).append(getOuterCellStr(grid[cellRow][col].getIcon()));
                break;
            case INACCESSIBLE:
                grid[cellRow][col] = new InaccessibleCell(cellRow, col);
                printableMap.get(row).append(getOuterCellStr(grid[cellRow][col].getIcon()));
                break;
        }
    }

    /**
     * generate a CellType at random
     * @return a CellType
     */
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

    private static String getInnerCellStr(String component){
        return "| " + component + " |   ";
    }

    /**
     *
     * @param row row of cells
     * @param col
     * @return
     */
    private String getCellComponent(int row, int col){

        StringBuilder result=new StringBuilder();
        boolean hasHero=false;
        for (int i = 0; i < legendsOfValor.getHeroes().size(); i++){
            if (row == legendsOfValor.getHeroes().get(i).getRow() && col == legendsOfValor.getHeroes().get(i).getCol()){
                 result.append("H" + (i + 1));
                 hasHero=true;
                 break;
            }
        }

        if(!hasHero) result.append("  ");

        boolean hasMonster=false;
        for (int j = 0; j < legendsOfValor.getMonsters().size(); j++){
            if (row == legendsOfValor.getMonsters().get(j).getRow() && col == legendsOfValor.getMonsters().get(j).getCol()){
                result.append (" M" + (j + 1)+"");
                hasMonster=true;
                break;
            }
        }

        if(!hasMonster) result.append("   ");

        return result.toString();
    }




//        if (row ==  && col == 1){
//            return "H1   ";
//        }else if (row == 1 && col == 3){
//            return "H2   ";
//        }else if (row == 3 && col == 1){
//            return "   M1";
//        }else if (row == 1 && col == 4){
//            return "   M2";
//        }else if (row == 3 && col == 6){
//            return "H3 M3";
//        }else{
//            return "     ";
//        }
//    }




    private void createInnerCell(CellType[][] map, List<StringBuilder> printableMap, int row, int col) {
        String component = getCellComponent(row/3, col);
        if (map[row/3][col] == CellType.INACCESSIBLE)
            component = "X X X";
        printableMap.get(row).append(getInnerCellStr(component));
    }

    private void createMap(){
        List<StringBuilder> printableMap = new ArrayList<StringBuilder>();
        CellType [][]map = {
                {CellType.NEXUS, CellType.NEXUS, CellType.INACCESSIBLE, CellType.NEXUS, CellType.NEXUS, CellType.INACCESSIBLE, CellType.NEXUS, CellType.NEXUS},
                {calculateCellType(), calculateCellType(), CellType.INACCESSIBLE, calculateCellType(), calculateCellType(), CellType.INACCESSIBLE, calculateCellType(), calculateCellType()},
                {calculateCellType(), calculateCellType(), CellType.INACCESSIBLE, calculateCellType(), calculateCellType(), CellType.INACCESSIBLE, calculateCellType(), calculateCellType()},
                {calculateCellType(), calculateCellType(), CellType.INACCESSIBLE, calculateCellType(), calculateCellType(), CellType.INACCESSIBLE, calculateCellType(), calculateCellType()},
                {calculateCellType(), calculateCellType(), CellType.INACCESSIBLE, calculateCellType(), calculateCellType(), CellType.INACCESSIBLE, calculateCellType(), calculateCellType()},
                {calculateCellType(), calculateCellType(), CellType.INACCESSIBLE, calculateCellType(), calculateCellType(), CellType.INACCESSIBLE, calculateCellType(), calculateCellType()},
                {calculateCellType(), calculateCellType(), CellType.INACCESSIBLE, calculateCellType(), calculateCellType(), CellType.INACCESSIBLE, calculateCellType(), calculateCellType()},
                {CellType.NEXUS, CellType.NEXUS, CellType.INACCESSIBLE, CellType.NEXUS, CellType.NEXUS, CellType.INACCESSIBLE, CellType.NEXUS, CellType.NEXUS},
        };
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

    public void display(){
        for (int i = 0; i < size * 3; i++) {
            System.out.print(printableMap.get(i));
        }
    }

    public void land(int row, int col, Hero h, Cell cell, String move){
//        printGrid(p);
        String icon = grid[row][col].getIcon();
        switch(icon){
            case "I":
                InaccessibleCell i = (InaccessibleCell) grid[row][col];
                i.land(move, h);
                h.makeMove(this);
                break;

            case "N":
                NexusCell n = (NexusCell) grid[row][col];
                n.land(h);
                h.makeMove(this);
                break;

            case "P":
//                CommonSpace c = (CommonSpace) cell;
//                c.land(p);
//                p.makeMove(this);
                //todo
//                CommonSpace c = (CommonSpace)grid[row][col];
//                c.land(p);
//                p.makeMove(this);
//                break;

        }
    }
}
