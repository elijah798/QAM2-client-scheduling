package models;

/**
 *
 */
public class countries {
    private int country_ID;
    private String country;


    /**
     *
     * @param country_ID
     * @param country
     */
    public countries(int country_ID, String country) {
        this.country_ID = country_ID;
        this.country = country;
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

    /**
     *
     * @return
     */
    public String getCountry() {
        return country;
    }

    /**
     *
     * @param country
     */
    public void setCountry(String country) {
        this.country = country;
    }


}
