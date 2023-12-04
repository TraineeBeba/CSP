package org.example;

class Timeslot {
  int subjectIdx;
  int teacherIdx;
  int groupIdx;

  public Timeslot() {
    this.subjectIdx = -1;
    this.teacherIdx = -1;
    this.groupIdx = -1;
  }

  Timeslot(Timeslot other) {
    this.subjectIdx = other.subjectIdx;
    this.teacherIdx = other.teacherIdx;
    this.groupIdx = other.groupIdx;
  }
}