import java.util.HashMap;

public class Markets {
    private final Graphic graphic;
    private Inventory market;
    private boolean isShopping;
    protected Colors colors;

    public Markets() {
        graphic = new Graphic();
        market = new Inventory();
        colors = new Colors();
    }

    public void storeConsole(LegendsPlayer p) {
        System.out.println(colors.addColor("cyan", "You arrive at a market. Would you like to enter?(y/n)"));
        isShopping = ScannerParser.parseBoolean();
        while(isShopping) {
                enterStore(p);
        }
        System.out.println(colors.addColor("cyan","You left the market.\n"));

    }

    public void initMarket(){
        FileParser fp = new FileParser();
        market.setArmors(fp.parseArmor());
        market.setPotions(fp.parsePotions());
        market.setSpells(fp.parseSpells());
        market.setWeapons(fp.parseWeapons());
    }

    public void enterStore(LegendsPlayer p){
        initMarket();
//        System.out.println("Welcome to the market!");
        graphic.printMarket();
        System.out.println(colors.addColor("green", "Please choose an hero from your list to enter the store:"));
        p.printPlayerHeroes();
        int input = ScannerParser.parseInt();
        Hero chosenHero = p.getHeroes().get(input - 1);
        System.out.println(colors.addColor("green","Hero " + chosenHero.getName() + " entered the store --"));
        System.out.println(colors.addColor("blue", "Would you like to buy or sell an item?"));
        System.out.println(" 0: Buy\n 1: Sell\n 2: Exit market");
        input = ScannerParser.parseInt();
        chooseBuySell(input, chosenHero, p);
    }


    public void chooseBuySell(int input, Hero h, LegendsPlayer p) {
        while (input != 0 &&input != 1 && input != 2) {
            System.out.println(colors.addColor("red","Invalid option! Please try again!"));
            input = ScannerParser.parseInt();
        }
        switch (input) {
            case 0:
                heroBuy(h);
                enterStore(p);
                break;
            case 1:
                heroSell(h);
                enterStore(p);
                break;
            case 2:
                isShopping = false;
//                enterStore(p);
                break;
        }
    }



    //TODO: need to be able to exit after choosing an action
    public void heroBuy(Hero h) {
        System.out.println(colors.addColor("green", "What would you like to purchase?"));
        System.out.println(colors.addColor("green"," 0: Weapon\n 1: Armor\n 2: Potion\n 3: Spell"));
        int input = ScannerParser.parseInt();
        Printer printer = new Printer();
        printer.printShop(market, input);
        System.out.println(colors.addColor("cyan", "Which item would you like to purchase?"));
        int id = ScannerParser.parseInt()-1;
        switch(input){
            case 0:
                while(h.getMoney()<market.getWeapons().get(id).getPrice()){
                    System.out.println(colors.addColor("red","Your hero doesn't have enough money to buy this weapon. Please try again"));
                    id = ScannerParser.parseInt()-1;
                }
                h.buy(market.getWeapons().get(id));
                System.out.println(colors.addColor("purple", "Item is purchased successfully!\n"));
                break;
            case 1:
                while(h.getMoney()<market.getArmors().get(id).getPrice()){
                    System.out.println(colors.addColor("red","Your hero doesn't have enough money to buy this armor. Please try again"));
                    id = ScannerParser.parseInt()-1;
                }
                h.buy(market.getArmors().get(id));
                System.out.println(colors.addColor("purple","Item is purchased successfully!\n"));
                break;
            case 2:
                HashMap<Potion, Integer> potions = market.getPotions();
                Potion[] keys = (Potion[])potions.keySet().toArray();
                while(h.getMoney()< keys[id-1].getPrice()){
                    System.out.println(colors.addColor("red", "Your hero doesn't have enough money to buy this potion. Please try again"));
                    id = ScannerParser.parseInt()-1;
                }
                h.buy(keys[id-1]);
                System.out.println(colors.addColor("purple", "Item is purchased successfully!\n"));
                break;
            case 3:
                while(h.getMoney()<market.getSpells().get(id).getPrice()){
                    System.out.println(colors.addColor("red", "Your hero doesn't have enough money to buy this spell. Please try again"));
                    id = ScannerParser.parseInt()-1;
                }
                h.buy(market.getSpells().get(id));
                System.out.println(colors.addColor("purple", "Item is purchased successfully!\n"));
                break;
        }

    }


    // TODO: need to finish this method!!

    public void heroSell(Hero h) {
        System.out.println("What would you like to sell?");
        System.out.println("0: Weapon\n1: Armor\n2: Potion\n3: Spell");
        int input = ScannerParser.parseInt();
        Printer printer = new Printer();
        printer.printShop(market, input);
        System.out.println("Which item would you like to sell?");
        int id = ScannerParser.parseInt()-1;
        switch(input){
            case 0:
                //sell a weapon
                while(h.getMoney()<market.getWeapons().get(id).getPrice()){
                    System.out.println(colors.addColor("red","Your hero doesn't have enough money to buy this weapon. Please try again"));
                    id = ScannerParser.parseInt()-1;
                }
                h.buy(market.getWeapons().get(id));
                System.out.println(colors.addColor("purple","Item is purchased successfully!\n"));
                break;
            case 1:
                //sell an armor
                while(h.getMoney()<market.getArmors().get(id).getPrice()){
                    System.out.println(colors.addColor("red","Your hero doesn't have enough money to buy this armor. Please try again"));
                    id = ScannerParser.parseInt()-1;
                }
                h.buy(market.getArmors().get(id));
                System.out.println(colors.addColor("purple","Item is purchased successfully!\n"));
                break;
            case 2:
                //sell a potion
                HashMap<Potion, Integer> potions = market.getPotions();
                Potion[] keys = (Potion[])potions.keySet().toArray();
                while(h.getMoney()< keys[id-1].getPrice()){
                    System.out.println(colors.addColor("red","Your hero doesn't have enough money to buy this potion. Please try again"));
                    id = ScannerParser.parseInt()-1;
                }
                h.buy(keys[id-1]);
                System.out.println(colors.addColor("purple", "Item is purchased successfully!\n"));
                break;
            case 3:
                //sell a spell
                while(h.getMoney()<market.getSpells().get(id).getPrice()){
                    System.out.println(colors.addColor("red","Your hero doesn't have enough money to buy this spell. Please try again"));
                    id = ScannerParser.parseInt()-1;
                }
                h.buy(market.getSpells().get(id));
                System.out.println(colors.addColor("purple","Item is purchased successfully!\n"));
                break;
        }

    }


}
