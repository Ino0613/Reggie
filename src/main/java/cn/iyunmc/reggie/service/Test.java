package cn.iyunmc.reggie.service;

import java.util.Arrays;
import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
//        int x = 34.5;
//        boolean boo = x;
//        int g = 17;
//        int y = g;
//        y = y+10;
//
//        short s ;
//        s = y;
//
//        byte b = 3;
//        byte v = b;
//
//        short n = 12;
//
//         v = n;
//
//         byte k = 128;
//
//        int p = 3 * g + y;
//
//        String name = "张三";
//        int age = 22;
//        String sex = "男";
//        String lobby = "IT";
//        String host = "山西省洪洞县";
//        String ouxiang = "无";
//        String zuoyouming = "好好学习天天向上";
//        System.out.println("姓名：" + name + "\n" + "年龄：" + age + "\n" +
//                "性别：" + sex + "\n" + "爱好：" + lobby + "\n" +
//                "籍贯：" + host + "\n" + "偶像：" + ouxiang + "\n" +
//                "座右铭：" + zuoyouming);
//        int a = 2;
//        System.out.println(a++);
//        System.out.println(a);
//        System.out.println(++a);
//        System.out.println(a);
//
//        int m = 5, n = 6;
//        int x = (m++) + n;
//        int y = (--m) + n;
//        System.out.println(x + "," + y);
//
//        int a1 = 10, b = 7;
//        int max;
//
//        if (a1 > b) {
//            max = a1;
//        } else
//            max = b;
//        System.out.println("max = " + max);
//
//        max = a1 > b ? a1 : b;
//        System.out.println("max = " + max);
//
//        boolean b1 = a1 > b ? (3 < 6) : (true == false);
//


//        Scanner sc = new Scanner(System.in);
//        String week = sc.next();
//        week = week.toUpperCase();
//        String[] weeks = new String[]{"MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"};
//        String[] weekString = new String[]{"一", "二", "三", "四", "五", "六", "七"};
//        for (int i = 0; i < weeks.length; i++) {
//            if (week.equals(weeks[i])) {
//                System.out.println("星期" + weekString[i]);
//                break;
//            }
//        }
//        int n = 1,sum=0;
//        while (n <= 5) {
//            sum += n;
//            n++;
//        }
//        System.out.println(sum);

//        int num = (int) (Math.random() * 10 + 1);
//        int guess;
//        System.out.println("猜数字");
//        do {
//            System.out.println("请输入你要猜的数");
//            Scanner sc1 = new Scanner(System.in);
//            guess = sc1.nextInt();
//            if (guess > num) {
//                System.out.println("猜大了!");
//            } else if (guess < num) {
//                System.out.println("猜小了!");
//            }
//        } while (guess != num);
//        System.out.println("恭喜你猜对了，数字为: " + guess);

        for (int i = 200; i <= 300; i++) {
            int bai = i / 100;
            int shi = i / 10 % 10;
            int ge = i % 10;
            if (ge + shi + bai == 12) {
                if (ge * shi * bai == 42) {
                    System.out.println(i);
                }
            }
        }

        for (int i = 0; i < 4; i++) {
            System.out.println("****");
        }
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < i; j++) {
                System.out.print("*");
            }
            System.out.println();
        }

        for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= i; j++) {
                System.out.print(i + "*" + j + "=" + i * j + " ");
            }
            System.out.println();
        }
    }


}
