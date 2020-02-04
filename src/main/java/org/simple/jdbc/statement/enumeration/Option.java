package org.simple.jdbc.statement.enumeration;

public enum Option {
    EQUAL(){
        public String toSQL(String... params) {
            if(params.length == 1) {
                return params[0]+" = ?";
            }else {
                return "";
            }
        }

    },LESSTHAN(){
        public String toSQL(String... params) {
            if(params.length == 1) {
                return params[0]+" < ?";
            }else {
                return "";
            }
        }
    }
    ,MORETHAN(){
        public String toSQL(String... params) {
            if(params.length == 1) {
                return params[0]+" > ?";
            }else {
                return "";
            }
        }
    },LESSANDEQUAL(){
        public String toSQL(String... params) {
            if(params.length == 1) {
                return params[0]+" <= ?";
            }else {
                return "";
            }
        }
    },MOREANDEQUAL(){
        public String toSQL(String... params) {
            if(params.length == 1) {
                return params[0]+" >= ?";
            }else {
                return "";
            }
        }
    },NOTEQUAL(){
        public String toSQL(String... params) {
            if(params.length == 1) {
                return params[0]+" != ?";
            }else {
                return "";
            }
        }
    };
    public String toSQL(String... params) {
        return "";
    }
}
