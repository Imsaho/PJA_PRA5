package zad1;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

public class Travel {
  protected String lokalizacja;
  protected String kraj;
  protected Date dataWyjazdu;
  protected Date dataPowrotu;
  protected String miejsce;
  protected double cena;
  protected String waluta;
  protected Locale inputLocale;

  public Travel (String[] travelData)
  {
    lokalizacja = travelData[0].replaceAll("[\\uFEFF\\n\\t ]", "");

    kraj = travelData[1];
    miejsce = travelData[4];
    waluta = travelData[6];

    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    try {
      dataWyjazdu = formatter.parse(travelData[2]);
      dataPowrotu = formatter.parse(travelData[3]);
    } catch (ParseException e) {
      e.printStackTrace();
    }

    String[] locs = lokalizacja.split("_");
    inputLocale = new Locale(locs[0], locs[1]);
    NumberFormat nf = NumberFormat.getInstance(inputLocale);
    try {
      cena = nf.parse(travelData[5]).doubleValue();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public String toString(Locale outLocale, String dateFormatString) {
    String krajLoc = "";
    for (Locale l : Locale.getAvailableLocales()) {
      if (l.getDisplayCountry(inputLocale).equals(kraj)) {
        krajLoc = l.getDisplayCountry(outLocale);
        break;
      }
    }

    ResourceBundle places = ResourceBundle.getBundle("Places", outLocale);

    SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatString);
    NumberFormat nf = NumberFormat.getInstance(outLocale);
    return krajLoc + " "
            + dateFormat.format(dataWyjazdu) + " "
            + dateFormat.format(dataPowrotu) + " "
            + places.getString(miejsce) + " "
            + nf.format(cena) + " "
            + waluta;
  }

  public String[] getTravelInfo(Locale outLocale)
  {
    String krajLoc = "";
    for (Locale l : Locale.getAvailableLocales()) {
      if (l.getDisplayCountry(inputLocale).equals(kraj)) {
        krajLoc = l.getDisplayCountry(outLocale);
        break;
      }
    }

    ResourceBundle places = ResourceBundle.getBundle("Places", outLocale);

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    NumberFormat nf = NumberFormat.getInstance(outLocale);

    return new String[]{
            krajLoc,
            dateFormat.format(dataWyjazdu),
            dateFormat.format(dataPowrotu),
            places.getString(miejsce),
            nf.format(cena),
            waluta
    };
  }
}
