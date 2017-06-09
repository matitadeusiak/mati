package zadanie2;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;

public class TravelData {

	public TravelData(File dataDir) {
		
		if (dataDir != null && dataDir.listFiles() != null && dataDir.listFiles().length > 0) {
			for (File file : dataDir.listFiles()) {
				travelsList = new ArrayList<TravelData>();
				addTravelDataToList(file);
			}
		}
	}
	
	public TravelData() {

	}

	public List<String> getOffersDescriptionsList(String locale, String dateFormat) {
		
		List<String> list = new ArrayList<>();
		for (TravelData data : travelsList){
			list.add(data.toString(locale,dateFormat));
		}
		return list;
	}
	
	public Locale getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = new Locale(locale.substring(0, 2));
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(String price) {
		price = price.contains(".") ? price.replaceAll(",", "") : price.replaceAll(",", "\\.");
		this.price = Double.valueOf(price);
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public List<TravelData> getTravelsList() {
		return travelsList;
	}

	public void setTravelsList(List<TravelData> travelsList) {
		this.travelsList = travelsList;
	}
	
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = getDateFromString(startDate,DATE_FORMAT);
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = getDateFromString(endDate,DATE_FORMAT);
	}
	
	public String getId() {
		return id;
	}

	public void setId(String country, String startDate, String endDate) {
		this.id = (country+startDate+endDate).replaceAll("\\s+", "");
	}

	public String toString(String locale, String format) {
		Locale loc = new Locale(locale.substring(0, 2));
		return getCountryByLocale(loc) + " " + getDateInSpecificFormat(format,startDate) + " "
				+ getDateInSpecificFormat(format,endDate) + " " + getPlaceByLocale(loc) + " " + getNumberByLocale(loc) + " " + currency;
	}
	
	public String getNumberByLocale(Locale loc){
		return NumberFormat.getInstance(loc).format(price);
	}

	private Date getDateFromString(String date, String format){
		try {
			return new SimpleDateFormat(format).parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public String getDateInSpecificFormat(String format, Date date){
		return new SimpleDateFormat(format).format(date);
	}

	private void addTravelDataToList(File file){
		
		if (file.isFile()) {
			Scanner scanner = null;
			try {
				scanner = new Scanner(file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			while (scanner.hasNextLine()) {
				TravelData travelData = getTravelDataFromArray(scanner.nextLine().split("\t"));
				if (travelData != null){
					travelsList.add(travelData);
				}
			}
			scanner.close();
		}
	}
	
	private TravelData getTravelDataFromArray(String[] tab){
		
		TravelData travelData = null;
		
		if (tab != null && tab.length == 7) {
			travelData = new TravelData();
			travelData.setLocale(tab[0]);
			travelData.setCountry(tab[1]);
			travelData.setStartDate(tab[2]);
			travelData.setEndDate(tab[3]);
			travelData.setPlace(tab[4]);
			travelData.setPrice(tab[5]);
			travelData.setCurrency(tab[6]);
			travelData.setId(tab[1],tab[2],tab[3]);
		} else {
			System.out.print("B³¹d danych w pliku Ÿród³owym!");
		}
		return travelData;
	}
	
	public String getCountryByLocale(Locale locale){
		return ResourceBundle.getBundle("locale", locale).getString(getId()+".country");
	}
	
	public String getPlaceByLocale(Locale locale){
		return ResourceBundle.getBundle("locale", locale).getString(getId()+".place");
	}
	
	public static final String DATE_FORMAT = "yyyy-MM-dd";
	
	private String id;

	private Locale locale;
	
	private String country;
	
	private Date startDate;
	
	private Date endDate;
	
	private String place;
	
	private Double price;
	
	private String currency;
	
	private List<TravelData> travelsList;

}
