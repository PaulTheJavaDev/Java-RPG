import java.util.*;

public class Events {

    static Player player = GameEngine.player;
    static Random random = new Random();

    static Map<Zone, List<Enemy>> enemyPool = new HashMap<>();

    public static void loadEnemies() {
        //I know it may look confusing, but bare with me here

        //creating Forest Enemies
        ArrayList<Enemy> forestEnemies = new ArrayList<>();
        forestEnemies.add(new Enemy("Goblin", 20, 5, 7));
        forestEnemies.add(new Enemy("Werewolf", 40, 12, 35));

        //creating Cave Enemies
        ArrayList<Enemy> caveEnemies = new ArrayList<>();
        caveEnemies.add(new Enemy("Bat", 15, 3, 5));
        caveEnemies.add(new Enemy("Troll", 25, 10, 25));

        //creating Mountain Enemies
        ArrayList<Enemy> mountainEnemies = new ArrayList<>();
        mountainEnemies.add(new Enemy("Bear", 50, 15, 40));
        mountainEnemies.add(new Enemy("Bandit", 35, 9, 20));

        //creating Town Enemies
        ArrayList<Enemy> townEnemies = new ArrayList<>();
        townEnemies.add(new Enemy("Drunk Thief", 25, 4, 10));

        //creating Ruins Enemies
        ArrayList<Enemy> ruinsEnemies = new ArrayList<>();
        ruinsEnemies.add(new Enemy("Skeleton", 30, 6, 15));
        ruinsEnemies.add(new Enemy("Shadow Mage", 45, 14, 50));

        //assigning to enemyPool map
        enemyPool.put(Zone.Forest, forestEnemies);
        enemyPool.put(Zone.Cave, caveEnemies);
        enemyPool.put(Zone.Mountain, mountainEnemies);
        enemyPool.put(Zone.Town, townEnemies);
        enemyPool.put(Zone.Ruins, ruinsEnemies);

    }

    public static void encounter() {

        //selects a random Zone
        Zone[] zones = Zone.values();
        player.currentZone = zones[random.nextInt(zones.length)];

        if (player.currentZone == Zone.Shop) Events.shopEncounter();

        System.out.println("You are now in " + player.currentZone + "!");

        //selects a random enemy from the current Zone (trust me)
        List<Enemy> enemyList = enemyPool.get(player.currentZone);
        Enemy zoneEnemy = enemyList.get(random.nextInt(enemyList.size()));

        System.out.println("You've encountered a " + zoneEnemy.name + "!");

        int damage = 0;

        /*
        If the player is a warrior, give him a preset warrior damage boost
        else the game checks with the player.nonWarriorBoost if the player has an upgraded sword
         */
        if (player.characterClass == CharacterClass.warrior) {
            damage += player.damageBoost;
        } else {
            damage += player.nonWarriorBoost;
        }

        while (zoneEnemy.hp > 0) {

            damage += random.nextInt(12);
            zoneEnemy.hp -= damage;
            System.out.println(String.format("You attack and did %d damage!", damage));

            int enemyDamage = random.nextInt(zoneEnemy.damage);
            player.hp -= enemyDamage;
            System.out.println(String.format("%s attacks and did %d damage!", zoneEnemy.name, enemyDamage));

            if (player.hp <= 0) {
                System.out.println("You lost..");
                System.exit(0);
            } else if (zoneEnemy.hp <= 0) {
                System.out.println("You've won the battle. Congrats!");
                System.out.println(String.format("Enjoy your %d Gold!", zoneEnemy.goldDrop));
                player.gold += zoneEnemy.goldDrop;
                System.out.println();
            }

        }
    }

    public static void loot() {

        String[] possibleLoot = {"Wooden Sword", "Health Potion", "gold"};

        int randomLoot = random.nextInt(possibleLoot.length + 1);

        switch (possibleLoot[randomLoot]) {
            case "Wooden Sword" -> player.nonWarriorBoost += 4;
            case "Health Potion" -> player.inventory.put("Health Potion", 1);
            case "gold" -> player.gold += random.nextInt(14);
        }

    }

    public static void npcEncounter() {

    }

    public static void shopEncounter() {

    }
}
