import java.io.*;
import java.util.Scanner;

abstract class Item {
    protected int itemId;
    protected String itemName;
    protected String location;

    public Item(int itemId, String itemName, String location) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.location = location;
    }

    public abstract void displayItem();
}

class LostItem extends Item {
    public LostItem(int itemId, String itemName, String location) {
        super(itemId, itemName, location);
    }

    public void displayItem() {
        System.out.println("LOST  | ID: " + itemId + " | Item: " + itemName + " | Location: " + location);
    }

    public String toFile() {
        return "LOST," + itemId + "," + itemName + "," + location;
    }
}

class FoundItem extends Item {
    public FoundItem(int itemId, String itemName, String location) {
        super(itemId, itemName, location);
    }

    public void displayItem() {
        System.out.println("FOUND | ID: " + itemId + " | Item: " + itemName + " | Location: " + location);
    }

    public String toFile() {
        return "FOUND," + itemId + "," + itemName + "," + location;
    }
}

class ItemFile {
    public static void saveItem(String data) {
        try (FileWriter fw = new FileWriter("items.txt", true)) {
            fw.write(data + "\n");
            System.out.println("\nRecord saved successfully!");
        } catch (IOException e) {
            System.out.println("\nFile error occurred.");
        }
    }

    public static void displayAllItems() {
        try (BufferedReader br = new BufferedReader(new FileReader("items.txt"))) {
            String line;
            System.out.println("\n--- Lost & Found Records ---");

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts.length == 4) {
                    String type = parts[0];
                    int id = Integer.parseInt(parts[1]);
                    String name = parts[2];
                    String location = parts[3];

                    Item item;

                    if (type.equals("LOST")) {
                        item = new LostItem(id, name, location);
                    } else {
                        item = new FoundItem(id, name, location);
                    }

                    item.displayItem();
                }
            }
        } catch (IOException e) {
            System.out.println("\nNo records found.");
        }
    }
}

class ReminderThread extends Thread {
    public void run() {
        try {
            while (true) {
                Thread.sleep(30000);
                System.out.println("\nReminder: Check Lost & Found records regularly!");
            }
        } catch (InterruptedException e) {
            System.out.println("\nReminder stopped.");
        }
    }
}

public class LostFoundManagementSystem {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        ReminderThread reminder = new ReminderThread();
        reminder.setDaemon(true);
        reminder.start();

        int choice;

        while (true) {
            System.out.println("\n--- Lost & Found Management System ---");
            System.out.println("1. Report Lost Item");
            System.out.println("2. Report Found Item");
            System.out.println("3. Display All Items");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            choice = sc.nextInt();
            sc.nextLine();

            try {
                if (choice == 1 || choice == 2) {
                    System.out.print("Enter Item ID: ");
                    int id = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Enter Item Name: ");
                    String name = sc.nextLine();

                    System.out.print("Enter Location: ");
                    String location = sc.nextLine();

                    if (name.isEmpty() || location.isEmpty()) {
                        throw new Exception("Item name and location cannot be empty.");
                    }

                    if (choice == 1) {
                        LostItem lost = new LostItem(id, name, location);
                        ItemFile.saveItem(lost.toFile());
                    } else {
                        FoundItem found = new FoundItem(id, name, location);
                        ItemFile.saveItem(found.toFile());
                    }
                } 
                else if (choice == 3) {
                    ItemFile.displayAllItems();
                } 
                else if (choice == 4) {
                    System.out.println("\nExiting program...");
                    break;
                } 
                else {
                    System.out.println("\nInvalid choice!");
                }
            } catch (Exception e) {
                System.out.println("\nError: " + e.getMessage());
            }
        }

        sc.close();
    }
}