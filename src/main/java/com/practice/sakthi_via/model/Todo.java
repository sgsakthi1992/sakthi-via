package com.practice.sakthi_via.model;

public class Todo {
    /**
     * User Id.
     */
    private int userId;
    /**
     * to-do id.
     */
    private int id;
    /**
     * to-do title.
     */
    private String title;
    /**
     * to-do Status.
     */
    private String completed;

    /**
     * Getter for user id.
     *
     * @return user id
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Setter for user id.
     *
     * @param userId user id
     */
    public void setUserId(final int userId) {
        this.userId = userId;
    }

    /**
     * Getter for to-do id.
     *
     * @return to-do id
     */
    public int getId() {
        return id;
    }

    /**
     * Setter for to-do id.
     *
     * @param id to-do id
     */
    public void setId(final int id) {
        this.id = id;
    }

    /**
     * Getter for to-do title.
     *
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Setter for to-do title.
     *
     * @param title title
     */
    public void setTitle(final String title) {
        this.title = title;
    }

    /**
     * Getter for completed status.
     *
     * @return Completed status
     */
    public String getCompleted() {
        return completed;
    }

    /**
     * Setter for Completed status.
     *
     * @param completed status
     */
    public void setCompleted(final String completed) {
        this.completed = completed;
    }

    /**
     * Overridden toString method.
     *
     * @return String
     */
    @Override
    public String toString() {
        return "Todo{"
                + "userId=" + userId
                + ", id=" + id
                + ", title='" + title + '\''
                + ", completed='" + completed + '\''
                + '}';
    }
}
