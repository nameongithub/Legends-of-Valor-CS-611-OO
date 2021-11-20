package legends.characters.heroes;

import legends.LovMap;
import legends.characters.Character;
import legends.gameplay.Inventory;
import legends.gameplay.Markets;
import legends.games.LegendsOfValor;
import legends.grids.cells.Cell;
import legends.grids.lanes.Lane;
import legends.items.Armor;
import legends.items.Item;
import legends.items.Potion;
import legends.items.Weapon;
import legends.characters.monsters.Monster;
import legends.items.spells.Spell;
import legends.utilities.Colors;
import legends.utilities.Graphic;
import legends.utilities.Printer;
import legends.utilities.ScannerParser;

import java.util.*;

public abstract class Hero extends Character {
    private int mana;
    private int strength;
    private int agility;
    private int dexterity;
    private int money;
    private int experience;
    private Inventory armedInventory;
    private Inventory inventory;
    private Weapon currentWeapon;
    private Armor currentArmor;

    protected Graphic graphic;
    protected Colors colors;
    private int row;
    private int col;
    private Lane currLane;
    private Lane initLane;


    private static final Map<String, Integer[]> MOVEMENT_DIRECTIONS =new HashMap<String, Integer[]>(){{
        put("w",new Integer[]{-1,0});
        put("a", new Integer[]{0, -1});
        put("s", new Integer[]{1, 0});
        put("d",new Integer[]{0,1});
    }};

    public Hero(String name, int level, int HP, int mana, int strength, int agility, int dexterity, int money, int experience, Lane initLane) {
        super(name, level, HP);
        this.mana = mana;
        this.strength = strength;
        this.agility = agility;
        this.dexterity = dexterity;
        this.money = money;
        this.experience = experience;
        this.initLane = initLane;
        armedInventory = new Inventory();
        inventory = new Inventory();
        currLane = initLane;
    }

    public abstract void levelUp();


