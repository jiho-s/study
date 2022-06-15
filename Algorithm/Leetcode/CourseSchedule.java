import java.util.*;

class Solution {
    public boolean canFinish(int numCourses, int[][] prerequisites) {
        Map<Integer, Course> courseMap = new HashMap<>(numCourses);
        for (int i = 0; i < numCourses; i++) {
            courseMap.put(i, new Course(i));
        }
        for (int[] prerequisite : prerequisites) {
            int current = prerequisite[0];
            int pre = prerequisite[1];
            Course currentCourse = courseMap.get(current);
            Course preCourse = courseMap.get(pre);
            preCourse.nextCourses.add(currentCourse);
            currentCourse.count++;
        }
        Queue<Course> queue = new LinkedList<>();
        for (int i = 0; i < numCourses; i++) {
            Course course = courseMap.get(i);
            if (course.count == 0) {
                queue.add(course);
                courseMap.remove(i);
            }

        }

        while (!queue.isEmpty()) {
            Course poll = queue.poll();
            for (Course nextCours : poll.nextCourses) {
                nextCours.count--;
                if (nextCours.count == 0) {
                    queue.add(nextCours);
                    courseMap.remove(nextCours.index);
                }
            }
        }
        return courseMap.isEmpty();
    }
}

class Course {
    int index;

    int count;
    List<Course> nextCourses;

    public Course(int index) {
        this.index = index;
        this.count = 0;
        this.nextCourses = new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return index == course.index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index);
    }
}