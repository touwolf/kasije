package com.kasije.main;

import java.util.Arrays;

/**
 * CLI.
 */
public class KasijeCli
{
    public static void main(String[] args)
    {
        if (args.length == 0)
        {
            System.out.println("Argument missing.");
            System.out.println("Run with 'help' for help.");
        }
        else
        {
            processUtility(args);
        }

    }

    private static void processUtility(String[] args)
    {
        String utility = args[0];
        String[] uArgs = Arrays.copyOfRange(args, 1, args.length);

        switch (utility)
        {
            case "create":
                processCreate(uArgs);
                break;
            case "generate":
                processGenerate(uArgs);
                break;
            case "help":
                processHelp(uArgs);
                break;
            default:
                System.out.println("Invalid argument.");
                System.out.println("Run with -h for help");
                break;

        }
    }

    private static void processGenerate(String[] args)
    {
        throw new UnsupportedOperationException("Not Implemented yet");
    }

    private static void processCreate(String[] args)
    {
        throw new UnsupportedOperationException("Not Implemented yet");
    }

    private static void processHelp(String[] args)
    {
        if (args.length == 0)
        {
            System.out.println("KasijeCli Utilities: ");
            System.out.println("    create - Creates a new site..");
            System.out.println("    generate - Generates page components.");
        }
        else
        {

        }
    }
}
