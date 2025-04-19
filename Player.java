import java.util.HashMap;
import java.util.Random;

public class Player {

    static Random random = new Random();

    final String username;
    int level = 0;
    CharacterClass characterClass;
    int hp = 100;
    int gold = 0;
    byte damageBoost = 0;
    byte nonWarriorBoost = 0;
    HashMap<String, Integer> inventory;
    Zone currentZone;

    public Player(String username, CharacterClass characterClass, HashMap<String, Integer> inventory) {
        this.username = username;
        this.characterClass = characterClass;
        this.inventory = inventory;
    }

    public void heal() {
        if (inventory.get("Health Potion") > 0) {
            //deletes one potion if found in inventory
            inventory.put("Health Potion", inventory.get("Health Potion") - 1);

            //add random health
            int healthGenerated = random.nextInt(5, 12) + 1;
            hp += healthGenerated;
            System.out.println(String.format("Successfully healed by %d health!", healthGenerated));
        } else {
            System.out.println("You can't heal right now! Collect another Health Potion to heal.");
        }
    }

    public void status() {
        switch (characterClass) {
            case warrior -> System.out.println(String.format("Health : %d | Gold : %d | Level : %d | Damage Boost : %d", hp, gold, level, damageBoost));
            case doctor -> {
                int potionCount = inventory.getOrDefault("Health Potion", 0);
                System.out.println(String.format("Health : %d | Gold : %d | Level : %d | Health potions : %d", hp, gold, level, potionCount));
            }
        }
    }
}
