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

            case "start":
                processStart(uArgs);
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

    private static void processStart(String[] args)
    {
        throw new UnsupportedOperationException("Not Implemented yet");
    }

    private static void processGenerate(String[] args)
    {

        String pageName;

        if (args.length == 0)
        {
            System.out.println("Missing options for the 'generate' utility.");
        }

        /* Site name forced to be the first argument */
        if (args.length >= 1)
        {
            // Page Name without ''
            pageName = args[0].substring(1, args[0].length());
        }
    }

    private static void processCreate(String[] args)
    {
        String siteName;
        String engine;
        String comp;

        if (args.length == 0)
        {
            System.out.println("Missing options for the 'create' utility.");
        }

        /* Site name forced to be the first argument */
        if (args.length >= 1)
        {
            // Site Name without ''
            siteName = args[0].substring(1, args[0].length());
        }

        /* Obtain further options, order is not important */
        if (args.length >= 2)
        {
            int i = 1;
            for (String arg : Arrays.copyOfRange(args, 1, args.length))
            {
                String[] split = arg.split("=");

                if (split.length != 2)
                {
                    System.out.println(String.format("Bad syntax at argument %d, assignment expression required.", i));
                    return;
                }
                else
                {
                    /* Switching among 'create' possible options */
                    switch (split[0])
                    {
                        case "engine":
                            engine = split[1];
                            break;
                        case "comp":
                            comp = split[1];
                            break;
                        default:

                    }
                }
                i++;
            }
        }

        // TODO: Hacer algo con siteName, engine & comp
        // TODO: Aquí
    }

    private static void processHelp(String[] args)
    {
        if (args.length == 0)
        {
            System.out.println("KasijeCli Utilities: ");
            System.out.println("    create - Creates a new site..");
            System.out.println("    generate - Generates page components.");
            System.out.println("    start - Start service.");
        }
        else
        {

        }
    }
}
