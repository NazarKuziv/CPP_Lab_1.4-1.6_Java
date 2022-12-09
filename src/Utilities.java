import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.text.Collator;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.time.ZoneId.systemDefault;

public class Utilities {
 public static List<Prisoner>  Read_File()
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        formatter = formatter.withLocale(Locale.ENGLISH);

        List<Prisoner> prisoners = new ArrayList<>();
        try {
            File myObj = new File("src/police_file.txt");
            Scanner myReader = new Scanner(myObj);

            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] entries = data.split(",");

                Prisoner newPrisoner = new Prisoner();
                newPrisoner.setID(Integer.parseInt(entries[0]));
                newPrisoner.setSurname(entries[1]);
                newPrisoner.setName(entries[2]);
                newPrisoner.setMiddle_Name(entries[3]);

                LocalDate localDate = LocalDate.parse(entries[4], formatter);
                newPrisoner.setBirthday(localDate);

                String[] date = new String[entries.length - 5];
                int i = 5, j = 0;

                while (i < entries.length)
                {
                    date[j] = entries[i];
                    i++;
                    j++;

                }
                newPrisoner.setDates_of_Convictions(date);
                localDate = LocalDate.parse(date[j-2], formatter);
                newPrisoner.setDate_of_Last_Imprisonment(localDate);
                if (!Objects.equals(date[j - 1], "...")){
                    localDate =LocalDate.parse(date[j-1], formatter);
                    newPrisoner.setDate_of_Last_Dismissal(localDate);
                }

