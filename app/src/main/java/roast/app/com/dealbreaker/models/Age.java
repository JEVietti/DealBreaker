package roast.app.com.dealbreaker.models;

public class Age
{
    private int days, months, years;

    // Default Constructor
    private Age() {}

    //Constructor
    public Age(int days, int months, int years)
    {
        this.days = days;
        this.months = months;
        this.years = years;
    }

    public int getDays() { return this.days;}
    public int getMonths() {return this.months;}
    public int getYears() {return this.years;}

    public String convertToString()
    {
        return days + " Days" + months + " Months" + years + " Years";
    }
}