    // hero chooses to attack, cast a spell, move, teleport, back, or quit game
    //return the play boolean which indicates whether player wants to quit the game
    public boolean takeAction(LovMap grid, LegendsOfValor lovgame) {
        boolean play = true;
        if (row == 7) {
            Markets market = new Markets();
            market.storeConsole(this);
            System.out.println("Please choose another action for your hero.");
        }
        System.out.println(" 1: Attack\n 2: Cast spell\n 3: Change Weapon/Armor\n 4: Use a potion\n " +
                "5: Move\n 6: Teleport\n 7: Back\n 8: Quit game\n");
        int move = ScannerParser.parseInt();
        while (move < 1 || move > 8) {
            System.out.println("Please input a number within the given range:");
            move = ScannerParser.parseInt();
        }
        Printer printer = new Printer();
        switch (move) {
            case 1: //attack
                if (withinRange(grid)) {
                    attack(getNeighborMonster(grid, lovgame), null);
                } else {
                    System.out.println("No monster is within your attacking range. Please try another move!\n");
                    takeAction(grid, lovgame);
                }
                break;

            case 2: //cast spell
                if (withinRange(grid)) {
                    ArrayList<Spell> spells = inventory.getSpells();
                    if (spells.size() != 0) {
                        System.out.println("Please choose a spell to cast (enter ID):");
                        printer.printSpells(spells);
                        int chosenSpell = ScannerParser.parseInt() - 1;
                        while (chosenSpell > inventory.getSpells().size()) {
                            System.out.println("Please input a number within the given range:");
                            chosenSpell = ScannerParser.parseInt() - 1;
                        }
                        attack(getNeighborMonster(grid, lovgame), inventory.getSpells().get(chosenSpell));
                    } else {
                        System.out.println("Your hero does not have any spell in their inventory! Choose another move!\n");
                        takeAction(grid, lovgame);
                    }
                } else {
                    System.out.println("No monster is within your attacking range. Please try another move!\n");
                    takeAction(grid, lovgame);
                }
                break;

            case 3: //change weapon/armor
                System.out.println("What would you like to change?");
                System.out.println(" 1: Armor\n 2: Weapon");
                int type = ScannerParser.parseInt();
                while (type != 1 && type != 2) {
                    System.out.println("Please input a number within the given range:");
                    type = ScannerParser.parseInt();
                }
                switch (type) {
                    case 1:
                        System.out.println("Your current armor is:" + currentArmor.getName());
                        unequip(currentArmor);
                        System.out.println("Current armor is taken off.");
                        System.out.println("Which armor would you like to wear now?");
                        printer.printArmors(inventory.getArmors());
                        int newarmor = ScannerParser.parseInt() - 1;
                        while (newarmor > inventory.getArmors().size()) {
                            System.out.println("Please input a number within the given range:");
                            newarmor = ScannerParser.parseInt() - 1;
                        }
//                        h.equip(h.getInventory().getArmors().get(newarmor));
                        changeArmor(currentArmor, inventory.getArmors().get(newarmor));
                        System.out.println("Armor " + currentArmor.getName() + " is equipped now");
                        break;

                    case 2:
                        System.out.println("Your current weapon is:" + currentWeapon.getName());
                        unequip(currentWeapon);
                        System.out.println("Current weapon is unarmed now.");
                        System.out.println("Which weapon would you like to arm now?");
                        printer.printWeapons(inventory.getWeapons());
                        int newWeapon = ScannerParser.parseInt() - 1;
                        while (newWeapon > inventory.getWeapons().size()) {
                            System.out.println("Please input a number within the given range:");
                            newWeapon = ScannerParser.parseInt() - 1;
                        }
//                      h.equip(h.getInventory().getWeapons().get(newWeapon));
                        changeWeapon(currentWeapon, inventory.getWeapons().get(newWeapon));
                        System.out.println("Weapon " + currentWeapon.getName() + " is equipped now");
                        break;
                }
                break;

            case 4: // use potion
                HashMap<Potion, Integer> potions = inventory.getPotions();
                if (potions.size() != 0) {
                    System.out.println("Please choose a potion to use (enter ID):");

                    Potion[] keys = (Potion[]) potions.keySet().toArray();
                    printer.printPotions(potions);
                    int chosenPotion = ScannerParser.parseInt() - 1;
                    while (chosenPotion > inventory.getPotions().size()) {
                        System.out.println("Please input a number within the given range:");
                        chosenPotion = ScannerParser.parseInt() - 1;
                    }
                    use(keys[chosenPotion]);
                } else {
                    System.out.println("You hero does not have any potion in their inventory! Choose another move!\n");
                    takeAction(grid, lovgame);
                }
                break;

            case 5: //make move
                //makeMove(grid);
                makeMoveNewVersion(grid);
                break;

            case 6: //teleport
                System.out.println("Rules of teleporting:\n 1. You shall not land on a row that surpass any monster\n" +
                        " 2. You shall not land on the same cell as another hero\n" +
                        " 3. You must teleport to a different lane than your current lane\n" +
                        " 4. You shall not go further than the max explored row in this lane");
                System.out.println("Please enter the name of lane you wish to teleport to (Top/ Mid/ Bot):");
                String input = ScannerParser.parseString();
                while (!input.equals("Top") && !input.equals("Mid") && !input.equals("Bot") || currLane.getName().equals(input)) {
                    if(currLane.getName().equals(input)){
                        System.out.println("You must teleport to a different lane!");
                    }
                    input = ScannerParser.tryString();
                }
                setCurrLane(lovgame.getLane(input));

                System.out.println("Which row would you like to land on?(Between 1~8)");
                int currrow = ScannerParser.parseInt();
                while (currrow > 8 || currrow < 1 || currLane.getMaxMonsterRow() > currrow - 1 || currLane.getMaxExplored()>currrow-1) {
                    if(currLane.getMaxMonsterRow() > currrow - 1){
                        System.out.println("You shall not bypass any monster!");
                    }
                    if(currLane.getMaxExplored()>currrow-1){
                        System.out.println("You shall not exceed the max explored row of this lane!");
                    }
                    System.out.println("Please input a number within the given range:");
                    currrow = ScannerParser.parseInt();
                }
                row = currrow - 1;
                System.out.println("Would you like to land on left or right column of this lane?\n 1. Left\n 2. Right");
                int leftorright = ScannerParser.parseInt();
                    //check if lands on a cell that has a already hero
                while ((leftorright != 1 && leftorright!=2) || grid.getCells()[row][currLane.getLeftCol()+(leftorright-1)].getHeroCount() > 0) {
                    if (grid.getCells()[row][currLane.getLeftCol()+(leftorright-1)].getHeroCount() > 0) {
                        System.out.println("You shall not land on the same cell with another hero!");
                    }
                    System.out.println("Please input a number within the given range:");
                    leftorright = ScannerParser.parseInt();
                }
                col = currLane.getLeftCol()+(leftorright-1);
                break;

            case 7: //back
                if (initLane.getName().equals("Top")) {
                    setCurrLane(lovgame.getLane("Top"));
                } else if (initLane.getName().equals("Mid")) {
                    setCurrLane(lovgame.getLane("Mid"));
                } else {
                    setCurrLane(lovgame.getLane("Bot"));
                }
                break;

            case 8: //quit
                System.out.println("Thanks for playing! Exiting program...");
                play = false;
                break;
        }
//        System.out.println(row+" "+col);
        return play;

    }

