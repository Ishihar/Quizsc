package com.example.a171y005.quizsc;

public class GetCategoryName {
    public String getTable(String tablename){
        switch (tablename) {
            case "ビジネス":
                tablename = "quiz_table_B";
                break;
            case "生活":
                tablename = "quiz_table_L";
                break;
            case "動物":
                tablename = "quiz_table_A";
                break;
            case "宇宙":
                tablename = "quiz_table_C";
                break;
            case "食べ物":
                tablename = "quiz_table_F";
                break;
            case " ":
                tablename = "quiz_table_IT";
                break;
        }
        return tablename;
    }
    public String getCategoryName(String catename){
        switch (catename) {
            case "quiz_table_B":
                catename = "ビジネス";
                break;
            case "quiz_table_L":
                catename = "生活";
                break;
            case "quiz_table_A":
                catename = "動物";
                break;
            case "quiz_table_C":
                catename = "宇宙";
                break;
            case "quiz_table_F":
                catename = "食べ物";
                break;
            case "quiz_table_IT":
                catename = "IT";
                break;
        }
        return catename;
    }
}
