package zad1;

import javax.swing.table.AbstractTableModel;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;

public class DbTable extends AbstractTableModel {

  private Vector cache;
  private int colCount = 6;
  private final String[] columns = {"kraj", "data_wyjazdu", "data_powrotu", "miejsce", "cena", "waluta"};


  public DbTable() {
    cache = new Vector();
  }

  @Override
  public int getRowCount() {
    return cache.size();
  }

  @Override
  public int getColumnCount() {
    return colCount;
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    return ((String[]) cache.elementAt(rowIndex))[columnIndex];
  }

  @Override
  public String getColumnName(int i) {
    ResourceBundle places = ResourceBundle.getBundle("Columns", Locale.getDefault());
    return places.getString(columns[i]);
  }


  public void setQuery(ResultSet rs) {
    cache = new Vector();
    try {
      ResultSetMetaData meta = rs.getMetaData();
      int colCount = meta.getColumnCount();

      while (rs.next()) {
        ArrayList<String> record = new ArrayList<>();
        for (int i = 1; i <= colCount; i++) {
          record.add(rs.getString(i));
        }
        record.remove(0);
        Travel travel = new Travel(record.toArray(new String[0]));
        cache.addElement(travel.getTravelInfo(Locale.getDefault()));
      }
      fireTableChanged(null);
    } catch (Exception e) {
      cache = new Vector();
      e.printStackTrace();
    }
  }
}
