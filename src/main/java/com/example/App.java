package com.example;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.example.Menus.MainMenu;
import com.google.gson.Gson;

public class App 
{
    public static void main( String[] args )
    {
        // ConfigurableApplicationContext context = App.run(App.class, args);
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class)) {
            // Get the bean and use it

            System.out.println("Starting now...");
            MainMenu mainMenu = context.getBean(MainMenu.class);
            mainMenu.PrintMainMenu();
        }
        
    }

}



//class User {
//    private String name;
//    private int age;
//
//    // Constructor, getters, setters
//    public User(String name, int age) {
//        this.name = name;
//        this.age = age;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public int getAge() {
//        return age;
//    }
//}
