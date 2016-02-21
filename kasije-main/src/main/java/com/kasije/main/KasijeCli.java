package com.kasije.main;

import java.util.*;

/**
 * CLI.
 */
public class KasijeCli
{

    private List<KasijeCliUtility> utilities = new LinkedList<>();

    private void addUtility(KasijeCliUtility utility)
    {
        utilities.add(utility);
    }

    public void parseInput(String input)
    {
        String[] split = input.split(" ");
        if (split.length == 0)
        {
            System.out.println("Utility name missing.");
            System.out.println("Type help for help.");
        }
        else
        {
            processUtility(split);
        }
    }

    public static void main(String[] args)
    {
        KasijeCli kasijeCli = new KasijeCli();

        KasijeCliUtility kuCreate = new KasijeCliUtility("create", "creates...");
        kuCreate.addOption(new KasijeCliOption("engine"));
        kuCreate.addOption(new KasijeCliOption("comp"));
        kuCreate.setKasijeCliExecutor(argumentValueMap ->
        {
            System.out.println("Creating site " + argumentValueMap.get("siteName"));
            System.out.println("    Using engine " + argumentValueMap.get("engine"));
            System.out.println("    Component type " + argumentValueMap.get("comp"));
        });

        kasijeCli.addUtility(kuCreate);
        kasijeCli.processUtility(args);

    }

    private void processUtility(String[] args)
    {
        for (KasijeCliUtility utility : utilities)
        {
            if (utility.getName().equals(args[0]))
            {
                utility.process(args);
                break;
            }
            else
            {
                System.out.println("Ignoring unknown utility: " + args[0]);
                System.out.println("    Type in help for help");
            }
        }
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

    public static class KasijeCliUtility
    {
        private String name;

        private List<KasijeCliOption> kasijeCliOptions;

        private String help;

        private KasijeCliExecutor kasijeCliExecutor;

        public KasijeCliUtility(String name, String help)
        {
            this.name = name;
            this.help = help;

            kasijeCliOptions = new LinkedList<>();
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

        public String getHelp()
        {
            return help;
        }

        public void setHelp(String help)
        {
            this.help = help;
        }

        public void process(String[] args)
        {

            System.out.println("-- Processing " + getName());
            Map<String, String> map = new HashMap<>();

            int i = 1;
            ARGS:
            for (String arg : Arrays.copyOfRange(args, 1, args.length))
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

        public KasijeCliOption(String name)
        {
            this.name = name;
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
        DEFAULT,
        ASSIGNED,
        SPACED
    }
}
