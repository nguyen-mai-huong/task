package com.ufinity.task.utils;

public enum RoleType {
  SYS_ADMIN("SYS_ADMIN"),
  ADMIN("ADMIN"), // User admin
  USER("USER");

  private final String displayName;

  RoleType(final String displayName) {
    this.displayName = displayName;
  }

  public String getDisplayName() {
    return displayName;
  }

}