    public void respawn(LovMap grid) {
        if (getHP() <= 0) {
            grid.getCells()[row][col].decreaseHeroCount();

        }
    }


    /**
     *
     */
    public void makeMoveNewVersion(LovMap lovMap){
        Scanner scanner=new Scanner(System.in);
        while(true){

            lovMap.display();
            System.out.println("Please choose a move:");
            System.out.println("W/w: move up\nA/a: move left\nS/s: move down\nD/d: move right\n");

            String inputString="";
            while (true) {
                boolean valid=false;
                if (scanner.hasNext()) {
                    inputString = scanner.next().toLowerCase();
                    if (MOVEMENT_DIRECTIONS.containsKey(inputString)){
                        valid=true;
                    }
                }
                if (valid) break;
            }

            int destinationRow=getRow()+MOVEMENT_DIRECTIONS.get(inputString)[0];
            int destinationColumn=getCol()+MOVEMENT_DIRECTIONS.get(inputString)[1];
            if(lovMap.moveToCell(destinationRow,destinationColumn,this)){
            //if(!lovMap.landOnMap(destinationRow,destinationColumn,this,null,null)){
                setPosition(destinationRow,destinationColumn);
                break;
            }
        }
    }


    /**
     * player choose to move a hero a certain direction. The hero then land on the cell and prompt the corresponding
     * scenarios
     *
     * @param
     * @return
     */
    public void makeMove(LovMap grid) {
        grid.display();
        System.out.println("Please choose a move:");
        System.out.println("W/w: move up\nA/a: move left\nS/s: move down\nD/d: move right\n");
        String move = ScannerParser.parseString();
        while (move.equals("W") && move.equals("w") && move.equals("A") && move.equals("a") && move.equals("S") && move.equals("s") &&
                move.equals("D") && move.equals("d")) {
            move = ScannerParser.tryString();
        }
        while (!isValidMove(move, grid)) {
            move = ScannerParser.tryString();
        }
        switch (move) {
            case "W":
            case "w":
//                if(grid.getGrid()[row-1][col].getIcon().equals("I")){
//                    ((InaccessibleCell)grid.getGrid()[row-1][col]).land();
//                }
//                grid.getGrid()[row][col].increaseHeroCount();
//                setRow(row - 1);
                grid.getCells()[row][col].decreaseHeroCount();
                grid.landOnMap(row - 1, col, this, grid.getCells()[row][col], move);
                setRow(row - 1);
                grid.getCells()[row][col].increaseHeroCount();
                break;

            case "A":
            case "a":
//                grid.getGrid()[row][col].increaseHeroCount();
//                setCol(col - 1);
//                grid.landOnMap(row, col, this, grid.getGrid()[row][col], move);
                grid.getCells()[row][col].decreaseHeroCount();
                grid.landOnMap(row, col - 1, this, grid.getCells()[row][col], move);
                setCol(col - 1);
                grid.getCells()[row][col].increaseHeroCount();
                break;

            case "S":
            case "s":
                grid.getCells()[row][col].decreaseHeroCount();
                grid.landOnMap(row + 1, col, this, grid.getCells()[row][col], move);
                setRow(row + 1);
                grid.getCells()[row][col].increaseHeroCount();
                break;

            case "D":
            case "d":
                grid.getCells()[row][col].decreaseHeroCount();
                grid.landOnMap(row, col + 1, this, grid.getCells()[row][col], move);
                setCol(col + 1);
                grid.getCells()[row][col].increaseHeroCount();
                break;
        }
//        grid.printGrid(this);
//        System.out.println(row+" "+col);
    }


