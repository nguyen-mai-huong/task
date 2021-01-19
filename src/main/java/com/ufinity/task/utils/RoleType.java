package com.ufinity.task.utils;

public enum RoleType {
  ADMIN("ADMIN"),
  USER("USER");

  private final String displayName;

  RoleType(final String displayName) {
    this.displayName = displayName;
  }

  public String getDisplayName() {
    return displayName;
  }

}
