package com.smartearth.order.util;

import com.smartearth.order.GlobalExceptionHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.regex.Pattern;

public class CheckUtil {
    private static final String checkConditionSql = "where|union|limit|exec|insert|select|drop|grant|alter|delete|update|count|chr|mid|master|truncate|char|declare|;|--|,|\"";
    private static final String checkColumnSql = "where|union|limit|exec|insert|select|drop|grant|alter|delete|update|count|chr|mid|master|truncate|char|declare|;|--|\"";

    private static final String checkGeneralSql = "union|exec|insert|drop|grant|alter|delete|update|chr|mid|master|truncate|char|declare|;|--|\"";

    private static boolean hasNumeric(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    private static void checkSql(String sql, String check) {
        String[] sqlInjectionArr = check.split("\\|");
        for (String sqlInjection : sqlInjectionArr) {
            if (sql.contains(sqlInjection)) {
                throw new RuntimeException(GlobalExceptionHandler.ILLEGAL_INPUT);
            }
        }
    }

    public static void checkTable(String tables) {
        String[] tableArr = tables.split(",");
        for (String table : tableArr) {
            if (!DataInitUtil.tableNameList.contains(table)) {
                throw new RuntimeException(GlobalExceptionHandler.ILLEGAL_INPUT);
            }
        }
    }


    private static final String expression_symbol = "=|>|<|>=|<=|!=";

    public static void checkCondition(String condition) {
        if(condition!=null){
            CheckUtil.checkSql(condition, CheckUtil.checkConditionSql);
            String[] conditionArr = condition.split(" ");
            String[] symbolArr = expression_symbol.split("\\|");
            for (String conditionStr : conditionArr) {
                for (String symbol : symbolArr) {
                    if (conditionStr.contains(symbol)) {
                        String[] conditionItemArr = conditionStr.split(expression_symbol);
                        if (conditionItemArr[0].contains("'")) {
                            throw new RuntimeException(GlobalExceptionHandler.ILLEGAL_INPUT);
                        }
                        if (CheckUtil.hasNumeric(conditionItemArr[0])) {
                            throw new RuntimeException(GlobalExceptionHandler.ILLEGAL_INPUT);
                        }
                    }
                }
            }
        }
    }

    public static void checkGeneral(String sql) {
        if(sql!=null){
            CheckUtil.checkSql(sql, CheckUtil.checkGeneralSql);
        }
    }


    public static void checkColumn(String column) {
        if (column != null) {
            CheckUtil.checkSql(column, CheckUtil.checkColumnSql);
        }
    }

    /**
     * 使用PreparedStatement处理sal注入
     */
    public  static void checkSqlInjection(Connection conn, String sql) {
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.execute();
        } catch (Exception e) {
            throw new RuntimeException(GlobalExceptionHandler.ILLEGAL_INPUT);
        }
    }
}
