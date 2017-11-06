package hu.sztomek.archdemo.presentation.app;

import hu.sztomek.archdemo.common.ThreadModule;
import hu.sztomek.archdemo.data.di.ApiModule;
import hu.sztomek.archdemo.domain.di.UseCaseModule;

public interface AppComponentExposes extends AppModule.Exposes
                                            , ThreadModule.Exposes
                                            , ApiModule.Exposes
                                            , UseCaseModule.Exposes {
}
