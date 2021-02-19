package com.example.lajusta.model;

public class General {
    public AvailableNode[] activeNodes;
    public boolean canClose;
    public String dateActivePage;
    public String dateDownPage;
    public String deletedAt;
    public int id;
    public User user;

    public AvailableNode[] getActiveNodes() {
        return activeNodes;
    }

    public void setActiveNodes(AvailableNode[] activeNodes) {
        this.activeNodes = activeNodes;
    }

    public boolean isCanClose() {
        return canClose;
    }

    public void setCanClose(boolean canClose) {
        this.canClose = canClose;
    }

    public String getDateActivePage() {
        return dateActivePage;
    }

    public void setDateActivePage(String dateActivePage) {
        this.dateActivePage = dateActivePage;
    }

    public String getDateDownPage() {
        return dateDownPage;
    }

    public void setDateDownPage(String dateDownPage) {
        this.dateDownPage = dateDownPage;
    }

    public String getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