    /**
     * check whether a hero move is valid. A hero can't land on the cell that has another hero, or
     * land outside the grid, or bypass any monster
     *
     * @param move
     * @param grid
     * @return
     */
    public boolean isValidMove(String move, LovMap grid) {
        boolean isValid = true;
        Cell[][] grids = grid.getCells();

        switch (move) {
            case "W":
            case "w":
                if (row - 1 < 0) {
                    System.out.println("You shall not land outside the map!");
                    isValid = false;
                } else {
                    if (grids[row - 1][col].getHeroCount() > 0) {
                        System.out.println("You shall not land in the same cell with another hero! Please try again!");
                        isValid = false;
                    }
                }
                if (row == currLane.getMaxMonsterRow()) {
                    System.out.println("You shall not bypass an monster without killing it! Please try again!");
                    isValid = false;
                }

                break;

            case "A":
            case "a":
                if (col - 1 < 0) {
                    System.out.println("You shall not land outside the map!");
                    isValid = false;
                } else {
                    if (grids[row][col - 1].getHeroCount() > 0) {
                        System.out.println("You shall not land in the same cell with another hero! Please try again!");
                        isValid = false;
                    }
                }
                break;

            case "S":
            case "s":
                if (row + 1 > 7) {
                    System.out.println("You shall not land outside the map!");
                    isValid = false;
                } else {
                    if (grids[row + 1][col].getHeroCount() > 0) {
                        System.out.println("You shall not land in the same cell with another hero! Please try again!");
                        isValid = false;
                    }
                }
                break;

            case "D":
            case "d":
                if (col + 1 > 7) {
                    System.out.println("You shall not land outside the map!");
                    isValid = false;
                } else {
                    if (grids[row][col + 1].getHeroCount() > 0) {
                        System.out.println("You shall not land in the same cell with another hero! Please try again!");
                        isValid = false;
                    }
                }
                break;
        }
        return isValid;
    }

