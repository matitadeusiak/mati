package zadanie2;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class Database {
	
	private Connection db;
	
	private Statement statement;
	
	private JFrame frame;
	
	private JTable table;
	
	private JScrollPane scrollPane;
	
	private TravelData travelData;
	
	JComboBox<Locale> comboBox;

	public Database(String url, TravelData travelData) {
		
		this.travelData = travelData;
		try {
			Class.forName("org.sqlite.JDBC");
			db = DriverManager.getConnection(url);
			statement = db.createStatement();
			insertAllTravelData(travelData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public void create() {
		frame = new JFrame();
		frame.setVisible(false);
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		frame.getContentPane().setLayout(gridBagLayout);
		
		comboBox = new JComboBox();
		comboBox.addItem(null);
		comboBox.addItem(new Locale("pl"));
		comboBox.addItem(new Locale("en"));
		
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.insets = new Insets(0, 0, 5, 0);
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.gridx = 0;
		gbc_comboBox.gridy = 0;
		
		frame.getContentPane().add(comboBox, gbc_comboBox);
		
		scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridheight = 8;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 1;
		frame.getContentPane().add(scrollPane, gbc_scrollPane);
		
		table = new JTable();
		comboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				table.setModel(new DefaultTableModel(
						
						getTableDataFromList(),
					new String[] {"","","","","",""
					}
						
				));
			}
		});
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setColumnHeaderView(table);
		
	}

	public void showGui() {
		frame.setVisible(true);
		
	}
	
	private void insertAllTravelData(TravelData travelData) throws SQLException{
		for (TravelData data : travelData.getTravelsList()){
			
			if(!statement.executeQuery(isTravelDataExistInDb(data.getId())).getBoolean("total")){
				statement.executeUpdate(insertTravelDataQuery(data));
			}
		}
	}
	
	private String insertTravelDataQuery(TravelData data){
		StringBuilder insertQuery = new StringBuilder();
		insertQuery.append("INSERT INTO TravelData VALUES (")
			.append(getStringBetweenApostrophe(data.getId(),true))
			.append(getStringBetweenApostrophe(data.getLocale().getLanguage(),true))
			.append(getStringBetweenApostrophe(data.getCountry(),true))
			.append(data.getStartDate().getTime()).append(COMMA)
			.append(data.getEndDate().getTime()).append(COMMA)
			.append(getStringBetweenApostrophe(data.getPlace(),true))
			.append(data.getPrice()).append(COMMA)
			.append(getStringBetweenApostrophe(data.getCurrency(),false)).append(")");
		return insertQuery.toString();
	}
	
	private String getStringBetweenApostrophe(String str, boolean withComma){
		return "'"+str+"'"+ (withComma ? COMMA : "");
	}
	
	private String isTravelDataExistInDb(String id){
		StringBuilder selectQuery = new StringBuilder();
		selectQuery.append("SELECT count(*) as total from TravelData where id='")
				.append(id).append("'");
		
		return selectQuery.toString();
	}
	
	private static final String COMMA = ",";
	
	private Object[][] getTableDataFromList(){
		Object[][] model = new Object[travelData.getTravelsList().size()][6]; 
		for (int j = 0; j < travelData.getTravelsList().size(); j++){	
			
			model[j][0] = travelData.getTravelsList().get(j).getCountryByLocale((Locale)comboBox.getSelectedItem());
			model[j][1] = travelData.getDateInSpecificFormat(TravelData.DATE_FORMAT,travelData.getTravelsList().get(j).getStartDate());
			model[j][2] = travelData.getDateInSpecificFormat(TravelData.DATE_FORMAT,travelData.getTravelsList().get(j).getEndDate());
			model[j][3] = travelData.getTravelsList().get(j).getPlaceByLocale((Locale)comboBox.getSelectedItem());
			model[j][4] = travelData.getTravelsList().get(j).getNumberByLocale((Locale)comboBox.getSelectedItem());
			model[j][5] = travelData.getTravelsList().get(j).getCurrency();
		}
		
		return model;
	}

}
