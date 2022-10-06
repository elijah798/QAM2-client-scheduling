package models;

/**
 *
 */
public class first_level_divisions {
    private int division_ID;
    private String division;
    private int country_ID;

    /**
     *
     * @param division_ID
     * @param division
     * @param country_ID
     */
    public first_level_divisions(int division_ID, String division, int country_ID) {
        this.division_ID = division_ID;
        this.division = division;
        this.country_ID = country_ID;
    }

    /**
     *
     * @return
     */
    public int getDivision_ID() {
        return division_ID;
    }

    /**
     *
     * @param division_ID
     */
    public void setDivision_ID(int division_ID) {
        this.division_ID = division_ID;
    }

    /**
     *
     * @return
     */
    public String getDivision() {
        return division;
    }

    /**
     *
     * @param division
     */
    public void setDivision(String division) {
        this.division = division;
    }


    /**
     *
     * @return
     */
    public int getCountry_ID() {
        return country_ID;
    }

    /**
     *
     * @param country_ID
     */
    public void setCountry_ID(int country_ID) {
        this.country_ID = country_ID;
    }


}
