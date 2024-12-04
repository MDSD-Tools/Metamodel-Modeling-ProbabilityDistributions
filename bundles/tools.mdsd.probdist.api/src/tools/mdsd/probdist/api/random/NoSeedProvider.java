package tools.mdsd.probdist.api.random;

public class NoSeedProvider implements ISeedProvider {

    @Override
    public int getInt() {
        return 0;
    }

    @Override
    public long getLong() {
        return 0;
    }

}
