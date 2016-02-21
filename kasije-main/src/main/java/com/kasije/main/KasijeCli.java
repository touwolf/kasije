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

    /* Here we define de main utilities and call custom methods to do the processing */
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
                System.out.println("Unknown utility.");
                System.out.println("    Run with 'help' for help");
                break;

        }
    }

    private static void processStart(String[] args)
    {
        /* Using default port */
        String port = "8080";

        if (args.length >= 1)
        {
            // Site Name without ''
            String[] split = args[0].split("=");

            if (split.length != 2)
            {
                System.out.println("Bad syntax at argument 1, assignment expression required.");
                return;
            }
            else
            {
                if (split[0].equals("port"))
                {
                    try
                    {
                        int i = Integer.parseInt(split[1]);
                        if (i > 0 && i < 65536)
                        {
                            port = split[1];
                        }
                        else
                        {
                            throw new NumberFormatException();
                        }

                    } catch (NumberFormatException ex)
                    {
                        System.out.println("Invalid port. Using default 8080.");
                    }
                }
                else
                {
                    System.out.println("Unrecognized option: " + split[0]);
                    /* Bad option, return */
                    return;
                }
            }
        }

        // TODO: start
        /* START service */
        System.out.println("Starting service in port " + port);
    }

    private static void processGenerate(String[] args)
    {
        String pageName;

        if (args.length == 0)
        {
            System.out.println("Missing options for the 'generate' utility.");
            /* No arguments, nothing to process, return */
            return;
        }
        else
        {
            pageName = args[0];

        }

        // TODO: La candela es aquí
        /* GENERATE */
        System.out.println("Generating page: " + pageName);
    }

    private static void processCreate(String[] args)
    {
        String siteName;
        String engine;
        String comp;

        if (args.length == 0)
        {
            System.out.println("Missing options for the 'create' utility.");
            /* Nothing to create, return */
            return;
        }

        /* Site name forced to be the first argument */
        if (args.length >= 1)
        {
            siteName = args[0];
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
                    System.out.println(String.format("Bad option at argument %d, assignment expression required.", i));
                    continue;
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
                            System.out.println("Unrecognized option: " + split[0]);
                            break;
                    }
                }
                i++;
            }
        }

        // TODO: Hacer algo con siteName, engine & comp
        // TODO: Aquí
        /* CREATE */
    }

    private static void processHelp(String[] args)
    {
        if (args.length == 0)
        {
            System.out.println("KasijeCli Utilities: ");
            System.out.println("    create - Creates a new site.");
            System.out.println("    generate - Generates page components.");
            System.out.println("    start - Start service.");
            System.out.println(" ");
        }
        else
        {
            switch (args[0])
            {
                case "create":
                    System.out.println("Uses of create:");
                    break;
                case "generate":
                    System.out.println("Uses of generate:");
                    break;
                case "start":
                    System.out.println("Uses of start");
                    break;
                case "help":
                    System.out.println("Uses of start");
                    System.out.println("    Call 'help utility' to see the utility related help.");
                    System.out.println(" ");
                    break;
            }
        }
    }
}
