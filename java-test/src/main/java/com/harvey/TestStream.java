package com.harvey;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by hanhui on 2017/8/28 0028 13:52
 */

public class TestStream {
    final static Collection<Streams.Task> tasks = Arrays.asList(
            new Streams.Task(Streams.Status.OPEN, 5),
            new Streams.Task(Streams.Status.OPEN, 13),
            new Streams.Task(Streams.Status.CLOSED, 8)
    );

    public static void main(String[] args) throws IOException {
        final long totalPointsOfOpenTasks = tasks
                .stream()
                .filter(task -> task.getStatus() == Streams.Status.OPEN)
                .mapToInt(Streams.Task::getPoints)
                .sum();

        System.out.println("Total points: " + totalPointsOfOpenTasks);

//算task分数和
        final double totalPoints = tasks
                .stream()
                .parallel()
                .map(task -> task.getPoints()) // or map( Task::getPoints )
                .reduce(0, Integer::sum);

        System.out.println("Total points (all tasks): " + totalPoints);
//按照某种准则来对集合中的元素进行分组
        final Map<Streams.Status, List<Streams.Task>> map = tasks
                .stream()
                .collect(Collectors.groupingBy(Streams.Task::getStatus));
        System.out.println(map);
//计算整个集合中每个task分数（或权重）的平均值
        final Collection<String> result = tasks
                .stream()                                        // Stream< String >
                .mapToInt(Streams.Task::getPoints)                     // IntStream
                .asLongStream()                                  // LongStream
                .mapToDouble(points -> points / totalPoints)   // DoubleStream
                .boxed()                                         // Stream< Double >
                .mapToLong(weigth -> (long) (weigth * 100)) // LongStream
                .mapToObj(percentage -> percentage + "%")      // Stream< String>
                .collect(Collectors.toList());                 // List< String >

        System.out.println(result);
        //从文本文件中逐行读取数据
        final Path path = new File("C:\\Users\\Administrator\\Desktop\\face-log.txt").toPath();
        try (Stream<String> lines = Files.lines(path, StandardCharsets.UTF_8)) {
            lines.onClose(() -> System.out.println("Done!")).forEach(System.out::println);
        }
    }
}

class Streams {
    public enum Status {
        OPEN, CLOSED
    }

    static final class Task {
        private final Status status;
        private final Integer points;

        Task(final Status status, final Integer points) {
            this.status = status;
            this.points = points;
        }

        public Integer getPoints() {
            return points;
        }

        public Status getStatus() {
            return status;
        }

        @Override
        public String toString() {
            return String.format("[%s, %d]", status, points);
        }
    }
}