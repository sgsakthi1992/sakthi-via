package com.practice.sakthi_via.model;

public class Todo {
    /**
     * User Id.
     */
    private int userId;
    /**
     * Todo id.
     */
    private int id;
    /**
     * Todo title.
     */
    private String title;
    /**
     * Todo Status.
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
     * Getter for todo id.
     *
     * @return todo id
     */
    public int getId() {
        return id;
    }

    /**
     * Setter for todo id.
     *
     * @param id todo id
     */
    public void setId(final int id) {
        this.id = id;
    }

    /**
     * Getter for todo title.
     *
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Setter for todo title.
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
