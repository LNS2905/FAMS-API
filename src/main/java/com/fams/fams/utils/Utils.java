package com.fams.fams.utils;

import com.fams.fams.models.entities.TrainingProgramModule;

import java.io.File;
import java.security.SecureRandom;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

public class Utils {
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");


    public static String generateFAAccount(){
        int length = 20;
        String charset = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder FAAccount = new StringBuilder(length);
        while (length -- > 0) {
            FAAccount.append(charset.charAt(random.nextInt(charset.length())));
        }
        return FAAccount.toString();
    }
    public static String generateStudentType() {
        // Array of possible strings
        String[] options = {"FE", "BE", "FullStack", "DevOps"};

        // Create a Random object
        Random random = new Random();

        // Generate a random index
        int randomIndex = random.nextInt(options.length);

        // Return the randomly selected string
        return options[randomIndex];
    }


    public static String generateStudentCode() {
        // Prefix
        String prefix = "FSA";

        // Generate a sequence of 10 random digits
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 10; i++) {
            int randomDigit = random.nextInt(10); // Generates a random digit between 0 and 9
            stringBuilder.append(randomDigit);
        }

        // Concatenate prefix and random digits
        return prefix + stringBuilder.toString();
    }

    public static String getFileType(String filename){
        String fileName = new File(filename).getName();
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
    }
    public static StringBuilder getStringBuilder(List<TrainingProgramModule> listModuleInThisCourse) {
        StringBuilder listNameOfModule = new StringBuilder();
        for (TrainingProgramModule module: listModuleInThisCourse) {
            String moduleName = module.getModules().getModuleName();
            if (!listNameOfModule.isEmpty()) {
                listNameOfModule.append(", ");
            }
            listNameOfModule.append(moduleName);
        }
        return listNameOfModule;
    }


    public static float calculateAverage(List<List<Float>> listOfLists) {
        int totalCount = 0;
        float sum = 0;

        for (List<Float> sublist : listOfLists) {
            if (sublist.size() > 1) {
                float sublistSum = 0;
                for (float num : sublist) {
                    sublistSum += num;
                }
                sum += sublistSum / sublist.size();
                totalCount++;
            } else if (sublist.size() == 1) {
                sum += sublist.get(0);
                totalCount++;
            }
        }

        if (totalCount == 0) {
            return 0; // Avoid division by zero
        }

        return sum / totalCount;
    }

    public static float getAverage(float ... scores){

        float sum = 0;
        for (float score: scores) {
            sum+=score;
        }
        return sum/scores.length;

    }




}
