package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Schedule {
  List<Timeslot> timetable;

  Schedule() {
    timetable = new ArrayList<>();
    for (int i = 0; i < CSPSolver.numTimeslots; i++) {
      timetable.add(new Timeslot());
    }
  }

  Schedule(Schedule other) {
    this.timetable = new ArrayList<>();
    for (Timeslot timeslot : other.timetable) {
      this.timetable.add(new Timeslot(timeslot));
    }
  }
}
