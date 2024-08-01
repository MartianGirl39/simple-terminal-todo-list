package com.example.menus;

import com.example.menus.options.OptionResponse;
import com.example.menus.options.OptionResponseFunction;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Menu {
    private Map<String, OptionResponse> options = new HashMap<>();
    private String exitOption;
    private String menuName;
    private String invalidMessage;

    public Menu(String menuName, String exitOption){
        this.menuName = menuName;
        this.exitOption = exitOption;
        this.invalidMessage = "Invalid Input";
    }

    public void putOption(String option, String description, OptionResponseFunction response){
        options.putIfAbsent(option, new OptionResponse(description, response));
    }

    public void viewMenu(){
        Scanner input = new Scanner(System.in);
        boolean stay = true;
        String choice;

        while(stay) {
            promptUser();
            System.out.println("Enter value >> ");
            choice = input.nextLine().strip();

            if(options.containsKey(choice)){
                stay = options.get(choice).getResponse().respond();
            }
            else if(choice.equals(exitOption)){
                stay = false;
            }
            else {
                System.out.println("\u001B[1;91m;" + invalidMessage + "\u001B[0m;");
            }
        }
    }

    private void promptUser(){
        System.out.println("***" + menuName.toUpperCase() + "***");
        for(Map.Entry<String, OptionResponse> opt : options.entrySet()){
            System.out.println("Enter " + opt.getKey() + " to " + opt.getValue().getActionName().toLowerCase());
        }
    }

    public void setInvalidMessage(String invalidMessage) {
        this.invalidMessage = invalidMessage;
    }
}
