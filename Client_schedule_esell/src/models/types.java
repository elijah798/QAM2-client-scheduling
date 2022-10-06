package models;

/**
 * Class that creates a type object for use in report section of program
 */
public class types {
    private String type;
    private int count;

    private String month;

    /**
     *
     * @param type
     * @param month
     * @param count
     */
    public types(String type, String month, int count) {
        this.type = type;
        this.count = count;
        this.month = month;
    }

    /**
     *
     * @return
     */
    public String getType() {
        return type;
    }

    /**
     *
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     *
     * @return
     */
    public int getCount() {
        return count;
    }

    /**
     *
     * @param count
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     *
     * @return
     */
    public String getMonth() {
        return month;
    }

    /**
     *
     * @param month
     */
    public void setMonth(String month) {
        this.month = month;
    }





}
