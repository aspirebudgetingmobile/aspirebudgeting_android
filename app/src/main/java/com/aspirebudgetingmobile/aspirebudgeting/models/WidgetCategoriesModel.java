package com.aspirebudgetingmobile.aspirebudgeting.models;



public class WidgetCategoriesModel {
    String categoryName;
    String budgetedAmount;
    String spentAmount;
    String availableAmount;
    boolean isSelected;


    public WidgetCategoriesModel(String categoryName){
        this.categoryName = categoryName;
        this.isSelected = false;
    }

    public WidgetCategoriesModel(String categoryName, String budgetedAmount, String spentAmount, String availableAmount) {
        this.categoryName = categoryName;
        this.budgetedAmount = budgetedAmount;
        this.spentAmount = spentAmount;
        this.availableAmount = availableAmount;
    }

    public void setCategoryName(String name) {
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public boolean getSelected() {
        return isSelected;
    }

    public void setSelected(boolean isChecked) {
        this.isSelected = isChecked;
    }public String getBudgetedAmount() {
        return budgetedAmount;
    }

    public void setBudgetedAmount(String budgetedAmount) {
        this.budgetedAmount = budgetedAmount;
    }

    public String getSpentAmount() {
        return spentAmount;
    }

    public void setSpentAmount(String spentAmount) {
        this.spentAmount = spentAmount;
    }

    public String getAvailableAmount() {
        return availableAmount;
    }

    public void setAvailableAmount(String availableAmount) {
        this.availableAmount = availableAmount;
    }


}
