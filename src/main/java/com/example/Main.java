package com.example;

import com.example.lists.ListItem;
import com.example.lists.ToBuy;
import com.example.lists.ToDo;
import com.example.menus.Menu;

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
    private Scanner input = new Scanner(System.in);

    public static void main(String[] args) {

        Main program = new Main();
        program.run();
    }


    private void run() {
        fetchList();
        Menu mainMenu = initMainMenu();
        mainMenu.viewMenu();
        input.close();
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
            boolean valid = false;
            int answer = 0;

            List<ListItem> toDos = new ArrayList<>();
            for(ListItem item : items){
                if(item instanceof ToDo){
                    toDos.add(item);
                }
            }

            while(!valid) {
                int counter = 1;
                for(ListItem item: toDos){
                    System.out.println(counter + ") " + item);
                    counter++;
                }

                System.out.println("Select a todo to mark complete, 0 to leave: ");
                try {
                    answer = Integer.parseInt(input.nextLine());
                    if(answer == 0){
                        valid = true;
                        continue;
                    }
                    toDos.get(answer-1).setCompleted(true);
                }
                catch(NumberFormatException e){
                    System.out.println("(Please enter a valid whole number) ");
                }
                catch(IndexOutOfBoundsException e){
                    System.out.println("(Please refer to list by their list number) ");
                }
            }
            return true;
        });
        completeMenu.putOption("2", "mark complete from shopping list", () -> {
            boolean valid = false;
            int answer = 0;

            List<ListItem> toBuys = new ArrayList<>();
            for(ListItem item : items){
                if(item instanceof ToBuy){
                    toBuys.add(item);
                }
            }

            while(!valid) {
                int counter = 1;
                for(ListItem item: toBuys){
                    System.out.println(counter + ") " + item);
                    counter++;
                }
                System.out.println("Select a todo to mark complete, 0 to leave: ");
                try {
                    answer = Integer.parseInt(input.nextLine());
                    if(answer == 0){
                        valid = true;
                        continue;
                    }
                    toBuys.get(answer-1).setCompleted(true);
                }
                catch(NumberFormatException e){
                    System.out.println("(Please enter a valid whole number) ");
                }
                catch(IndexOutOfBoundsException e){
                    System.out.println("(Please refer to list by their list number) ");
                }
            }
            return true;
        });

        Menu mainMenu = new Menu("Main Menu", "q");
        mainMenu.putOption("1", "view todos", ()->{viewMenu.viewMenu(); return true;});
        mainMenu.putOption("2", "add todos", ()->{addMenu.viewMenu(); return true;});
        mainMenu.putOption("3", "mark complete", ()->{completeMenu.viewMenu(); return true;});
        return mainMenu;
    }

}
