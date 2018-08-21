package org.aid.externalsorting;

import static org.aid.externalsorting.common.Utils.nullCheck;

import java.io.File;
import java.io.IOException;

public class Main {

    private static void displayUsage() {
        System.out.println("java org.aid.externalsorting.Main -i inputFolder -t tempFolder -k 10");
        System.out.println("-i : directory of input files");
        System.out.println("-t : directory of tempory files");        
        System.out.println("-k : value of K");

    }

    public static void main(final String[] args) throws IOException {
        String inputFolderPath = "";
        File inputFolder = null;
        String tempFolderPath = "";
        File tempFolder = null;
        Integer K = null;
        if (args.length < 6) {
            displayUsage();
            throw new IllegalArgumentException("invalid properties");
        }
        for (int param = 0; param < args.length; param++) {
            if (args[param].equals("-i")) {
                param = param + 1;
                inputFolderPath = args[param];

                inputFolder = new File(inputFolderPath);
                if (inputFolder.exists() && inputFolder.isDirectory()) {
                    System.out.println("input directory exits at path:" + inputFolder.getAbsolutePath());
                } else {
                    displayUsage();
                    throw new IllegalArgumentException("no input directory found at:" + inputFolder.getAbsolutePath());
                }
            }
            if (args[param].equals("-t")) {
                param = param + 1;
                tempFolderPath = args[param];

                tempFolder = new File(tempFolderPath);
                if (tempFolder.exists() && tempFolder.isDirectory()) {
                    System.out.println("temporary directory exits at path:" + tempFolder.getAbsolutePath());
                } else {
                    displayUsage();
                    throw new IllegalArgumentException("no temporary directory found at:" + tempFolder.getAbsolutePath());
                }
            }            
            if (args[param].equals("-k")) {
                param = param + 1;
                try {
                    K = Integer.parseInt(args[param]);
                    if (K < 2) {
                        displayUsage();
                        throw new IllegalArgumentException("invalid value for 'k', it must be bigger than 2. ");
                    }
                } catch (Exception e) {
                    System.err.print(e);
                    throw new IllegalArgumentException("invalid value for 'k'");
                }
            }
        }


        try {
            nullCheck(inputFolder, "inputFolder property missing");            
            nullCheck(tempFolder, "tempFolder property missing");
            nullCheck(K, "K property missing");

            System.out.println("start program with values");
            System.out.println("inputFolderPath @:" + inputFolder.getAbsolutePath());            
            System.out.println("temporaryFolderPath @:" + tempFolder.getAbsolutePath());
            System.out.println("k:" + K);
            
            ExternalMargeSortTask<String> sortingTask = ExternalMargeSortTask.creatStringSortingTask(inputFolder, new File("temp"), 2);		
    		File sortedFile = sortingTask.executeSort();
        	
        	System.out.println("the sorted file is produced @:" + sortedFile.getAbsolutePath());
        } catch (Exception exception) {
            exception.printStackTrace();
            displayUsage();
            System.exit(0);
        }


    }
}
