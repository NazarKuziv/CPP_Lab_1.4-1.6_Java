import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    static Scanner input = new Scanner(System. in);
    public static void main(String[] args){

        List<Prisoner> prisoners = new ArrayList<>();
//        prisoners = Utilities.Read_File();
//        Utilities.PrintTable(prisoners);
        boolean exit = false;
        do
        {
            int action;

            System.out.println("  [1] - Десеріалізувати");
            System.out.println("  [2] - Cеріалізувати");
            System.out.println("  [3] - Додати");
            System.out.println("  [4] - Редагувати");
            System.out.println("  [5] - Видалити");
            System.out.println("  [6] - Сортувати");
            System.out.println("  [7] - Знайти");
            System.out.println("  [0] - Завершити роботу");
            System.out.print("  Виберіть дію: "); action = input.nextInt();
            switch (action) {
                case 1 -> {
                    prisoners = Utilities.Deserialize();
                    if(prisoners ==null)
                        break;

                    Utilities.PrintTable(prisoners);
                }
                case 2 -> {
                    if(prisoners ==null)
                        break;
                    Utilities.Serialize(prisoners);
                }
                case 3 -> {
                    if(prisoners ==null)
                        break;
                    Utilities.Add(prisoners);
                    Utilities.PrintTable(prisoners);
                }
                case 4 -> {
                    if(prisoners ==null)
                        break;
                    Utilities.Edit(prisoners);
                    Utilities.PrintTable(prisoners);
                }
                case 5 -> {
                    if(prisoners ==null)
                        break;
                    Utilities.Delete(prisoners);
                    Utilities.PrintTable(prisoners);
                }
                case 6 -> {
                    System.out.println("  Сортувати за : ");
                    System.out.println("  [1] - Прізвищем");
                    System.out.println("  [2] - Датаою народження");
                    System.out.println("  [3] - Датаою ув'язнення");
                    System.out.println("  [4] - Дата ост звільнення");
                    System.out.print("  Виберіть дію: ");int sort_by = input.nextInt();
                    switch (sort_by) {
                        case 1 -> {
                            if(prisoners ==null)
                                break;
                            System.out.print("\n Відсортовано за Прізвищем\n");
                            Utilities.Sort_by(prisoners,1);
                            Utilities.PrintTable(prisoners);
                        }
                        case 2 -> {
                            if(prisoners ==null)
                                break;
                            System.out.print("\n Відсортовано за Датаою народження\n");
                            Utilities.Sort_by(prisoners,2);
                            Utilities.PrintTable(prisoners);
                        }
                        case 3 -> {
                            if(prisoners ==null)
                                break;
                            System.out.print("\n Відсортовано за Датаою ув'язнення\n");
                            Utilities.Sort_by(prisoners,3);
                            Utilities.PrintTable(prisoners);
                        }
                        case 4 -> {
                            if(prisoners ==null)
                                break;
                            System.out.print("\n Відсортовано за Дата ост звільнення\n");
                            Utilities.Sort_by(prisoners,4);
                            Utilities.PrintTable(prisoners);
                        }
                        default -> System.out.println("\nНевідома дія!\n");
                    }

                }
                case 7 -> {
                    System.out.println("Знайдено всіх ув’язнених не молодше 20 років з прізвищем, що починається з голосної  та містить сполучення 'ко'");
                    Utilities.PrintTable(Utilities.Search(prisoners));
                }
                case 0 -> exit = true;
                default -> System.out.println("\nНевідома дія!");
            }

        } while (!exit);
    }

}