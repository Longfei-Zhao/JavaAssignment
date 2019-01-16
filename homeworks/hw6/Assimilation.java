import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

class Assimilation implements Serializable {

    private List<Borg> borgCollective;

    public List<Borg> getBorg() {
        return borgCollective;
    }

    public List<Individual> cull(List<Individual> people,
                                 Selection<Individual> filter) {
        // no culling at this point
        List<Individual> culled = new ArrayList<Individual>();
        for (Individual id : people) {
            if (filter.qualify(id))
                culled.add(id);
        }
        return culled;
    }

    ;

    public List<Borg> convert(List<Individual> people,
                              Operation<Individual, Borg> op) {
        List<Borg> newBorg = new ArrayList<Borg>();
        for (Individual id : people)
            newBorg.add((Borg) op.doIt(id));
        return newBorg;
    }

    public void assimilate(List<Borg> newBorg) {
        this.borgCollective.addAll(newBorg);
    }

    public Assimilation(List<Borg> previousBorg) {
        this.borgCollective = new ArrayList<Borg>(previousBorg);
    }

    public static void main(String[] args) throws FileNotFoundException {

        // if Borg already exists, get it by deserializing and
        // use as constructor's parameter
        Assimilation assimilation = new Assimilation(new ArrayList<Borg>());

        if (args.length < 1) {
            System.err.println("Usage: java Assimilation <mankindFileName>");
            System.exit(1);
        }

        System.out.println("First, mankind had developed and explored Universe...");
        List<Individual> mankind = new ArrayList<Individual>();

        Scanner sc = new Scanner(new File(args[0]));
        // System.out.println("Getting data from " + args[0]);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            // System.out.println(line);
            mankind.add(new Individual(line));
        }
        sc.close();

        System.out.println();
        System.out.println("These were the people of Earth:");
        // System.out.println("===============================");
        for (Individual id : mankind)
            System.out.printf("%s, value %d%n", id, id.getStatus().value());

        System.out.println();
        System.out.println("Then came the Borg, and first removed the \"unworthy\"");
        // List<Borg> borgCollective = assimilation.getBorg();
        mankind = assimilation.cull(mankind, new Selection<Individual>() {
            @Override
            public Boolean qualify(Individual id) {
                // return id.getStatus().value() > 35;
                // return id.getGender() == Individual.Gender.FEMALE;
                return id.getAge() < 21 || id.getStatus().value() > 35;
            }
        });

        System.out.println("Those who had left --- ");
        for (Individual id : mankind)
            System.out.printf("%s, value %d%n", id, id.getStatus().value());
        System.out.println("--- were assimilated");

        List<Borg> newBorgMembers =
                assimilation.convert(mankind, new Operation<Individual, Borg>() {
                    @Override
                    public Borg doIt(Individual id) {
                        return new Borg(id);
                    }
                });
        assimilation.assimilate(newBorgMembers);
        System.out.println();
        System.out.println("This are the Borg:");
        for (Borg b : assimilation.borgCollective)
            System.out.println(b);
        System.out.println();
        System.out.println(Borg.seeBorg());
    }

}
