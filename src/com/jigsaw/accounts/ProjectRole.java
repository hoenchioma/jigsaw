package com.jigsaw.accounts;

import java.util.Map;

public class ProjectRole {
    private String name;
    private Map<String,Boolean> permissions;

    public void addRole(String name,Boolean hasPermission) {
        permissions.put(name,true);

    }
    public void removeRole(String name) {
        permissions.put(name,false);
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Boolean> getPermissions() {
        return permissions;
    }

}
