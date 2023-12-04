package org.example;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;



class CSPSolver {
  static List<String> subjects = List.of(
      "Discrete Math", "Physics", "Programming", "Chemistry", "Philosophy", "Calculus"
  );
  static List<Teacher> teachers = List.of(
      new Teacher("Tom", List.of(false, true, false, false, false, true), 7),
      new Teacher("Bob", List.of(false, false, true, true, true, false), 8),
      new Teacher("Mary", List.of(true, false, false, false, true, true), 3)
  );
  static List<Group> groups = List.of(
      new Group("A", List.of(false, false, false, false, true, false), 7),
      new Group("B", List.of(false, true, false, false, false, true), 5),
      new Group("C", List.of(true, false, false, false, false, false), 9),
      new Group("D", List.of(false, false, true, false, false, false), 2),
      new Group("E", List.of(false, false, false, true, false, false), 4)
  );
  static int numTeachers = teachers.size();
  static int numGroups = groups.size();
  static int numSubjects = subjects.size();
  static int numTimeslots = 15;

  List<List<Integer>> domains;
  Schedule bestSchedule;
  Schedule schedule;

  CSPSolver() {
    initializeDomains();
    schedule = new Schedule();
    bestSchedule = new Schedule();
  }

  void initializeDomains() {
    domains = new ArrayList<>();

    for (int timeslotIdx = 0; timeslotIdx < numTimeslots; ++timeslotIdx) {
      domains.add(generateAssignment(timeslotIdx));
    }
  }

  List<Integer> generateAssignment(int timeslotIdx) {
    List<Integer> validAssignments = new ArrayList<>();

    for (int subjectIdx = 0; subjectIdx < numSubjects; ++subjectIdx) {
      for (int teacherIdx = 0; teacherIdx < numTeachers; ++teacherIdx) {
        for (int groupIdx = 0; groupIdx < numGroups; ++groupIdx) {
          int assignment = subjectIdx + numSubjects * teacherIdx + numSubjects * numTeachers * groupIdx;
          validAssignments.add(assignment);
        }
      }
    }

    return validAssignments;
  }

  boolean isValidAssignment(int timeslotIdx, int subjectIdx, int teacherIdx, int groupIdx) {
    if (!teachers.get(teacherIdx).subjectKnowledge.get(subjectIdx)) {
      return false;
    }

    if (!groups.get(groupIdx).subjects.get(subjectIdx)) {
      return false;
    }

    return true;
  }

  boolean noTeacherOverload(Schedule schedule) {
    int[] teacherHours = new int[numTeachers];
    for (Timeslot timeslot : schedule.timetable) {
      if (timeslot.teacherIdx != -1) {
        teacherHours[timeslot.teacherIdx]++;
        if (teacherHours[timeslot.teacherIdx] > teachers.get(timeslot.teacherIdx).maxHours) {
          return false;
        }
      }
    }
    return true;
  }

  boolean noGroupOverload(Schedule schedule) {
    int[] groupHours = new int[numGroups];
    for (Timeslot timeslot : schedule.timetable) {
      if (timeslot.groupIdx != -1) {
        groupHours[timeslot.groupIdx]++;
        if (groupHours[timeslot.groupIdx] > groups.get(timeslot.groupIdx).maxHours) {
          return false;
        }
      }
    }
    return true;
  }

  void backtracking(int timeslotIdx) {
    if (timeslotIdx == numTimeslots) {
      // Create a deep copy of the current schedule and assign it to bestSchedule
      bestSchedule = new Schedule(schedule);
      return;
    }

    List<Integer> variableOrder = getVariableOrder(timeslotIdx);

    for (int assignment : variableOrder) {
      int subjectIdx = assignment % numSubjects;
      int teacherIdx = (assignment / numSubjects) % numTeachers;
      int groupIdx = assignment / (numSubjects * numTeachers);

      Timeslot timeslot = schedule.timetable.get(timeslotIdx);
      timeslot.subjectIdx = subjectIdx;
      timeslot.teacherIdx = teacherIdx;
      timeslot.groupIdx = groupIdx;

      if (constraintsSatisfied(schedule) && isValidAssignment(timeslotIdx, subjectIdx, teacherIdx, groupIdx)) {
        backtracking(timeslotIdx + 1);
      }
    }
  }

  List<Integer> getVariableOrder(int timeslotIdx) {
    List<Integer> values = new ArrayList<>(domains.get(timeslotIdx));
    values.sort(Comparator.comparingInt(lhs -> countTeachersForSubject(lhs % numSubjects)));
    return values;
  }

  int countTeachersForSubject(int subjectIdx) {
    int count = 0;
    for (Teacher teacher : teachers) {
      if (teacher.subjectKnowledge.get(subjectIdx)) {
        ++count;
      }
    }
    return count;
  }

  boolean constraintsSatisfied(Schedule schedule) {
    if (noTeacherOverload(schedule) && noGroupOverload(schedule)) {
      return true;
    }
    return false;
  }

  void solve() {
    backtracking(0);
  }

  void printSchedule(Schedule schedule) {
    System.out.println("Schedule:");
    for (int timeslotIdx = 0; timeslotIdx < schedule.timetable.size(); ++timeslotIdx) {
      Timeslot timeslot = schedule.timetable.get(timeslotIdx);

      String subject = timeslot.subjectIdx >= 0 ? subjects.get(timeslot.subjectIdx) : "None";
      String teacher = timeslot.teacherIdx >= 0 ? teachers.get(timeslot.teacherIdx).name : "None";
      String group = timeslot.groupIdx >= 0 ? groups.get(timeslot.groupIdx).name : "None";

      System.out.println("Timeslot " + (timeslotIdx + 1) + ": " +
          "Subject: " + subject + ", " +
          "Teacher: " + teacher + ", " +
          "Group: " + group);
    }
  }

  Schedule getBestSchedule() {
    return bestSchedule;
  }

  public static void main(String[] args) {
    CSPSolver csp = new CSPSolver();
    csp.solve();
    csp.printSchedule(csp.getBestSchedule());
  }
}
