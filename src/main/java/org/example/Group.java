package org.example;

import java.util.List;

class Group {
  String name;
  List<Boolean> subjects;
  int maxHours;

  Group(String name, List<Boolean> subjects, int maxHours) {
    this.name = name;
    this.subjects = subjects;
    this.maxHours = maxHours;
  }
}