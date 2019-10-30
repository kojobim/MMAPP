package com.bim.commons.enums;

public enum Month {
	January("01"),
	February("02"),
	March("03"),
	April("04"),
	May("05"),
	June("06"),
	July("07"),
	August("08"),
	September("09"),
	Octuber("10"),
	November("11"),
	December("12");
	
	private final String month;
	
	public static String validateMonth(String mes) {
		if(mes == null || mes.isEmpty())
			return null;
		
        switch(mes) {
            case "01":
                return Month.January.getMonth();
            case "02":
                return Month.February.getMonth();
            case "03":
                return Month.March.getMonth();
            case "04":
                return Month.April.getMonth();
            case "05":
                return Month.May.getMonth();
            case "06":
                return Month.June.getMonth();
            case "07":
                return Month.July.getMonth();
            case "08":
                return Month.August.getMonth();
            case "09":
                return Month.September.getMonth();
            case "10":
                return Month.Octuber.getMonth();
            case "11":
                return Month.November.getMonth();
            case "12":
                return Month.December.getMonth();

            default:
                return null;
        }
	}
	
	private Month(String month) {
		this.month = month;
	}
	
	public String getMonth() {
		return this.month;
	}
}
