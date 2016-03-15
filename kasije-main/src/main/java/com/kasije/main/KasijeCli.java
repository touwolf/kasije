package com.kasije.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * CLI.
 */
public class KasijeCli
{
    private List<KasijeCliUtility> utilities = new LinkedList<>();

    private String helpCommand;

    public List<KasijeCliUtility> getUtilities()
    {
        return utilities;
    }

    private void addUtility(KasijeCliUtility utility)
    {
        utilities.add(utility);
    }

    public void parseInput(String input)
    {
        String[] split = input.split(" ");
        if (split.length == 0)
        {
            System.out.println("  Utility name missing.");
            System.out.println("  Type help for help.");
        }
        else
        {
            processUtility(split);
        }
    }

    public static void main(String[] args)
    {
        KasijeCli kasijeCli = new KasijeCli();

        /* create */
        KasijeCliUtility kuCreate = new KasijeCliUtility("create", "creates a site...");
        kuCreate.addOption(new KasijeCliOption("engine", KasijeCliOptionStyle.WITH_ARGUMENT));
        kuCreate.addOption(new KasijeCliOption("comp", KasijeCliOptionStyle.WITH_ARGUMENT));
        kuCreate.setFirstMandatoryOptionName("siteName");
        kuCreate.setKasijeCliExecutor(argumentValueMap ->
        {
            /* Do what needs to be done with create*/
            System.out.println("Creating site " + argumentValueMap.get("siteName"));
            System.out.println("  Using engine " + argumentValueMap.get("engine"));
            System.out.println("  Component type " + argumentValueMap.get("comp"));
        });

        /* start */
        KasijeCliUtility kuStart = new KasijeCliUtility("start", "start a site...");
        kuStart.setKasijeCliExecutor(argumentValueMap ->
        {
            /* Do what needs to be done with start */
            System.out.println("  Starting kasije server...");
        });

        /* Adding utilities*/
        kasijeCli.addUtility(kuCreate);
        kasijeCli.addUtility(kuStart);
        kasijeCli.setHelpCommand("help");

        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
        while (true)
        {
            try
            {
                String input = console.readLine();
                kasijeCli.parseInput(input);

            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void processUtility(String[] args)
    {
        /* Utility processing */
        for (KasijeCliUtility utility : utilities)
        {
            if (utility.getName().equals(args[0]))
            {
                utility.process(Arrays.copyOfRange(args, 1, args.length));
                return;
            }
        }

        /* Help processing */
        if (getHelpCommand() != null)
        {
            processHelp(args);
            return;
        }

        System.out.println("  Ignoring unknown utility: " + args[0]);
        System.out.println("  Type in help for help");

    }

    private void processHelp(String[] args)
    {
        if (args[0].equals(getHelpCommand()))
        {
            if (args.length > 1)
            {
                for (KasijeCliUtility utility : utilities)
                {
                    if (utility.getName().equals(args[1]))
                    {
                        System.out.println("NAME");
                        System.out.println(utility.getName() + " - " + utility.getOutline());
                        System.out.println(" ");

                        System.out.println("SYNOPSIS");
                        StringBuilder synopsisBuilder = new StringBuilder(utility.getName());
                        synopsisBuilder.append(" ");
                        if (utility.getFirstMandatoryOptionName() != null)
                        {
                            synopsisBuilder.append(String.format("<%s>", utility.getFirstMandatoryOptionName()));
                            synopsisBuilder.append(" ");
                        }

                        for (KasijeCliOption kasijeCliOption : utility.getKasijeCliOptions())
                        {
                            synopsisBuilder.append(String.format("[%s=<%s>] ", kasijeCliOption.getName(), kasijeCliOption.getName()));
                        }
                        System.out.println(synopsisBuilder.toString());

                        System.out.println(" ");
                        System.out.println("DESCRIPTION");
                        System.out.println(utility.getDescription());
                        return;
                    }
                }

                if (args[1].equals("help"))
                {
                    System.out.println(":/");
                    return;
                }

                System.out.println("  No help for non-existent utility: " + args[1]);
                System.out.println("  Type in help for more help");
            }
            else
            {
                System.out.println("Available utilities are:");
                for (KasijeCliUtility kasijeCliUtility : utilities)
                {
                    System.out.println("  " + kasijeCliUtility.getName());
                }
                System.out.println("  Enter help followed by one of the above for further assistance.");
            }
        }
    }

    public String getHelpCommand()
    {
        return helpCommand;
    }

    public void setHelpCommand(String helpCommand)
    {
        this.helpCommand = helpCommand;
    }

    public static class KasijeCliUtility
    {
        private String name;

        private String firstMandatoryOptionName;

        private List<KasijeCliOption> kasijeCliOptions;

        private String outline;

        private KasijeCliExecutor kasijeCliExecutor;

        private String description;

        public KasijeCliUtility(String name, String outline)
        {
            this.name = name;
            this.outline = outline;

            kasijeCliOptions = new LinkedList<>();
        }

        public List<KasijeCliOption> getKasijeCliOptions()
        {
            return kasijeCliOptions;
        }

        public void setKasijeCliOptions(List<KasijeCliOption> kasijeCliOptions)
        {
            this.kasijeCliOptions = kasijeCliOptions;
        }

        public String getDescription()
        {
            return description;
        }

        public void setDescription(String description)
        {
            this.description = description;
        }

        public String getFirstMandatoryOptionName()
        {
            return firstMandatoryOptionName;
        }

        public void setFirstMandatoryOptionName(String firstMandatoryOptionName)
        {
            this.firstMandatoryOptionName = firstMandatoryOptionName;
        }

        public void addOption(KasijeCliOption kasijeCliOption)
        {
            kasijeCliOptions.add(kasijeCliOption);
        }

        public void setKasijeCliExecutor(KasijeCliExecutor kasijeCliExecutor)
        {
            this.kasijeCliExecutor = kasijeCliExecutor;
        }

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        public String getOutline()
        {
            return outline;
        }

        public void setOutline(String outline)
        {
            this.outline = outline;
        }

        /* Receives options array */
        public void process(String[] args)
        {
            Map<String, String> map = new HashMap<>();

            /* Check if this utility have no options */
            if (kasijeCliOptions.size() == 0)
            {
                if (kasijeCliExecutor != null)
                {
                    kasijeCliExecutor.execute(map);
                }
                return;
            }

            /* Check if arguments does not suffice options */
            if (args.length == 0)
            {
                System.out.format("Missing options for the %s utility", getName());
                System.out.println(getOutline());
                return;
            }

            /* Proceed to process options */
            String[] arguments = args;

            /* Obtaining mandatory option if required */
            if (firstMandatoryOptionName != null)
            {
                /* Mandatory option always the first */
                map.put(firstMandatoryOptionName, args[0]);

                /* If other options, accommodate and proceed  */
                if (args.length > 1)
                {
                    arguments = Arrays.copyOfRange(args, 1, args.length);
                }
                else
                {
                    /* return */
                    if (kasijeCliExecutor != null)
                    {
                        kasijeCliExecutor.execute(map);
                    }
                    return;
                }
            }

            int i = 1;
            ARGS:
            for (String arg : arguments)
            {
                String[] split = arg.split("=");

                if (split.length != 2)
                {
                    System.out.println(String.format("  Bad option at argument %d, assignment expression required.", i));
                }
                else
                {
                    for (KasijeCliOption option : kasijeCliOptions)
                    {
                        if (split[0].equals(option.getName()))
                        {
                            map.put(option.getName(), split[1]);
                            continue ARGS;
                        }
                    }
                    System.out.println("Ignoring unrecognized option: " + split[0]);
                }
            }

            if (kasijeCliExecutor != null)
            {
                kasijeCliExecutor.execute(map);
            }
        }

    }

    public static class KasijeCliOption
    {
        private String name;

        private KasijeCliOptionStyle kasijeCliOptionStyle;

        public KasijeCliOption(String name, KasijeCliOptionStyle kasijeCliOptionStyle)
        {
            this.name = name;
            this.kasijeCliOptionStyle = kasijeCliOptionStyle;
        }

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }
    }

    public interface KasijeCliExecutor
    {
        void execute(Map<String, String> argumentValueMap);
    }

    enum KasijeCliOptionStyle
    {
        SIMPLE,
        WITH_ARGUMENT,
        SPACED
    }
}
