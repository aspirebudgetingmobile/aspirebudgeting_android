package com.aspirebudgetingmobile.aspirebudgeting.models;

import java.util.List;

public class DashboardCardsModel {
    String name;
    List<String> categoryName;
    List<String> budgetedAmount;
    List<String> spentAmount;
    List<String> availableAmount;

    public DashboardCardsModel(String name, List<String> categoryName, List<String> budgetedAmount, List<String> spentAmount, List<String> availableAmount) {
        this.name = name;
        this.categoryName = categoryName;
        this.budgetedAmount = budgetedAmount;
        this.spentAmount = spentAmount;
        this.availableAmount = availableAmount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(List<String> categoryName) {
        this.categoryName = categoryName;
    }

    public List<String> getBudgetedAmount() {
        return budgetedAmount;
    }

    public void setBudgetedAmount(List<String> budgetedAmount) {
        this.budgetedAmount = budgetedAmount;
    }

    public List<String> getSpentAmount() {
        return spentAmount;
    }

    public void setSpentAmount(List<String> spentAmount) {
        this.spentAmount = spentAmount;
    }

    public List<String> getAvailableAmount() {
        return availableAmount;
    }

    public void setAvailableAmount(List<String> availableAmount) {
        this.availableAmount = availableAmount;
    }
}
