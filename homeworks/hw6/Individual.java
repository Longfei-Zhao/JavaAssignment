class Individual {

    private String name;
    private int age;
    private Gender gender;
    private Status status;

    enum Gender {
        MALE("M"), FEMALE("F"), ANDROID("A");

        Gender(String s) {
            this.designator = s;
        }

        private final String designator;

        public String getDesignator() {
            return designator;
        }

        static Gender genderMap(String s) {
            Gender gender = Gender.ANDROID;
            for (Gender g : Gender.values()) {
                if (g.getDesignator().equals(s)) {
                    gender = g;
                    break;
                }
            }
            return gender;
        }
    }

    enum Status {
        CAPTAIN(100, "Captain"),
        COMMANDER(50, "Commander"),
        LTCOMMANDER(35, "Lt. Commander"),
        LIEUTENANT(25, "Lieutenant"),
        DOCTOR(50, "Dr."),
        COUNSELLOR(15, "Counselor"),
        NOBODY(1, "");

        Status(int value, String s) {
            this.value = value;
            this.title = s;
        }

        private final int value;
        private final String title;

        public int value() {
            return value;
        }

        public String title() {
            return title;
        }

        public static Status statusMap(String title) {
            Status status = Status.NOBODY;
            for (Status st : Status.values()) {
                if (st.title().equals(title)) {
                    status = st;
                    break;
                }
            }
            return status;
        }
    }

    @Override
    public String toString() {
        return String.format("%s (%s, %s), age %d",
                name, status, gender, age);
    }

    public String getName() {
        return name;
    }

    public Status getStatus() {
        return this.status;
    }

    public Gender getGender() {
        return gender;
    }

    public int getAge() {
        return age;
    }

    public Individual(String n, int a, Gender g, Status st) {
        this.name = n;
        this.age = a;
        this.gender = g;
        this.status = st;
    }

    // cheating
    public Individual(String lineOfInput) {
        String[] fields = lineOfInput.split(",");
        this.name = fields[1].trim();
        this.age = Integer.parseInt(fields[3]);
        this.gender = Gender.genderMap(fields[2].trim());
        this.status = Status.statusMap(fields[0].trim());
    }
}