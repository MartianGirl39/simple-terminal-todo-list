package com.techelevator;

import com.techelevator.lists.ListItem;
import com.techelevator.lists.ToBuy;
import com.techelevator.lists.ToDo;
import com.techelevator.menus.Menu;

import javax.print.DocFlavor;
import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*
* Goals!
*
* 1. Write some unit tests
*
* 2. Preload the the list of items with the contents of the file todo-items.txt
*
* 3. Build a multi-level menu
*
* 4. (If there is time) Build a logger class
*
* */


public class Main {

    private List<ListItem> items = new ArrayList<>();
    Logger logger = new Logger("todo-items.txt");

    public static void main(String[] args) {

        Main program = new Main();
        program.run();
    }


    private void run() {
        fetchList();
        Menu mainMenu = initMainMenu();
        mainMenu.viewMenu();
    }

    private void fetchList(){
        try(Scanner reader = new Scanner(new File("todo-items.txt"))){
            while(reader.hasNext()){
                String[] data = reader.nextLine().split("\\|");
                if(data[2].equals("todo")){
                    items.add(new ToDo(data[0], data[1].split(",")));
                }
                else if(data[2].equals("tobuy")){
                    items.add(new ToBuy(data[0], new BigDecimal(data[1])));
                }
                else {
                    System.out.println("What have you done to format the file wrong!!!");
                    throw new RuntimeException();
                }
            }
        }
        catch (FileNotFoundException e){
            System.out.println("Cannot read from logs, exiting program...");
            throw new RuntimeException();
        }
    }

    private Menu initMainMenu(){
        Menu viewMenu = new Menu("Todos View", "b");
        viewMenu.putOption("1", "view todo list", ()->{
            for(ListItem item: items){
                if(item instanceof ToDo){
                    System.out.println(item);
                }
            }
            return true;
        });
        viewMenu.putOption("2", "view shopping list", ()->{
            for(ListItem item: items){
                if(item instanceof ToBuy){
                    System.out.println(item);
                }
            }
            return true;
        });
        viewMenu.putOption("3", "view all", ()->{
            for(ListItem item: items){
                System.out.println(item);
            }
            return true;
        });

        Menu addMenu = new Menu("Add Todos", "b");
        addMenu.putOption("1", "add a todo list item", () -> {
            Scanner input = new Scanner(System.in);
            String description = "";
            String[] participants;

            System.out.print("enter a description: ");
            description = input.nextLine().strip();
            System.out.println();
            System.out.println("enter a list of names involved seperated by a comma and a space: ");
            participants = input.nextLine().strip().split(", ");

            items.add(new ToDo(description, participants));
            logger.writeMessage(String.format("%s|%s|todo", description, String.join(",", participants)));
            return true;
        });
        addMenu.putOption("2", "add a shopping list item", () -> {
            Scanner input = new Scanner(System.in);
            String description = "";
            BigDecimal price = new BigDecimal("0.00");
            boolean valid = false;

                System.out.print("enter a description: ");
                description = input.nextLine().strip();
                System.out.println();
                while(!valid) {
                System.out.print("enter the estimated cost of the item: ");
                try {
                    price = new BigDecimal(input.nextLine());
                    valid = true;
                }
                catch (NumberFormatException e){
                    System.out.print(" (Please enter a number)");
                }
            }

            items.add(new ToBuy(description, price));
            logger.writeMessage(String.format("%s|%s|tobuy", description, price));
            return true;
        });

        Menu completeMenu = new Menu("Mark Complete", "b");
        completeMenu.putOption("1", "mark complete from todos", () -> {
            return true;
        });
        completeMenu.putOption("2", "mark complete from shopping list", () -> {
            return true;
        });

        Menu mainMenu = new Menu("Main Menu", "q");
        mainMenu.putOption("1", "view todos", ()->{viewMenu.viewMenu(); return true;});
        mainMenu.putOption("2", "add todos", ()->{addMenu.viewMenu(); return true;});
        mainMenu.putOption("3", "mark complete", ()->{completeMenu.viewMenu(); return true;});
        return mainMenu;
    }

}
