package Homework.task3;

public class Task3 {

    public static void main(String[] args) {
        Box<Apple> appleBox = new Box<>();
        Box<Apple> appleBox2 = new Box<>();

        Box<Orange> orangeBox = new Box<>();
        orangeBox.add(new Orange());
        orangeBox.add(new Orange());

        appleBox.add(new Apple());
        appleBox.add(new Apple());
        System.out.println("appleBox weight is " + appleBox.getWeight());

        appleBox2.add(new Apple());
        System.out.println("appleBox2 weight is " + appleBox2.getWeight());

        appleBox.moveAllTo(appleBox2);

        System.out.println("appleBox weight is " + appleBox.getWeight());
        System.out.println("appleBox2 weight is " + appleBox2.getWeight());

        System.out.println("Compare oranges and apples: " + appleBox.compareTo(orangeBox));
        System.out.println("Compare oranges and apples2: " + appleBox2.compareTo(orangeBox));

        Box testBox = new Box();
        testBox.add(new Fruit() {
            @Override
            public String getName() {
                return null;
            }

            @Override
            public double getWeight() {
                return 5.0;
            }
        });

        appleBox.compareTo(testBox);
    }
}