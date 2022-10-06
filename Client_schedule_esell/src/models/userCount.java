package models;

/**
 *
 */
public class userCount {
    private String user;
    private int count;

    /**
     *
     * @param user
     * @param count
     */
    public userCount(String user, int count) {
        this.user = user;
        this.count = count;
    }

    /**
     *
     * @return
     */
    public String getUser() {
        return user;
    }

    /**
     *
     * @param user
     */
    public void setUser(String user) {
        this.user = user;
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
}

