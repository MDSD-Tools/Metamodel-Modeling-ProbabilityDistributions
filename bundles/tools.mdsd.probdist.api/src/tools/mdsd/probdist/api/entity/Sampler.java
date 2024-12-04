package tools.mdsd.probdist.api.entity;

import tools.mdsd.probdist.api.random.ISeedable;

public interface Sampler<T> extends ISeedable {
    public T sample();
}
