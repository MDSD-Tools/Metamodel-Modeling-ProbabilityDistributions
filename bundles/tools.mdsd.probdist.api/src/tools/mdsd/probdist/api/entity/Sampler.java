package tools.mdsd.probdist.api.entity;

public interface Sampler<T> {

    void init(int seed);

    public T sample();
}
