import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class GameEngine {

    static Scanner scanner = new Scanner(System.in);
    final static String curseWordsFile = "src/CurseWords.txt";

    //userCreationCriteria
    static String username;
    static CharacterClass characterClass;
    static Player player = null;

    static Random random = new Random();

    public static void run() {
        System.out.printf("Welcome to my first self-written Game Engine in Java in %s!\n"
                , Runtime.version().feature());
        Events.loadEnemies();
        createUser();
        mainLoop();
    }

    public static void createUser() {
        askForUsername();
        askForCharacterClass();

        player = new Player(username, characterClass, new HashMap<>());
        assignClassBenefits();
    }

    //validate Username
    private static void askForUsername() {
        System.out.println("What should your username be?");
        boolean isClean = false;

        while (!isClean) {
            username = scanner.nextLine();
            isClean = compareToCurse(username, curseWordsFile);

            if (!isClean || username == null) {
                System.out.println("That username isn't allowed. Try again.");
            }
        }

        System.out.println("Username accepted!\n");
    }

    //validate Character class
    private static void askForCharacterClass() {
        System.out.println("Now you need to choose a CharacterClass ");
        boolean isActualClass = false;

        while (!isActualClass) {

            //print each individual CharacterClass
            CharacterClass[] classes = CharacterClass.values();
            for (int i = 0; i < classes.length; i++) {
                System.out.print(classes[i]);

                //if it's not the last element, print a comma and space after it
                if (i < classes.length - 1) {
                    System.out.print(", ");
                }
            }
            System.out.println();

            System.out.println("\nChoose one of the given Classes: ");
            String input = scanner.next();

            for (CharacterClass cl : CharacterClass.values()) {
                if (input.equalsIgnoreCase(cl.toString())) {
                    characterClass = CharacterClass.valueOf(input.toLowerCase());
                    isActualClass = true;
                }
            }

            if (!isActualClass || characterClass == null) {
                System.out.println("That isn't an actual class. Try again.");
            }
        }
        System.out.println("Class accepted!\n");
    }

    public static void mainLoop() {
        //options
        String[] options = {"explore", "heal", "status", "quit"};

        //fight if explore
        while (player.hp > 0) {
            System.out.println("What action do you want to do?");
            System.out.print("| ");
            for (String str : options) System.out.print(str + " | ");
            System.out.println();
            String input = scanner.next();
            System.out.println();

            switch (input) {
                case "explore" -> explore();
                case "heal" -> player.heal();
                case "status" -> player.status();
                case "quit" -> System.exit(0);
                default -> System.out.println("Please enter a valid option!");
            }
        }
    }

    public static String getRandomEvent(HashMap<String, Integer> events) {

        int total = events.values().stream().mapToInt(Integer::intValue).sum();

        if (total != 100) {
            System.out.println("Percentage is not equal to 100! Current Percentage: " + total);
            System.exit(1);
        }

        int randomNumber = random.nextInt(100) + 1;

        //used to keep track of the runningTotal range as we iterate
        int runningTotal = 0;

        //iterate over each event and its corresponding percentage
        for (Map.Entry<String, Integer> entry : events.entrySet()) {
            runningTotal += entry.getValue(); //add the current event's weight to the cumulative sum

            //if the random number falls within the current runningTotal range, return this event
            if (randomNumber <= runningTotal) {
                return entry.getKey(); //return the event (key)
            }
        }

        return null;
    }


    public static void explore() {
        //random event selection
        HashMap<String, Integer> events = new HashMap<>(); // Key = Event | Value = percentage as a whole number to 100
        events.put("Encounter", 70);
        events.put("Loot", 29);
        events.put("NPC", 1);

        String selectedEvent = getRandomEvent(events);

        switch (selectedEvent) {
            case "Encounter" -> Events.encounter();
            case "Loot" -> Events.loot();
            case "NPC" -> Events.npcEncounter();
        }
    }

    public static void assignClassBenefits() {
        switch (characterClass) {
            case warrior -> player.damageBoost += 10;
            case doctor -> {
                player.hp += 50;
                player.inventory.put("Health Potion", 2);
            }
        }
    }

    @SuppressWarnings("All")
    public static boolean compareToCurse(String word, String path) {
        try (FileReader fr = new FileReader(path);
             BufferedReader br = new BufferedReader(fr)) {

            String line;
            while ((line = br.readLine()) != null) {
                if (word.equalsIgnoreCase(line)) {
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

}
