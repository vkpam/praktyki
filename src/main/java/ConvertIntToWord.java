import java.util.Scanner;

public class ConvertIntToWord {

    private static String jednosci[] = {" ", " jeden", " dwa", " trzy", " cztery", " pięć", " sześć", " siedem", " osiem", " dziewięć"};
    private static String nastki[] = {" dziesięć", " jedenaście", " dwanaście", " trzynaście", " czternaście", " piętnaście", " szesnaście", " siedemnaście", " osiemnaście", " dziewiętnaście"};
    private static String dziesiatki[] = {"", " dziesięć", " dwadzieścia", " trzydzieści", " czterdzieści", " pięćdziesiąt", " szesśćdziesiąt", " siedemdziesiąt", " osiemdziesiąt", " dziewięćdziesiąt"};
    private static String setki[] = {"", " sto", " dwieście", " trzysta", " czterysta", " pięćset", " sześćset", " siedemset", " osiemset", " dziewięćset"};
    private static String x[] = {"", " tys.", " mln.", " mld.", " bln.", " bld."};

    public static void main() {

        String slownie = " ";
        int liczba, koncowka;
        int rzad = 0;
        int j = 0;
        int minus = 0;

        System.out.print("Type number: ");
        Scanner reading = new Scanner(System.in);
        liczba = reading.nextInt();

        if (liczba < 0) {
            minus = 1;
            liczba = -liczba;
        }

        if (liczba == 0) slownie = "zero";

        while (liczba > 0) {
            koncowka = (liczba % 10);
            liczba /= 10;
            if ((j == 0) && (liczba % 100 != 0 || koncowka != 0)) slownie = x[rzad] + slownie;
            if ((j == 0) && (liczba % 10 != 1)) slownie = jednosci[koncowka] + slownie;
            if ((j == 0) && (liczba % 10 == 1)) {
                slownie = nastki[koncowka] + slownie;
                liczba /= 10;
                j += 2;
                continue;
            }
            if (j == 1) slownie = dziesiatki[koncowka] + slownie;
            if (j == 2) {
                slownie = setki[koncowka] + slownie;
                j = -1;
                rzad++;
            }
            j++;
        }

        if (minus == 1) slownie = "minus" + slownie;

        System.out.println("Odpowiedz: " + slownie);
    }
}