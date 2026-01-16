package com.fdbatt.entity;

public class Permission {
    private Long id;
    private String permCode;
    private String permName;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPermCode() { return permCode; }
    public void setPermCode(String permCode) { this.permCode = permCode; }

    public String getPermName() { return permName; }
    public void setPermName(String permName) { this.permName = permName; }
}
