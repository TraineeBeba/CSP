package org.example;

import java.util.List;

class Teacher {
  String name;
  List<Boolean> subjectKnowledge;
  int maxHours;

  Teacher(String name, List<Boolean> subjectKnowledge, int maxHours) {
    this.name = name;
    this.subjectKnowledge = subjectKnowledge;
    this.maxHours = maxHours;
  }
}