    public Monster getNeighborMonster(LovMap grid, LegendsOfValor legendofvalor) {
        Cell[][] grids = grid.getCells();
        if (grids[Math.max(row - 1, 0)][Math.max(col - 1, 0)].getMonsterCount() > 0) {
            for (int i = 0; i < legendofvalor.getMonsters().size(); i++) {
                if (legendofvalor.getMonsters().get(i).getRow() == Math.max(row - 1, 0) && legendofvalor.getMonsters().get(i).getCol() == Math.max(col - 1, 0)) {
                    return legendofvalor.getMonsters().get(i);
                }
            }

        } else if (grids[Math.max(row - 1, 0)][col].getMonsterCount() > 0) {
            for (int i = 0; i < legendofvalor.getMonsters().size(); i++) {
                if (legendofvalor.getMonsters().get(i).getRow() == Math.max(row - 1, 0) && legendofvalor.getMonsters().get(i).getCol() == col) {
                    return legendofvalor.getMonsters().get(i);
                }
            }
        } else if (grids[Math.max(row - 1, 0)][Math.min(col + 1, 7)].getMonsterCount() > 0) {
            for (int i = 0; i < legendofvalor.getMonsters().size(); i++) {
                if (legendofvalor.getMonsters().get(i).getRow() == Math.max(row - 1, 0) && legendofvalor.getMonsters().get(i).getCol() == Math.min(col + 1, 7)) {
                    return legendofvalor.getMonsters().get(i);
                }
            }
        } else if (grids[row][Math.max(col - 1, 0)].getMonsterCount() > 0) {
            for (int i = 0; i < legendofvalor.getMonsters().size(); i++) {
                if (legendofvalor.getMonsters().get(i).getRow() == row && legendofvalor.getMonsters().get(i).getCol() == Math.min(col - 1, 0)) {
                    return legendofvalor.getMonsters().get(i);
                }
            }
        } else if (grids[row][col].getMonsterCount() > 0) {
            for (int i = 0; i < legendofvalor.getMonsters().size(); i++) {
                if (legendofvalor.getMonsters().get(i).getRow() == row && legendofvalor.getMonsters().get(i).getCol() == col) {
                    return legendofvalor.getMonsters().get(i);
                }
            }
        } else if (grids[row][Math.min(col + 1, 7)].getMonsterCount() > 0) {
            for (int i = 0; i < legendofvalor.getMonsters().size(); i++) {
                if (legendofvalor.getMonsters().get(i).getRow() == row && legendofvalor.getMonsters().get(i).getCol() == Math.min(col + 1, 7)) {
                    return legendofvalor.getMonsters().get(i);
                }
            }
        } else if (grids[Math.min(row + 1, 7)][Math.max(col - 1, 0)].getMonsterCount() > 0) {
            for (int i = 0; i < legendofvalor.getMonsters().size(); i++) {
                if (legendofvalor.getMonsters().get(i).getRow() == Math.min(row + 1, 7) && legendofvalor.getMonsters().get(i).getCol() == Math.max(col - 1, 0)) {
                    return legendofvalor.getMonsters().get(i);
                }
            }
        } else if (grids[Math.min(row + 1, 7)][col].getMonsterCount() > 0) {
            for (int i = 0; i < legendofvalor.getMonsters().size(); i++) {
                if (legendofvalor.getMonsters().get(i).getRow() == Math.min(row + 1, 7) && legendofvalor.getMonsters().get(i).getCol() == col) {
                    return legendofvalor.getMonsters().get(i);
                }
            }
        } else {
            for (int i = 0; i < legendofvalor.getMonsters().size(); i++) {
                if (legendofvalor.getMonsters().get(i).getRow() == Math.min(row + 1, 7) && legendofvalor.getMonsters().get(i).getCol() == Math.min(col + 1, 7)) {
                    return legendofvalor.getMonsters().get(i);
                }
            }
        }
        return null;
    }

    // return boolean indicating whether there is a monster within the hero's attacking range
    public boolean withinRange(LovMap grid) {
        Cell[][] grids = grid.getCells();
        if (grids[Math.max(row - 1, 0)][Math.max(col - 1, 0)].getMonsterCount() > 0 || grids[Math.max(row - 1, 0)][col].getMonsterCount() > 0 || grids[Math.max(row - 1, 0)][Math.min(col + 1, 7)].getMonsterCount() > 0 ||
                grids[row][Math.max(col - 1, 0)].getMonsterCount() > 0 || grids[row][col].getMonsterCount() > 0 || grids[row][Math.min(col + 1, 7)].getMonsterCount() > 0 ||
                grids[Math.min(row + 1, 7)][Math.max(col - 1, 0)].getMonsterCount() > 0 || grids[Math.min(row + 1, 7)][col].getMonsterCount() > 0 || grids[Math.min(row + 1, 7)][Math.min(col + 1, 7)].getMonsterCount() > 0) {
            return true;
        } else {
            return false;
        }
    }


    public void buy(Item item) {
        inventory.addItem(item);
        money -= item.getPrice();
    }

    public void sell(Item item) {
        inventory.removeItem(item);
        money += item.getPrice();
    }


    public void equip(Item i) {
        if (i instanceof Weapon) {
            for (Weapon w : inventory.getWeapons()) {
                if (i.getName().equals(w.getName())) {
                    w.equipItem();
                    currentWeapon = w;
                    return;
                }
            }
        } else if (i instanceof Armor) {
            for (Armor a : inventory.getArmors()) {
                if (i.getName().equals(a.getName())) {
                    a.equipItem();
                    currentArmor = a;
                    return;
                }
            }

        } else {
            System.out.println("You shall not equip such item.");
        }
    }


