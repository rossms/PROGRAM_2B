/**************************************************************************************************
 * Created by Ross Silberquit (RMS) on 1/15/17.
 *
 * Name: RealNumberStore.java
 * Purpose: This program stores and retrieves a series of n real numbers into and from a file.
 * Version: 2.0
 * Notes: this program uses Java 8 code standards
 * Revision Notes:
 *      Version 2.0:
 *          02/02/2017 (RMS) added functionality to modify values in file
 *          02/02/2017 (RMS) added save as functionality
 *
 *************************************************************************************************/
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class RealNumberStore {
    /**
     * This is the main function for the program. It calls helper functions as defined below,
     * to carry out program functionality.
     *
     * @param args
     * Return: void
     */
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        String fileName = getFileName(scanner).replaceAll("\\u0020", "%20");
        String programMode = programMode(scanner);
        if(programMode.equalsIgnoreCase("WRITE")){
            writeToFile(scanner, fileName);
        }else if(programMode.equalsIgnoreCase("MODIFY")){
            modifyFile(scanner, fileName);
        }else{
            readFile(fileName);
        }
        scanner.close();
    }
    /**
     * This function prompts a user for a file name, and returns the value entered
     *
     * @param scanner
     * Return: String (file name)
     */
    private static String getFileName(Scanner scanner){
        System.out.println("Enter an absolute file name: ");
        return scanner.next();
    }
    /**
     * This function prompts the user to choose read or write  or modify mode
     *
     * @param scanner
     * Return: String (READ, WRITE, or MODIFY)
     */
    private static String programMode(Scanner scanner){
        String programMode = "";
        System.out.println("Enter read mode (r), write mode (w) or modify mode (m):");
        String mode = scanner.next();
        if(mode.equalsIgnoreCase("r")){
            programMode = "READ";
        }else if(mode.equalsIgnoreCase("w")){
            programMode = "WRITE";
        }else if(mode.equalsIgnoreCase("m")){
            programMode = "MODIFY";
        }else{
            System.out.println("Invalid option");
            programMode(scanner);
        }
        return programMode;
    }
    /**
     * This function prompts the user to enter values they wish to save, and saves to file
     *
     * @param scanner
     * @param fileName
     * Return: void
     */
    private static void writeToFile(Scanner scanner, String fileName){
        System.out.println("Enter quantity of numbers to be recorded: ");
        int inputNum = scanner.nextInt();
        String stringToSave = "";
        while(inputNum > 0){
            System.out.println("Enter next number");
            double inputDouble = scanner.nextDouble();
            stringToSave += String.valueOf(inputDouble)+ "\n";
            inputNum--;
        }
        try {
            Files.write(Paths.get(fileName), String.valueOf(stringToSave).getBytes());
            System.out.println("Saving and exiting");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * This function reads the file, prints out line by line
     *
     * @param fileName
     * Return: void
     */
    private static void readFile(String fileName){
        try(Stream<String> stream = Files.lines(Paths.get(fileName))){
            stream.forEach(System.out::println);
        } catch(IOException e){
            e.printStackTrace();
        }
    }
    /**
     * This function reads each line of the file, and gives the user modification options
     * Once done, gives the user the option to save or save as
     *
     * @param scanner
     * @param fileName
     * Return: void
     */
    private static void modifyFile(Scanner scanner, String fileName){
        List<String> list = new ArrayList<>();
        try(Stream<String> stream = Files.lines(Paths.get(fileName))){
            stream.forEach(line->{
                list.add(line);
            });
        } catch(IOException e){
            e.printStackTrace();
        }
        for(ListIterator<String> it = list.listIterator(); it.hasNext();){
            String number = it.next();
            System.out.println("Current number value is: " + number);
            System.out.println("Please choose an option: accept (A), modify (M), or delete (D) " +
                    " this value");
            String modOption = scanner.next();
            if(modOption.equalsIgnoreCase("M")){
                System.out.println("Enter a new value: ");
                double newDouble = scanner.nextDouble();
                it.remove();
                it.add(String.valueOf(newDouble));
                //list.remove(list.indexOf(number));
                System.out.println("Value "+number+" has been removed and new value "+ String.
                        valueOf(newDouble)+" has been added in its place.");
            }else if(modOption.equalsIgnoreCase("D")){
                it.remove();
                System.out.println("Value "+number+" has been removed.");
            }else if(modOption.equalsIgnoreCase("A")){
                System.out.println("Value accepted and unchanged.");
            }else{
                System.out.println("Invalid option, value unchanged.");
            }
            System.out.println("Please choose an option: insert new number(I), finish (F) which " +
                "accepts all remaining numbers, or next (N) number in the file: ");
            String nextAction = scanner.next();
            if(nextAction.equalsIgnoreCase("I")){
                System.out.println("Enter a new double value: ");
                double newDouble = scanner.nextDouble();
                it.add(String.valueOf(newDouble));
            }else if(nextAction.equalsIgnoreCase("F")){
                System.out.println("Accepting all remaining number and exiting modify mode.");
                break;
            }else if(nextAction.equalsIgnoreCase("N")){
                System.out.println("Reading next value from file");
            }else{
                System.out.println("Invalid option. Ignoring, and reading next value from file");
            }
        }
        System.out.println("EOF; would you like to save (S) the file or save changes as a new (N) " +
                "file?");
        String saveOption = scanner.next();
        saveOrSaveAs(scanner, list, fileName, saveOption);
    }
    /**
     * This function saves the content in the existing file, or saves as a new file
     *
     * @param scanner
     * @param list
     * @param fileName
     * @param saveOption
     * Return: void
     */
    private static void saveOrSaveAs(Scanner scanner, List<String> list, String fileName, String saveOption){
        String stringToSave = "";
        for(String line : list){
            stringToSave+= line+ "\n";
        }
        if(saveOption.equalsIgnoreCase("S")){
            try {
                Files.write(Paths.get(fileName), String.valueOf(stringToSave).getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(saveOption.equalsIgnoreCase("N")){
            System.out.println("Please enter a new fully qualified file name:");
            String newFileName = scanner.next();
            try {
                Files.write(Paths.get(newFileName), String.valueOf(stringToSave).getBytes());
                System.out.println("Saving and exiting");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
