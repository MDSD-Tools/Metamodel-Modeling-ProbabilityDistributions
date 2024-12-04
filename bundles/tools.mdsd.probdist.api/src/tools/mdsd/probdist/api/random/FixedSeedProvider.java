package tools.mdsd.probdist.api.random;

public class FixedSeedProvider implements ISeedProvider {
    private final Long seed;

    public FixedSeedProvider(long seed) {
        this.seed = seed;
    }

    @Override
    public int getInt() {
        return seed.intValue();
    }

    @Override
    public long getLong() {
        return seed;
    }
}
