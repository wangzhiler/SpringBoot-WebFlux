import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by thinkpad on 2018/10/11.
 */
class Student {
    private String name;
    private int age;

    //两个枚举
    private Gender gender;
    private Grade grade;

    public String getName() {
        return name;
    }

    public Student(String name, int age, Gender gender, Grade grade) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.grade = grade;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }
}

enum Gender{
    MALE, FEMALE
}

enum Grade{
    ONE, TWO, THREE, FOUR
}


public class CollectDemo {

    public static void main(String[] args) {
        //测试数据
        List<Student> students = Arrays.asList(
                new Student("小明1", 11, Gender.MALE, Grade.ONE),
                new Student("小明2", 11, Gender.FEMALE, Grade.ONE),
                new Student("小明3", 13, Gender.MALE, Grade.TWO),
                new Student("小明4", 14, Gender.FEMALE, Grade.FOUR),
                new Student("小明5", 15, Gender.MALE, Grade.ONE),
                new Student("小明6", 15, Gender.FEMALE, Grade.THREE),
                new Student("小明7", 13, Gender.MALE, Grade.ONE),
                new Student("小明8", 18, Gender.MALE, Grade.ONE)
        );

        //得到所有学生的年龄列表
        //s -> s.getAge() --> Student::getAge, 不会生成一个类似lambda@()这样的函数
        List<Integer> ages = students.stream().map(Student::getAge)
                .collect(Collectors.toList());
        Set<Integer> ages2 = students.stream().map(Student::getAge)
                .collect(Collectors.toSet()); //默认是HashSet，可以指定Set类型
        Set<Integer> ages3 = students.stream().map(Student::getAge)
                .collect(Collectors.toCollection(TreeSet::new)); //定义成TreeSet
        System.out.println("所有学生的年龄：" + ages);
        System.out.println("所有学生的年龄去重：" + ages2);

        //统计汇总信息
        IntSummaryStatistics intSummaryStatistics = students.stream()
                .collect(Collectors.summarizingInt(Student::getAge));
        System.out.println("年龄汇总信息："+intSummaryStatistics);
        //年龄汇总信息：IntSummaryStatistics{count=8, sum=115, min=11, average=14.375000, max=18}

        //分块
        Map<Boolean, List<Student>> genders = students.stream()
                .collect(Collectors.partitioningBy(s -> s.getGender() == Gender.MALE));
        System.out.println("男女学生列表：" + genders);
        //男女学生列表：{false=[Student@dde6e5, Student@177ecd, Student@80bfe8]
        // , true=[Student@a29884, Student@169b07b, Student@c34f4d, Student@1a7cec2, Student@1b3120a]}

        //分组
        Map<Grade, List<Student>> grades = students.stream()
                .collect(Collectors.groupingBy(Student::getGrade));
        System.out.println("学生班级列表：" + grades);
        //学生班级列表：{THREE=[Student@80bfe8], TWO=[Student@169b07b], ONE=[Student@a29884, Student@dde6e5
        // , Student@c34f4d, Student@1a7cec2, Student@1b3120a], FOUR=[Student@177ecd]}

        //得到所有班级学生的个数
        Map<Grade, Long> gradesCount = students.stream()
                .collect(Collectors.groupingBy(Student::getGrade,Collectors.counting()));
        System.out.println("学生班级列表：" + gradesCount);
        //学生班级列表：{THREE=1, TWO=1, ONE=5, FOUR=1}
    }
}
