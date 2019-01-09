package zad1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class Database extends JFrame {
  private String url;
  private Connection conn;
  private Statement stmt;
  private TravelData travelData;

  public Database(String url, TravelData travelData) {
    super("Travels");

    this.url = url;
    this.travelData = travelData;
  }

  public void create() {
    dbInit();

    this.createTable();
    this.populateTable();

    dbClose();
  }

  public void showGui() {
    dbInit();

    Locale.setDefault(new Locale("en", "GB"));

    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setSize(640, 480);

    JPanel topPanel = new JPanel();
    JButton enBtn = new JButton("En");    
    JButton plBtn = new JButton("Pl");
    
    enBtn.setPreferredSize(new Dimension(50, 50));
    plBtn.setPreferredSize(new Dimension(50, 50));

    topPanel.add(enBtn, BorderLayout.WEST);
    topPanel.add(plBtn, BorderLayout.EAST);

    DbTable dbTable = new DbTable();
    JTable table = new JTable(dbTable);
    JScrollPane scrollpane = new JScrollPane(table);

    getContentPane().add(topPanel, BorderLayout.NORTH);
    getContentPane().add(scrollpane, BorderLayout.CENTER);

    setVisible(true);

    enBtn.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        dbInit();
        Locale.setDefault(new Locale("en", "GB"));
        dbTable.setQuery(getResultSet());
        dbClose();
      }
    });

    plBtn.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        dbInit();
        Locale.setDefault(new Locale("pl", "PL"));
        dbTable.setQuery(getResultSet());
        dbClose();
      }
    });

    dbTable.setQuery(getResultSet());

    dbClose();
  }

  private void dbInit() {
    try {
      Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
      conn = DriverManager.getConnection("jdbc:derby:" + url);
      stmt = conn.createStatement();
    } catch (SQLException sqlExc) {
      sqlExc.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void dbClose() {
    try {
      stmt.close();
      conn.close();
    } catch (SQLException exc) {
      exc.printStackTrace();
      System.exit(1);
    }
  }

  private void createTable() {
    String dropTable = "drop table OFERTY";

    String createTable = "create table OFERTY (" +
            "  OFFERID integer not null generated by default as identity," +
            "  LOKALIZACJA varchar(20) not null," +
            "  KRAJ varchar(50)," +
            "  DATA_WYJAZDU date," +
            "  DATA_POWROTU date," +
            "  MIEJSCE varchar(255)," +
            "  CENA  real," +
            "  WALUTA varchar(10) not null," +
            "  PRIMARY KEY(OFFERID)" +
            ")";
    try {
      stmt.executeUpdate(dropTable);
      stmt.executeUpdate(createTable);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private void populateTable() {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    String insert = "insert into OFERTY (LOKALIZACJA, KRAJ, DATA_WYJAZDU, DATA_POWROTU, MIEJSCE, CENA, WALUTA) values ";

    for (Travel travel : travelData.getAllTravels()) {
      String travelInfo = "('" + travel.lokalizacja + "', "
              + "'" + travel.kraj + "', "
              + "DATE('" + dateFormat.format(travel.dataWyjazdu) + "'), "
              + "DATE('" + dateFormat.format(travel.dataPowrotu) + "'), "
              + "'" + travel.miejsce + "', "
              + travel.cena + ", "
              + "'" + travel.waluta + "'"
              + "),";
      insert += travelInfo;
    }
    insert = insert.substring(0, insert.length() - 1);

    /*
    String insertRow = "insert into OFERTY (LOKALIZACJA, KRAJ, DATA_WYJAZDU, DATA_POWROTU, MIEJSCE, CENA, WALUTA)" +
            "values" +
            "  ('pl', 'Japonia', DATE('2015-09-01'), DATE('2015-10-01'), 'jezioro', 10000.20, 'PLN')," +
            "  ('pl_PL', 'Włochy', DATE('2015-07-10'), DATE('2015-07-30'), 'morze', 4000.10, 'PLN')," +
            "  ('en_GB', 'United States', DATE('2015-07-10'), DATE('2015-08-30'), 'mountains', 5400.20, 'USD')";
    */
    try {
      stmt.executeUpdate(insert);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private ResultSet getResultSet() {
    ResultSet rs = null;

    String q = "select * from OFERTY";
    try {
      rs = stmt.executeQuery(q);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return rs;
  }
}
