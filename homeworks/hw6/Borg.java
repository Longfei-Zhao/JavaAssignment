class Borg {

    private static long borgCount;
    private final long borgID;
    private String formerName;

    public long getBorgCount() {
        return borgCount;
    }

    public Borg(Individual id) {
        this.formerName = id.getName();
        Borg.borgCount++;
        this.borgID = Borg.borgCount;
    }

    public Borg makeBorg(Individual id) {
        return new Borg(id);
    }

    @Override
    public String toString() {
        return String.format("Borg-%d, former %s", borgID, formerName);
    }

    static public String seeBorg() {
        return String.format("We are the Borg. We are %d. " +
                "Resistance is futile!", Borg.borgCount);
    }
}