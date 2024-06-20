package com.k.app.utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BeanGenerator {

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/sample_library?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai";
        String username = "root";
        String password = "root";

        String tableName = "ais";
        String packageName = "com.k.app.utils";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            DatabaseMetaData metaData = connection.getMetaData();

            ResultSet columnsResultSet = metaData.getColumns(null, null, tableName, null);

            List<ColumnDefinition> columnDefinitions = new ArrayList<>();

            while (columnsResultSet.next()) {
                String columnName = columnsResultSet.getString("COLUMN_NAME");
                String columnType = columnsResultSet.getString("TYPE_NAME");

                ColumnDefinition columnDefinition = new ColumnDefinition(columnName, columnType);
                columnDefinitions.add(columnDefinition);
            }

            generateBeanClass(tableName, packageName, columnDefinitions);

            System.out.println("Bean class generated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void generateBeanClass(String tableName, String packageName, List<ColumnDefinition> columnDefinitions) {
        StringBuilder classContent = new StringBuilder();

        classContent.append("package ").append(packageName).append(";\n\n");
        classContent.append("public class ").append(getClassNameFromTableName(tableName)).append(" {\n\n");

        for (ColumnDefinition columnDefinition : columnDefinitions) {
            String columnName = columnDefinition.getColumnName();
            String fieldName = getFieldNameFromColumnName(columnName);
            String fieldType = getFieldTypeFromColumnType(columnDefinition.getColumnType());

            classContent.append("\tprivate ").append(fieldType).append(" ").append(fieldName).append(";\n");
        }

        classContent.append("\n");

        for (ColumnDefinition columnDefinition : columnDefinitions) {
            String columnName = columnDefinition.getColumnName();
            String fieldName = getFieldNameFromColumnName(columnName);
            String fieldType = getFieldTypeFromColumnType(columnDefinition.getColumnType());

            String capitalizedFieldName = capitalize(fieldName);

            classContent.append("\tpublic ").append(fieldType).append(" get").append(capitalizedFieldName).append("() {\n");
            classContent.append("\t\treturn ").append(fieldName).append(";\n");
            classContent.append("\t}\n\n");

            classContent.append("\tpublic void set").append(capitalizedFieldName).append("(").append(fieldType).append(" ").append(fieldName).append(") {\n");
            classContent.append("\t\tthis.").append(fieldName).append(" = ").append(fieldName).append(";\n");
            classContent.append("\t}\n\n");
        }

        classContent.append("}");

        System.out.println(classContent.toString());
    }

    private static String getClassNameFromTableName(String tableName) {
        String[] parts = tableName.split("_");
        StringBuilder className = new StringBuilder();

        for (String part : parts) {
            className.append(capitalize(part));
        }

        return className.toString();
    }

    private static String getFieldNameFromColumnName(String columnName) {
        String[] parts = columnName.split("_");
        StringBuilder fieldName = new StringBuilder();

        fieldName.append(parts[0].toLowerCase());

        for (int i = 1; i < parts.length; i++) {
            fieldName.append(capitalize(parts[i]));
        }

        return fieldName.toString();
    }

    private static String getFieldTypeFromColumnType(String columnType) {
        if (columnType.equalsIgnoreCase("int")) {
            return "int";
        } else if (columnType.equalsIgnoreCase("varchar")) {
            return "String";
        } else if (columnType.equalsIgnoreCase("datetime")) {
            return "java.util.Date"; // You may need to import the java.util.Date class
        }

        return "Object";
    }

    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }

        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private static class ColumnDefinition {
        private String columnName;
        private String columnType;

        public ColumnDefinition(String columnName, String columnType) {
            this.columnName = columnName;
            this.columnType = columnType;
        }

        public String getColumnName() {
            return columnName;
        }

        public String getColumnType() {
            return columnType;
        }
    }
}