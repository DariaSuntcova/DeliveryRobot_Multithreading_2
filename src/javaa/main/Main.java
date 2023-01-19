package javaa.main;

import javaa.robot.DeliveryRobot;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();
    public static final char R = 'R';

    public static void main(String[] args) throws InterruptedException {

        ArrayList<Thread> threads = new ArrayList<>();

        for (int i = 0; i < 1000; i++) {
            Thread thread = new Thread(() -> {
                String route = DeliveryRobot.generateRoute("RLRFR", 100);
                char[] chars = route.toCharArray();
                int countR = 0;
                for (char c : chars) {
                    if (c == R) {
                        countR++;
                    }
                }
                synchronized (sizeToFreq) {
                    if (countR > 0 & sizeToFreq.containsKey(countR)) {
                        sizeToFreq.put(countR, sizeToFreq.get(countR) + 1);
                    } else {
                        sizeToFreq.put(countR, 1);
                    }
                }
            });
            thread.start();
            threads.add(thread);
        }

        for (Thread thread : threads) {
            thread.join();
        }

        LinkedHashMap<Integer, Integer> newMap = sizeToFreq.entrySet().stream()
                .sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        boolean first = true;

        for (Map.Entry<Integer, Integer> entry : newMap.entrySet()) {
            if (first) {
                System.out.println("Самое частое количество повторений " + entry.getKey() + " (встретилось "
                        + entry.getValue() + " раз)");
                System.out.println("Другие размеры:");
                first = false;
            } else {
                System.out.println("- " + entry.getKey() + " (" + entry.getValue() + " раз)");
            }
        }
    }
}
