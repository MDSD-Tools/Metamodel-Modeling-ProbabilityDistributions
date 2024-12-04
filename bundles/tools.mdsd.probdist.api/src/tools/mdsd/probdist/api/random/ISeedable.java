package tools.mdsd.probdist.api.random;

import java.util.Optional;

public interface ISeedable {
    void init(Optional<ISeedProvider> seedProvider);
}