                prisoners.add(newPrisoner);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return prisoners;
    }
    static int tableWidth = 188;
    public static void PrintTable(List<Prisoner> prisonerList)
    {
        PrintLine();
        PrintRow(new String[]{"ID","П.І.П", "Дата народження ", "Дати ув'язнень", "Дата ост ув'язнення", "Дата ост звільнення"});
        PrintLine();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String date;
        for (Prisoner p : prisonerList){
            if (p.getDates_of_Convictions().length == 2)
            {
                date = p.getDate_of_Last_Dismissal().isEqual(LocalDate.MIN)?"...":p.getDate_of_Last_Dismissal().format(formatter);
                String[] str = {String.valueOf(p.getID()),p.getSurname() +" "+ p.getName() +" "+ p.getMiddle_Name(), p.getBirthday().format(formatter),
                        p.getDates_of_Convictions()[0]+"-"+ p.getDates_of_Convictions()[1],p.getDate_of_Last_Imprisonment().format(formatter), date};

                PrintRow(str);
            }
            else
            {
                date = p.getDate_of_Last_Dismissal().isEqual(LocalDate.MIN)?"...":p.getDate_of_Last_Dismissal().format(formatter);
                String[] str = {String.valueOf(p.getID()),p.getSurname() +" "+ p.getName() +" "+ p.getMiddle_Name(), p.getBirthday().format(formatter),
                        p.getDates_of_Convictions()[0]+"-"+ p.getDates_of_Convictions()[1],p.getDate_of_Last_Imprisonment().format(formatter), date};
                PrintRow(str);
                for (int i = 2; i < p.getDates_of_Convictions().length - 1; i++)
                {
                    PrintRow(new String[]{"","", "", p.getDates_of_Convictions()[i] + "-" + p.getDates_of_Convictions()[i+1], "", ""});
                }
            }
            PrintLine();
        }
        System.out.println();
    }
    public static void PrintLine()
    {
        System.out.println(new String(new char[tableWidth]).replace("\0", "-"));
    }
    public static String centerString (int width, String s) {
        return String.format("%-" + width  + "s", String.format("%" + (s.length() + (width - s.length()) / 2) + "s", s));
    }
    public static void PrintRow(String[] columns)
    {
        int width = (tableWidth - 10) / 5;

        StringBuilder row = new StringBuilder("|");
        for (int i = 0; i < columns.length; i++)
        {
            if (i != 0)
            {
                row.append(centerString(width, columns[i])).append("|");
            }
            else
            {
                row.append(centerString(5, columns[i])).append("|");
            }

        }
        System.out.print(row);
        System.out.println();

    }
    public static void Sort_by(List<Prisoner> listPrisoner, int sort_by)
    {

        switch (sort_by) {
            case 1 -> {
                Collator uaCollator = Collator.getInstance(new Locale("uk", "UA"));
                uaCollator.setStrength(Collator.PRIMARY);
                listPrisoner.sort((s1, s2)->uaCollator.compare(s1.getSurname(), s2.getSurname()));
            }
            case 2 -> listPrisoner.sort(Comparator.comparing(x -> x.Birthday));
            case 3 -> listPrisoner.sort(Comparator.comparing(x -> x.Date_of_Last_Imprisonment));
            case 4 -> listPrisoner.sort(Comparator.comparing(x -> x.Date_of_Last_Dismissal));
            default -> {
            }
        }

    }

    public static void Serialize(List<Prisoner> p)
    {
        final JFrame iFRAME = new JFrame();
        iFRAME.setAlwaysOnTop(true);    // ****
        iFRAME.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        iFRAME.setLocationRelativeTo(null);
        iFRAME.requestFocus();

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Serialization files(.ser)","ser");
        fileChooser.addChoosableFileFilter(filter);

        if (fileChooser.showSaveDialog(iFRAME) == JFileChooser.APPROVE_OPTION) {
            try
            {
                try (FileOutputStream fileIn = new FileOutputStream(fileChooser.getSelectedFile());
                     ObjectOutputStream out = new ObjectOutputStream(fileIn)) {
                    out.writeObject(p);
                }
            }
            catch (IOException i)
            {
                i.printStackTrace();
            }
        }
    }

    public static List<Prisoner> Deserialize()
    {
        final JFrame iFRAME = new JFrame();
        iFRAME.setAlwaysOnTop(true);    // ****
        iFRAME.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        iFRAME.setLocationRelativeTo(null);
        iFRAME.requestFocus();

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Serialization files(.ser)","ser");
        fileChooser.addChoosableFileFilter(filter);

        if (fileChooser.showOpenDialog(iFRAME) == JFileChooser.APPROVE_OPTION) {
            try
            {
                try (FileInputStream fileIn = new FileInputStream(fileChooser.getSelectedFile());
                     ObjectInputStream in = new ObjectInputStream(fileIn)) {
                    return (List<Prisoner>)in.readObject();

                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
            catch (IOException i)
            {
                i.printStackTrace();
            }
        }
        return null;
    }

    static Scanner input = new Scanner(System. in,"UTF-8");
    public static List<Prisoner> Add(List<Prisoner> pf1)
    {

        ZoneId zid = systemDefault();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        formatter = formatter.withZone(zid);

        boolean errors = false;
        Pattern pattern = Pattern.compile("^([0]?[0-9]|[12][0-9]|[3][01])[/]([0]?[1-9]|[1][0-2])[/]([0-9]{4}|[0-9]{2})$", Pattern.CASE_INSENSITIVE);

        List<String> data = new ArrayList<>();

        Prisoner newPrisoner = new Prisoner();
        int id_arr[] = new int[pf1.size()];

        for(int i =0;i<id_arr.length;i++){
            id_arr[i]= pf1.get(i).getID();
        }
        newPrisoner.setID(Arrays.stream(id_arr).max().getAsInt()+1);
        System.out.println("  ID :" + newPrisoner.getID());


        System.out.println("  Введіть:");
        System.out.print("  Прізвище: "); data.add(input.next());
        System.out.print("  Ім'я:"); data.add(input.next());
        System.out.print("  По батькові:"); data.add(input.next());

        do
        {
            System.out.print("  День народження(дд/мм/рррр): "); data.add(input.next());
            Matcher matcher = pattern.matcher(data.get(3));
            boolean matchFound = matcher.find();
            if (matchFound)
            {
                if (LocalDate.now(zid).isAfter(LocalDate.parse(data.get(3), formatter)))
                {
                    errors = true;
                }
            }
            else
            {
                data.remove(3);
                System.out.println("Дата введена на правильно");
            }

        } while (!errors);

        errors = false;
        do
        {
            System.out.print("  Дата ув'язнення (дд.мм.рррр):"); data.add(input.next());
            Matcher matcher = pattern.matcher(data.get(4));
            boolean matchFound = matcher.find();
            if (matchFound)
            {
                if (LocalDate.now(zid).isAfter(LocalDate.parse(data.get(4), formatter)))
                {
                    errors = true;
                }

            }
            else
            {
                data.remove(4);
                System.out.println("Дата введена на правильно");
            }

        } while (!errors);

        newPrisoner.setSurname(data.get(0));
        newPrisoner.setName(data.get(1));
        newPrisoner.setMiddle_Name(data.get(2));

        newPrisoner.setBirthday(LocalDate.parse(data.get(3), formatter));
        newPrisoner.setDate_of_Last_Imprisonment(LocalDate.parse(data.get(4), formatter));
        String[] date = { data.get(4), "..." };
        newPrisoner.setDates_of_Convictions(date);

        pf1.add(newPrisoner);
        return pf1;
    }

    public static List<Prisoner> Edit(List<Prisoner> pf1)
    {
        boolean found = false;
        int index = 0;

        do
        {
            System.out.print("  Введіть ID: "); int id = input.nextInt();
            for(Prisoner p : pf1)
            {
                if(p.ID == id)
                {
                    index = pf1.indexOf(p);
                    found=true;
                    break;
                }
            }

            if (found == false)  System.out.println(" Неправильне ID!");

        } while (found == false);

        String date;
        boolean errors = false;
        Pattern pattern = Pattern.compile("^(0?[0-9]|[12][0-9]|3[01])/(0?[1-9]|1[0-2])/([0-9]{4}|[0-9]{2})$", Pattern.CASE_INSENSITIVE);
        ZoneId zid = systemDefault();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        formatter = formatter.withZone(zid);

        System.out.println("  ID :" + pf1.get(index).getID());
        System.out.println("  Прізвище: " + pf1.get(index).getSurname());
        System.out.println("  Ім'я: " + pf1.get(index).getName());
        System.out.println("  По батькові: " + pf1.get(index).getMiddle_Name());
        System.out.println("  День народження: " + pf1.get(index).getBirthday().format(formatter));


        if (pf1.get(index).Date_of_Last_Dismissal.isEqual(LocalDate.MIN))
        {
            System.out.println("  Дата ув'язнення: " + pf1.get(index).getDate_of_Last_Imprisonment().format(formatter));
            do
            {
                System.out.print("  Дата звільнення(дд/мм/рррр):"); date = input.next();
                Matcher matcher = pattern.matcher(date);
                boolean matchFound = matcher.find();
                if (matchFound)
                {
                    if ((LocalDate.now(zid).isAfter(LocalDate.parse(date , formatter))||
                            LocalDate.now(zid).isEqual(LocalDate.parse(date , formatter)))&&
                            LocalDate.parse(date , formatter).isAfter(pf1.get(index).getDate_of_Last_Imprisonment()))
                    {
                        errors = true;
                    }

                }
                else
                {
                    date = null;
                    System.out.println("Дата введена на правильно");
                }


            } while (!errors);

            pf1.get(index).Date_of_Last_Dismissal = LocalDate.parse(date , formatter);

            String[] date_arr = new String[pf1.get(index).getDates_of_Convictions().length];
            int i = 0;
            while (i < date_arr.length)
            {
                date_arr[i] = pf1.get(index).Dates_of_Convictions[i];
                i++;
            }
            date_arr[i - 1] = date;
            pf1.get(index).Dates_of_Convictions = date_arr;

        }
        else
        {
            System.out.println("  Дата ост ув'язнення: " + pf1.get(index).getDate_of_Last_Imprisonment().format(formatter));
            System.out.println("  Дата ост звільнення: " + pf1.get(index).getDate_of_Last_Dismissal().format(formatter));
            do
            {
                System.out.print("  Дата ув'язнення (дд/мм/рррр):"); date = input.next();
                Matcher matcher = pattern.matcher(date);
                boolean matchFound = matcher.find();
                if (matchFound)
                {
                    if ((LocalDate.now(zid).isAfter(LocalDate.parse(date , formatter))||
                            LocalDate.now(zid).isEqual(LocalDate.parse(date , formatter)))&&
                            LocalDate.parse(date , formatter).isAfter(pf1.get(index).getDate_of_Last_Dismissal()))
                    {
                        errors = true;
                    }

                }
                else
                {
                    date = null;
                    System.out.println("Дата введена на правильно");
                }


            } while (!errors);

            pf1.get(index).Date_of_Last_Imprisonment = LocalDate.parse(date , formatter);
            String[] date_arr = new String[pf1.get(index).getDates_of_Convictions().length + 2];
            int i = 0;
            while (i < date_arr.length - 2)
            {
                date_arr[i] = pf1.get(index).Dates_of_Convictions[i];
                i++;
            }
            date_arr[i] = date;
            date_arr[i + 1] = "...";
            pf1.get(index).Dates_of_Convictions = date_arr;
            pf1.get(index).Date_of_Last_Dismissal = LocalDate.MIN;

        }

        return pf1;
    }
    public static List<Prisoner>  Delete( List<Prisoner> pf1)
    {
        boolean delete = false;
        do
        {
            System.out.print("  Введіть ID: ");int id = input.nextInt();

            for (Prisoner p : pf1)
            {
                if(p.ID == id)
                {
                    pf1.remove(p);
                    delete = true;
                    break;
                }
            }

            if (delete == false) System.out.println(" Неправильне ID!");

        } while (delete == false);

        return  pf1;
    }

    public static List<Prisoner> Search(List<Prisoner> pf1)
    {
        List<Prisoner> pf2 = new ArrayList<>();
        Pattern pattern = Pattern.compile("(?:^|\s)([АЄИІЇОУЮЯ][а-я]*[ко])", Pattern.CASE_INSENSITIVE);
        ZoneId zid = systemDefault();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");


        for (Prisoner p : pf1)
        {
            if (ChronoUnit.DAYS.between( p.getBirthday() , LocalDate.now())/365 >= 20)
            {
                Matcher matcher = pattern.matcher(p.getSurname());
                if (matcher.find())
                {
                    pf2.add(p);
                }
            }
        }
        return pf2;
    }

}
