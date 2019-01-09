package zad1;

import java.io.File;
import java.util.*;

public class TravelData {
  private ArrayList<File> files = new ArrayList<>();
  private ArrayList<Travel> travels = new ArrayList<>();

  public TravelData(File dataFile)
  {
    this.listFilesForFolder(dataFile);
    for (File file : files) {
      try {
        Scanner sc = new Scanner(file);
        while (sc.hasNextLine()) {
          String line = sc.nextLine().trim();
          String[] listStr = line.split("\\t+");
          Travel travel = new Travel(listStr);
          travels.add(travel);
        }
        sc.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  /*
  Japonia 2015-09-01 2015-10-01 jezioro 10 000,2 PLN
  Włochy 2015-07-10 2015-07-30 morze 4 000,1 PLN
  Stany Zjednoczone Ameryki 2015-07-10 2015-08-30 góry 5 400,2 USD
  Japan 2015-09-01 2015-10-01 lake 10,000.2 PLN
  Italy 2015-07-10 2015-07-30 sea 4,000.1 PLN
  United States 2015-07-10 2015-08-30 mountains 5,400.2 USD
 */
  public List<String> getOffersDescriptionsList(String locale, String dateFormat)
  {
    String[] locs = locale.split("_");
    Locale loc = new Locale(locs[0], locs[1]);
    List<String> offers = new ArrayList<>();
    for (Travel t : travels) {
      offers.add(t.toString(loc, dateFormat));
    }
    return offers;
  }

  public List<Travel> getAllTravels()
  {
    return travels;
  }

  private void listFilesForFolder(File folder)
  {
    files.addAll(Arrays.asList(folder.listFiles()));
  }
}
