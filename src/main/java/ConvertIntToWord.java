public class ConvertIntToWord {

    private static String jednosci[] = {" ", " jeden", " dwa", " trzy", " cztery", " pięć", " sześć", " siedem", " osiem", " dziewięć"};
    private static String nastki[] = {" dziesięć", " jedynaście", " dwanaście", " trzynaście", " czternaście", " piętnaście", " szesnaście", " siedemnaście", " osiemnaście", " dziewiętnaście"};
    private static String dziesiatki[] = {"", " dziesięć", " dwadzieścia", " trzydzieści", " czterdzieści", " pięćdziesiąt", " sześćdziesiąt", " siedemdziesiąt", " osiemdziesiąt", " dziewięćdziesiąt"};
    private static String setki[] = {"", " sto", " dwieście", " trzysta", " czterysta", " pięćset", " sześćset", " siedemset", " osiemset", " dziewięćset"};
    private static String x[] = {"", " tys.", " mln.", " mld.", " bln.", " bld."};

    public static String convert(int liczba) {

        String slownie = " ";
        int koncowka;
        int rzad = 0;
        int j = 0;
        boolean minus = false;

        if (liczba < 0) {
            minus = true;
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

        if (minus) slownie = "minus" + slownie;

        return slownie.trim();
    }
}