    public void changeWeapon(Weapon orig, Weapon wear) {
        unequip(orig);
        equip(wear);
        setCurrentWeapon(wear);
    }

    public void changeArmor(Armor orig, Armor wear) {
        unequip(orig);
        equip(wear);
        setCurrentArmor(wear);
    }

    public void unequip(Item i) {
        for (Item item : getInventory().getArmors()) {
            if (item.getName().equals(i.getName())) {
                if (item instanceof Weapon) {
                    ((Weapon) item).equipItem();
                    currentWeapon = null;
                } else if (item instanceof Armor) {
                    ((Armor) item).equipItem();
                    currentArmor = null;
                }
            }
        }
    }

    public void use(Potion p) {
        int[] uses = p.calcUse();
        setHP(getHP() + p.getIncrease() * uses[0]);
        setMana(getMana() + p.getIncrease() * uses[1]);
        setStrength(getStrength() + p.getIncrease() * uses[2]);
        setDexterity(getDexterity() + p.getIncrease() * uses[3]);
        setAgility(getAgility() + p.getIncrease() * uses[4]);
    }

    public void attack(Monster m, Spell spell) {
        int dmg = 50;
        int newHP;
        if (spell == null) {
            for (Weapon w : getInventory().getWeapons()) {
                if (w.isArmed()) {
                    dmg = w.calcAttack(strength, m);
                    break;
                }
            }
            System.out.println("Hero" + getName() + " has dealt " + dmg + " damage to " + m.getName());
            if (m.getHP() <= dmg) {
                m.setHP(0);
                m.setFaint(true);
                System.out.println("Monster " + m.getName() + " fainted!");

            } else {
                newHP = m.getHP() - dmg;
                m.setHP(newHP);
            }
        } else {
            mana -= spell.getMana();
            dmg = spell.calcAttack(dexterity, m);
            if (m.getHP() <= dmg) {
                m.setHP(0);
                System.out.println("Monster " + m.getName() + " fainted!");
                m.setFaint(true);
            } else {
                newHP = m.getHP() - dmg;
                m.setHP(newHP);
            }
        }
    }


    public void takeDamage(int dmg) {
        int actualdmg = dmg;
        for (Armor a : getInventory().getArmors()) {
            if (a.isArmed()) {
                actualdmg -= a.getReduction();
                break;
            }
        }
        System.out.println("Monster" + getName() + " has dealt " + dmg + " damage to " + getName());
        if (getHP() <= actualdmg) {
            setHP(0);
            setFaint(true);
            System.out.println("Hero " + getName() + " fainted!");

        } else {
            setHP(getHP() - actualdmg);
        }
    }


    public Inventory getArmedInventory() {
        return armedInventory;
    }

    public void setArmedInventory(Inventory armedInventory) {
        this.armedInventory = armedInventory;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }


    public Weapon getCurrentWeapon() {
        return currentWeapon;
    }

    public void setCurrentWeapon(Weapon currentWeapon) {
        this.currentWeapon = currentWeapon;
    }


    public Armor getCurrentArmor() {
        return currentArmor;
    }

    public void setCurrentArmor(Armor currentArmor) {
        this.currentArmor = currentArmor;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getAgility() {
        return agility;
    }

    public void setAgility(int agility) {
        this.agility = agility;
    }

    public int getDexterity() {
        return dexterity;
    }

    public void setDexterity(int dexterity) {
        this.dexterity = dexterity;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }


    public void setInitLane(Lane initLane) {
        this.initLane = initLane;
        this.currLane = initLane;
    }

    public Lane getCurrLane() {
        return currLane;
    }

    public void setPosition(int row, int column) {
        setRow(row);
        setCol(column);
    }

    public void setCurrLane(Lane currLane) {
        this.currLane = currLane;
    }

    public Lane getInitLane() {
        return initLane;
    }
}
