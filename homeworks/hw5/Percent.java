import java.util.*;

public class Percent{
    public static void main(String[] args){
        Scanner scan = new Scanner(System.in);

        String line;
        List<Integer> nums = new ArrayList();
        double sum = 0;
        int temp;

        while(true) {
            line = scan.nextLine();
            if (line.isEmpty()) {
                break;
            }
            try{
                temp = Integer.parseInt(line);
                if (temp < 0) {
                    throw new BadValueException();
                }
            }
            catch (NumberFormatException ne){
                System.out.println("Non-negative integers only, try again:");
                continue;
            }
            catch (BadValueException e) {
                System.out.println(e.getMessage());
                continue;
            }
            sum += temp;
            nums.add(temp);
        }

        //Output
        if (sum == 0) {
            System.out.println("There are no numbers input, or their total is zero.");
            return;
        }
        System.out.println("The numbers and percentage:");
        for(int num : nums) {
            System.out.printf("%4d    %5.1f%%\n", num, num * 100 / sum);
        }
        System.out.printf("%4d    100.0%%\n", (int)sum);
    }

    public static class BadValueException extends Exception {
        public BadValueException() {
            super();
        }
        @Override
        public String getMessage(){
            return "Non-negative integers only, try again:";
        }
    }
}
