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

//        for (int i = 200; i <= 300; i++) {
//            int bai = i / 100;
//            int shi = i / 10 % 10;
//            int ge = i % 10;
//            if (ge + shi + bai == 12) {
//                if (ge * shi * bai == 42) {
//                    System.out.println(i);
//                }
//            }
//        }
//
//        for (int i = 0; i < 4; i++) {
//            System.out.println("****");
//        }
//        for (int i = 0; i < 5; i++) {
//            for (int j = 0; j < i; j++) {
//                System.out.print("*");
//            }
//            System.out.println();
//        }
//
//        for (int i = 1; i <= 3; i++) {
//            for (int j = 1; j <= i; j++) {
//                System.out.print(i + "*" + j + "=" + i * j + " ");
//            }
//            System.out.println();
//        }
//
//        /*
//            n!的阶乘
//         */
//        int n = 3;
//
//
//        int i1 = 1;
//
//        for (int i = 1; i <= n; i++) {
//            i1 *= i;
//        }
//        System.out.println("n的阶乘："+ i1);
//        int jiecheng = jiecheng(4);
//        System.out.println(jiecheng);
//
//        //若求从1！到n！
//        int a1 = 4;
//        int sum = 0;
//        int fact = 1;
//        for (int i = 1; i <= a1; i++) {
//            fact*=i;
//            sum+=fact;
//        }
//        System.out.println("1!~ "+ a1 + "!的和为: "+ sum);
//        System.out.println(sumNum(100));
        int[] nums = new int[]{40, 8, 15, 18, 12};
        int temp;
        int leng = nums.length;
        long start = System.currentTimeMillis();
        for (int i = 0; i < leng - 1; i++) {

            for (int j = 0; j < leng - i - 1; j++) {
                if (nums[j] > nums[j + 1]) {

                    temp = nums[j + 1];
                    nums[j + 1] = nums[j];
                    nums[j] = temp;
                    long end = System.currentTimeMillis();
                    System.out.println(Arrays.toString(nums) + "时间：" + (end-start));
                }
            }
        }
        int[] num = new int[]{43, 8, 15, 18, 12};
        Arrays.sort(num);
        System.out.println(Arrays.toString(num));
        int a = 10;
        int index=0;
        for (int i = 0; i < nums.length; i++) {
            if (a > nums[i]) {
                index = i;
            }
        }
        System.out.println(index);
        int[] b = new int[num.length + 1];
        System.arraycopy(num, 0, b, 0, index+1);
        System.out.println(Arrays.toString(b));
        b[index+1] = a;
        System.arraycopy(num,index+1,b,index+2,num.length-index-1);
        System.out.println(Arrays.toString(b));
    }

    public static int jiecheng(int n) {
        if (n == 1) {
            return n;
        } else
            return n * jiecheng(n - 1);
    }


    public static int factorialSum(int n) {
        if (n == 1) {
            return 1;
        }
        return factorial(n) + factorialSum(n - 1);
    }

    public static int factorial(int n) {
        int res = 1;
        for (int i = 1; i <= n; i++) {
            res *= i;
        }
        return res;
    }

    public static int sumNum(int n) {
        int sum = 0;
        for (int i = 1; i <= n; i++) {
            if (i % 2 == 0) {
                continue;
            }
            sum += i;
        }
        return sum;
    }


}
